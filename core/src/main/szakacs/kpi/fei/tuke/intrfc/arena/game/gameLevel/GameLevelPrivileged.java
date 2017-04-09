package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 *
 */
public interface GameLevelPrivileged extends GameLevelUpdatable {

    void startNewGame(DummyLevel level) throws GameLevelInitializationException;

    void update();

    GameWorldPrivileged getGameWorld();

    ActorManagerPrivileged getActorManager();

    PlayerManagerPrivileged getPlayerManager();

    ActorGameInterface getActorInterface();

    PlayerGameInterface getPlayerInterface();
}
