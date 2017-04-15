package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;

import java.util.Map;
import java.util.Set;

/**
 * <p>The interface representing the subset of Player manager functionality
 * exposed to the player.</p>
 */
public interface PlayerManagerBasic {

    /**
     * Gets all pipes in the current game level.
     *
     * @return the (unmodifiable) set of all pipes in the current game level.
     */
    Set<PipeBasic> getPipes();

    /**
     * Gets the scores of every player as a mapping of Player instances
     * to Integer objects representing their scores.
     *
     * @return the scores for each player in the current game level.
     */
    Map<Player, Integer> getPlayersAndScores();

    /**
     * Gets an object which is responsible for anything the player buys while playing
     * (repairing the pipe, buying bullets etc.).
     *
     * @return GameShop instance which is responsible for player purchases
     */
    GameShop getGameShop();
}
