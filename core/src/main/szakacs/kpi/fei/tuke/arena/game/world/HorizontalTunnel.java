package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;

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
    private List<TunnelCell> searchResults;
    private GameWorldPrivileged world;

    /*
     * begin builder methods
     */

    public HorizontalTunnel(DummyTunnel dt, GameWorldPrivileged world){
        this.x = dt.getX();
        this.y = dt.getY();
        this.world = world;
        this.buildTunnel(dt.getNumCells());
        this.nuggetCount = dt.getNumCells();
        this.searchResults = new ArrayList<>(dt.getNumCells());
    }

    private void buildTunnel(int numCells){
        // Variable declarations and initialization
        TunnelCell leftmost, rightmost, prevCell, nextCell;
        int count = 0;
        int rightEdge = this.x + (numCells - 1) * world.getOffsetX() * Direction.RIGHT.getXStep();
        this.cells = new ArrayList<>(numCells);

        // create and add left edge cell cell of the tunnel
        leftmost = new TunnelCell(
                this.x,
                this.y,
                TunnelCellType.LEFT_EDGE,
                this,
                this.world
        );
        this.cells.add(leftmost);
        count++;

        // create and add all tunnel cells between the left and right edge
        prevCell = leftmost;
        for (int x = this.x + Direction.RIGHT.getXStep()*world.getOffsetX();
             count < numCells - 1;
             x += world.getOffsetX()*Direction.RIGHT.getXStep(), count++){
            nextCell = new TunnelCell(x, this.y, TunnelCellType.TUNNEL, this, world);
            this.cells.add(nextCell);
            nextCell.setAtDirection(Direction.LEFT, prevCell, world.getAuthenticator());
            prevCell.setAtDirection(Direction.RIGHT, nextCell, world.getAuthenticator());
            prevCell = nextCell;
        }

        // create and add the right edge cell of the tunnel
        rightmost = new TunnelCell(
                rightEdge, this.y, TunnelCellType.RIGHT_EDGE, this, world);
        this.cells.add(rightmost);
        rightmost.setAtDirection(Direction.LEFT, prevCell, world.getAuthenticator());
        prevCell.setAtDirection(Direction.RIGHT, rightmost, world.getAuthenticator());
    }

    void addInterconnect(int x, HorizontalTunnel exitTunnel) {
        /*if (interconnects == null || interconnects.isEmpty())
            return;*/
        for (int idx = 0; idx < cells.size() ; idx++) {
            TunnelCell previous = cells.get(idx);
            if (previous.getX() == x) {
                // Remove previous TUNNEL cell and add a new EXIT cell in its place
                TunnelCell removed = this.cells.get(idx);
                TunnelCell added = new TunnelCell(
                        removed.getX(),
                        removed.getY(),
                        TunnelCellType.EXIT,
                        this,
                        world
                );
                this.cells.set(idx, added);

                // Connect the new EXIT cell with the previous TUNNEL cell's neighbors
                TunnelCell left = removed.getCellAtDirection(Direction.LEFT);
                TunnelCell right = removed.getCellAtDirection(Direction.RIGHT);
                added.setAtDirection(Direction.LEFT, left, world.getAuthenticator());
                added.setAtDirection(Direction.RIGHT, right, world.getAuthenticator());
                left.setAtDirection(Direction.RIGHT, added, world.getAuthenticator());
                right.setAtDirection(Direction.LEFT, added, world.getAuthenticator());

                // Create new cells until the specified exit tunnel is reached
                TunnelCell prevCell = added, nextCell;
                for (int y = this.y + world.getOffsetY() * Direction.DOWN.getYStep();
                     y > exitTunnel.getY();
                     y += world.getOffsetY() * Direction.DOWN.getYStep()) {
                    nextCell = new TunnelCell(added.getX(), y, TunnelCellType.INTERCONNECT, null, world);
                    nextCell.setAtDirection(Direction.UP, prevCell, world.getAuthenticator());
                    prevCell.setAtDirection(Direction.DOWN, nextCell, world.getAuthenticator());
                    prevCell = nextCell;
                }
                // Tell the exit tunnel to create an entrance
                exitTunnel.setEntrance(prevCell);
            }
        }
    }

    void setEntrance(TunnelCell entranceCell) {
        if (entranceCell == null || Math.abs(entranceCell.getY() - this.y) > world.getOffsetY())
            return;
        for (int idx = 0; idx < cells.size() ; idx++){
            TunnelCell previous = cells.get(idx);
            if (previous.getX() == entranceCell.getX()) {
                TunnelCell newCell = new TunnelCell(
                        previous.getX(),
                        previous.getY(),
                        TunnelCellType.ENTRANCE,
                        this,
                        world
                );
                this.cells.set(idx, newCell);
                TunnelCell left = previous.getCellAtDirection(Direction.LEFT);
                TunnelCell right = previous.getCellAtDirection(Direction.RIGHT);
                left.setAtDirection(Direction.RIGHT, newCell, world.getAuthenticator());
                right.setAtDirection(Direction.LEFT, newCell, world.getAuthenticator());
                newCell.setAtDirection(Direction.LEFT, left, world.getAuthenticator());
                newCell.setAtDirection(Direction.RIGHT, right, world.getAuthenticator());
                newCell.setAtDirection(Direction.UP, entranceCell, world.getAuthenticator());
                entranceCell.setAtDirection(Direction.DOWN, newCell, world.getAuthenticator());
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

    void onNuggetCollected(Pipe pipe, int nuggetValue){
        this.nuggetCount--;
        this.world.onNuggetCollected(pipe, nuggetValue);
    }

    /*
     * end tunnel manipulation methods
     */
}