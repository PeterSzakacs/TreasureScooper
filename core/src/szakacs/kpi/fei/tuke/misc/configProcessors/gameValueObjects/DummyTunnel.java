package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

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

    private int x;
    private int y;
    private int numCells;
    private String id;
    private Map<Integer, DummyTunnel> connectedTunnelsBelow;

    public DummyTunnel(int x, int y, int numCells, String id) {
        this.connectedTunnelsBelow = new HashMap<>();
        this.x = x;
        this.y = y;
        this.numCells = numCells;
        this.id = id;
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
