package szakacs.kpi.fei.tuke.intrfc.game.actorManager;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;

import java.util.Collection;
import java.util.List;
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
public interface ActorManagerQueryable {

    /**
     * Gets the pipe used by the player in the game
     *
     * @return the pipe used by the player in the game
     */
    Pipe getPipeOfPlayer(Player player);

    /**
     * Gets all currently active game actors
     * (meaning actors whose update() method is
     * still called)
     *
     * @return all game actors (except the PipeHead) as an unmodifiable list
     */
    List<Actor> getActors();

    /**
     * Queries all currently active game actors
     * (meaning actors whose update() method is
     * still called) satisfying the criteria
     * passed as the argument
     *
     * @param predicate a functional interface or lambda function
     *                  used in evaluating whether the actor should
     *                  be included in the query results.
     * @return a list of all game actors satisfying the criteria specified
     *         in the predicate passed to this method as argument.
     */
    List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate);
}
