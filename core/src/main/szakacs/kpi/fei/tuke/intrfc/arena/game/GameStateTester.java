package szakacs.kpi.fei.tuke.intrfc.arena.game;

import szakacs.kpi.fei.tuke.enums.GameState;

/**
 * The interface of an object designed to evaluate the current state of the game level.
 */
public interface GameStateTester extends ResettableGameClass {

    /**
     * Evaluates the current game level's state.
     *
     * @return the state of the game level
     */
    GameState testGameState();
}
