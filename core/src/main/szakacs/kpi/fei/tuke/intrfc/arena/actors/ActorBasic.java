package szakacs.kpi.fei.tuke.intrfc.arena.actors;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorRectangle;

/**
 * The interface implemented by all game actors.
 * This is a safe subset of actor functionality
 * to be exposed to the player.
 */
public interface ActorBasic {

    /**
     * Gets the horizontal coordinate of the actor.
     *
     * @return the horizontal coordinate of the actor.
     */
    int getX();

    /**
     * Gets the vertical coordinate of the actor.
     *
     * @return the vertical coordinate of the actor.
     */
    int getY();

    /**
     * Gets the tunnel cell where the actor is currently located.
     *
     * @return the tunnel cell where the actor is currently located.
     */
    TunnelCell getCurrentPosition();

    /**
     * Returns an actorRectangle object which contains actor properties related to position.
     *
     * @return an unmodifiable view of the actor's rectangle object.
     */
    ActorRectangle getActorRectangle();

    /**
     * Gets the general category of the actor. Note: this is not the actual class of an actor,
     * just an enum constant grouping possibly multiple actor classes according to their role
     * within the game (different Enemies, different calibers of Bullets etc.).
     *
     * @return The ActorType of the actor.
     */
    ActorType getType();

    /**
     * Gets the current orientation of the actor relative to the screen (facing left, right, up, down).
     * Possibly could be better named getOrientation().
     *
     * @return the {@link Direction} that the actor is facing in.
     */
    Direction getDirection();

    /**
     * Returns true if this actor intersects the actor passed as parameter to this method.
     * Most commonly this means whether their current position, or TunnelCell objects are
     * equal, though if not, it checks if their rectangles touch or overlap at any point).
     *
     * @param other The actor to check if it intersects this actor.
     *
     * @return boolean true if the given actor is not null and intersects this actor | false otherwise.
     */
    boolean intersects(ActorBasic other);
}
