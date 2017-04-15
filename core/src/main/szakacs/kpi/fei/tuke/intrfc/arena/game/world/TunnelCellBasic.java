package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.game.world.HorizontalTunnel;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;

/**
 * Created by developer on 14.4.2017.
 */
public interface TunnelCellBasic {

    /**
     * Gets the horizontal coordinate of this cell.
     *
     * @return the horizontal coordinate of this cell.
     */
    int getX();

    /**
     * Gets the vertical coordinate of this cell.
     *
     * @return the vertical coordinate of this cell.
     */
    int getY();

    /**
     * Gets the horizontal tunnel to which this cell belongs.
     * If this cell is does not belong to any (i.e. if it is
     * part ot a connection between 2 horizontal tunnels), it
     * returns null.
     *
     * @return the horizontal tunnel to which this cell belongs or null if it is not part of a horizontal tunnel.
     */
    HorizontalTunnelBasic getTunnel();

    /**
     * Returns the adjacent cell in the direction given, or null, if there is no such cell
     * (meaning there is a wall in that direction).
     *
     * @param dir the direction to look at.
     * @return The adjacent cell in the direction given or null if there is a wall in that direction.
     */
    TunnelCellBasic getCellAtDirection(Direction dir);

    /**
     * Returns an enum value specifying the configuration of adjacent cells to this cell.
     *
     * @see TunnelCellType for more info on the meaning of these values.
     * @return the type of this tunnel cell according to the configuration of adjacent cells.
     */
    TunnelCellType getCellType();

    /**
     * Checks if there is a nugget or piece of treasure at the current position,
     *
     * @return boolean true if there is a piece of treasure in the current cell | false otherwise.
     */
    boolean hasNugget();

    /**
     * Checks if the supplied set of coordinates is within this tunnel cell.
     *
     * @param x the horizontal coordinate supplied.
     * @param y the vertical coordinate supplied.
     * @return boolean true if the coordinates refer to a position within this cell | false otherwise.
     */
    boolean isWithinCell(int x, int y);
}
