package szakacs.kpi.fei.tuke.player;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.Player;

import java.util.List;

/**
 * Created by developer on 5.11.2016.
 *
 * GameLevel API quality at current stage is still NOT guaranteed. :P
 */
public class PlayerA implements Player {

    private PipeHead head;
    // again, this is all code the student can implement
    private Pipe pipe;

    private enum State {
        // Currently this example student code does NOT take enemies into account
        // so the player loses
        BEGIN, ENTERTUNNEL, CLEAR, RETURN, FINISH, PURSUE_ENEMY
    }

    private State state;
    private Direction currentDir;
    private TunnelCell currentPosition;
    private HorizontalTunnel currentTunnel;
    private TunnelCell entrance;
    private PlayerGameInterface world;
    private Stack<PipeSegment> segmentStack;

    public PlayerA() {
    }

    /**
     * again, this is a method called on each iteration of the game loop
     * (at each turn in the game) that the student has to implement.
     *
     * This is just an example solution of update() implemented as a state machine.
     * states are student defined in this case
     */

    // Still stuff to debug remains

    // what has changed:
    // the underground tunnel maze is now represented as a graph of cells,
    // enabling reduction of background code complexity and allowing more
    // options for tunnel network design, such as world that do not stretch
    // from one end of the screen to the next (not tested yet)
    //
    // also it should make the programming API easier for students to use
    // for implementing their solutions to win the game.

    // Just for reference, the player destroys enemies by eating them.

    @Override
    public void act() {
        // TODO: Investigate ways to prevent access to private member variables
        /*try {
            Field f = Pipe.class.getDeclaredField("world");
            f.setAccessible(true); //Very important, this allows the setting to work.
            ActorGameInterface value = (ActorGameInterface) f.get(pipe);
            System.out.println(value);
        } catch (Exception e){
            e.printStackTrace();
        }*/
        switch(this.state) {
            case BEGIN:
                this.handleBegin();
                break;
            case CLEAR:
                this.handleClear();
                break;
            case RETURN:
                this.handleReturn();
                break;
            case ENTERTUNNEL:
                this.handleEntertunnel();
                break;
            case FINISH:
                if (!segmentStack.isEmpty())
                    segmentStack.pop();
                break;
        }
    }

    @Override
    public void initialize(PlayerGameInterface world) {
        this.state = State.BEGIN;
        this.pipe = world.getPipe();
        this.head = pipe.getHead();
        this.currentDir = this.head.getDirection();
        this.world = world;
        this.currentPosition = pipe.getHead().getCurrentPosition();
        this.currentTunnel = null;
        this.entrance = null;
        this.segmentStack = pipe.getSegmentStack();
    }

    @Override
    public void deallocate() {

    }

    private void handleEntertunnel() {
        //pipe.fire();
        segmentStack.push(pipe.calculateNextSegment(currentDir));
        if (pipe.getHead().getCurrentPosition().getCellType() == TunnelCellType.EXIT) {
            currentDir = Direction.DOWN;
            this.state = State.BEGIN;
        }
    }

    private TunnelCell findNearestExit() {
        List<TunnelCell> exits = this.currentTunnel.getCellsBySearchCriteria((cell) ->
                cell.getCellType() == TunnelCellType.EXIT
        );
        TunnelCell nearest = exits.get(0);
        int nearest_diff = Math.abs(nearest.getX() - head.getX()), diff_x;
        for (TunnelCell cell : exits) {
            diff_x = Math.abs(head.getX() - cell.getX());
            if (diff_x < nearest_diff)
                nearest = cell;
        }
        return nearest;
    }

    private void handleBegin() {
        segmentStack.push(pipe.calculateNextSegment(currentDir));
        currentPosition = head.getCurrentPosition();
        if (currentPosition.getCellType() == TunnelCellType.ENTRANCE) {
            this.entrance = currentPosition;
            this.state = State.CLEAR;
            this.currentDir = Direction.LEFT;
            this.currentTunnel = currentPosition.getTunnel();
        }
    }

    private void handleReturn() {
        segmentStack.pop();
        if (pipe.getHead().getCurrentPosition().equals(this.entrance)) {
            if (currentTunnel.getNuggetCount() == 0) {
                this.state = State.ENTERTUNNEL;
                TunnelCell exit = this.findNearestExit();
                if (exit == null) {
                    this.state = State.FINISH;
                } else if (head.getX() < exit.getX())
                    this.currentDir = Direction.RIGHT;
                else
                    this.currentDir = Direction.LEFT;
            }else {
                currentDir = Direction.RIGHT;
                this.state = State.CLEAR;
            }
        }
    }

    private void handleClear() {
        head.getWeapon().getBulletQueue().enqueue(world.getGameShop().buyBullet());
        segmentStack.push(pipe.calculateNextSegment(currentDir));
        if (pipe.isWall(currentDir)){
            TunnelCellType cellType = head.getCurrentPosition().getCellType();
            if (cellType != TunnelCellType.LEFT_EDGE
                    && cellType != TunnelCellType.RIGHT_EDGE)
                head.getWeapon().getBulletQueue().dequeue();
            else
                this.state = State.RETURN;
        }
    }
}
