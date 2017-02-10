package szakacs.kpi.fei.tuke.misc.configProcessors.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 24.11.2016.
 *
 * This is an object that merely holds initialization values
 * for the actual horizontal tunnel that is to be created later
 * in the game code.
 */
public class DummyTunnel {

    int xIndex;
    int yIndex;
    int numCells;
    String id;
    Map<Integer, DummyTunnel> connectedTunnelsBelow;

    DummyTunnel(int xIndex, int yIndex, int numCells, String id) {
        this();
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.numCells = numCells;
        this.id = id;
    }

    DummyTunnel() {
        this.connectedTunnelsBelow = new HashMap<>();
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public int getNumCells() {
        return numCells;
    }

    public String getId(){
        return this.id;
    }

    public Map<Integer, DummyTunnel> getConnectedTunnelsBelow(){
        return this.connectedTunnelsBelow;
    }
}
