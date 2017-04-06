package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;

/**
 * Created by developer on 29.1.2017.
 *
 * Extension of the basic interface for the Actor manager object.
 *
 * This interface is designed to be exposed to game actors.
 */
public interface ActorManagerUpdatable extends ActorManagerBasic {

    /**
     * Registers an actor, thereby adding the actor to the game level.
     * Also optionally sets a method (an action) that is to be called
     * (performed) when the actor given as argument is unregistered,
     * that is, removed from the game level.
     *
     * @param actor Not Null: The actor to add to the game level.
     * @param action Optional: The action to be performed when the actor is unregistered,
     *               Must be a reference to a method returning void and taking no arguments
     *               or null if nothing is to be performed on a given actors removal.
     */
    void registerActor(ActorPrivileged actor, Runnable action);

    /**
     * Unregisters an actor (removing from it from the game, in the sense
     * that its update() method is no longer called),
     *
     * Note that the actual implementation can use late removal to prevent
     * ConcurrentModificationException.
     *
     * Optionally also executes a method that is to be called when the actor
     * is unregistered if such a method has been set, otherwise it does nothing.
     *
     * @param actor Not Null: The actor to remove
     */
    void unregisterActor(ActorBasic actor);
}
