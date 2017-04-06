package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * Created by developer on 29.1.2017.
 *
 * An extension to {@link GameLevelBasic} interface exposing functionality
 * necessary for background code running the game.
 */
public interface GameLevelPrivileged extends GameLevelUpdatable {

    void startNewGame(DummyLevel level) throws GameLevelInitializationException;

    /**
     * Updates the entire game
     */
    void update();

    GameWorldPrivileged getGameWorld();

    /**
     * Gets a reference to the actor manager with full functionality exposed.
     *
     * @return the {@link ActorManagerPrivileged} single instance of the current game level
     */
    ActorManagerPrivileged getActorManager();

    PlayerManagerPrivileged getPlayerManager();

    ActorGameInterface getActorInterface();

    PlayerGameInterface getPlayerInterface();
}
