package szakacs.kpi.fei.tuke.player;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.player.common.AbstractPlayer;

import java.util.Set;

/**
 * Created by developer on 5.11.2016.
 */
public class PlayerA extends AbstractPlayer {

    // again, this is all code the student can implement

    private enum State {
        // Currently this example student code does NOT take enemies into account
        // so the player loses
        BEGIN, ENTERTUNNEL, CLEAR, RETURN, FINISH, PURSUE_ENEMY
    }

    private State state;
    private Direction currentDir;
    private TunnelCellBasic currentPosition;
    private HorizontalTunnelBasic currentTunnel;
    private TunnelCellBasic entrance;

    @Override
    public void initialize(PlayerGameInterface gameInterface, PipeBasic pipe, PlayerToken token) {
        if (super.getToken().validate(token)) {
            super.initialize(gameInterface, pipe, token);
            this.state = State.BEGIN;
            this.currentDir = this.head.getDirection();
            this.currentPosition = pipe.getHead().getCurrentPosition();
            this.currentTunnel = null;
            this.entrance = null;
        }
    }

    @Override
    public void act(PlayerToken token) {
        if (!super.getToken().validate(token))
            return;
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

    private void handleEntertunnel() {
        //pipe.fire();
        segmentStack.push(pipe.calculateNextSegment(currentDir));
        if (pipe.getHead().getCurrentPosition().getCellType() == TunnelCellType.EXIT) {
            currentDir = Direction.DOWN;
            this.state = State.BEGIN;
        }
    }

    private TunnelCellBasic findNearestExit() {
        Set<TunnelCellBasic> exits = this.currentTunnel.getCellsBySearchCriteria((cell) ->
                cell.getCellType() == TunnelCellType.EXIT
        );
        if ( ! exits.isEmpty() ) {
            TunnelCellBasic nearest = exits.iterator().next();
            int nearest_diff = Math.abs(nearest.getX() - head.getX()), diff_x;
            for (TunnelCellBasic cell : exits) {
                diff_x = Math.abs(head.getX() - cell.getX());
                if (diff_x < nearest_diff)
                    nearest = cell;
            }
            return nearest;
        } else
            return null;
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
                TunnelCellBasic exit = this.findNearestExit();
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
        head.getWeapon().getBulletQueue().enqueue(gameInterface.getGameShop().buyBullet(this));
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
