package szakacs.kpi.fei.tuke.intrfc.arena.game;

import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * Created by developer on 7.3.2017.
 */
public interface ResettableGameClass {

    void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level) throws GameLevelInitializationException;
}
