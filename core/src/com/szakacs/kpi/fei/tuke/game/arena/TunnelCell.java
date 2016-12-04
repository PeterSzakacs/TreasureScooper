package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.eventHandlers.TunnelEventHandler;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by developer on 5.11.2016.
 */
public class TunnelCell {
    private int x;
    private int y;
    private TunnelCellType tcType;
    private int NuggetValue;
    private Map<Direction, TunnelCell> fourDirections;
    private TunnelEventHandler handler;

    public TunnelCell(int x, int y, TunnelCellType tcType, TunnelEventHandler handler) {
        this.x = x;
        this.y = y;
        this.tcType = tcType;
        if (tcType != TunnelCellType.INTERCONNECT)
            this.NuggetValue = 50;
        else
            this.NuggetValue = 0;
        this.handler = handler;
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

    public void setAtDirection(Direction dir, TunnelCell pos){
        fourDirections.put(dir, pos);
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

    public int collectNugget(GoldCollector collector){
        if (collector.getX() == this.x && collector.getY() == this.y) {
            int nuggetVal = this.NuggetValue;
            this.NuggetValue = 0;
            if (this.handler != null && nuggetVal != 0)
                this.handler.onNuggetCollected(collector);
            return nuggetVal;
        } else
            return 0;
    }
}
