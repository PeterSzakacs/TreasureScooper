package szakacs.kpi.fei.tuke.intrfc.arena.proxies;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerBasic;

/**
 * The interface to the game exposed to the player
 * (more accurately, the programmer of the player).
 *
 * This interface shall serve as a proxy object
 * for the player to access the game functionality
 * for practical (accessing all functionality
 * through a single object, as opposed to handling
 * multiple separate objects) as well as security
 * reasons (not allowing access to any methods
 * which could be regarded as cheating) although
 * the latter can be theoretically bypassed using
 * reflection.
 */
public interface PlayerGameInterface extends GameLevelBasic, ActorManagerBasic, PlayerManagerBasic {
}
