package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.enums.Direction;

/**
 * An extension of the base {@link TunnelCellBasic} interface for exposing
 * functionality specifically to game actors.
 */
public interface TunnelCellUpdatable extends TunnelCellBasic {

    /**
     * Sets the neighboring cell of this cell in the given direction
     * to be the one passed as argument.
     *
     * @param dir the direction of the new cell.
     * @param pos the new cell to be set as neighbor.
     * @param authToken an authentication token to verify the caller
     *                  is allowed to call this method
     */
    void setAtDirection(Direction dir, TunnelCellUpdatable pos, Object authToken);

    /**
     * Collects a nugget at this position and adds to the score of the player
     * owning the passed pipe, if said pipe's head is at this position and
     * there is a nugget in this cell.
     *
     * @param pipe the pipe whose head has to be at this position.
     */
    void collectNugget(Pipe pipe);

    /**
     * {@inheritDoc}
     */
    TunnelCellUpdatable getCellAtDirection(Direction dir);

    /**
     * {@inheritDoc}
     */
    HorizontalTunnelUpdatable getTunnel();
}
