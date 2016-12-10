package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Bullet;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Weapon;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

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
    /**
     * For convenience, the head is stored in a separate reference
     * and is not an element of the list (it is not the top of stack).
     */
    private List<PipeSegment> searchResults;
    private List<PipeSegment> edges;
    private TunnelCell currentPosition;
    private PipeHead head;
    private Direction dir;
    /**
     * For convenience, we store the direction we expect the player
     * to move in when the player calls push(), to make updating the
     * head later easier.
     */
    private Weapon weapon;
    private ManipulableGameInterface world;
    private int score;
    private int healthPoints;
    private boolean moveAllowed;
    private boolean calculated;

    public Pipe(ManipulableGameInterface world) {
        this.segmentStack = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.currentPosition = world.getRootCell();
        this.head = new PipeHead(currentPosition.getX(), currentPosition.getY(), Direction.DOWN, world);
        this.dir = Direction.DOWN;
        this.weapon = new Weapon();
        this.world = world;
        this.score = 0;
        this.healthPoints = 100;
        this.moveAllowed = true;
        this.calculated = false;
        weapon.enqueue(new Bullet(this.world));
    }

    public void damagePipe(){
        this.healthPoints -= 10;
    }

    public void repairPipe(int byHowMuch){
        this.score -= byHowMuch * 5;
        this.healthPoints += byHowMuch;
    }

    public boolean loadBullet(){
        if (this.score < 10)
            return false;
        this.score -= 10;
        return this.weapon.enqueue(new Bullet(this.world));
    }

    public void fire(){
        if (!weapon.isEmpty()){
            Bullet fired = this.weapon.dequeue();
            fired.launch(this.currentPosition, this.head.getDirection(), this.world);
        }
    }

    public Weapon getWeapon(ManipulableGameInterface world){
        if (world != null && world.equals(this.world))
            return this.weapon;
        else
            return null;
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
        if (this.moveAllowed && this.calculated) {
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

    public void allowMovement(ManipulableGameInterface world){
        if (world != null && world.equals(this.world))
            this.moveAllowed = true;
    }

    public int getScore() {
        return score;
    }

    public int getHealth(){
        return this.healthPoints;
    }

    public TunnelCell getCurrentPosition(){
        return this.currentPosition;
    }

    /**
     * Checks if a given direction from the head contains a wall
     *
     * @return boolean true if wall is present | false otherwise
     */
    public boolean isWall(Direction dir){
        return currentPosition.getCellAtDirection(dir) == null;
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
        if (dir == null)
            throw new IllegalArgumentException("Direction value of null was passed as parameter!");
        if (isWall(dir))
            return null;
        else {
            this.dir = dir;
            this.calculated = true;
        }
        switch (dir) {
            case LEFT:
                if (head.getDirection() == Direction.LEFT)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.HORIZONTAL);
                else if (head.getDirection() == Direction.UP)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_LEFT);
                else
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_LEFT);
            case RIGHT:
                if (head.getDirection() == Direction.RIGHT)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.HORIZONTAL);
                else if (head.getDirection() == Direction.UP)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_RIGHT);
                else
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_RIGHT);
            case UP:
                if (head.getDirection() == Direction.UP)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.VERTICAL);
                else if (head.getDirection() == Direction.LEFT)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_RIGHT);
                else
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_LEFT);
            case DOWN:
                if (head.getDirection() == Direction.DOWN)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.VERTICAL);
                else if (head.getDirection() == Direction.LEFT)
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_RIGHT);
                else
                    return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_LEFT);
            default:
                throw new IllegalArgumentException("Undefined value passed as parameter");
        }
    }

    /**
     * updates the head of the pipe (its orientation and coordinates)
     * after moving in a given direction (pushing a segment to the pipe).
     */
    private void updateAfterPush(){
        this.currentPosition = this.currentPosition.getCellAtDirection(dir);
        head.setX(this.currentPosition.getX());
        head.setY(this.currentPosition.getY());
        head.setDirection(dir);
        this.score += this.currentPosition.collectNugget(this.world, this.head);
        PipeSegment pushed = top();
        if (pushed.getSegmentType() != PipeSegmentType.HORIZONTAL
                && pushed.getSegmentType() != PipeSegmentType.VERTICAL)
            this.edges.add(pushed);
    }

    /**
     * updates the head of the pipe (its orientation and coordinates)
     * after taking a step back (popping a segment off the pipe).
     *
     * @param popped the popped pipe segment.
     */
    private void updateAfterPop(PipeSegment popped){
        currentPosition = currentPosition.getCellAtDirection(head.getDirection().getOpposite());
        switch (popped.getSegmentType()) {
            case VERTICAL:
            case HORIZONTAL:
                break;
            case BOTTOM_LEFT:
                this.edges.remove(popped);
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.RIGHT);
                else
                    head.setDirection(Direction.UP);
                break;
            case BOTTOM_RIGHT:
                this.edges.remove(popped);
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.LEFT);
                else
                    head.setDirection(Direction.UP);
                break;
            case TOP_LEFT:
                this.edges.remove(popped);
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.RIGHT);
                else
                    head.setDirection(Direction.DOWN);
                break;
            case TOP_RIGHT:
                this.edges.remove(popped);
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.LEFT);
                else
                    head.setDirection(Direction.DOWN);
                break;
            default:
                throw new IllegalArgumentException("Invalid or null direction value in popped pipe segment!");
        }
        head.setX(popped.getX());
        head.setY(popped.getY());
    }

    public boolean intersects(Actor actor){
        for (PipeSegment seg : this.edges){
            if (Math.abs(seg.getX() - actor.getX()) <= world.getOffsetX() &&
                    Math.abs(seg.getY() - actor.getY()) <= world.getOffsetY())
                return true;
        }
        return Math.abs(head.getX() - actor.getX()) <= world.getOffsetX() &&
                Math.abs(head.getY() - actor.getY()) <= world.getOffsetY();
    }

    /*
     * end helper methods
     */
}