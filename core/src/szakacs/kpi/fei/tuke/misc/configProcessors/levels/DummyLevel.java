package szakacs.kpi.fei.tuke.misc.configProcessors.levels;

import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;

/**
 * Created by developer on 2.2.2017.
 */
public class DummyLevel {

    GameType gameType;
    GameWorldPrototype world;

    DummyLevel() {
    }

    public GameType getGameType() {
        return gameType;
    }

    public GameWorldPrototype getGameWorldPrototype() {
        return world;
    }
}
