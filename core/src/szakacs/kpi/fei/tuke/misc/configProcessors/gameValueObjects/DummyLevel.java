package szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects;

import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyLevel {

    private GameType gameType;
    private GameWorldPrototype world;

    public DummyLevel(GameWorldPrototype world, GameType gameType) {
        this.gameType = gameType;
        this.world = world;
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameWorldPrototype getGameWorldPrototype() {
        return world;
    }
}
