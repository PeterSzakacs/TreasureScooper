package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.QueryableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.TunnelEventHandler;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy extends AbstractActor {

    // The Y coordinates are already in the HorizontalTunnel object.
    private Pipe player;
    private int movementDelta;
    private boolean moving;
    private int bound;
    private TunnelEventHandler handler;

    public Enemy(Direction leftToRightDirection, HorizontalTunnel ht, TunnelEventHandler handler, ManipulableGameInterface world){
        super(world);
        this.dir = leftToRightDirection;
        if (leftToRightDirection == Direction.RIGHT) {
            this.x = ht.getX() - world.getOffsetX();
            this.movementDelta = world.getOffsetX() / 4;
            this.bound = ht.getWidth();
        } else {
            this.x = ht.getWidth();
            this.movementDelta = -world.getOffsetX() / 4;
            this.bound = ht.getX();
        }
        this.y = ht.getY();
        this.actorType = ActorType.MOLE;
        this.world = world;
        this.player = world.getPipe();
        this.moving = true;
        this.handler = handler;
    }

    public void act(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            if (moving) {
                this.x += this.movementDelta;
                if (outOfBounds()) {
                    this.handler.onEnemyDestroyed(this);
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

    private boolean outOfBounds(){
        return (this.dir == Direction.RIGHT && this.x > this.bound)
                || this.dir == Direction.LEFT && this.x < this.bound;
    }
}
