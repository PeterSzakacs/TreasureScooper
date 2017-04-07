package szakacs.kpi.fei.tuke.intrfc.arena.actors;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;

/**
 * Created by developer on 6.11.2016.
 *
 * The interface implemented by all game actors
 *
 * A game actor is defined as any game object
 * implementing this interface and whose size
 * on the screen is offsetX * offsetY.
 */
public interface ActorBasic {

    int getX();
    int getY();
    TunnelCell getCurrentPosition();

    /**
     * Returns an actorRectangle object which contains actor properties related to position.
     */
    ActorRectangle getActorRectangle();

    /**
     * Gets the type of the actor (note that this is not the actual class of the object,
     * just an enum constant saying whether the actor is an Mole (multiple kinds possible)
     * some kind of ammunition (multiple kinds possible) etc.
     */
    ActorType getType();

    /**
     * Gets the current orientation of the actor relative to the screen
     */
    Direction getDirection();

    /**
     * Returns true if the actor intersects the actor passed as parameter to this method.
     * Most commonly this means whether their current position, or TunnelCell objects are
     * equal).
     */
    boolean intersects(ActorBasic other);
}
