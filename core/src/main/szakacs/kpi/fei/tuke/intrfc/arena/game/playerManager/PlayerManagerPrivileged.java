package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

/**
 * Created by developer on 7.3.2017.
 */
public interface PlayerManagerPrivileged extends PlayerManagerQueryable, GameUpdater, ResettableGameClass {

    OnScoreEventCallback getScoreChangeCallback();
}
