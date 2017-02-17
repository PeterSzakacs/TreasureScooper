package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy extends AbstractMoveableActor {

    // The Y coordinates are already in the HorizontalTunnel object.
    private Pipe pipe;
    private int xDelta;
    private int yDelta;
    private boolean moving;

    public Enemy(Direction direction, TunnelCell currentPosition, ActorGameInterface world){
        super(currentPosition, ActorType.MOLE, direction, world);
        this.xDelta = world.getOffsetX()/4;
        this.yDelta = world.getOffsetY()/4;
        this.pipe = world.getPipe();
        this.moving = true;
    }

    public void act(ActorGameInterface world){
        if (world != null && world.equals(super.world)) {
            if (moving) {
                this.move(xDelta, yDelta, super.getDirection());
                if (boundReached()) {
                    world.unregisterActor(this);
                    return;
                }
                if (pipe.intersects(this)) {
                    moving = false;
                    pipe.setHealth(pipe.getHealth() - 10, world);
                }
            } else {
                pipe.setHealth(pipe.getHealth() - 10, world);
                if (pipe.getHealth() <= 0)
                    moving = true;
            }
        }
    }
}
