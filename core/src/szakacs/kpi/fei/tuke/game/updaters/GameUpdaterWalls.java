package szakacs.kpi.fei.tuke.game.updaters;

import szakacs.kpi.fei.tuke.arena.actors.Wall;
import szakacs.kpi.fei.tuke.arena.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 1.1.2017.
 *
 * Updater class to manage walls during the game,
 * specifically. their creation and destruction
 */
public class GameUpdaterWalls extends AbstractGameUpdater {

    private int turnCounter;

    // determines the number of iterations of the game loop
    // before creating a new wall.
    private int turnBound;

    // max. number of walls that can exist at the same time in the game
    private int wallCountMax;

    // current number of walls in the game world
    private int createdWallsCount;

    // list of all previous positions where a wall actor was put,
    // serves to prevent creating walls always at the same position
    private List<TunnelCell> previousPositions;

    // list of all positions where to put a wall Actor,
    // basically all TUNNEL type positions of all tunnels
    // in the game world,
    private List<TunnelCell> eligiblePositions;



    public GameUpdaterWalls(GameLevelPrivileged game) {
        super(game);
        this.initialize();
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(31);
        } while (this.turnBound < 20);
    }

    public GameUpdaterWalls(GameLevelPrivileged game, int turnBound) {
        super(game);
        this.initialize();
        this.turnBound = turnBound;
    }

    /**
     * Initializes the collections used in this class
     * and calculates the maximum number of walls
     * that can exist at the same time in the game
     */
    private void initialize(){
        this.turnCounter = 0;
        this.eligiblePositions = new ArrayList<>();
        for (HorizontalTunnel ht : gameWorld.getTunnels()){
            eligiblePositions.addAll(ht.getCellsBySearchCriteria(
                    (cell) ->
                            cell.getCellType() == TunnelCellType.TUNNEL
                    )
            );
        }
        this.wallCountMax = eligiblePositions.size()/6;
        this.previousPositions = new ArrayList<>(wallCountMax);
    }



    /**
     * Updates the game by creating new walls
     * at turnBound-th iterations of the game
     * loop.
     */
    @Override
    public void update() {
        turnCounter++;
        if (turnCounter > turnBound && createdWallsCount < wallCountMax) {
            turnCounter = 0;
            addWall();
        }
    }

    /**
     * Creates a new wall Actor and registers it
     * in the list of arena of the actor manager
     */
    private void addWall() {
        // get all positions, where the pipe is located, including the head
        List<PipeSegment> segmentStack = actorManager.getPipe().getSegmentStack().getElementsByCriteria(null);
        List<TunnelCell> positions = new ArrayList<>(segmentStack.size());
        for (PipeSegment seg : segmentStack){
            positions.add(seg.getCurrentPosition());
        }
        positions.add(actorManager.getPipe().getHead().getCurrentPosition());

        // pick a random TunnelCell where to put the new wall actor
        TunnelCell cell; Random rand = new Random();
        if (previousPositions.size() == wallCountMax)
            previousPositions.clear();
        do {
            cell = eligiblePositions.get(rand.nextInt(eligiblePositions.size()));
        } while (previousPositions.contains(cell) || positions.contains(cell));
        previousPositions.add(cell);

        // register the wall actor
        Wall wall = new Wall(cell, super.actorManager.getActorGameProxy());
        actorManager.registerActor(wall);
        actorManager.setOnDestroy(wall, () -> {
            removeWall(wall);
            wall.reconnectCells();
        });
        this.createdWallsCount++;
    }

    /**
     * Callback function that the shall be called when
     * the wall is removed from the game.
     *
     * @param wall the wall to remove (the caller basically)
     */
    private void removeWall(Actor wall){
        this.createdWallsCount--;
        this.previousPositions.remove(wall.getCurrentPosition());
    }
}
