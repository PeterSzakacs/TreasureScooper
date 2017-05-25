package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <p>The basic interface for the class responsible for all game actors except pipes
 * used by players.</p>
 *
 * <p>This interface defines methods enabling querying to get information about actors
 * in the game.</p>
 *
 * <p>This interface is to be the only actor manager interface exposed to the player.</p>
 *
 * <p><b>Note:</b> the term active game actor refers to those actors, which still exist
 * and are capable of performing actions on every iteration of the game loop</p>
 */
public interface ActorManagerBasic {

    /**
     * Gets the set of all active game actors.
     *
     * @return all game actors (except the pipe heads
     * and pipe segments) as an unmodifiable set
     */
    Set<ActorBasic> getActors();

    /**
     * Gets all active game actors which are of a given {@link ActorType}.
     *
     * @param type the actor type of the actors desired.
     * @return a set of all active game actors for whom {@link ActorBasic#getType()} == type.
     * If type == ActorType.PIPESEGMENT || type == ActorType.PIPEHEAD, then an empty set is
     * returned.
     */
    Set<ActorBasic> getActorsByType(ActorType type);

    /**
     * Gets a mapping between tunnel cells and all actors (except pipe segments or pipe heads)
     * located at the particular tunnel cell
     *
     * <p><b>Note</b> that no actors of types {@link ActorType#PIPESEGMENT}
     * or {@link ActorType#PIPEHEAD} are stored and handled by the actor
     * manager; if a query for these is performed, the returned result is
     * an empty set.</p>
     *
     * @return a mapping between actors' current positions and actors themselves
     */
    Map<TunnelCellBasic, Set<ActorBasic>> getPositionToActorsMap();

    /**
     * <p>Queries the set of all currently active game actors (meaning actors whose act()
     * method is still called) except pipe heads and pipe segments and returns a subset
     * of them satisfying the criteria passed as argument. If no criteria is passed
     * (null is passed) it returns all game actors.</p>
     *
     * <p>If {@code getActorsByType} or {@code getPositionToActorsMap} does not satisfy
     * your particular search criteria, this is a generic search method, but beware,
     * unlike those other methods, this method has a complexity of O(n), where n is
     * the number of active game actors, minus pipe segments and pipe heads.</p>
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether the actor should
     *                 be included in the query results. If null
     *                 is passed, all game actors are returned.
     * @return a set of all game actors satisfying the criteria specified.
     */
    Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> criteria);
}
