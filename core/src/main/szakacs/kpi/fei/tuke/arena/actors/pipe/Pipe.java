package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Set;

/**
 * Created by developer on 4.11.2016.
 */
public class Pipe implements PipeBasic {

    private OnStackUpdatedCallback<PipeSegment> segmentStackCallback = new OnStackUpdatedCallback<PipeSegment>() {

        /**
         * updates the head of the pipe (its orientation and coordinates)
         * after moving in a given direction (pushing a segment to the pipe).
         */
        @Override
        public void onPush() {
            GameWorldBasic world = gameInterface.getGameWorld();
            head.move(world.getOffsetX(), world.getOffsetY(), segmentStack.top().getDirection());
            head.getCurrentPosition().collectNugget(Pipe.this);
            /*PipeSegment pushed = segmentStack.top();
            if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                    && pushed.getSegmentType() != PipeSegmentType.VERTICAL) {
                edges.add(pushed);
            }*/
            Set<ActorBasic> enemies = gameInterface.getActorsBySearchCriteria(actor ->
                    actor.getType() == ActorType.ENEMY && actor.intersects(head)
            );
            for (ActorBasic actor : enemies){
                gameInterface.unregisterActor(actor);
            }
        }

        /**
         * updates the head of the pipe (its orientation and coordinates)
         * after taking a step back (popping a segment off the pipe).
         *
         * @param popped the popped pipe segment.
         */
        @Override
        public void onPop(PipeSegment popped) {
            int xDelta = popped.getX() - head.getX();
            int yDelta = popped.getY() - head.getY();
            head.move(Math.abs(xDelta), Math.abs(yDelta), head.getDirection().getOpposite());
            head.setDirection(popped.getOriginDirection().getOpposite());
        }
    };

    private PipeSegmentStack segmentStack;
    /**
     * For convenience, the head is stored in a separate reference
     * and is not an element of the list (it is not the top of segmentStack).
     */
    private PipeHead head;
    private Player controller;
    private ActorGameInterface gameInterface;
    private int healthPoints;

    public Pipe(ActorGameInterface gameInterface, TunnelCell startPosition, Player controller) {
        this.head = new PipeHead(Direction.DOWN, gameInterface, startPosition);
        this.controller = controller;
        this.gameInterface = gameInterface;
        this.healthPoints = 100;
        this.segmentStack = new PipeSegmentStack(
                10,
                true,
                1,
                3,
                segmentStackCallback
        );
    }

    /*
     * begin interface methods
     */

    @Override
    public PipeHead getHead(){
        return head;
    }
    
    @Override
    public Stack<PipeSegment> getSegmentStack(){
        return segmentStack;
    }
    
    @Override
    public PipeSegment calculateNextSegment(Direction dir) throws IllegalArgumentException {
        System.out.println("calculating new segment: " + dir.name());
        switch (dir) {
            case LEFT:
            case RIGHT:
            case UP:
            case DOWN:
                break;
            default:
                throw new IllegalArgumentException("Unknown or null Direction value was passed as parameter: " + dir);
        }
        if (isWall(dir)) {
            return null;
        } else {
            return new PipeSegment(head.getCurrentPosition(), head.getDirection().getOpposite(), dir, gameInterface);
        }
    }
    
    @Override
    public boolean isWall(Direction dir){
        return head.getCurrentPosition().getCellAtDirection(dir) == null;
    }

    @Override
    public boolean intersects(ActorBasic actor){
        GameWorldBasic world = gameInterface.getGameWorld();
        for (PipeSegment seg : segmentStack){
            if (Math.abs(seg.getX() - actor.getX()) <= world.getOffsetX() &&
                    Math.abs(seg.getY() - actor.getY()) <= world.getOffsetY())
                return true;
        }
        return head.intersects(actor);
    }

    @Override
    public int getHealth(){
        return healthPoints;
    }
    
    /*
     * end interface methods
     *
     * begin helper methods
     */

    public Player getController() {
        return controller;
    }
    
    public void setPlayer(Player controller) {
        if (this.controller == null) {
            this.controller = controller;
        }
    }

    public void allowMovement(Object authToken) {
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            segmentStack.unlock();
        }
    }

    public void setHealth(int health, Object authToken) {
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            healthPoints = health;
            if (healthPoints < 0) {
                healthPoints = 0;
            }
        }
    }
    
    /*
     * end helper methods
     */
}