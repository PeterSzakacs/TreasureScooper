package com.szakacs.kpi.fei.tuke.game.arena.world;

import com.szakacs.kpi.fei.tuke.game.enums.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.OnNuggetCollectedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorldPrototype;
import com.szakacs.kpi.fei.tuke.game.misc.world.DummyEntrance;
import com.szakacs.kpi.fei.tuke.game.misc.world.DummyTunnel;

import java.util.*;

public class TreasureScooperWorld implements GameWorld {

    private final int width;
    private final int height;
    private final int offsetX;
    private final int offsetY;

    private List<TunnelCell> entrances;
    private List<HorizontalTunnel> tunnels;
    private int nuggetCount;

    private OnNuggetCollectedCallback worldCallback = new OnNuggetCollectedCallback(){
        @Override
        public void onNuggetCollected(int nuggetValue){
            TreasureScooperWorld.this.nuggetCount--;
            TreasureScooperWorld.this.gameCallback.onNuggetCollected(nuggetValue);
        }
    };
    private OnNuggetCollectedCallback gameCallback;

    public TreasureScooperWorld(GameWorldPrototype worldPrototype, OnNuggetCollectedCallback gameCallback) {
        this.tunnels = new ArrayList<>(worldPrototype.getDummyTunnels().size());
        this.offsetX = worldPrototype.getOffsetX();
        this.offsetY = worldPrototype.getOffsetY();
        this.width = worldPrototype.getWidth() * this.offsetX;
        this.height = worldPrototype.getHeight() * this.offsetY;
        this.buildTunnelGraph(worldPrototype);
        for (HorizontalTunnel ht : this.tunnels)
            this.nuggetCount += ht.getNuggetCount();
        this.gameCallback = gameCallback;
    }


    /**
     * Initializes the tunnels list, connects those tunnels on particular spots
     * thereby building the maze of underground tunnels and creates entrances
     * into this maze for the players.
     *
     * @param worldPrototype a GameWorldInitializerObject holding information
     *                    necessary to create the underground maze
     */
    private void buildTunnelGraph(GameWorldPrototype worldPrototype) {
        Map<String, DummyTunnel> dummyTunnels = worldPrototype.getDummyTunnels();
        Map<String, HorizontalTunnel> tunnelMap = new HashMap<>();

        // Create the tunnels from their DummyTunnel Prototypes
        for (DummyTunnel dt : dummyTunnels.values()) {
            HorizontalTunnel ht = new HorizontalTunnel(dt, this, this.worldCallback);
            tunnelMap.put(dt.getId(), ht);
            // it is more efficient (in terms of time complexity) to add items to two lists simultaneously,
            // instead of calling new ArrayList<>(tunnelMap.values()) at the end of this method.
            this.tunnels.add(ht);
        }

        // Connect the tunnels with each other
        for (DummyTunnel dt : dummyTunnels.values()) {
            HorizontalTunnel ht = tunnelMap.get(dt.getId());
            Map<Integer, DummyTunnel> tunnelsBelowDt = dt.getConnectedTunnelsBelow();
            for (Integer xIndex : tunnelsBelowDt.keySet()) {
                ht.addInterconnects(
                        xIndex * this.offsetX,
                        tunnelMap.get(
                                tunnelsBelowDt.get(xIndex).getId()
                        )
                );
            }
        }

        // Set the entrances to the tunnel network
        this.entrances = new ArrayList<>(worldPrototype.getDummyEntrances().size());
        for (DummyEntrance de : worldPrototype.getDummyEntrances()){
            TunnelCell entrance = new TunnelCell(
                    de.getXIndex() * offsetX,
                    de.getYIndex() * offsetY,
                    TunnelCellType.INTERCONNECT,
                    null,
                    this
            );
            this.entrances.add(entrance);
            HorizontalTunnel rootTunnel = tunnelMap.get(de.getTunnel().getId());
            TunnelCell newCell, prevCell = entrance;
            for (int y = entrance.getY() - offsetY; y > rootTunnel.getY(); y -= offsetY) {
                newCell = new TunnelCell(entrance.getX(), y, TunnelCellType.INTERCONNECT, null, this);
                newCell.setAtDirection(Direction.UP, prevCell, this);
                prevCell.setAtDirection(Direction.DOWN, newCell, this);
                prevCell = newCell;
            }
            rootTunnel.setEntrance(prevCell);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getNuggetCount() {
        return nuggetCount;
    }

    public TunnelCell getRootCell() {
        return this.entrances.get(0);
    }

    public List<HorizontalTunnel> getTunnels(){
        return Collections.unmodifiableList(this.tunnels);
    }
}