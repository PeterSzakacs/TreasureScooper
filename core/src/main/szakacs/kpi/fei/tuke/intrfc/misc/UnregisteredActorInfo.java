package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;

/**
 * A value object containing information about an actor instance
 * that has been removed from the game.
 */
public interface UnregisteredActorInfo {

    /**
     * Returns the number of iterations of the game loop since this actor
     * was unregistered (removed) from the game.
     *
     * @return the number of iterations of the game loop since this actor
     *         was unregistered (removed) from the game.
     */
    int getTurnCountSinceUnregister();

    /**
     * Returns the instance of this unregistered actor.
     *
     * @return the instance of this unregistered actor.
     */
    ActorBasic getActor();
}
