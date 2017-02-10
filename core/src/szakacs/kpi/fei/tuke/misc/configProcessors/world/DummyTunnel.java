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

    int x;
    int y;
    int numCells;
    String id;
    Map<Integer, DummyTunnel> connectedTunnelsBelow;

    DummyTunnel(int x, int y, int numCells, String id) {
        this();
        this.x = x;
        this.y = y;
        this.numCells = numCells;
        this.id = id;
    }

    DummyTunnel() {
        this.connectedTunnelsBelow = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
