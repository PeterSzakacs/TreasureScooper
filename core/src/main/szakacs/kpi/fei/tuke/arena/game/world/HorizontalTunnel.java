package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.*;
import java.util.function.Predicate;

/**
 * A class representing a tunnel stretching from left to right on the screen.
 */
public class HorizontalTunnel implements HorizontalTunnelUpdatable {
    private int x;
    private int y;
    private int nuggetCount;
    private Set<TunnelCellUpdatable> cells;
    private Set<TunnelCellUpdatable> searchResults;
    private GameWorldPrivileged world;

    /*
     * begin builder methods
     */

    public HorizontalTunnel(DummyTunnel dt, GameWorldPrivileged world){
        this.x = dt.getX();
        this.y = dt.getY();
        this.world = world;
        int numCells = dt.getNumCells();
        this.cells = new HashSet<>(numCells);
        this.searchResults = new HashSet<>(numCells);
        this.buildTunnel(dt.getNumCells());
        this.nuggetCount = numCells;
    }

    private void buildTunnel(int numCells){
        // Variable declarations and initialization
        TunnelCell leftmost, rightmost, prevCell, nextCell;
        int count = 0;
        int rightEdge = this.x + (numCells - 1) * world.getOffsetX() * Direction.RIGHT.getXStep();

        // create and add left edge cell cell of the tunnel
        leftmost = new TunnelCell(
                this.x,
                this.y,
                TunnelCellType.LEFT_EDGE,
                this,
                this.world
        );
        cells.add(leftmost);
        count++;

        // create and add all tunnel cells between the left and right edge
        prevCell = leftmost;
        for (int x = this.x + Direction.RIGHT.getXStep()*world.getOffsetX();
             count < numCells - 1;
             x += world.getOffsetX()*Direction.RIGHT.getXStep(), count++){
            nextCell = new TunnelCell(x, this.y, TunnelCellType.TUNNEL, this, world);
            cells.add(nextCell);
            nextCell.setAtDirection(Direction.LEFT, prevCell, world.getAuthenticator());
            prevCell.setAtDirection(Direction.RIGHT, nextCell, world.getAuthenticator());
            prevCell = nextCell;
        }

        // create and add the right edge cell of the tunnel
        rightmost = new TunnelCell(
                rightEdge, this.y, TunnelCellType.RIGHT_EDGE, this, world);
        cells.add(rightmost);
        rightmost.setAtDirection(Direction.LEFT, prevCell, world.getAuthenticator());
        prevCell.setAtDirection(Direction.RIGHT, rightmost, world.getAuthenticator());
    }

    @Override
    public Set<TunnelCellUpdatable> getUpdatableCells() {
        return cells;
    }

    @Override
    public Set<TunnelCellUpdatable> getUpdatableCellsBySearchCriteria(Predicate<TunnelCellUpdatable> criteria) {
        if (criteria == null)
            return cells;
        searchResults.clear();
        for (TunnelCellUpdatable cell : this.cells)
            if (criteria.test(cell))
                searchResults.add(cell);
        return searchResults;
    }



    /*
     * end builder methods
     */
    /*
     * begin public getters
     */

    /**
     * Gets the horizontal coordinate of this tunnel
     * (it is defined as the horizontal coordinate
     * of the tunnels leftmost cell).
     *
     * @return the horizontal coordinate of the tunnel.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Gets the vertical coordinate of this tunnel
     * (it is defined as the vertical coordinate
     * of all cells of this tunnel).
     *
     * @return the vertical coordinate of the tunnel.
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * Gets the number of remaining nuggets or pieces of treasure within this tunnel.
     *
     * @return the number of still uncollected nuggets remaining in this tunnel.
     */
    @Override
    public int getNuggetCount(){
        return nuggetCount;
    }

    /**
     * Returns all cells of this tunnel as an unmodifiable set.
     *
     * @return all cells that make up this tunnel.
     */
    @Override
    public Set<szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic> getCells() {
        return Collections.unmodifiableSet(cells);
    }

    /**
     * Returns all cells of this tunnel that satisfy the criteria passed as argument.
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether a cell should
     *                 be included in the query results. If null
     *                 is passed, all cells are returned.
     * @return a set of all tunnel cells satisfying the criteria specified.
     */
    @Override
    public Set<szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic> getCellsBySearchCriteria(Predicate<szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic> criteria){
        if (criteria == null)
            return Collections.unmodifiableSet(cells);
        searchResults.clear();
        for (TunnelCellUpdatable cell : this.cells)
            if (criteria.test(cell))
                searchResults.add(cell);
        return Collections.unmodifiableSet(searchResults);
    }


    /*
     * end public getters
     */
    /*
     * begin tunnel manipulation methods
     */

    void onNuggetCollected(Pipe pipe, int nuggetValue){
        nuggetCount--;
        world.onNuggetCollected(pipe, nuggetValue);
    }

    /*
     * end tunnel manipulation methods
     */
}