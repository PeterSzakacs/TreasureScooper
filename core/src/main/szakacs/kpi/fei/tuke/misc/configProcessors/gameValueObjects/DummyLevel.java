package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyLevel {

    private GameType gameType;
    private GameWorldPrototype world;
    private Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap;

    public DummyLevel(GameWorldPrototype world, GameType gameType) {
        this.gameType = gameType;
        this.world = world;
        this.entranceToPlayerMap = new HashMap<>(3);
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameWorldPrototype getGameWorldPrototype() {
        return world;
    }

    public Map<DummyEntrance, Class<? extends Player>> getEntranceToPlayerMap() {
        return entranceToPlayerMap;
    }
}
