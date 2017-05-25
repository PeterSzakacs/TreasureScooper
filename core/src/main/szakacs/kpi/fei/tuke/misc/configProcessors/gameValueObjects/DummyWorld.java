package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of the {@link GameWorldPrototype} value object interface.
 */
public class DummyWorld implements GameWorldPrototype {

    private Map<String, DummyTunnel> dummyTunnels;
    private Map<String, DummyEntrance> dummyEntrances;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public DummyWorld(int width, int height, int offsetX, int offsetY){
        this.dummyTunnels = new HashMap<>();
        this.dummyEntrances = new HashMap<>();
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
    public Map<String, DummyEntrance> getDummyEntrances() {
        return this.dummyEntrances;
    }

    @Override
    public Map<String, DummyTunnel> getDummyTunnels() {
        return this.dummyTunnels;
    }
}
