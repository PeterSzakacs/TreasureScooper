package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;

import java.util.Map;

/**
 *
 */
public interface PlayerManagerUpdatable extends PlayerManagerBasic {
    
    Map<PlayerToken, Pipe> getPipesUpdatable();

    Map<PlayerToken, PlayerInfo> getPlayerTokenMap();
}
