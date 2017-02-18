package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.List;
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
 *
 * Keeping it for now, though without method documentation.
 */
public interface GameWorldPrototype {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    Map<String, DummyEntrance> getDummyEntrances();

    Map<String, DummyTunnel> getDummyTunnels();
}
