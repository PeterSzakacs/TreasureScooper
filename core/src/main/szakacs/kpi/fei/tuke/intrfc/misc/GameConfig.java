package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 11.2.2017.
 *
 * The interface of any object which stores initial game configuration
 * data retrieved from an external storage source.
 *
 * This interface might be implemented by a value or container class
 * which simply stores the information retrieved or by a class also
 * implementing the retrieval functionality for the data, if it is
 * trivial.
 */
public interface GameConfig {

    /**
     * Gets the value objects for all game levels as a list
     *
     * @return a list of value objects for instantiating the game levels
     */
    List<DummyLevel> getLevels();

    Set<Class<? extends Player>> getPlayerClasses();

    Set<Class<? extends GameUpdater>> getUpdaterClasses();

    /**
     * Returns a map which states which directions an actor
     * is allowed to move in. This information also means
     * for which directions an actor has a sprite associated
     * with that direction (probably will change to a more
     * elegant solution)
     *
     * @return the mapping between actors and the allowed
     *         directions they can be oriented at
     */
    Map<Class<? extends ActorBasic>, Set<Direction>> getActorToDirectionsMap();
}
