package szakacs.kpi.fei.tuke.intrfc.arena.game;

import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * The interface of a class that is designed to be reused in every game level.
 */
public interface ResettableGameClass {

    /**
     * Performs the actions to reset the implementing class to its default state
     * when a new game level begins.
     *
     * @param gameLevel the object representing the current game level
     * @param level the value object containing additional information to initiazlize the current game level
     * @throws GameLevelInitializationException if some error has occurred which prevented
     *                                          initialization (malformed data in the value
     *                                          object etc.)
     */
    void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException;
}
