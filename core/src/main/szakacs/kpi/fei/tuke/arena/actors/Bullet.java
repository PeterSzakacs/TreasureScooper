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

    public Bullet(ActorGameInterface gameInterface){
        super(ActorType.BULLET, gameInterface);
    }

    public void act(Object authToken){
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
            this.move(xDelta, yDelta, super.getDirection());
            if (boundReached()) {
                super.gameInterface.unregisterActor(this);
            } /*else {
                List<Actor> intersecting = super.gameInterface.getActorsBySearchCriteria(
                        (Actor a) ->
                                (a.getType() != ActorType.BULLET
                                        && a.getType() != ActorType.PIPEHEAD)
                                        && a.intersects(this));
                if (!intersecting.isEmpty()) {
                    for (Actor actor : intersecting)
                        super.gameInterface.unregisterActor(actor);
                    super.gameInterface.unregisterActor(this);
                }
            }*/
        }
    }

    public void launch(TunnelCell position, Direction dir, ActorGameInterface gameInterface){
        System.out.println("launching Bullet");
        if (gameInterface != null && gameInterface.equals(super.gameInterface)) {
            super.setDirection(dir);
            super.setCurrentPosition(position);
            this.xDelta = gameInterface.getGameWorld().getOffsetX() * 2;
            this.yDelta = gameInterface.getGameWorld().getOffsetY() * 2;
            gameInterface.registerActor(this);
            super.move(xDelta, yDelta, dir);
        }
    }

    @Override
    public String toString(){
        return super.toString() + " moving: " + super.getDirection()
                + "\nX: " + super.getX() + " Y: " + super.getY();
    }
}
