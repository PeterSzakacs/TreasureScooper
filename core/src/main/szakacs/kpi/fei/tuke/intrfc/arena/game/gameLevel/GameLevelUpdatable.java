package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldUpdatable;

/**
 *
 */
public interface GameLevelUpdatable extends GameLevelBasic {

    MethodCallAuthenticator getAuthenticator();

    GameWorldUpdatable getGameWorld();
}
