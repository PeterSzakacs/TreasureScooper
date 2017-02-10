package szakacs.kpi.fei.tuke.arena.pipe;

import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.weapon.Weapon;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.Actor;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 4.11.2016.
 */
public class Pipe {
    /**
     * A list of all pipe segments with information about their type
     * as well as their coordinates relative to the game background,
     * presenting an external Stack ADT interface for manipulation.
     */
    private List<PipeSegment> segmentStack;
    private List<PipeSegment> searchResults;
    private List<PipeSegment> edges;
    /**
     * For convenience, the head is stored in a separate reference
     * and is not an element of the list (it is not the top of stack).
     */
    private PipeHead head;
    private Direction dir;
    /**
     * For convenience, we store the direction we expect the player
     * to move in when the player calls push(), to make updating the
     * head later easier.
     */
    private ActorGameInterface world;
    private int healthPoints;
    private boolean moveAllowed;
    private boolean calculated;

    public Pipe(ActorGameInterface world) {
        this.segmentStack = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.head = new PipeHead(Direction.DOWN, world);
        this.dir = Direction.DOWN;
        this.world = world;
        this.healthPoints = 100;
        this.moveAllowed = true;
        this.calculated = false;
        Weapon weapon = head.getWeapon();
        int capacity = weapon.getAmmoQueue().getCapacity();
        for (int i = 0; i < capacity; i++){
            weapon.loadBullet(new Bullet(this.world));
        }
    }

    public int getHealth(){
        return this.healthPoints;
    }

    public void setHealth(int health, ActorGameInterface gameInterface){
        this.healthPoints = health;
    }

    /*
     * begin segmentStack manipulation methods
     */


    /**
     * Adds a segment to the top of the pipe and moves the pipe head in
     * that direction according to the segment
     *
     * @param segment the segment to add to the pipe
     */
    public void push(PipeSegment segment){
        System.out.println("pipe.push");
        if (this.moveAllowed && this.calculated && segment != null) {
            segmentStack.add(segment);
            this.calculated = false;
            this.moveAllowed = false;
            this.updateAfterPush();
        }
    }

    /**
     * Moves pipe as well as head one step back from its current position
     *
     * @return the removed pipe segment
     * (in conformance with the return value definition of the pop() method
     * for the Stack ADT)
     */
    public PipeSegment pop(){
        if (isEmpty())
            return null;
        if (this.moveAllowed) {
            PipeSegment popped = segmentStack.remove(segmentStack.size() - 1);
            this.updateAfterPop(popped);
            return popped;
        }
        return null;
    }

    public PipeSegment top(){
        if (isEmpty())
            return null;
        return segmentStack.get(segmentStack.size() - 1);
    }

    public boolean isEmpty(){
        return segmentStack.isEmpty();
    }

    public PipeSegment getSegmentFromBottom(int offsetFromBottom){
        return this.segmentStack.get(offsetFromBottom);
    }

    public List<PipeSegment> getSegmentByCriteria(Predicate<PipeSegment> criteria){
        searchResults.clear();
        for (PipeSegment seg : segmentStack){
            if (criteria.test(seg))
                searchResults.add(seg);
        }
        return searchResults;
    }

    /*
     * end segmentStack manipulation methods
     */
    /*
     * begin getters
     */

    public List<PipeSegment> getSegmentStack(){
        return Collections.unmodifiableList(this.segmentStack);
    }

    public PipeHead getHead(){
        return this.head;
    }

    public void allowMovement(GameWorld world){
        if (world != null && world.equals(this.world))
            this.moveAllowed = true;
    }

    /**
     * Checks if a given direction from the head contains a wall
     *
     * @return boolean true if wall is present | false otherwise
     */
    public boolean isWall(Direction dir){
        return this.head.getCurrentPosition().getCellAtDirection(dir) == null;
    }

    public List<PipeSegment> getEdges(){
        return this.edges;
    }

    /*
     * end getters
     */
    /*
     * begin helper methods
     */


    /**
     * A method to calculate the segment to push onto the stack
     * according to the direction the player wants to move to.
     * This method is made purely for convenience purposes,
     * to unburden the person calling push() from having to
     * implement calculating the segment manually.
     *
     * @param dir the direction the player wishes to move the head to
     * @return the pipe segment to push onto the Stack
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
            this.calculated = true;
            return new PipeSegment(head.getCurrentPosition(), head.getDirection().getOpposite(), dir);
        }
    }

    public boolean intersects(Actor actor){
        for (PipeSegment seg : this.edges){
            if (Math.abs(seg.getX() - actor.getX()) <= world.getOffsetX() &&
                    Math.abs(seg.getY() - actor.getY()) <= world.getOffsetY())
                return true;
        }
        return this.head.intersects(actor);
    }

    /**
     * updates the head of the pipe (its orientation and coordinates)
     * after moving in a given direction (pushing a segment to the pipe).
     */
    private void updateAfterPush(){
        head.move(world.getOffsetX(), world.getOffsetY(), dir);
        this.head.getCurrentPosition().collectNugget(this.world.getGameWorld(), this.head);
        PipeSegment pushed = top();
        if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                && pushed.getSegmentType() != PipeSegmentType.VERTICAL)
            this.edges.add(pushed);
        List<Actor> enemies = this.world.getActorsBySearchCriteria(actor ->
                actor.getType() == ActorType.MOLE && this.intersectsHead(actor)
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
    private void updateAfterPop(PipeSegment popped){
        int xDelta = popped.getX() - head.getX();
        int yDelta = popped.getY() - head.getY();
        head.move(Math.abs(xDelta), Math.abs(yDelta), head.getDirection().getOpposite());
        if (popped.getSegmentType() == PipeSegmentType.HORIZONTAL
                || popped.getSegmentType() == PipeSegmentType.VERTICAL)
            head.setDirection(head.getDirection().getOpposite());
        else {
            PipeSegment top = top();
            head.setDirection(Direction.getDirectionByDeltas(
                    head.getX() - top.getX(), head.getY() - top.getY()
            ));
        }
    }

    private boolean intersectsHead(Actor actor){
        return Math.abs(head.getX() - actor.getX()) <= world.getOffsetX() &&
                Math.abs(head.getY() - actor.getY()) <= world.getOffsetY();
    }

    /*
     * end helper methods
     */
}