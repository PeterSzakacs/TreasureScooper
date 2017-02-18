package szakacs.kpi.fei.tuke.arena.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

import java.util.List;

/**
 * Created by developer on 4.11.2016.
 */
public class Pipe {

    private OnStackUpdatedCallback<PipeSegment> segmentStackCallback = new OnStackUpdatedCallback<PipeSegment>() {

        /**
         * updates the head of the pipe (its orientation and coordinates)
         * after moving in a given direction (pushing a segment to the pipe).
         */
        @Override
        public void onPush() {
            head.move(world.getOffsetX(), world.getOffsetY(), dir);
            head.getCurrentPosition().collectNugget(world.getGameWorld(), head);
            /*PipeSegment pushed = segmentStack.top();
            if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                    && pushed.getSegmentType() != PipeSegmentType.VERTICAL) {
                edges.add(pushed);
            }*/
            List<Actor> enemies = world.getActorsBySearchCriteria(actor ->
                    actor.getType() == ActorType.MOLE && actor.intersects(head)
            );
            for (Actor actor : enemies){
                world.unregisterActor(actor);
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
            Direction dir = head.getDirection();
            head.move(Math.abs(xDelta), Math.abs(yDelta), dir.getOpposite());
            if (popped.getSegmentType() == PipeSegmentType.HORIZONTAL
                    || popped.getSegmentType() == PipeSegmentType.VERTICAL) {
                head.setDirection(dir);
            } else {
                PipeSegment top = segmentStack.top();
                head.setDirection(Direction.getDirectionByDeltas(
                        head.getX() - top.getX(), head.getY() - top.getY()
                ));
            }
        }
    };

    private PipeSegmentStack segmentStack;
    /**
     * For convenience, the head is stored in a separate reference
     * and is not an element of the list (it is not the top of segmentStack).
     */
    private PipeHead head;
    /**
     * For convenience, we store the direction we expect the player
     * to move in when the player calls push(), to make updating the
     * head later easier.
     */
    private Player controller;
    private Direction dir;
    private ActorGameInterface world;
    private int healthPoints;

    public Pipe(ActorGameInterface world, TunnelCell startPosition, Player controller) {
        this.head = new PipeHead(Direction.DOWN, world, startPosition);
        this.controller = controller;
        this.dir = Direction.DOWN;
        this.world = world;
        this.healthPoints = 100;
        this.segmentStack = new PipeSegmentStack(
                10,
                true,
                1,
                3,
                segmentStackCallback
        );
        Queue<Bullet> weaponQueue = head.getWeapon().getBulletQueue();
        int capacity = weaponQueue.getCapacity();
        for (int i = 0; i < capacity; i++) {
            weaponQueue.enqueue(new Bullet(this.world));
        }
    }

    /*
     * begin getters
     */

    public int getHealth(){
        return this.healthPoints;
    }

    public Stack<PipeSegment> getSegmentStack(){
        return this.segmentStack;
    }

    public PipeHead getHead(){
        return this.head;
    }

    /*
     * end getters
     *
     * begin helper methods
     */

    public void setPlayer(Player controller) {
        if (this.controller == null) {
            this.controller = controller;
        }
    }

    public void allowMovement(ActorGameInterface gameInterface) {
        if (gameInterface != null && world.equals(gameInterface)) {
            segmentStack.unlock();
        }
    }

    public void setHealth(int health, ActorGameInterface gameInterface) {
        if (gameInterface != null && world.equals(gameInterface)) {
            healthPoints = health;
            if (healthPoints < 0) {
                healthPoints = 0;
            }
        }
    }

    /**
     * A method to calculate the segment to push onto the segmentStack
     * according to the direction the player wants to move to.
     * This method is made purely for convenience purposes,
     * to unburden the person calling push() from having to
     * implement calculating the segment manually.
     *
     * @param dir the direction the player wishes to move the head to
     * @return the pipe segment to push onto the ArrayStack
     * @throws IllegalArgumentException if null or an unknown Direction enum value passed as parameter
     */
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
            this.dir = dir;
            return new PipeSegment(head.getCurrentPosition(), head.getDirection().getOpposite(), dir);
        }
    }

    /**
     * Checks if a given direction from the head contains a wall
     *
     * @return boolean true if wall is present | false otherwise
     */
    public boolean isWall(Direction dir){
        return head.getCurrentPosition().getCellAtDirection(dir) == null;
    }

    public boolean intersects(Actor actor){
        for (PipeSegment seg : segmentStack){
            if (Math.abs(seg.getX() - actor.getX()) <= world.getOffsetX() &&
                    Math.abs(seg.getY() - actor.getY()) <= world.getOffsetY())
                return true;
        }
        return head.intersects(actor);
    }

    /*
     * end helper methods
     */
}