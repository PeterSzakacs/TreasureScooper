package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import java.util.Set;
import java.util.function.Predicate;

/**
 * The base interface for a class representing a tunnel
 * stretching from left to right on the screen.
 *
 * This is the interface of such a class that is exposed
 * to the player.
 */
public interface HorizontalTunnelBasic {

    /**
     * Gets the horizontal coordinate of this tunnel
     * (it is defined as the horizontal coordinate
     * of the tunnels leftmost cell).
     *
     * @return the horizontal coordinate of the tunnel.
     */
    int getX();

    /**
     * Gets the vertical coordinate of this tunnel
     * (it is defined as the vertical coordinate
     * of all cells of this tunnel).
     *
     * @return the vertical coordinate of the tunnel.
     */
    int getY();

    /**
     * Gets the number of remaining nuggets or pieces of treasure within this tunnel.
     *
     * @return the number of still uncollected nuggets remaining in this tunnel.
     */
    int getNuggetCount();

    /**
     * Returns all cells of this tunnel as an unmodifiable set.
     *
     * @return all cells that make up this tunnel.
     */
    Set<TunnelCellBasic> getCells();

    /**
     * Returns all cells of this tunnel that satisfy the criteria passed as argument.
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether a cell should
     *                 be included in the query results. If null
     *                 is passed, all cells are returned.
     * @return a set of all tunnel cells satisfying the criteria specified.
     */
    Set<TunnelCellBasic> getCellsBySearchCriteria(Predicate<TunnelCellBasic> criteria);
}
