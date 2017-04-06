package szakacs.kpi.fei.tuke.intrfc.arena.proxies;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerUpdatable;

/**
 * Created by developer on 2.12.2016.
 *
 * The interface to the game exposed to game actors
 *
 * This interface shall serve as a proxy object
 * for the actors to access the game functionality
 * for practical reasons (accessing functionality
 * through a single object, as opposed to handling
 * multiple separate objects).
 *
 * Although unlike the PlayerGameInterface, security
 * (not allowing access to any methods which could
 * be regarded as cheating) is not a concern here,
 * this interface does not include "privileged"
 * level methods, such as update(), just to remind
 * programmers who want to add new game actors not
 * to think of using said methods (basically, this
 * interface serves as a programmer contract).
 *
 * The game has exactly one instance of this object
 * which also serves as an authentication of sorts
 * for certain methods of actors which the player
 * should not call, since it would be considered
 * cheating.
 */
public interface ActorGameInterface extends PlayerGameInterface, ActorManagerUpdatable, PlayerManagerUpdatable, GameLevelUpdatable {
}
