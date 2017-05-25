package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * An extension to the {@link GameLevelUpdatable} interface that is exposed
 * to the core backend classes managing the game logic as well as
 * any classes external to the game logic.
 */
public interface GameLevelPrivileged extends GameLevelUpdatable {

    /**
     * Starts a new game level according to information contained
     * in the passed DummyLevel value object.
     *
     * @param level the value object containing information to initialize a new game level.
     * @throws GameLevelInitializationException if some error has occurred which prevented
     *                                          initialization (malformed data in the value
     *                                          object etc.)
     */
    void startNewGame(DummyLevel level) throws GameLevelInitializationException;

    /**
     * Updates the entire game logic.
     */
    void update();

    /**
     * Gets the game world.
     *
     * @return the game world
     */
    GameWorldPrivileged getGameWorld();

    /**
     * Gets the actor manager.
     *
     * @return the actor manager
     */
    ActorManagerPrivileged getActorManager();

    /**
     * Gets the player manager.
     *
     * @return the player manager
     */
    PlayerManagerPrivileged getPlayerManager();

    /**
     * Gets the proxy object for accessing game related functionality by actors.
     *
     * @return the proxy object for accessing game related functionality by actors
     */
    ActorGameInterface getActorInterface();

    /**
     * Gets the proxy object for accessing game related functionality by players.
     *
     * @return the proxy object for accessing game related functionality by players
     */
    PlayerGameInterface getPlayerInterface();
}
