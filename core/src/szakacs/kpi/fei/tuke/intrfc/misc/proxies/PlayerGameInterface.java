package szakacs.kpi.fei.tuke.intrfc.misc.proxies;

import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerQueryable;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevel;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;

/**
 * Created by developer on 1.12.2016.
 *
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
public interface PlayerGameInterface extends GameWorld, GameLevel, ActorManagerQueryable {
}
