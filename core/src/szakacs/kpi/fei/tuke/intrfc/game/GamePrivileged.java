package szakacs.kpi.fei.tuke.intrfc.game;

import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.game.actormanager.ActorManagerPrivileged;

import java.util.Set;

/**
 * Created by developer on 29.1.2017.
 */
public interface GamePrivileged extends Game {

    ActorManagerPrivileged getActorManager();

    void update();

    void startNewGame(Set<GameUpdater> updaters, Player player);
}
