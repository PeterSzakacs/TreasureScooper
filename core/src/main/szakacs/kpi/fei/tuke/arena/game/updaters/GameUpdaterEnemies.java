package szakacs.kpi.fei.tuke.arena.game.updaters;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.arena.actors.Enemy;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 31.12.2016.
 *
 * Updater class to manage enemies during the game,
 * specifically, their creation and destruction
 */
public class GameUpdaterEnemies extends AbstractGameUpdater {

    private int turnCounter;

    // determines the number of iterations of the game loop
    // before creating a new enemy.
    private int turnBound;

    // max. number of enemies that can exist at the same time in the game
    private int enemyCountMax;

    // current number of enemies in the game gameInterface
    private int createdEnemiesCount;

    // list of all previous positions where an enemy actor was put,
    // serves to prevent creating enemies always at the same position
    private List<TunnelCell> previousPositions;

    // list of all positions where to put an enemy Actor,
    // basically all LEFT_EDGE and RIGHT_EDGE positions
    // of all tunnels in the game gameInterface,
    private List<TunnelCell> eligiblePositions;



    public GameUpdaterEnemies(GameLevelPrivileged game){
        super(game);
        this.initialize();
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(31);
        } while (this.turnBound < 20);
    }

    public GameUpdaterEnemies(GameLevelPrivileged game, int turnBound){
        super(game);
        this.initialize();
        this.turnBound = turnBound;
    }

    /**
     * Initializes the collections used in this class
     * and calculates the maximum number of enemies
     * that can exist at the same time in the game
     */
    private void initialize(){
        this.turnCounter = 0;
        this.eligiblePositions = new ArrayList<>();
        for (HorizontalTunnel ht : gameWorld.getTunnels()){
            eligiblePositions.addAll(ht.getCellsBySearchCriteria(
                    (cell) ->
                            cell.getCellType() == TunnelCellType.LEFT_EDGE
                                    || cell.getCellType() == TunnelCellType.RIGHT_EDGE
                    )
            );
        }
        this.enemyCountMax = eligiblePositions.size() - 4;
        this.previousPositions = new ArrayList<>(enemyCountMax);
    }



    /**
     * Updates the game by creating new enemies
     * at turnBound-th iterations of the game
     * loop.
     */
    @Override
    public void update(){
        turnCounter++;
        if (turnCounter > turnBound && createdEnemiesCount < enemyCountMax) {
            turnCounter = 0;
            createNewEnemy();
        }
    }

    /**
     * Creates a new enemy Actor and registers it
     * in the list of arena of the actor manager
     */
    private void createNewEnemy() {
        // pick a random position that was not selected before
        List<TunnelCell> pipeHeadPositions = new ArrayList<>(actorManager.getPlayerToPipeMap().size());
        for (Pipe pipe : actorManager.getPlayerToPipeMap().values()) {
            pipeHeadPositions.add(pipe.getHead().getCurrentPosition());
        }
        TunnelCell cell; Random rand = new Random();
        if (previousPositions.size() == enemyCountMax)
            previousPositions.clear();
        do {
            cell = eligiblePositions.get(rand.nextInt(eligiblePositions.size()));
        } while (previousPositions.contains(cell) || pipeHeadPositions.contains(cell));
        previousPositions.add(cell);

        // select direction for enemy to move in based on the picked tunnel cell
        Direction dir;
        if (cell.getCellType() == TunnelCellType.RIGHT_EDGE) {
            dir = Direction.LEFT;
        } else {
            dir = Direction.RIGHT;
        }

        // register the actor
        Enemy enemy = new Enemy(dir, cell, actorManager.getActorGameProxy());
        actorManager.registerActor(enemy);
        actorManager.setOnDestroy(enemy, () -> this.createdEnemiesCount-- );
        this.createdEnemiesCount++;
    }
}
