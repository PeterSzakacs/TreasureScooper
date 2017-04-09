package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;

import java.util.Map;
import java.util.Set;

/**
 * The interface for the game gameInterface of a particular level
 *
 * Since so far none of the properties of the game gameInterface are modifiable
 * (e.g. the worlds dimensions and tunnels can't change once instantiated)
 * this interface does not have separate "Updatable" or "Privileged"
 * subinterfaces for any hypothetical methods which would modify them.
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
    Map<String, TunnelCell> getEntrances();

    /**
     * Gets a set of all {@link HorizontalTunnel}s in the game world (no sorting order).
     *
     * @return all horizontal tunnels of the game world as an unmodifiable set.
     */
    Set<HorizontalTunnel> getTunnels();
}
