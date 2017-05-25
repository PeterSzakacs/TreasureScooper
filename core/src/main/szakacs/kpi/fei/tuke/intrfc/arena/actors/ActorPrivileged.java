package szakacs.kpi.fei.tuke.intrfc.arena.actors;

/**
 * The interface of a game actor that is visible only to the actor manager object.
 */
public interface ActorPrivileged extends ActorUpdatable {

    /**
     * Performs actor-specific actions at every iteration of the game loop.
     *
     * @param authToken An authentication token to verify the caller
     *                  is allowed to call this method.
     */
    void act(Object authToken);
}
