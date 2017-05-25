package szakacs.kpi.fei.tuke.arena.actors;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

import java.util.Collection;
import java.util.Set;

/**
 * A class representing an enemy in the form of a mole.
 * It moves left to right or vice versa and upon contact
 * with a pipe segment starts damaging its health value.
 */
public class Mole extends AbstractMoveableActor {

    private Collection<Pipe> pipes;
    private Pipe intersectingPipe;
    private int xDelta;
    private int yDelta;
    private boolean moving;

    public Mole(Direction direction, TunnelCellUpdatable currentPosition, ActorGameInterface gameInterface){
        super(currentPosition, ActorType.ENEMY, direction, gameInterface);
        this.xDelta = gameInterface.getGameWorld().getOffsetX()/8;
        this.yDelta = gameInterface.getGameWorld().getOffsetY()/8;
        this.pipes = gameInterface.getPipesUpdatable().values();
        this.moving = true;
    }

    /**
     * Moves by offsetX or offsetY in the initially set direction.
     * If the end of a tunnel in the given direction was reached,
     * this actor unregisters itself.
     *
     * If a pipe intersects this mole, it stops moving
     * and proceeds to start damaging the pipe.
     *
     * @param authToken An authentication token to verify the caller
     */
    @Override
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
