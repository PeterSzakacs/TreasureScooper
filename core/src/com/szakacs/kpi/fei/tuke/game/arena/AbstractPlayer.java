package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 4.11.2016.
 */
public abstract class AbstractPlayer {
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
    private PipeHead head;
    /**
     * For convenience, we store the direction we expect the player
     * to move in when the player calls push(), to make updating the
     * head later easier.
     */
    private Direction dir;
    private TreasureScooper world;
    private int score;
    private int healthPoints;
    private HorizontalTunnel currentTunnel;
    private boolean operationApplied;
    private boolean calculated;

    protected AbstractPlayer(TreasureScooper world){
        this.segmentStack = new ArrayList<PipeSegment>();
        this.dir = Direction.DOWN;
        this.head = new PipeHead(15 * TreasureScooper.STD_OFFSET, 13 * TreasureScooper.STD_OFFSET, Direction.DOWN);
        this.world = world;
        this.score = 0;
        this.healthPoints = 100;
        this.currentTunnel = null;
        this.operationApplied = false;
        this.calculated = false;
    }

    List<PipeSegment> getSegmentStack(){
        return this.segmentStack;
    }

    PipeHead getHead(){
        return this.head;
    }

    void setOperationApplied(boolean val){
        this.operationApplied = val;
    }

    public TreasureScooper getWorld(){
        return this.world;
    }

    public int getScore() {
        return score;
    }

    public int getHealth(){
        return this.healthPoints;
    }

    public int getHeadX(){
        return this.head.getX();
    }

    public int getHeadY(){
        return this.head.getY();
    }

    public Direction getCurrentHeadOrientation(){
        return this.head.getDirection();
    }

    public HorizontalTunnel getCurrentTunnel(){
        return this.currentTunnel;
    }

    void damagePipe(){
        this.healthPoints -= 10;
        if (this.healthPoints <= 0)
            this.world.setGameState(GameState.LOST);
    }

    public void repairPipe(int byHowMuch){
        this.score -= byHowMuch * 5;
        this.healthPoints += byHowMuch;
    }


    /*
     * segmentStack manipulation methods
     */


