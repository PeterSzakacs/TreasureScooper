package com.szakacs.kpi.fei.tuke.game.arena.world;

import com.szakacs.kpi.fei.tuke.game.enums.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.OnNuggetCollectedCallback;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorldInitializer;
import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;

import java.util.*;

public class TreasureScooperWorld implements GameWorld {
	private final int width;
    private final int height;
    private final int offsetX;
    private final int offsetY;

    private TunnelCell rootCell;
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

    public TreasureScooperWorld(GameWorldInitializer initializer, OnNuggetCollectedCallback gameCallback) {
        this.tunnels = new ArrayList<>(initializer.getDummyTunnels().size());
        this.width = initializer.getWidth();
        this.height = initializer.getHeight();
        this.offsetX = initializer.getOffsetX();
        this.offsetY = initializer.getOffsetY();
        this.rootCell = new TunnelCell(
                initializer.getInitX(),
                initializer.getInitY(),
                TunnelCellType.INTERCONNECT,
                null,
                this
        );
        this.buildTunnelGraph(initializer);
        for (HorizontalTunnel ht : this.tunnels)
            this.nuggetCount += ht.getNuggetCount();
        this.gameCallback = gameCallback;
    }


    /**
     * Initializes the tunnels list, connects those tunnels on particular spots
     * thereby building the maze of underground tunnels and creates an entrance
     * into this maze for the player.
     *
     * @param initializer a GameWorldInitializerObject holding information
     *                    necessary to create the underground maze
     */
    private void buildTunnelGraph(GameWorldInitializer initializer) {
        Map<String, DummyTunnel> dummyTunnels = initializer.getDummyTunnels();
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
                        xIndex,
                        tunnelMap.get(
                                tunnelsBelowDt.get(xIndex).getId()
                        )
                );
            }
        }

        // Set the entrance to the tunnel network

        HorizontalTunnel rootTunnel = tunnelMap.get(initializer.getRootTunnelId());
        TunnelCell newCell, prevCell = rootCell;
        for (int y = rootCell.getY() - offsetY; y > rootTunnel.getY(); y -= offsetY) {
            newCell = new TunnelCell(rootCell.getX(), y, TunnelCellType.INTERCONNECT, null, this);
            newCell.setAtDirection(Direction.UP, prevCell, this);
            prevCell.setAtDirection(Direction.DOWN, newCell, this);
            prevCell = newCell;
        }
        rootTunnel.setEntrance(prevCell);
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
        return this.rootCell;
    }

    public List<HorizontalTunnel> getTunnels(){
        return Collections.unmodifiableList(this.tunnels);
    }
}