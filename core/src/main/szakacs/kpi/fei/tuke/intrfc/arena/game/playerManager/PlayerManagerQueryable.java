package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.intrfc.Player;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 7.3.2017.
 */
public interface PlayerManagerQueryable {

    Set<Pipe> getPipes();

    Map<Player, Integer> getPlayersAndScores();

    /**
     * Gets an object which is responsible for anything the player buys while playing
     * (repairing the pipe, buying bullets etc.).
     *
     * @return GameShop instance which is responsible for player purchases
     */
    GameShop getGameShop();
}
