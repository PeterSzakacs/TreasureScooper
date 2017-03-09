package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;

/**
 * Created by developer on 7.3.2017.
 */
public interface GameLevelUpdatable extends GameLevelQueryable {

    MethodCallAuthenticator getAuthenticator();
}
