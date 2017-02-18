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

    public Enemy(Direction direction, TunnelCell currentPosition, ActorGameInterface gameInterface){
        super(currentPosition, ActorType.MOLE, direction, gameInterface);
        this.xDelta = gameInterface.getGameWorld().getOffsetX()/4;
        this.yDelta = gameInterface.getGameWorld().getOffsetY()/4;
        List<Player> players = gameInterface.getPlayers();
        this.pipes = new ArrayList<>(players.size());
        for (Player player : players) {
            this.pipes.add(gameInterface.getPipeOfPlayer(player));
        }
        this.moving = true;
    }

    public void act(ActorGameInterface gameInterface){
        if (gameInterface != null && gameInterface.equals(super.gameInterface)) {
            if (moving) {
                this.move(xDelta, yDelta, getDirection());
                if (boundReached()) {
                    gameInterface.unregisterActor(this);
                    return;
                }
                for (Pipe pipe : pipes) {
                    if (pipe.intersects(this)) {
                        this.intersectingPipe = pipe;
                        moving = false;
                        pipe.setHealth(pipe.getHealth() - 10, gameInterface);
                        break;
                    }
                }
            } else {
                intersectingPipe.setHealth(intersectingPipe.getHealth() - 10, gameInterface);
                if (intersectingPipe.getHealth() <= 0)
                    moving = true;
            }
        }
    }
}
