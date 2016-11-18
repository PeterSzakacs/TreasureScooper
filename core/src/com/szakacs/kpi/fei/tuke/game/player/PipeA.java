package com.szakacs.kpi.fei.tuke.game.player;

import com.szakacs.kpi.fei.tuke.game.arena.*;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;

import java.util.List;

/**
 * Created by developer on 5.11.2016.
 */

/**
 * Game API quality at current stage is NOT guaranteed. :P
 */
public class PipeA extends AbstractPlayer {

    // this is all code the student can implement

    private enum State {
        BEGIN, ENTERTUNNEL, CLEARLEFT, CLEARRIGHT, RETURN, FINISH, PURSUE_ENEMY
    }

    private HorizontalTunnel currentTunnel;
    private TreasureScooper world;
    private State state;
    private boolean leftCleared;
    private boolean rightCleared;
    private int entrance;
    private int currentIndex;

    public PipeA(TreasureScooper goldwell){
        super(goldwell);
        this.world = goldwell;
        this.state = State.BEGIN;
        this.currentIndex = 0;
        this.currentTunnel = world.getTunnel(currentIndex);
    }

    /**
     * this is a method called on each iteration of the game loop
     * (at each turn in the game) that the student has to implement.
     *
     * This is just an example solution of act() implemented as a state machine.
     * states are student defined in this case
     */

    // Still stuff to debug remains

    @Override
    protected void act() {
        switch(this.state){
            case BEGIN:
                this.entrance = this.getHeadX();
                push(calculateNextSegment(Direction.DOWN));
                if (currentTunnel.getY() == getHeadY()){
                    this.state = State.CLEARLEFT;
                }
                break;
            case CLEARLEFT:
                push(calculateNextSegment(Direction.LEFT));
                if (isWall(Direction.LEFT)){
                    this.state = State.RETURN;
                    this.leftCleared = true;
                }
                break;
            case RETURN:
                if (getHeadX() != this.entrance)
                    pop();
                else {
                    if (this.leftCleared == this.rightCleared){
                        this.currentTunnel = currentTunnel.getNextTunnel();
                        if (this.currentTunnel == null)
                            this.state = State.FINISH;
                        else {
                            this.entrance = this.currentTunnel.getNearestEntrance(getHeadX());
                            this.state = State.ENTERTUNNEL;
                            this.leftCleared = false;
                            this.rightCleared = false;
                        }
                    } else if (leftCleared)
                        this.state = State.CLEARRIGHT;
                    else
                        this.state = State.CLEARLEFT;
                }
                break;
            case CLEARRIGHT:
                push(calculateNextSegment(Direction.RIGHT));
                if (isWall(Direction.RIGHT)){
                    this.state = State.RETURN;
                    this.rightCleared = true;
                }
                break;
            case ENTERTUNNEL:
                if (getHeadX() < this.entrance)
                    push(calculateNextSegment(Direction.RIGHT));
                else if (getHeadX() > this.entrance)
                    push(calculateNextSegment(Direction.LEFT));
                else
                    this.state = State.BEGIN;
                break;
            case FINISH:
                if (!isEmpty())
                    pop();
                break;
        }
    }
}
