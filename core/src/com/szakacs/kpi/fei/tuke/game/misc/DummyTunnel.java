package com.szakacs.kpi.fei.tuke.game.misc;

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

    public DummyTunnel(int x, int y, int numCells, String id) {
        this.xIndex = x;
        this.yIndex = y;
        this.numCells = numCells;
        this.id = id;
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
}
