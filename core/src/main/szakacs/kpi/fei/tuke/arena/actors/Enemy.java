package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by developer on 5.11.2016.
 */
public class Enemy extends AbstractMoveableActor {

    // The Y coordinates are already in the HorizontalTunnel object.
    private Collection<Pipe> pipes;
    private Pipe intersectingPipe;
    private int xDelta;
    private int yDelta;
    private boolean moving;

    public Enemy(Direction direction, TunnelCell currentPosition, ActorGameInterface world){
        super(currentPosition, ActorType.MOLE, direction, world);
        this.xDelta = world.getOffsetX()/4;
        this.yDelta = world.getOffsetY()/4;
        List<Player> players = world.getPlayers();
        this.pipes = new ArrayList<>(players.size());
        for (Player player : players) {
            this.pipes.add(world.getPipeOfPlayer(player));
        }
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
                for (Pipe pipe : pipes) {
                    if (pipe.intersects(this)) {
                        this.intersectingPipe = pipe;
                        moving = false;
                        pipe.setHealth(pipe.getHealth() - 10, world);
                        break;
                    }
                }
            } else {
                intersectingPipe.setHealth(intersectingPipe.getHealth() - 10, world);
                if (intersectingPipe.getHealth() <= 0)
                    moving = true;
            }
        }
    }
}
