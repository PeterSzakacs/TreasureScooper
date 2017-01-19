package com.szakacs.kpi.fei.tuke.game.arena.tunnels;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Enemy;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 5.11.2016.
 */
public class HorizontalTunnel {
    private int x;
    private int y;
    private int nuggetCount;
    private List<TunnelCell> cells;
    private List<TunnelCell> entrances;
    private List<TunnelCell> exits;
    private List<TunnelCell> searchResults;
    private ManipulableGameInterface world;

    /*
     * begin builder methods
     */

    public HorizontalTunnel(DummyTunnel dt, ManipulableGameInterface world){
        this.x = dt.getXIndex()*world.getOffsetX();
        this.y = dt.getYIndex()*world.getOffsetY();
        this.world = world;
        this.buildTunnel(dt.getNumCells());
        this.nuggetCount = dt.getNumCells();
        this.searchResults = new ArrayList<>(dt.getNumCells());
    }

    private void buildTunnel(int numCells){
        this.entrances = new ArrayList<>(3);
        this.exits = new ArrayList<>(3);
        this.cells = new ArrayList<>(numCells);
        TunnelCell rightmostCell, prevCell, newCell;
        int rightEdge = this.x + (numCells - 1) * world.getOffsetX();
        prevCell = new TunnelCell(this.x, this.y, TunnelCellType.LEFT_EDGE, this, this.world);
        this.cells.add(prevCell);
        for (int x = this.x + world.getOffsetX(); x < rightEdge; x += world.getOffsetX()){
            newCell = new TunnelCell(x, this.y, TunnelCellType.TUNNEL, this, this.world);
            this.cells.add(newCell);
            newCell.setAtDirection(this.world, Direction.LEFT, prevCell);
            prevCell.setAtDirection(this.world, Direction.RIGHT, newCell);
            prevCell = newCell;
        }
        rightmostCell = new TunnelCell(
                rightEdge, this.y, TunnelCellType.RIGHT_EDGE, this, this.world);
        rightmostCell.setAtDirection(this.world, Direction.LEFT, prevCell);
        prevCell.setAtDirection(this.world, Direction.RIGHT, rightmostCell);
        this.cells.add(rightmostCell);
    }

    public void addInterconnects(Map<Integer, HorizontalTunnel> interconnects) {
        if (interconnects == null || interconnects.isEmpty())
            return;
        for (Integer xCoord : interconnects.keySet()){
            HorizontalTunnel exitTunnel = interconnects.get(xCoord);
            int idx = xCoord/world.getOffsetX();
            TunnelCell removed = this.cells.get(idx);
            TunnelCell added = new TunnelCell(removed.getX(), removed.getY(), TunnelCellType.EXIT, this, this.world);
            TunnelCell prevCell = added, nextCell;
            this.cells.set(idx, added);
            this.exits.add(added);
            TunnelCell left = removed.getCellAtDirection(Direction.LEFT);
            TunnelCell right = removed.getCellAtDirection(Direction.RIGHT);
            added.setAtDirection(this.world, Direction.LEFT, left);
            added.setAtDirection(this.world, Direction.RIGHT, right);
            left.setAtDirection(this.world, Direction.RIGHT, added);
            right.setAtDirection(this.world, Direction.LEFT, added);
            for (int y = this.y - world.getOffsetY(); y > exitTunnel.getY(); y -= world.getOffsetY()){
                nextCell = new TunnelCell(xCoord, y, TunnelCellType.INTERCONNECT, null, this.world);
                nextCell.setAtDirection(this.world, Direction.UP, prevCell);
                prevCell.setAtDirection(this.world, Direction.DOWN, nextCell);
                prevCell = nextCell;
            }
            exitTunnel.setEntrance(prevCell);
        }
    }

    public void setEntrance(TunnelCell entranceCell) {
        if (entranceCell == null || Math.abs(entranceCell.getY() - this.y) > world.getOffsetY())
            return;
        int idx = entranceCell.getX()/world.getOffsetX();
        TunnelCell previous = this.cells.get(idx);
        if (previous.getX() == entranceCell.getX()) {
            TunnelCell newCell = new TunnelCell(previous.getX(), previous.getY(), TunnelCellType.ENTRANCE, this, this.world);
            this.cells.set(idx, newCell);
            entrances.add(newCell);
            TunnelCell left = previous.getCellAtDirection(Direction.LEFT);
            TunnelCell right = previous.getCellAtDirection(Direction.RIGHT);
            left.setAtDirection(this.world, Direction.RIGHT, newCell);
            right.setAtDirection(this.world, Direction.LEFT, newCell);
            newCell.setAtDirection(this.world, Direction.LEFT, left);
            newCell.setAtDirection(this.world, Direction.RIGHT, right);
            newCell.setAtDirection(this.world, Direction.UP, entranceCell);
            entranceCell.setAtDirection(this.world, Direction.DOWN, newCell);
        }
    }



    /*
     * end builder methods
     */
    /*
     * begin public getters
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNumCells() {
        return cells.size();
    }

    public int getWidth(){
        return cells.size()*world.getOffsetX();
    }

    public List<TunnelCell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public int getNuggetCount(){
        return this.nuggetCount;
    }

    public List<TunnelCell> getCellsBySearchCriteria(Predicate<TunnelCell> criteria){
        searchResults.clear();
        for (TunnelCell cell : this.cells)
            if (criteria.test(cell))
                searchResults.add(cell);
        return searchResults;
    }


    /*
     * end public getters
     */
    /*
     * begin tunnel manipulation methods
     */


    void onNuggetCollected(GoldCollector collector){
        this.nuggetCount--;
        this.world.onNuggetCollected();
    }

    /*
     * end tunnel manipulation methods
     */
}