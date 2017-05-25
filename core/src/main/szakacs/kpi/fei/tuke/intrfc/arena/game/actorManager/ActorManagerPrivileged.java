package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;
import szakacs.kpi.fei.tuke.intrfc.misc.UnregisteredActorInfo;

import java.util.Map;
import java.util.Set;

/**
 * Extension of the {@link ActorManagerUpdatable} interface for the actor manager object
 * that is exposed to the core backend classes managing the game logic as well as any
 * classes external to the game logic.
 */
public interface ActorManagerPrivileged extends ActorManagerUpdatable, GameUpdater {

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
    Set<UnregisteredActorInfo> getUnregisteredActors();
}
