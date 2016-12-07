package com.szakacs.kpi.fei.tuke.game.player;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.PipeHead;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.QueryableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;

/**
 * Created by developer on 5.11.2016.
 *
 * Game API quality at current stage is still NOT guaranteed. :P
 */
public class PlayerA implements Player {

    private final PipeHead head;
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
    private QueryableGameInterface world;

    public PlayerA(QueryableGameInterface world, Pipe pipe){
        this.state = State.BEGIN;
        this.pipe = pipe;
        this.head = pipe.getHead();
        this.currentDir = this.head.getDirection();
        this.world = world;
        this.currentPosition = pipe.getCurrentPosition();
        this.currentTunnel = null;
        this.entrance = null;
    }

    /**
     * again, this is a method called on each iteration of the game loop
     * (at each turn in the game) that the student has to implement.
     *
     * This is just an example solution of act() implemented as a state machine.
     * states are student defined in this case
     */

    // Still stuff to debug remains

    // what has changed:
    // the underground tunnel maze is now represented as a graph of cells,
    // enabling reduction of background code complexity and allowing more
    // options for tunnel network design, such as tunnels that do not stretch
    // from one end of the screen to the next (not tested yet)
    //
    // also it should make the programming API easier for students to use
    // for implementing their solutions to win the game.

    // Just for reference, the player destroys enemies by eating them.

    @Override
    public void act() {
        switch(this.state){
            case BEGIN:
                pipe.push(pipe.calculateNextSegment(currentDir));
                currentPosition = pipe.getCurrentPosition();
                if (currentPosition.getCellType() == TunnelCellType.ENTRANCE) {
                    this.entrance = currentPosition;
                    this.state = State.CLEAR;
                    this.currentDir = Direction.LEFT;
                    for (HorizontalTunnel ht : world.getTunnels()) {
                        if (ht.getEntrances().contains(currentPosition))
                            this.currentTunnel = ht;
                    }
                }
                break;
            case CLEAR:
                pipe.loadBullet();
                pipe.push(pipe.calculateNextSegment(currentDir));
                if (pipe.isWall(currentDir)){
                    this.state = State.RETURN;
                }
                break;
            case RETURN:
                pipe.pop();
                if (pipe.getCurrentPosition().equals(this.entrance)) {
                    if (currentTunnel.getNuggetCount() == 0) {
                        this.state = State.ENTERTUNNEL;
                        TunnelCell exit = this.currentTunnel.getNearestExit(head.getX());
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
                break;
            case ENTERTUNNEL:
                pipe.fire();
                pipe.push(pipe.calculateNextSegment(currentDir));
                if (pipe.getCurrentPosition().getCellType() == TunnelCellType.EXIT) {
                    currentDir = Direction.DOWN;
                    this.state = State.BEGIN;
                }
                break;
            case FINISH:
                if (!pipe.isEmpty())
                    pipe.pop();
                break;
        }
    }
}
