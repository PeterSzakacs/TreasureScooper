package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.HorizontalTunnelUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

    // HorizontalTunnelBasic methods

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getNuggetCount(){
        return nuggetCount;
    }

    @Override
    public Set<TunnelCellBasic> getCells() {
        return Collections.unmodifiableSet(cells);
    }

    @Override
    public Set<TunnelCellBasic> getCellsBySearchCriteria(Predicate<TunnelCellBasic> criteria){
        if (criteria == null)
            return Collections.unmodifiableSet(cells);
        searchResults.clear();
        for (TunnelCellUpdatable cell : this.cells)
            if (criteria.test(cell))
                searchResults.add(cell);
        return Collections.unmodifiableSet(searchResults);
    }

    // HorizontalTunnelUpdatable methods

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

    // tunnel manipulation methods

    void onNuggetCollected(Pipe pipe, int nuggetValue){
        nuggetCount--;
        world.onNuggetCollected(pipe, nuggetValue);
    }
}