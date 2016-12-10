package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

/**
 * Created by developer on 7.12.2016.
 */
public abstract class AbstractActor implements Actor {

    protected int x;
    protected int y;
    protected ActorType actorType;
    protected Direction dir;
    protected ManipulableGameInterface world;

    protected AbstractActor(ManipulableGameInterface world){
        if (world == null)
            throw new IllegalArgumentException("No game world passed");
        this.world = world;
    }

    protected AbstractActor(int x, int y, ActorType type, Direction dir, ManipulableGameInterface world){
        this.x = x;
        this.y = y;
        this.actorType = type;
        this.dir = dir;
        this.world = world;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public ActorType getType() {
        return this.actorType;
    }

    @Override
    public Direction getDirection() {
        return this.dir;
    }

    @Override
    public boolean intersects(Actor other){
        return other != null && other.getX() / world.getOffsetX() == this.x / world.getOffsetX()
                && other.getY() / world.getOffsetY() == this.y / world.getOffsetY();
    }

    public abstract void act(ManipulableGameInterface world);
}
