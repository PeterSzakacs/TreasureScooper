package com.szakacs.kpi.fei.tuke.game.misc.levels;

import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorldPrototype;

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
