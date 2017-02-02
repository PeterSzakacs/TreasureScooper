package com.szakacs.kpi.fei.tuke.game.arena.actors;

import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

import java.util.List;

/**
 * Created by developer on 6.11.2016.
 */
public class Bullet extends AbstractMoveableActor {
    private int xDelta;
    private int yDelta;

    public Bullet(ActorGameInterface world){
        super(ActorType.BULLET, world);
    }

    public void act(ActorGameInterface world){
        if (world != null && world.equals(super.world)) {
            this.move(xDelta, yDelta, super.getDirection());
            if (boundReached()) {
                super.world.unregisterActor(this);
            } else {
                List<Actor> intersecting = super.world.getActorsBySearchCriteria(
                        (Actor a) ->
                                (a.getType() != ActorType.BULLET && a.getType() != ActorType.PIPEHEAD)
                                && a.intersects(this));
                if (!intersecting.isEmpty()) {
                    super.world.unregisterActor(intersecting.get(0));
                    super.world.unregisterActor(this);
                }
            }
        }
    }

    public void launch(TunnelCell position, Direction dir, ActorGameInterface world){
        System.out.println("launching Bullet");
        if (world != null && world.equals(this.world)) {
            /*TunnelCell current = position.getCellAtDirection(dir);
            if (current == null)
                return;
            Collection<HorizontalTunnel> world = world.getTunnels();
            for (HorizontalTunnel ht : world) {
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
