package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.game.GameShop;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;

import java.util.Map;
import java.util.Set;

/**
 * <p>The interface representing the subset of Player manager functionality
 * exposed to the player.</p>
 */
public interface PlayerManagerBasic {

    /**
     * Gets a set of all players in the current level and
     * any info associated with them, such as the pipe
     * assigned to them and their current score.
     *
     * @return a set of {@link PlayerInfo} containing
     *         the player and associated info.
     */
    Set<PlayerInfo> getPlayerInfo();

    /**
     * Gets an object which is responsible for anything the player buys while playing
     * (repairing the pipe, buying bullets etc.).
     *
     * @return GameShop instance which is responsible for player purchases
     */
    GameShop getGameShop();

    /**
     * Gets a mapping between tunnel cells and any pipe actors (pipe heads or segments)
     * that are at these positions. Used for collision detection. Can be used to check
     * if a position does not contain a pipe's component to avoid biting one's own or
     * others' pipes.
     *
     * @return a mapping between tunnel cells and pipe components.
     */
    Map<TunnelCellBasic, Set<ActorBasic>> getPositionsToPipesMap();
}
