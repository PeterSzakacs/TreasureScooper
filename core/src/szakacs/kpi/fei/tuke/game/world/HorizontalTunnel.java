package szakacs.kpi.fei.tuke.game.world;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnNuggetCollectedCallback;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;
import szakacs.kpi.fei.tuke.misc.configProcessors.world.DummyTunnel;

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
    /*private List<TunnelCell> entrances;
    private List<TunnelCell> exits;*/
    private List<TunnelCell> searchResults;
    private GameWorld world;
    private OnNuggetCollectedCallback worldCallback;

    /*
     * begin builder methods
     */

    public HorizontalTunnel(DummyTunnel dt, GameWorld world, OnNuggetCollectedCallback worldCallback){
        this.x = dt.getX();
        this.y = dt.getY();
        this.world = world;
        this.buildTunnel(dt.getNumCells());
        this.nuggetCount = dt.getNumCells();
        this.searchResults = new ArrayList<>(dt.getNumCells());
        this.worldCallback = worldCallback;
    }

    private void buildTunnel(int numCells){
        /*this.entrances = new ArrayList<>(3);
        this.exits = new ArrayList<>(3);*/
        this.cells = new ArrayList<>(numCells);
        TunnelCell rightmostCell, prevCell, newCell;
        int rightEdge = this.x + (numCells - 1) * world.getOffsetX();
        prevCell = new TunnelCell(this.x, this.y, TunnelCellType.LEFT_EDGE, this, this.world);
        this.cells.add(prevCell);
        for (int x = this.x + world.getOffsetX(); x < rightEdge; x += world.getOffsetX()){
            newCell = new TunnelCell(x, this.y, TunnelCellType.TUNNEL, this, this.world);
            this.cells.add(newCell);
            newCell.setAtDirection(Direction.LEFT, prevCell, this.world);
            prevCell.setAtDirection(Direction.RIGHT, newCell, this.world);
            prevCell = newCell;
        }
        rightmostCell = new TunnelCell(
                rightEdge, this.y, TunnelCellType.RIGHT_EDGE, this, this.world);
        rightmostCell.setAtDirection(Direction.LEFT, prevCell, this.world);
        prevCell.setAtDirection(Direction.RIGHT, rightmostCell, this.world);
        this.cells.add(rightmostCell);
    }

    public void addInterconnects(int x, HorizontalTunnel exitTunnel) {
        /*if (interconnects == null || interconnects.isEmpty())
            return;*/
        for (int idx = 0; idx < cells.size() ; idx++) {
            TunnelCell previous = cells.get(idx);
            if (previous.getX() == x) {
                TunnelCell removed = this.cells.get(idx);
                TunnelCell added = new TunnelCell(removed.getX(), removed.getY(), TunnelCellType.EXIT, this, this.world);
                TunnelCell prevCell = added, nextCell;
                this.cells.set(idx, added);
                //this.exits.add(added);
                TunnelCell left = removed.getCellAtDirection(Direction.LEFT);
                TunnelCell right = removed.getCellAtDirection(Direction.RIGHT);
                added.setAtDirection(Direction.LEFT, left, this.world);
                added.setAtDirection(Direction.RIGHT, right, this.world);
                left.setAtDirection(Direction.RIGHT, added, this.world);
                right.setAtDirection(Direction.LEFT, added, this.world);
                for (int y = this.y - world.getOffsetY(); y > exitTunnel.getY(); y -= world.getOffsetY()) {
                    nextCell = new TunnelCell(added.getX(), y, TunnelCellType.INTERCONNECT, null, this.world);
                    nextCell.setAtDirection(Direction.UP, prevCell, this.world);
                    prevCell.setAtDirection(Direction.DOWN, nextCell, this.world);
                    prevCell = nextCell;
                }
                exitTunnel.setEntrance(prevCell);
            }
        }
    }

    public void setEntrance(TunnelCell entranceCell) {
        if (entranceCell == null || Math.abs(entranceCell.getY() - this.y) > world.getOffsetY())
            return;
        for (int idx = 0; idx < cells.size() ; idx++){
            TunnelCell previous = cells.get(idx);
            if (previous.getX() == entranceCell.getX()) {
                TunnelCell newCell = new TunnelCell(previous.getX(), previous.getY(), TunnelCellType.ENTRANCE, this, this.world);
                this.cells.set(idx, newCell);
                //entrances.add(newCell);
                TunnelCell left = previous.getCellAtDirection(Direction.LEFT);
                TunnelCell right = previous.getCellAtDirection(Direction.RIGHT);
                left.setAtDirection(Direction.RIGHT, newCell, this.world);
                right.setAtDirection(Direction.LEFT, newCell, this.world);
                newCell.setAtDirection(Direction.LEFT, left, this.world);
                newCell.setAtDirection(Direction.RIGHT, right, this.world);
                newCell.setAtDirection(Direction.UP, entranceCell, this.world);
                entranceCell.setAtDirection(Direction.DOWN, newCell, this.world);
                break;
            }
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

    void onNuggetCollected(int nuggetValue){
        this.nuggetCount--;
        this.worldCallback.onNuggetCollected(nuggetValue);
    }

    /*
     * end tunnel manipulation methods
     */
}