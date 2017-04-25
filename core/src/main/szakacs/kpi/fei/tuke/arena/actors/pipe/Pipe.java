package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.misc.CollectionsCustom;

import java.util.Collections;
import java.util.HashSet;
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
            PipeSegment pushed = segmentStack.top();
            if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                    && pushed.getSegmentType() != PipeSegmentType.VERTICAL) {
                edges.add(pushed);
            }
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
            head.setDirection(popped.getDirectionFrom().getOpposite());
        }
    };

    /**
     * For convenience, the head is stored in a separate reference
     * and is not an element of the list (it is not the top of segmentStack).
     */
    private PipeHead head;
    private PipeSegmentStack segmentStack;
    private Set<PipeSegment> edges;
    private PlayerToken token;
    private ActorGameInterface gameInterface;
    private int healthPoints;



    public Pipe(ActorGameInterface gameInterface, TunnelCellUpdatable startPosition, PlayerToken token) {
        this.head = new PipeHead(Direction.DOWN, gameInterface, startPosition, token);
        this.token = token;
        this.gameInterface = gameInterface;
        this.healthPoints = 100;
        this.segmentStack = new PipeSegmentStack(
                10,
                true,
                1,
                3,
                segmentStackCallback,
                gameInterface
        );
        this.edges = new HashSet<>(20);
    }



    // PipeBasic methods



    @Override
    public PipeHead getHead(){
        return head;
    }
    
    @Override
    public Stack<PipeSegment> getSegmentStack(PlayerToken token){
        if (this.token.validate(token)) {
            // Only the player owning this pipe is allowed to modify this stack
            return segmentStack;
        } else {
            // Anyone else can only view its contents and info, but not modify it
            return CollectionsCustom.unmodifiableStack(segmentStack);
        }
    }
    
    @Override
    public PipeSegment calculateNextSegment(Direction dir) throws IllegalArgumentException {
        //System.out.println("calculating new segment: " + dir.name());
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
            PipeSegment toPush = new PipeSegment(head.getCurrentPosition(),
                    head.getDirection().getOpposite(), dir, gameInterface);
            segmentStack.setSegmentToPush(toPush);
            return toPush;
        }
    }
    
    @Override
    public boolean isWall(Direction dir){
        return head.getCurrentPosition().getCellAtDirection(dir) == null
                || dir == head.getDirection().getOpposite();
    }

    @Override
    public boolean intersects(ActorBasic actor){
        if (actor == null)
            return false;
        for (PipeSegment seg : edges){
            if (seg.intersects(actor))
                return true;
        }
        return head.intersects(actor);
    }

    @Override
    public int getHealth(){
        return healthPoints;
    }



    // helper methods



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

    public Set<PipeSegment> getEdges() {
        return Collections.unmodifiableSet(edges);
    }
}