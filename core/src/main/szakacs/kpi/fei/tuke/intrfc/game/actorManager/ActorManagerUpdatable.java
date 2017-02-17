package szakacs.kpi.fei.tuke.intrfc.game.actorManager;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;

/**
 * Created by developer on 29.1.2017.
 *
 * Extension of the basic interface for the Actor manager object.
 *
 * This interface is designed to be exposed to game actors.
 */
public interface ActorManagerUpdatable extends ActorManagerQueryable {

    /**
     * Sets a method (an action) that is to be called (performed)
     * when the actor given as argument is unregistered, that is,
     * removed from the game.
     *
     * @param actor: The actor
     * @param action: The action to be performed when the actor is unregistered,
     *              must be a method returning void and taking no arguments
     */
    void setOnDestroy(Actor actor, Runnable action);

    /**
     * Registers an actor, thereby adding the actor to the game world
     *
     * @param actor Not Null: The actor to add to the game world
     *
     */
    void registerActor(Actor actor);

    /**
     * Unregisters an actor (removing from the game, in the sense that update()
     * is no longer called),
     *
     * Note that the actual implementation can use late removal to prevent
     * ConcurrentModificationException.
     *
     * Optionally also executes a method that is to be called when the actor
     * is unregistered if such a method has been set, otherwise it does nothing.
     *
     * @param actor Not Null: The actor to remove
     */
    void unregisterActor(Actor actor);
}
