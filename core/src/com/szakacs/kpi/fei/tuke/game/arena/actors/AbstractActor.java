package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

/**
 * Created by developer on 7.12.2016.
 */
public abstract class AbstractActor implements Actor {

    private int x;
    private int y;
    private TunnelCell currentPosition;
    private Direction dir;
    protected ActorType actorType;
    protected ActorGameInterface world;

    protected AbstractActor(ActorGameInterface world, ActorType at){
        if (world == null)
            throw new IllegalArgumentException("No game world passed");
        this.world = world;
        this.actorType = at;
    }

    protected AbstractActor(TunnelCell currentPosition, ActorType type, Direction dir, ActorGameInterface world){
        this.actorType = type;
        this.world = world;
        this.initialize(dir, currentPosition);
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
        return other != null && other.getCurrentPosition() == this.currentPosition;
    }

    @Override
    public TunnelCell getCurrentPosition(){
        return this.currentPosition;
    }

    protected void setDirection(Direction direction) {
        this.dir = direction;
    }

    protected void setCurrentPosition(TunnelCell currentPosition){
        this.currentPosition = currentPosition;
    }

    protected void setX(int x){
        this.x = x;
    }

    protected void setY(int y){
        this.y = y;
    }

    protected void initialize(Direction dir, TunnelCell currentPosition){
        this.dir = dir;
        this.currentPosition = currentPosition;
        this.x = currentPosition.getX();
        this.y = currentPosition.getY();
    }
}
