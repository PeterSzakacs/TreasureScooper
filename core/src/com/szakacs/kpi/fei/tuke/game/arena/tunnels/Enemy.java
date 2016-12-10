package com.szakacs.kpi.fei.tuke.game.arena.tunnels;

import com.szakacs.kpi.fei.tuke.game.arena.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy extends AbstractActor {

    // The Y coordinates are already in the HorizontalTunnel object.
    private Pipe player;
    private int xDelta;
    private int yDelta;
    private int xBound;
    private int yBound;
    private boolean moving;

    public Enemy(Direction direction, TunnelCell currentPosition, ManipulableGameInterface world){
        super(world);
        this.dir = direction;
        this.x = currentPosition.getX();
        this.y = currentPosition.getY();
        this.actorType = ActorType.MOLE;
        this.xDelta = (direction.getXStep() * world.getOffsetX())/4;
        this.yDelta = (direction.getYStep() * world.getOffsetY())/4;
        this.player = world.getPipe();
        this.moving = true;
        this.setBound(currentPosition);
    }

    public void act(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            if (moving) {
                this.x += this.xDelta;
                this.y += this.yDelta;
                if (outOfBounds()) {
                    this.world.unregisterActor(this);
                    return;
                }
                if (this.player.intersects(this)) {
                    this.moving = false;
                    this.player.damagePipe();
                }
            } else
                this.player.damagePipe();
        }
    }

    private void setBound(TunnelCell currentPosition){
        TunnelCell next = currentPosition.getCellAtDirection(this.dir),
                cur = currentPosition;
        while(next != null) {
            cur = next;
            next = cur.getCellAtDirection(this.dir);
        }
        this.xBound = cur.getX();
        this.yBound = cur.getY();
    }

    private boolean outOfBounds(){
        return (this.dir == Direction.RIGHT && this.x > this.xBound)
                || this.dir == Direction.LEFT && this.x < this.xBound;
    }
}
