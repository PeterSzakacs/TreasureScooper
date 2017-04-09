package szakacs.kpi.fei.tuke.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;

import java.util.EnumMap;
import java.util.Map;

/**
 * A class representing a cell of a horizontal or interconnecting tunnel in the game world.
 * Actors can only move in these cells and a single piece of treasure can only be found
 * within a tunnel cell,
 */
public class TunnelCell {
    private int x;
    private int y;
    private HorizontalTunnel tunnel;
    private GameWorldPrivileged world;
    private TunnelCellType tcType;
    private int nuggetValue;
    private Map<Direction, TunnelCell> fourDirections;

    public TunnelCell(int x, int y, TunnelCellType tcType, HorizontalTunnel tunnel, GameWorldPrivileged world) {
        this.x = x;
        this.y = y;
        this.tcType = tcType;
        this.tunnel = tunnel;
        this.world = world;
        if (tcType != TunnelCellType.INTERCONNECT)
            this.nuggetValue = 50;
        else
            this.nuggetValue = 0;
        this.fourDirections = new EnumMap<>(Direction.class);
    }

    @Override
    public String toString(){
        return super.toString() + ": " + tcType.name()
                + "\nX: " + this.x + " Y: " + this.y;
    }

    /**
     * Gets the horizontal coordinate of this cell.
     *
     * @return the horizontal coordinate of this cell.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the vertical coordinate of this cell.
     *
     * @return the vertical coordinate of this cell.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the horizontal tunnel to which this cell belongs.
     * If this cell is does not belong to any (i.e. if it is
     * part ot a connection between 2 horizontal tunnels), it
     * returns null.
     *
     * @return the horizontal tunnel to which this cell belongs or null if it is not part of a horizontal tunnel.
     */
    public HorizontalTunnel getTunnel(){
        return this.tunnel;
    }

    public void setAtDirection(Direction dir, TunnelCell pos, Object authToken) {
        if (world.getAuthenticator().authenticate(authToken)) {
            fourDirections.put(dir, pos);
        }
    }

    /**
     * Returns the adjacent cell in the direction given, or null, if there is no such cell
     * (meaning there is a wall in that direction).
     *
     * @param dir the direction to look at.
     * @return The adjacent cell in the direction given or null if there is a wall in that direction.
     */
    public TunnelCell getCellAtDirection(Direction dir){
        return fourDirections.get(dir);
    }

    /**
     * Returns an enum value specifying the configuration of adjacent cells to this cell.
     *
     * @see TunnelCellType for more info on the meaning of these values.
     * @return the type of this tunnel cell according to the configuration of adjacent cells.
     */
    public TunnelCellType getCellType(){
        return this.tcType;
    }

    /**
     * Checks if there is a nugget or piece of treasure at the current position,
     *
     * @return boolean true if there is a piece of treasure in the current cell | false otherwise.
     */
    public boolean hasNugget(){
        return this.nuggetValue != 0;
    }

    public void collectNugget(Pipe pipe) {
        if (pipe.getHead().getCurrentPosition().equals(this)) {
            int nuggetVal = this.nuggetValue;
            this.nuggetValue = 0;
            if (this.tunnel != null && nuggetVal != 0)
                this.tunnel.onNuggetCollected(pipe, nuggetVal);
        }
    }

    /**
     * Checks if the supplied set of coordinates is within this tunnel cell.
     *
     * @param x the horizontal coordinate supplied.
     * @param y the vertical coordinate supplied.
     * @return boolean true if the coordinates refer to a position within this cell | false otherwise.
     */
    public boolean isWithinCell(int x, int y) {
        int absDx = Math.abs(x - this.x);
        int absDy = Math.abs(y - this.y);
        if (absDx >= world.getOffsetX() || absDy >= world.getOffsetY())
            return false;
        else
            return x + absDx * Direction.LEFT.getXStep() == this.x
                    && y + absDy * Direction.DOWN.getYStep() == this.y;
    }

    void setNuggetValue(int value){

    }
}