package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;

import java.util.Map;
import java.util.Set;

/**
 * An extension to the {@link GameWorldBasic} interface that is exposed
 * to game actors via proxy object.
 */
public interface GameWorldUpdatable extends GameWorldBasic {

    /**
     * Gets a set of all horizontal tunnels in the game world (no sorting order).
     *
     * @return all horizontal tunnels of the game world as an unmodifiable set.
     */
    Set<HorizontalTunnelUpdatable> getTunnelsUpdatable();

    /**
     *
     * Gets all entrances into the underground tunnel maze. All have a unique String id.
     *
     * @return an unmodifiable map of entrance IDs to their entrance.
     */
    Map<String, TunnelCellUpdatable> getEntrancesUpdatable();
}
