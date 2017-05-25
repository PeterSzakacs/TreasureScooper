package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A value object representing a particular game level.
 */
public class DummyLevel {

    private Set<Class<? extends GameUpdater>> gameUpdaterClasses;
    private GameWorldPrototype world;
    private Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap;

    public DummyLevel(GameWorldPrototype world) {
        this.gameUpdaterClasses = new HashSet<>(3);
        this.world = world;
        this.entranceToPlayerMap = new HashMap<>(3);
    }

    /**
     * Gets the set of additional updater classes used in this game level.
     *
     * @return the set of additional updater classes used in this game level
     */
    public Set<Class<? extends GameUpdater>> getGameUpdaterClasses() {
        return gameUpdaterClasses;
    }

    /**
     * Gets the value object representing the game world of this level.
     *
     * @return the value object representing the game world of this level
     */
    public GameWorldPrototype getGameWorldPrototype() {
        return world;
    }

    /**
     * Gets a mapping between value objects representing the entrances
     * to the tunnel maze and player classes, which have an instance
     * with a pipe, whose initial position is the topmost cell of this
     * entrance.
     *
     * @return a mapping of value objects representing entrances to player classes.
     */
    public Map<DummyEntrance, Class<? extends Player>> getEntranceToPlayerMap() {
        return entranceToPlayerMap;
    }
}
