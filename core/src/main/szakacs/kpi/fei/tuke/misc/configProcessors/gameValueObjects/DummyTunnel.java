package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import java.util.HashMap;
import java.util.Map;

/**
 * A value object representing a horizontal tunnel in the game world.
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

    /**
     * Gets the absolute value of the horizontal coordinate of this tunnel's leftmost cell.
     *
     * @return the absolute value of the horizontal coordinate of this tunnel's leftmost cell
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the absolute value of the vertical coordinate of this tunnel's leftmost cell.
     *
     * @return the absolute value of the vertical coordinate of this tunnel's leftmost cell
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the number of cells that make up this tunnel from left to right.
     *
     * @return the number of cells that make up this tunnel from left to right
     */
    public int getNumCells() {
        return numCells;
    }

    /**
     * Gets the unique string identifier of this tunnel.
     *
     * @return the unique string identifier of this tunnel
     */
    public String getId(){
        return this.id;
    }

    /**
     * Gets information about the set of tunnels connected to this tunnel below.
     *
     * This is in the form of a mapping between the absolute values of the horizontal
     * coordinates of the cell where the interconnecting tunnel begins and the value
     * object representing the horizontal tunnel below to which this interconnection
     * leads.
     *
     * @return information about the set of tunnels connected to this tunnel below
     */
    public Map<Integer, DummyTunnel> getConnectedTunnelsBelow(){
        return this.connectedTunnelsBelow;
    }
}
