package szakacs.kpi.fei.tuke.intrfc.game.world;

import szakacs.kpi.fei.tuke.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.game.world.TunnelCell;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 25.1.2017.
 *
 * The interface for the game world of a particular level
 *
 * Since so far none of the properties of the game world are modifiable
 * (e.g. the worlds dimensions and tunnels can't change once instantiated)
 * this interface does not have separate "Updatable" or "Privileged"
 * subinterfaces for any hypothetical methods which would modify them.
 */
public interface GameWorld {

    /**
     * Gets the width of the game world
     *
     * @return the absolute width of the game world
     */
    int getWidth();

    /**
     * Gets the height of the game world
     *
     * @return the absolute height of the game world
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
     * Gets the number of remaining pieces of treasure in the game world
     *
     * @return the current number of pieces of treasure in the game world.
     */
    int getNuggetCount();

    /**
     * Gets the entrance into the underground tunnel maze
     *
     * @return the entrance into the underground tunnel maze
     */
    Map<String, TunnelCell> getEntrances();

    /**
     * Gets a collection of all horizontal tunnels in the game world (no sorting order).
     *
     * @return all horizontal tunnels of the game world as an unmodifiable list
     */
    List<HorizontalTunnel> getTunnels();
}
