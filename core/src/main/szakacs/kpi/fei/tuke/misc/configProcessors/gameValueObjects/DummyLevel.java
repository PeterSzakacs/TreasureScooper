package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by developer on 2.2.2017.
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

    public Set<Class<? extends GameUpdater>> getGameUpdaterClasses() {
        return gameUpdaterClasses;
    }

    public GameWorldPrototype getGameWorldPrototype() {
        return world;
    }

    public Map<DummyEntrance, Class<? extends Player>> getEntranceToPlayerMap() {
        return entranceToPlayerMap;
    }
}
