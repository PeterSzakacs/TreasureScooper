package szakacs.kpi.fei.tuke.misc.configProcessors.world;

import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 3.2.2017.
 */
public class DummyWorld implements GameWorldPrototype {

    Map<String, DummyTunnel> dummyTunnels;
    List<DummyEntrance> dummyEntrances;
    int width;
    int height;
    int offsetX;
    int offsetY;

    DummyWorld(){
        this.dummyTunnels = new HashMap<>();
        this.dummyEntrances = new ArrayList<>();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getOffsetX() {
        return this.offsetX;
    }

    @Override
    public int getOffsetY() {
        return this.offsetY;
    }

    @Override
    public List<DummyEntrance> getDummyEntrances() {
        return this.dummyEntrances;
    }

    @Override
    public Map<String, DummyTunnel> getDummyTunnels() {
        return this.dummyTunnels;
    }
}
