package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 *
 * The interface of the value object used for holding data
 * that is used for instantiating a game world.
 *
 * Not sure if this interface has much practical value,
 * since only one class implements it and furthermore
 * there are no interfaces for value objects for tunnels,
 * entrances, or game levels.
 */
public interface GameWorldPrototype {

    /**
     * Gets the absolute width of the game world.
     *
     * @return the absolute width of the game world
     */
    int getWidth();

    /**
     * Gets the absolute height of the game world.
     *
     * @return the absolute height of the game world
     */
    int getHeight();

    /**
     * Gets the absolute width of a single tunnel cell in the game world.
     *
     * @return the absolute width of a single tunnel cell in the game world
     */
    int getOffsetX();

    /**
     * Gets the absolute height of a single tunnel cell in the game world.
     *
     * @return the absolute height of a single tunnel cell in the game world
     */
    int getOffsetY();

    /**
     * Gets a mapping between unique string identifiers and value objects
     * representing entrances to the underground tunnel maze.
     *
     * @return a mapping between unique string identifiers and value objects
     *         representing entrances to the underground tunnel maze
     */
    Map<String, DummyEntrance> getDummyEntrances();

    /**
     * Gets a mapping between unique string identifiers and value objects
     * representing horizontal tunnels.
     *
     * @return a mapping between unique string identifiers and value objects
     * representing horizontal tunnels
     */
    Map<String, DummyTunnel> getDummyTunnels();
}
