package szakacs.kpi.fei.tuke.arena.game.updaters;

import szakacs.kpi.fei.tuke.arena.actors.Wall;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    // current number of walls in the game gameInterface
    private int createdWallsCount;

    // list of all previous positions where a wall actor was put,
    // serves to prevent creating walls always at the same position
    private List<TunnelCellUpdatable> previousPositions;

    // list of all positions where to put a wall Actor,
    // basically all TUNNEL type positions of all tunnels
    // in the game gameInterface,
    private List<TunnelCellUpdatable> eligiblePositions;



    public GameUpdaterWalls(){
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(31);
        } while (this.turnBound < 20);
        this.eligiblePositions = new ArrayList<>();
    }



    /**
     * Initializes the collections used in this class
     * and calculates the maximum number of walls
     * that can exist at the same time in the game
     */
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level){
        super.startNewGame(gameLevel, level);
        eligiblePositions.clear();
        createdWallsCount = 0;
        turnCounter = 0;
        for (HorizontalTunnelUpdatable ht : gameWorld.getTunnelsUpdatable()){
            eligiblePositions.addAll(ht.getCellsBySearchCriteria(
                    (cell) ->
                            cell.getCellType() == TunnelCellType.TUNNEL
                                    && cell.getCellAtDirection(Direction.RIGHT).getCellType() == TunnelCellType.TUNNEL,
                    gameLevel.getAuthenticator()
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
        List<TunnelCellUpdatable> positions = new ArrayList<>();
        Map<PlayerToken, Pipe> pipeTokenMap = playerManager.getPipesUpdatable();
        for (PlayerToken token : pipeTokenMap.keySet()) {
            Pipe pipe = pipeTokenMap.get(token);
            Stack<PipeSegment> segmentStack = pipe.getSegmentStack(token);
            for (PipeSegment seg : segmentStack) {
                positions.add(seg.getCurrentPosition());
            }
            positions.add(pipe.getHead().getCurrentPosition());
        }
        // pick a random TunnelCell where to put the new wall actor
        TunnelCellUpdatable cell; Random rand = new Random();
        if (previousPositions.size() == wallCountMax)
            previousPositions.clear();
        do {
            cell = eligiblePositions.get(rand.nextInt(eligiblePositions.size()));
        } while (previousPositions.contains(cell) || positions.contains(cell));
        previousPositions.add(cell);

        // register the wall actor
        Wall wall = new Wall(cell, game.getActorInterface());
        actorManager.registerActor(wall, () -> {
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
    private void removeWall(ActorUpdatable wall){
        this.createdWallsCount--;
        this.previousPositions.remove(wall.getCurrentPosition());
    }
}
