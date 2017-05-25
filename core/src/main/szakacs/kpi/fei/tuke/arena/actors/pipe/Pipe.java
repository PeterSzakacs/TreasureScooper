package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnPipeMovedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
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

        private int collectedNuggetsCounter = 0;

        /**
         * updates the head of the pipe (its orientation and coordinates)
         * after moving in a given direction (pushing a segment to the pipe).
         */
        @Override
        public void onPush(PipeSegment pushed) {
            GameWorldBasic world = gameInterface.getGameWorld();
            TunnelCellBasic adjacent = pushed.getCurrentPosition()
                    .getCellAtDirection(pushed.getDirection());
            if (adjacent != null) {
                Set<ActorBasic> pipeActors = gameInterface.getPositionsToPipesMap()
                        .get(adjacent);
                if (pipeActors.size() > 0){
                    // A collision with another pipe is detected.
                    // Prevent moving in that direction and handle collision.
                    segmentStack.pop();
                    ActorBasic pipeActor = pipeActors.iterator().next();
                    switch (pipeActor.getType()){
                        case PIPEHEAD:
                            healthPoints -= 20;
                            break;
                        case PIPESEGMENT:
                            // Only reduce your own health,
                            // the other pipe will reduce
                            // its health in turn.
                            Pipe otherPipe = (Pipe) ((PipeSegment)pipeActor).getPipe();
                            otherPipe.setHealth(otherPipe.getHealth() - 20,
                                    gameInterface.getAuthenticator());
                            break;
                    }
                    return;
                }
            }

            head.move(world.getOffsetX(), world.getOffsetY(), segmentStack.top().getDirection());
            if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                    && pushed.getSegmentType() != PipeSegmentType.VERTICAL) {
                edges.add(pushed);
            }
            onPipeMovedCallback.onPush(head, pushed);
            head.onPush(Pipe.this);
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
            onPipeMovedCallback.onPop(head, popped);
        }
    };
    private OnPipeMovedCallback onPipeMovedCallback;

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



    public Pipe(ActorGameInterface gameInterface,
                TunnelCellUpdatable startPosition,
                OnPipeMovedCallback onPipeMovedCallback,
                PlayerToken token) {
        this.head = new PipeHead(Direction.DOWN, gameInterface, startPosition, token);
        this.token = token;
        this.gameInterface = gameInterface;
        this.healthPoints = 100;
        this.segmentStack = new PipeSegmentStack(
                10,
                true,
                1,
                1,
                segmentStackCallback,
                gameInterface,
                token
        );
        this.edges = new HashSet<>(20);
        this.onPipeMovedCallback = onPipeMovedCallback;
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
                    head.getDirection().getOpposite(), dir, gameInterface, token);
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


    /**
     * Allows pushing segments to the pipe or popping them off it,
     * which is limited one per iteration of the game loop for
     * players.
     *
     * @param authToken An authentication token to verify the caller
     */
    public void allowMovement(Object authToken) {
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            segmentStack.unlock();
        }
    }

    /**
     * Sets the value of health of this pipe.
     *
     * @param health the new health value of this pipe
     * @param authToken An authentication token to verify the caller
     */
    public void setHealth(int health, Object authToken) {
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            healthPoints = health;
            if (healthPoints < 0) {
                healthPoints = 0;
            }
        }
    }
}