package szakacs.kpi.fei.tuke.intrfc.arena.actors;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 6.11.2016.
 *
 * The interface implemented by all game actors
 *
 * A game actor is defined as any game object
 * implementing this interface and whose size
 * on the screen is offsetX * offsetY.
 */
public interface Actor {

    /**
     * Gets the horizontal coordinate of the actor (it is within its current position).
     */
    int getX();

    /**
     * Gets the vertical coordinate of the actor (it is within its current position)
     */
    int getY();

    /**
     * Gets the type of the actor (note that this is not the actual class of the object,
     * just an enum constant saying whether the actor is an Enemy (multiple kinds possible)
     * some kind of ammunition (multiple kinds possible) etc.
     */
    ActorType getType();

    /**
     * Gets the current orientation of the actor relative to the screen
     */
    Direction getDirection();

    /**
     * Called on every iteration of the game loop (the world passed as argument is only
     * method caller authentication of sorts)
     */
    void act(ActorGameInterface world);

    /**
     * Returns true if the actor intersects the actor passed as parameter to this method.
     * Most commonly this means whether their current position, or TunnelCell objects are
     * equal).
     */
    boolean intersects(Actor other);

    /**
     * Gets the current position of the actor (i.e. a TunnelCell object which is the current
     * cell within which the actor is located).
     */
    TunnelCell getCurrentPosition();
}
