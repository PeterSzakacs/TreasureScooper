package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

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
            } /*else {
                List<Actor> intersecting = super.world.getActorsBySearchCriteria(
                        (Actor a) ->
                                (a.getType() != ActorType.BULLET
                                        && a.getType() != ActorType.PIPEHEAD)
                                        && a.intersects(this));
                if (!intersecting.isEmpty()) {
                    for (Actor actor : intersecting)
                        super.world.unregisterActor(actor);
                    super.world.unregisterActor(this);
                }
            }*/
        }
    }

    public void launch(TunnelCell position, Direction dir, ActorGameInterface world){
        System.out.println("launching Bullet");
        if (world != null && world.equals(this.world)) {
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
