package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.Collection;
import java.util.List;

/**
 * Created by developer on 6.11.2016.
 */
public class Bullet extends AbstractMoveableActor {
    private int xDelta;
    private int yDelta;

    public Bullet(ManipulableGameInterface world){
        super(world, ActorType.BULLET);
    }

    public void act(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            this.move(xDelta, yDelta, super.getDirection());
            if (boundReached()) {
                world.unregisterActor(this);
            } else {
                List<Actor> intersecting = world.getActorsBySearchCriteria(
                        (Actor a) ->
                                (a.getType() != ActorType.BULLET && a.getType() != ActorType.PIPEHEAD)
                                && a.intersects(this));
                if (!intersecting.isEmpty()) {
                    world.unregisterActor(intersecting.get(0));
                    world.unregisterActor(this);
                }
            }
        }
    }

    public void launch(TunnelCell position, Direction dir, ManipulableGameInterface world){
        System.out.println("launching Bullet");
        if (world != null && world.equals(this.world)) {
            /*TunnelCell current = position.getCellAtDirection(dir);
            if (current == null)
                return;
            Collection<HorizontalTunnel> tunnels = world.getTunnels();
            for (HorizontalTunnel ht : tunnels) {
                if (ht.getCells().contains(current)) {
                    break;
                }
            }*/
            this.initialize(dir, position);
            this.xDelta = world.getOffsetX() * 2;
            this.yDelta = world.getOffsetY() * 2;
            world.registerActor(this);
        }
    }

    @Override
    public String toString(){
        return super.toString() + " moving: " + super.getDirection()
                + "\nX: " + super.getX() + " Y: " + super.getY();
    }
}
