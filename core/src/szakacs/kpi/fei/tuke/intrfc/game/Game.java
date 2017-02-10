package szakacs.kpi.fei.tuke.intrfc.game;

import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.game.GameShop;
import szakacs.kpi.fei.tuke.intrfc.Player;

/**
 * Created by developer on 25.1.2017.
 */
public interface Game {

    Player getPlayer();

    GameState getState();

    GameWorld getGameWorld();

    int getScore();

    GameShop getGameShop();
}
