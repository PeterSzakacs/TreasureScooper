package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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

    /**
     * Gets a set of all player classes that are expected to play
     * at least in one level during the game.
     *
     * @return a set of all expected player classes to be used during the game
     */
    Set<Class<? extends Player>> getPlayerClasses();

    /**
     * Gets a set of all additional updater classes that are expected to be used
     * at least in one level during the game.
     *
     * @return a set of all additional updater classes that are expected to be used
     *         at least in one level during the game
     */
    Set<Class<? extends GameUpdater>> getUpdaterClasses();

    /**
     * Returns a mapping between actor classes and additional information
     * about them used for rendering purposes.
     *
     * @return a mapping between actor classes and additional information
     *         about them used for rendering purposes.
     */
    Map<Class<? extends ActorBasic>, ActorClassInfo> getActorInfoMap();
}
