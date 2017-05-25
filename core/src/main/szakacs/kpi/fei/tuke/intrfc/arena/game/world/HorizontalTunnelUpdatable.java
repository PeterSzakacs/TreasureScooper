package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import java.util.Set;
import java.util.function.Predicate;

/**
 * An extension of the base {@link HorizontalTunnelBasic} interface for exposing
 * functionality specifically to game actors.
 */
public interface HorizontalTunnelUpdatable extends HorizontalTunnelBasic {

    /**
     * Returns all cells of this tunnel. Note that this is a modifiable set.
     *
     * @param authToken an authentication token to verify the caller
     *                  is allowed to call this method
     * @return all cells that make up this tunnel
     */
    Set<TunnelCellUpdatable> getCells(Object authToken);

    /**
     * Returns all cells of this tunnel that satisfy the criteria passed as argument.
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether a cell should
     *                 be included in the query results. If null
     *                 is passed, all cells are returned
     * @param authToken an authentication token to verify the caller
     *                  is allowed to call this method
     * @return a set of all tunnel cells satisfying the criteria specified
     */
    Set<TunnelCellUpdatable> getCellsBySearchCriteria(Predicate<TunnelCellUpdatable> criteria, Object authToken);
}
