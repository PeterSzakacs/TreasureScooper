package com.szakacs.kpi.fei.tuke.game.arena.tunnels;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by developer on 5.11.2016.
 */
public class TunnelCell {
    private int x;
    private int y;
    private HorizontalTunnel tunnel;
    private ManipulableGameInterface world;
    private TunnelCellType tcType;
    private int NuggetValue;
    private Map<Direction, TunnelCell> fourDirections;

    public TunnelCell(int x, int y, TunnelCellType tcType, HorizontalTunnel tunnel, ManipulableGameInterface world) {
        this.x = x;
        this.y = y;
        this.tcType = tcType;
        this.tunnel = tunnel;
        this.world = world;
        if (tcType != TunnelCellType.INTERCONNECT)
            this.NuggetValue = 50;
        else
            this.NuggetValue = 0;
        this.fourDirections = new EnumMap<>(Direction.class);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TunnelCellType getTcType() {
        return tcType;
    }

    public HorizontalTunnel getTunnel(){
        return this.tunnel;
    }

    public void setAtDirection(ManipulableGameInterface world, Direction dir, TunnelCell pos){
        if (world != null && world.equals(this.world)) {
            fourDirections.put(dir, pos);
        }
    }

    public TunnelCell getCellAtDirection(Direction dir){
        return fourDirections.get(dir);
    }

    public TunnelCellType getCellType(){
        return this.tcType;
    }

    public boolean hasNugget(){
        return this.NuggetValue != 0;
    }

    public int collectNugget(ManipulableGameInterface world, GoldCollector collector){
        if (world != null && world.equals(this.world)){
            if (collector.getX() == this.x && collector.getY() == this.y) {
                int nuggetVal = this.NuggetValue;
                this.NuggetValue = 0;
                if (this.tunnel != null && nuggetVal != 0)
                    this.tunnel.onNuggetCollected(collector);
                return nuggetVal;
            }
        }
        return 0;
    }

    @Override
    public String toString(){
        return super.toString() + ": " + tcType.name()
                + "\nX: " + this.x + " y: " + this.y;
    }

    void setNuggetValue(int value){

    }
}