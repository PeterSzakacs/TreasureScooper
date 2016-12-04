package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;
import com.szakacs.kpi.fei.tuke.game.intrfc.ManipulableActor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.QueryableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.eventHandlers.TunnelEventHandler;

import java.util.List;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy implements ManipulableActor {

    // The Y coordinates are already in the HorizontalTunnel object.

    private int x;
    private int y;
    private Direction direction;
    private QueryableGameInterface world;
    private Pipe player;
    private int movementDelta;
    private boolean moving;
    private int bound;
    private TunnelEventHandler handler;

    public Enemy(Direction leftToRightDirection, HorizontalTunnel ht, TunnelEventHandler handler, QueryableGameInterface world){
        this.direction = leftToRightDirection;
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
        this.world = world;
        this.player = world.getPipe();
        this.moving = true;
        this.handler = handler;
    }

    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    @Override
    public ActorType getType() {
        return ActorType.MOLE;
    }

    @Override
    public Direction getDirection(){
        return this.direction;
    }

    public void act(){
        if (moving) {
            this.x += this.movementDelta;
            if (outOfBounds()) {
                this.handler.onEnemyDestroyed(this);
                return;
            }
            if (this.player.getHeadY() < this.y) {
                List<PipeSegment> res = this.player.getSegmentByCriteria(
                        (PipeSegment seg) -> seg.getSegmentType() != PipeSegmentType.HORIZONTAL
                                && seg.getSegmentType() != PipeSegmentType.VERTICAL
                                && Math.abs(seg.getX() - this.x) < world.getOffsetX()
                                && seg.getY() == this.y
                );
                if (!res.isEmpty()) {
                    this.moving = false;
                    this.player.damagePipe();
                }
            }
        } else
            this.player.damagePipe();
    }

    private boolean outOfBounds(){
        return (this.direction == Direction.RIGHT && this.x > this.bound)
                || this.direction == Direction.LEFT && this.x < this.bound;
    }
}
