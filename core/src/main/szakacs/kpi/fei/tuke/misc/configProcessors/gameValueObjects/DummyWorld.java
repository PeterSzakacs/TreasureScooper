package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 3.2.2017.
 */
public class DummyWorld implements GameWorldPrototype {

    private Map<String, DummyTunnel> dummyTunnels;
    private List<DummyEntrance> dummyEntrances;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public DummyWorld(int width, int height, int offsetX, int offsetY){
        this.dummyTunnels = new HashMap<>();
        this.dummyEntrances = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
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
