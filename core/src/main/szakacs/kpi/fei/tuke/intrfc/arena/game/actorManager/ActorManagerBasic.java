package szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by developer on 25.1.2017.
 *
 * The basic interface for the class responsible for all game actors and all pipes
 * used by the players.
 *
 * This interface defines methods enabling querying to get information about actors
 * in the game.
 *
 * This interface is to be the only actor manager interface exposed to the player.
 */
public interface ActorManagerBasic {

    /**
     * Gets all currently active game actors
     * (meaning actors whose act() method is
     * still called)
     *
     * @return all game actors (except the PipeHead) as an unmodifiable set
     */
    Set<ActorBasic> getActors();

    /**
     * Queries the set of all currently active
     * game actors (meaning actors whose act()
     * method is still called) and returns a subset of them satisfying the criteria
     * passed as argument. If no criteria is passed (null is passed) it returns all game actors
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether the actor should
     *                 be included in the query results. If null
     *                 is passed, all game actors are returned.
     * @return a set of all game actors satisfying the criteria specified.
     */
    Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> criteria);
}
