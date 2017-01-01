package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 31.12.2016.
 */
public abstract class AbstractMoveableActor extends AbstractActor {

    private boolean boundReached;

    protected AbstractMoveableActor(ManipulableGameInterface world, ActorType at) {
        super(world, at);
    }

    protected AbstractMoveableActor(TunnelCell currentPosition, ActorType type, Direction dir, ManipulableGameInterface world) {
        super(currentPosition, type, dir, world);
    }

    protected void setDirection(Direction direction){
        super.setDirection(direction);
        this.boundReached = false;
    }

    protected void move(int dxAbs, int dyAbs, Direction dir){
        if (getDirection() != dir) {
            this.boundReached = false;
            setDirection(dir);
        }
        int x = getX() + dir.getXStep() * dxAbs;
        int y = getY() + dir.getYStep() * dyAbs;
        TunnelCell prev, next;
        prev = next = getCurrentPosition();
        do {
            if (Math.abs(next.getX() - x) < world.getOffsetX()
                    && Math.abs(next.getY() - y) < world.getOffsetY()) {
                setCurrentPosition(next);
                break;
            }
            prev = next;
            next = next.getCellAtDirection(dir);
        } while ( next != null );
        if (next != null){
            // successfully found the new position
            setX(x);
            setY(y);
        } else {
            //reached the end of a tunnel in a given direction
            setX(prev.getX());
            setY(prev.getY());
            setCurrentPosition(prev);
            this.boundReached = true;
        }
    }

    protected void move(int dx, int dy){
        this.move(dx, dy, Direction.getDirectionByDeltas(dx, dy));
    }

    protected boolean boundReached(){
        return this.boundReached;
    }
}