    /**
     * Adds a segment to the top of the pipe and moves the pipe head in
     * that direction according to the segment
     *
     * @param segment the segment to add to the pipe
     */
    public void push(PipeSegment segment){
        if (this.calculated) {
            segmentStack.add(segment);
            this.calculated = false;
            this.updateHeadAfterPush();
            if (currentTunnel == null)
                currentTunnel = this.world.getTunnelByYCoordinate(head.getY());
            if (currentTunnel != null) {
                this.score += currentTunnel.collectNugget(head.getX());
                if (head.getY() != currentTunnel.getY())
                    currentTunnel = null;

            } if (world.getRemainingNuggetsCount() == 0)
                world.setGameState(GameState.WON);
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
        PipeSegment popped = segmentStack.remove(segmentStack.size() - 1);
        this.updateHeadAfterPop(popped);
        return popped;
    }

    public PipeSegment top(){
        if (isEmpty())
            return null;
        return segmentStack.get(segmentStack.size() - 1);
    }

    public boolean isEmpty(){
        return segmentStack.isEmpty();
    }



    /*
     * Helper methods
     */


    /**
     * A method to calculate the segment to push onto the stack
     * according to the direction the player wants to move to.
     * This method is made purely for convenience purposes,
     * to unburden the person calling push() from having to
     * implement calculating the segment manually.
     *
     * TODO: It can calculate the segment, but can't detect a wall
     * in the specified direction and what to return is undefined
     * in that case.
     *
     * @param dir the direction the player wishes to move the head to
     * @return the pipe segment to push onto the Stack
     * @throws IllegalArgumentException if null or an unknown Direction enum value passed as parameter
     */
    public PipeSegment calculateNextSegment(Direction dir) throws IllegalArgumentException{
        if (isWall(dir))
            return null;
        if (this.dir == null)
            throw new IllegalArgumentException("Direction value of null was passed as parameter!");
        else {
            this.dir = dir;
            this.calculated = true;
        }
        if (isEmpty() && dir == Direction.DOWN) {
            // Pipe always begins with the head pointing downward
            return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.VERTICAL);
        } else {
            PipeSegment prev = this.top();
            switch (dir) {
                case LEFT:
                    if (head.getDirection() == Direction.LEFT)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.HORIZONTAL);
                    else if (head.getDirection() == Direction.UP)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_LEFT);
                    else
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_LEFT);
                    /*if (prev.getSegmentType() != PipeSegmentType.VERTICAL)
                        return new PipeSegment(prev.getX() - TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.HORIZONTAL);
                    else if (prev.getY() < head.getY())
                        return new PipeSegment(prev.getX(), prev.getY() + TreasureScooper.STD_OFFSET, PipeSegmentType.BOTTOM_LEFT);
                    else
                        return new PipeSegment(prev.getX(), prev.getY() - TreasureScooper.STD_OFFSET, PipeSegmentType.TOP_LEFT);*/
                case RIGHT:
                    if (head.getDirection() == Direction.RIGHT)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.HORIZONTAL);
                    else if (head.getDirection() == Direction.UP)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_RIGHT);
                    else
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_RIGHT);
                    /*if (prev.getSegmentType() != PipeSegmentType.VERTICAL)
                        return new PipeSegment(prev.getX() + TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.HORIZONTAL);
                    else if (prev.getY() < head.getY())
                        return new PipeSegment(prev.getX(), prev.getY() + TreasureScooper.STD_OFFSET, PipeSegmentType.BOTTOM_RIGHT);
                    else
                        return new PipeSegment(prev.getX(), prev.getY() - TreasureScooper.STD_OFFSET, PipeSegmentType.TOP_RIGHT);*/
                case UP:
                    if (head.getDirection() == Direction.UP)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.VERTICAL);
                    else if (head.getDirection() == Direction.LEFT)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_RIGHT);
                    else
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.TOP_LEFT);
                    /*if (prev.getSegmentType() != PipeSegmentType.HORIZONTAL)
                        return new PipeSegment(prev.getX(), prev.getY() + TreasureScooper.STD_OFFSET, PipeSegmentType.VERTICAL);
                    else if (prev.getX() < head.getX())
                        return new PipeSegment(prev.getX() + TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.TOP_LEFT);
                    else
                        return new PipeSegment(prev.getX() - TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.TOP_RIGHT);*/
                case DOWN:
                    if (head.getDirection() == Direction.DOWN)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.VERTICAL);
                    else if (head.getDirection() == Direction.LEFT)
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_RIGHT);
                    else
                        return new PipeSegment(head.getX(), head.getY(), PipeSegmentType.BOTTOM_LEFT);
                    /*if (prev.getSegmentType() != PipeSegmentType.HORIZONTAL)
                        return new PipeSegment(prev.getX(), prev.getY() - TreasureScooper.STD_OFFSET, PipeSegmentType.VERTICAL);
                    else if (prev.getX() < head.getX())
                        return new PipeSegment(prev.getX() + TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.BOTTOM_LEFT);
                    else
                        return new PipeSegment(prev.getX() - TreasureScooper.STD_OFFSET, prev.getY(), PipeSegmentType.BOTTOM_RIGHT);*/
                default:
                    throw new IllegalArgumentException("Undefined value passed as parameter");
            }
        }
    }

    /**
     * updates the head of the pipe (its orientation and coordinates)
     * after moving in a given direction (pushing a segment to the pipe).
     */
    private void updateHeadAfterPush(){
        head.setDirection(dir);
        switch (dir){
            case LEFT:
                head.setX(top().getX() - TreasureScooper.STD_OFFSET);
                head.setY(top().getY());
                break;
            case RIGHT:
                head.setX(top().getX() + TreasureScooper.STD_OFFSET);
                head.setY(top().getY());
                break;
            case UP:
                head.setX(top().getX());
                head.setY(top().getY() + TreasureScooper.STD_OFFSET);
                break;
            case DOWN:
                head.setX(top().getX());
                head.setY(top().getY() - TreasureScooper.STD_OFFSET);
                break;
            default:
                throw new IllegalArgumentException("Invalid or null direction value");
        }
    }

    /**
     * updates the head of the pipe (its orientation and coordinates)
     * after taking a step back (popping a segment off the pipe).
     *
     * @param popped the popped pipe segment.
     */
    private void updateHeadAfterPop(PipeSegment popped){
        switch (popped.getSegmentType()) {
            case VERTICAL:
            case HORIZONTAL:
                break;
            case BOTTOM_LEFT:
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.RIGHT);
                else
                    head.setDirection(Direction.UP);
                break;
            case BOTTOM_RIGHT:
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.LEFT);
                else
                    head.setDirection(Direction.UP);
                break;
            case TOP_LEFT:
                if (head.getX() == popped.getX())
                    head.setDirection(Direction.RIGHT);
                else
                    head.setDirection(Direction.DOWN);
                break;
            case TOP_RIGHT:
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

    /**
     * Checks if a given direction from the head contains a wall
     *
     * @return boolean true if wall is present | false otherwise
     */
    public boolean isWall(Direction dir){
        switch (dir){
            case LEFT:
                if (currentTunnel == null)
                    return true;
                if (head.getX() == 0)
                    return true;
                else
                    return false;
            case RIGHT:
                if (currentTunnel == null)
                    return true;
                if (head.getX() == TreasureScooper.WIDTH - TreasureScooper.STD_OFFSET)
                    return true;
                else
                    return false;
            case DOWN:
                if (currentTunnel == null){
                    return false;
                } else {
                    HorizontalTunnel next = currentTunnel.getNextTunnel();
                    if (next != null && head.getX() == next.getNearestEntrance(head.getX()))
                        return false;
                    return true;
                }
            case UP:
                if (currentTunnel == null) {
                    return false;
                } else {
                    if (head.getX() == currentTunnel.getNearestEntrance(head.getX()))
                        return false;
                    return true;
                }
            default:
                throw new IllegalArgumentException("Unknown or null Direction value recieved");
        }
    }

    public PipeSegment getSegmentFromBottom(int offsetFromBottom){
        return this.segmentStack.get(offsetFromBottom);
    }

    protected abstract void act();
}


    /*public boolean isWall(int x, int y) {
        int xDiff = 0, yDiff = 0;
        if (!segmentStack.isEmpty()){
            xDiff = Math.abs(top().getX() - x);
            yDiff = Math.abs(top().getY() - y);
        }else{
            if (dir == Direction.LEFT || dir == Direction.RIGHT)
                return true;
            else
                return false;
        }
        // Illegal position change by more than one cell in one turn
        if (xDiff > TreasureScooper.STD_OFFSET || yDiff > TreasureScooper.STD_OFFSET)
            return true;
        if (currentTunnel == null) {
            // if current tunnel is null, then the pipe head is in a vertical shaft
            if (xDiff > 0)
                return true;
            else
                return false;
        } else {
            *//*
             * else it is in a horizontal tunnel, if moving up or down, check if an entrance is there,
             * else check if it is out of game field bounds.
             *//*
            if (xDiff > 0) {
                if (x < 0 || x >= TreasureScooper.WIDTH)
                    return true;
                else
                    return false;
            }
            if (yDiff > 0) {
                if (top().getY() > y){
                    for (Integer entrance : currentTunnel.getNextTunnel().getEntrances()) {
                        if (entrance == x)
                            return false;
                    }
                    return true;
                }else{
                    for (Integer entrance : currentTunnel.getEntrances()) {
                        if (entrance == x)
                            return false;
                    }
                    return true;
                }
            }
        }
        // This return statement should theoretically be unreachable, but testing is needed
        return true;
    }*/