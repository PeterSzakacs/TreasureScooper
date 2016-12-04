package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.ManipulableActor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

import java.util.Collection;
import java.util.List;

/**
 * Created by developer on 6.11.2016.
 */
public class Bullet implements ManipulableActor {
    private Direction dir;
    private ActorType actorType;
    private int x;
    private int y;
    private int xDelta;
    private int yDelta;
    private int xBound;
    private int yBound;
    private TunnelCell current;
    private ManipulableGameInterface world;
    private HorizontalTunnel tunnel;
    private Enemy target;

    public Bullet(){
        this.actorType = ActorType.BULLET;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public ActorType getType() {
        return this.actorType;
    }

    @Override
    public Direction getDirection() {
        return this.dir;
    }

    public void act(){
        this.x += xDelta;
        this.y += yDelta;
        if (boundReached()) {
            world.unregisterActor(this);
        }else{
            if (world.intersects(this, target)) {
                tunnel.destroyEnemy(target);
                world.unregisterActor(this);
            }
        }
    }

    void launch(TunnelCell position, Direction dir, ManipulableGameInterface world){
        this.current = position.getCellAtDirection(dir);
        if (this.current == null)
            return;
        Collection<HorizontalTunnel> tunnels = world.getTunnels();
        for (HorizontalTunnel ht : tunnels){
            if (ht.getCells().contains(this.current)){
                this.tunnel = ht;
                break;
            }
        }
        this.x = position.getX();
        this.y = position.getY();
        this.world = world;
        this.dir = dir;
        world.registerActor(this);
        switch (dir){
            case LEFT:
                this.xDelta = -world.getOffsetX() * 2;
                this.yDelta = 0;
                break;
            case RIGHT:
                this.xDelta = world.getOffsetX() * 2;
                this.yDelta = 0;
                break;
            case DOWN:
                this.xDelta = 0;
                this.yDelta = -world.getOffsetY() * 2;
                break;
            case UP:
                this.xDelta = 0;
                this.yDelta = world.getOffsetY() * 2;
                break;
            default:
                world.unregisterActor(this);
                System.err.println("unknown direction value passed as argument: " + dir.name());
        }
        this.setBound(position, dir);
        this.lockTarget();
    }

    private void lockTarget(){
        List<Actor> enemies = this.world.getActorsBySearchCriteria(
                (Actor a) -> a.getType() == ActorType.MOLE
        );
        int diff = tunnel.getWidth();
        for (Actor enemy : enemies){
            if (Math.abs(enemy.getX() - this.x) < diff) {
                target = (Enemy) enemy;
                diff = Math.abs(target.getX() - this.x);
            }
        }
    }

    private void setBound(TunnelCell currentPosition, Direction dir){
        TunnelCell next = currentPosition.getCellAtDirection(dir),
                cur = currentPosition;
        while(next != null) {
            cur = next;
            next = cur.getCellAtDirection(dir);
        }
        switch (dir){
            case LEFT:
                this.xBound = cur.getX() - world.getOffsetX();
                this.yBound = cur.getY();
                break;
            case RIGHT:
                this.xBound = cur.getX() + world.getOffsetX();
                this.yBound = cur.getY();
                break;
            case UP:
                this.xBound = cur.getX();
                this.yBound = cur.getY() + world.getOffsetY();
                break;
            case DOWN:
                this.xBound = cur.getX();
                this.yBound = cur.getY() - world.getOffsetY();
                break;
        }
    }

    private boolean boundReached(){
        switch (dir){
            case DOWN:
                return this.y <= yBound;
            case RIGHT:
                return this.x >= xBound;
            case LEFT:
                return this.x <= xBound;
            case UP:
                return this.y >= yBound;
        }
        return false;
    }
}
