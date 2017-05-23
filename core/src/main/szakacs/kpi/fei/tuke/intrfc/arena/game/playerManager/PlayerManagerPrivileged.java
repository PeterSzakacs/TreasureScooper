package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;

import java.util.Map;

/**
 *
 */
public interface PlayerManagerPrivileged extends PlayerManagerUpdatable, GameUpdater {

    OnScoreEventCallback getScoreChangeCallback();

    Map<PlayerToken, PlayerInfo> getUnregisteredPlayers();
}
