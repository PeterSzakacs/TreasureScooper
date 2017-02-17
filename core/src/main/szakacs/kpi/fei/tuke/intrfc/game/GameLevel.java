package szakacs.kpi.fei.tuke.intrfc.game;

import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.game.GameShop;
import szakacs.kpi.fei.tuke.intrfc.Player;

/**
 * Created by developer on 25.1.2017.
 *
 * The base interface of a level in the game.
 *
 * This is the only game level interface which shall be exposed to the player.
 *
 * It enables querying the game for various data.
 */
public interface GameLevel {

    /**
     * Gets the player controlling the pipe in the game.
     *
     * @return the current player controlling the pipe in the game.
     */
    Player getPlayer();

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
    GameWorld getGameWorld();

    /**
     * Gets the current score of the player.
     *
     * @return the current game score
     */
    int getScore();

    /**
     * Gets an object which is responsible for anything the player buys while playing
     * (repairing the pipe, buying bullets etc.).
     *
     * @return GameShop instance which is responsible for player purchases
     */
    GameShop getGameShop();
}
