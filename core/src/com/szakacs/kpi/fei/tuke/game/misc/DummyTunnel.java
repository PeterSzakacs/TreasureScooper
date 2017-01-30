package com.szakacs.kpi.fei.tuke.game.misc;

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

    private int xIndex;
    private int yIndex;
    private int numCells;
    private String id;
    private Map<Integer, DummyTunnel> connectedTunnelsBelow;

    public DummyTunnel(int xIndex, int yIndex, int numCells, String id) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.numCells = numCells;
        this.id = id;
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

    public void addConnectionToTunnelBelow(int xIndex, DummyTunnel dt){
        this.connectedTunnelsBelow.put(xIndex, dt);
    }
}
