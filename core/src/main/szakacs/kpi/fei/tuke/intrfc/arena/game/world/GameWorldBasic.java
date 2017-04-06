package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;

import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 25.1.2017.
 *
 * The interface for the game gameInterface of a particular level
 *
 * Since so far none of the properties of the game gameInterface are modifiable
 * (e.g. the worlds dimensions and tunnels can't change once instantiated)
 * this interface does not have separate "Updatable" or "Privileged"
 * subinterfaces for any hypothetical methods which would modify them.
 */
public interface GameWorldBasic {

    /**
     * Gets the width of the game gameInterface
     *
     * @return the absolute width of the game gameInterface
     */
    int getWidth();

    /**
     * Gets the height of the game gameInterface
     *
     * @return the absolute height of the game gameInterface
     */
    int getHeight();

    /**
     * Gets the size of the cell offset in the horizontal direction
     *
     * @return the size of one cell of the game world grid (the size of a cell coordinate offset)
     * @see <a href="file://projectDir/core/assets/worlds/README.txt">README for gameWorld XML</a>
     * for further detailing of the difference between cell coordinates and absolute coordinates
     */
    int getOffsetX();

    /**
     * Gets the size of the cell offset in the horizontal direction
     *
     * @return the size of one cell of the game world grid (the size of a cell coordinate offset)
     * @see <a href="file://projectDir/core/assets/worlds/README.txt">README for gameWorld XML</a>
     * for further detailing of the difference between cell coordinates and absolute coordinates
     */
    int getOffsetY();

    /**
     * Gets the number of remaining pieces of treasure in the game gameInterface
     *
     * @return the current number of pieces of treasure in the game gameInterface.
     */
    int getNuggetCount();

    /**
     * Gets the entrance into the underground tunnel maze
     *
     * @return the entrance into the underground tunnel maze
     */
    Map<String, TunnelCell> getEntrances();

    /**
     * Gets a collection of all horizontal tunnels in the game gameInterface (no sorting order).
     *
     * @return all horizontal tunnels of the game gameInterface as an unmodifiable list
     */
    Set<HorizontalTunnel> getTunnels();
}
