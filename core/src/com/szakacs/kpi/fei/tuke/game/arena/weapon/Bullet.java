package com.szakacs.kpi.fei.tuke.game.arena.weapon;

import com.szakacs.kpi.fei.tuke.game.arena.AbstractActor;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

import java.util.Collection;
import java.util.List;

/**
 * Created by developer on 6.11.2016.
 */
public class Bullet extends AbstractActor {
    private int xDelta;
    private int yDelta;
    private int xBound;
    private int yBound;

    public Bullet(ManipulableGameInterface world){
        super(world);
        this.actorType = ActorType.BULLET;
    }

    public void act(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            this.x += xDelta;
            this.y += yDelta;
            if (boundReached()) {
                world.unregisterActor(this);
            } else {
                List<Actor> intersecting = world.getActorsBySearchCriteria(
                        (Actor a) ->
                        a.getType() == ActorType.MOLE
                                && a.intersects(this));
                if (!intersecting.isEmpty()) {
                    world.unregisterActor(intersecting.get(0));
                    world.unregisterActor(this);
                }
            }
        }
    }

    public void launch(TunnelCell position, Direction dir, ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            TunnelCell current = position.getCellAtDirection(dir);
            if (current == null)
                return;
            Collection<HorizontalTunnel> tunnels = world.getTunnels();
            for (HorizontalTunnel ht : tunnels) {
                if (ht.getCells().contains(current)) {
                    break;
                }
            }
            this.x = position.getX();
            this.y = position.getY();
            this.dir = dir;
            this.xDelta = dir.getXStep() * world.getOffsetX() * 2;
            this.yDelta = dir.getYStep() * world.getOffsetY() * 2;
            world.registerActor(this);
            this.setBound(position, dir);
        }
    }

    private void setBound(TunnelCell currentPosition, Direction dir){
        TunnelCell next = currentPosition.getCellAtDirection(dir),
                cur = currentPosition;
        while(next != null) {
            cur = next;
            next = cur.getCellAtDirection(dir);
        }
        this.xBound = cur.getX() + dir.getXStep() * world.getOffsetX();
        this.yBound = cur.getY() + dir.getYStep() * world.getOffsetY();
    }

    private boolean boundReached(){
        switch (this.dir){
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
