package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <p>The basic interface for the class responsible for all game actors and all pipes
 * used by the players.</p>
 *
 * <p>This interface defines methods enabling querying to get information about actors
 * in the game.</p>
 *
 * <p>This interface is to be the only actor manager interface exposed to the player.</p>
 */
public interface ActorManagerBasic {

    /**
     * Gets all currently active game actors
     * (meaning actors whose act() method is
     * still called).
     *
     * @return all game actors (except the pipe heads
     * and pipe segments) as an unmodifiable set
     */
    Set<ActorBasic> getActors();

    /**
     * Gets all active (those, whose act() method is still called) actors which are
     * of a given {@link ActorType}.
     *
     * @param type the actor type of the actors we want to get.
     * @return a set of all active game actors for whom actor.getType() == type
     */
    Set<ActorBasic> getActorsByType(ActorType type);

    /**
     * Gets a mapping between tunnel cells and all actors
     * located at the particular tunnel cell.
     *
     * @return a mapping between actors' current positions and actors themselves
     */
    Map<TunnelCellBasic, Set<ActorBasic>> getPositionToActorsMap();

    /**
     * <p>Queries the set of all currently active game actors (meaning actors whose act()
     * method is still called) and returns a subset of them satisfying the criteria
     * passed as argument. If no criteria is passed (null is passed) it returns all
     * game actors.</p>
     *
     * <p>If {@code getActorsByType} or {@code getPositionToActorsMap} does not satisfy
     * your particular search criteria, this is a generic search method, but beware,
     * unlike those other methods, this method has a complexity of O(n), where n is
     * the number of active game actors.</p>
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether the actor should
     *                 be included in the query results. If null
     *                 is passed, all game actors are returned.
     * @return a set of all game actors satisfying the criteria specified.
     */
    Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> criteria);
}
