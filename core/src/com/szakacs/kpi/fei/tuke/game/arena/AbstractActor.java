package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

/**
 * Created by developer on 7.12.2016.
 */
public abstract class AbstractActor implements Actor {

    private int x;
    private int y;
    private TunnelCell currentPosition;
    private Direction dir;
    private boolean boundReached;
    protected ActorType actorType;
    protected ManipulableGameInterface world;

    protected AbstractActor(ManipulableGameInterface world){
        if (world == null)
            throw new IllegalArgumentException("No game world passed");
        this.world = world;
    }

    protected AbstractActor(TunnelCell currentPosition, ActorType type, Direction dir, ManipulableGameInterface world){
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
        this.boundReached = false;
    }

    protected void initialize(Direction dir, TunnelCell currentPosition){
        this.setDirection(dir);
        this.currentPosition = currentPosition;
        this.x = currentPosition.getX();
        this.y = currentPosition.getY();
    }

    protected void move(int dxAbs, int dyAbs, Direction dir){
        if (this.dir != dir) {
            this.boundReached = false;
            this.dir = dir;
        }
        int x = this.x + dir.getXStep() * dxAbs;
        int y = this.y + dir.getYStep() * dyAbs;
        TunnelCell prev, next;
        prev = next = currentPosition;
        do{
            if (Math.abs(next.getX() - x) < world.getOffsetX()
                    && Math.abs(next.getY() - y) < world.getOffsetY()) {
                currentPosition = next;
                break;
            }
            prev = next;
            next = next.getCellAtDirection(dir);
        } while ( next != null );
        if (next != null){
            // successfully found the new position
            this.x = x;
            this.y = y;
        } else {
            //reached the end of a tunnel in a given direction
            this.x = prev.getX();
            this.y = prev.getY();
            this.currentPosition = prev;
            this.boundReached = true;
        }
    }

    protected void move(int dx, int dy){
        this.move(dx, dy, Direction.getDirectionByDeltas(dx, dy));
    }

    protected boolean boundReached(){
        return this.boundReached;
    }

    public abstract void act(ManipulableGameInterface world);
}
