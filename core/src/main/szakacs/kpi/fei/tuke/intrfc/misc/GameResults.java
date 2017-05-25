package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.player.Player;

import java.util.List;
import java.util.Map;

/**
 * A value object containing {@link GameResult} objects
 * describing the results (score etc.) for each player
 * class during the whole game and for each level.
 */
public interface GameResults {

    /**
     * Gets a list of mappings between player classes and their results for each level.
     *
     * @return a list of mappings between player classes and their results for each level
     */
    List<Map<Class<? extends Player>, GameResult>> getLevelScores();

    /**
     * Gets a mapping between player classes and their results for the whole game.
     *
     * @return a mapping between player classes and their results for the whole game
     */
    Map<Class<? extends Player>, GameResult> getTotalGameScores();
}
