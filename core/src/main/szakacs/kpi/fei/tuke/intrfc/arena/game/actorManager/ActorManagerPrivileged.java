package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

import java.util.Map;

/**
 * Extension of the "updatable" interface for the actor manager object.
 *
 * This interface is designed to be exposed only to game backend classes
 * which need access to specific state and behavior of the actor manager.
 */
public interface ActorManagerPrivileged extends ActorManagerUpdatable, GameUpdater, ResettableGameClass {

    /**
     * Updates all game actors (except actors unregistered in the previous turn),
     */
    void update();

    /**
     * Gets a map of all unregistered actors (those whose update() method is not called
     * anymore) and a value detailing for how many game loop iterations they have been
     * unregistered (only kept for N further iterations, where N is arbitrary).
     * Primarily, these values serve for effects rendering when an actor is removed.
     *
     * @return a mapping between unregistered actors and the number of turns since it was unregistered.
     */
    Map<ActorBasic, Integer> getUnregisteredActors();
}
