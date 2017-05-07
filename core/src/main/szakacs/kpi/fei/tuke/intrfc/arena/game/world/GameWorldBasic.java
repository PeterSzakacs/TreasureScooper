package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;

import java.util.Map;
import java.util.Set;

/**
 * The interface for the game world of a particular level
 */
public interface GameWorldBasic {

    /**
     * Gets the width of the game world.
     *
     * @return the absolute width of the game gameInterface.
     */
    int getWidth();

    /**
     * Gets the height of the game world.
     *
     * @return the absolute height of the game gameInterface.
     */
    int getHeight();

    /**
     * Gets the size of a {@link TunnelCell} in the horizontal direction.
     *
     * @return the size of one cell of the game world grid (the size of a cell coordinate offset)
     */
    int getOffsetX();

    /**
     * Gets the size of a {@link TunnelCell} in the horizontal direction.
     *
     * @return the size of one cell of the game world grid (the size of a cell coordinate offset)
     */
    int getOffsetY();

    /**
     * Gets the number of remaining pieces of treasure in the game world.
     *
     * @return the current number of pieces of treasure in the game world.
     */
    int getNuggetCount();

    /**
     * Gets all entrances into the underground tunnel maze. All have a unique String id.
     *
     * @return an unmodifiable map of entrance IDs to their {@link TunnelCell} entrance.
     */
    Map<String, TunnelCellBasic> getEntrances();

    /**
     * Gets a set of all {@link HorizontalTunnelBasic} in the game world (no sorting order).
     *
     * @return all horizontal tunnels of the game world as an unmodifiable set.
     */
    Set<HorizontalTunnelBasic> getTunnels();

    /**
     * Gets a set of all tunnel cells (those of horizontal as well as interconnecting
     * tunnels) representing the game world.
     *
     * @return the set of all tunnel cells of the game world.
     */
    Set<TunnelCellBasic> getCells();
}
