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
public class Mole extends AbstractMoveableActor {

    private Collection<Pipe> pipes;
    private Pipe intersectingPipe;
    private int xDelta;
    private int yDelta;
    private boolean moving;

    public Mole(Direction direction, TunnelCell currentPosition, ActorGameInterface gameInterface){
        super(currentPosition, ActorType.ENEMY, direction, gameInterface);
        this.xDelta = gameInterface.getGameWorld().getOffsetX()/4;
        this.yDelta = gameInterface.getGameWorld().getOffsetY()/4;
        this.pipes = new ArrayList<>(gameInterface.getPlayerToPipeMap().values());
        this.moving = true;
    }

    public void act(Object authToken){
        if (gameInterface.getAuthenticator().authenticate(authToken)) {
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
                        pipe.setHealth(pipe.getHealth() - 10, gameInterface.getAuthenticator());
                        break;
                    }
                }
            } else {
                intersectingPipe.setHealth(intersectingPipe.getHealth() - 10, gameInterface.getAuthenticator());
                if (intersectingPipe.getHealth() <= 0)
                    moving = true;
            }
        }
    }
}
