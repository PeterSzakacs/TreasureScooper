package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 */
public interface GameWorldPrototype {

    int getWidth();

    int getHeight();

    int getOffsetX();

    int getOffsetY();

    List<DummyEntrance> getDummyEntrances();

    Map<String, DummyTunnel> getDummyTunnels();
}
