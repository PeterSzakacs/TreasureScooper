package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;

/**
 * Created by developer on 24.1.2017.
 *
 * Interface for all helper classes used to render specific aspects
 * of the game, e.g. players, score, actors, tunnels, etc.
 */
public interface GameRenderer {

    /**
     * Renders a particular aspect of the game.
     */
    void render();

    /**
     * Called when the application ends to release any resources.
     */
    void dispose();

    /**
     * Called when the game level changes to update any level-specific
     * cached member variables stored in the updater object.
     *
     * @param game the new game level
     */
    void reset(GameLevelPrivileged game);
}
