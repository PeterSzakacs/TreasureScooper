package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

/**
 *
 */
public interface PlayerManagerPrivileged extends PlayerManagerUpdatable, GameUpdater, ResettableGameClass {

    OnScoreEventCallback getScoreChangeCallback();
}
