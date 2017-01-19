package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.actors.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.OnActorRemovedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy extends AbstractMoveableActor {

    // The Y coordinates are already in the HorizontalTunnel object.
    private Pipe player;
    private int xDelta;
    private int yDelta;
    private boolean moving;
    private OnActorRemovedCallback onDestroyCallback;

    public Enemy(Direction direction, TunnelCell currentPosition, OnActorRemovedCallback onDestroyCallback,
                 ManipulableGameInterface world){
        super(currentPosition, ActorType.MOLE, direction, world);
        this.xDelta = world.getOffsetX()/4;
        this.yDelta = world.getOffsetY()/4;
        this.player = world.getPipe();
        this.moving = true;
        this.onDestroyCallback = onDestroyCallback;
    }

    public void act(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            if (moving) {
                this.move(this.xDelta, this.yDelta, super.getDirection());
                if (boundReached()) {
                    this.world.unregisterActor(this);
                    this.onDestroyCallback.onRemove(this);
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
}
