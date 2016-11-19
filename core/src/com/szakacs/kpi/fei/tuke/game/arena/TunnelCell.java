package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.eventHandlers.TunnelEventHandler;

import java.util.HashMap;
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
        this.fourDirections = new HashMap<Direction, TunnelCell>(4);
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
        /*System.out.println("x: " + head.getX() + " y: " + head.getY());
        System.out.println("this.x" + this.x + " this.y: " + this.y);
        System.out.println("hasNugget: " + this.hasNugget() + " cellType: " + this.tcType.name());
        System.out.println("left: " + fourDirections.get(Direction.LEFT));
        System.out.println("right: " + fourDirections.get(Direction.RIGHT));
        System.out.println("down: " + fourDirections.get(Direction.DOWN));
        System.out.println("up" + fourDirections.get(Direction.UP));
        System.out.println();*/
        if (collector.getX() == this.x && collector.getY() == this.y) {
            int nuggetVal = this.NuggetValue;
            this.NuggetValue = 0;
            if (this.handler != null)
                this.handler.onNuggetCollected(collector);
            return nuggetVal;
        } else
            return 0;
    }
}
