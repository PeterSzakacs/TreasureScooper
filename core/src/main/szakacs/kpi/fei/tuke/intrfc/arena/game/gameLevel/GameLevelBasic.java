package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;

/**
 * Created by developer on 25.1.2017.
 *
 * The base interface of a level in the game.
 *
 * This is the only game level interface which shall be exposed to the player.
 *
 * It enables querying the game for various data.
 */
public interface GameLevelBasic {

    /**
     * Gets the state of this level (currently playing, won, lost).
     *
     * @return Enum GameState describing the current state of this level
     */
    GameState getState();

    /**
     * Gets the game world of this level.
     *
     * @return the current game world
     */
    GameWorldBasic getGameWorld();
}
