package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameInitializer;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.misc.levels.DummyLevel;

import java.util.List;

/**
 * Created by developer on 3.2.2017.
 */
public class GameManager {

    private final List<DummyLevel> dummyLevels;
    private final TreasureScooperBuilder gameLevelBuilder;
    private GamePrivileged currentGameLevel;
    private int nextLevelIndex;

    public GameManager(GameInitializer initializer) {
        this.dummyLevels = initializer.getLevels();
        this.gameLevelBuilder = new TreasureScooperBuilder();
        this.nextLevelIndex = 0;
    }

    public GamePrivileged getNextGameLevel() {
        if (nextLevelIndex < dummyLevels.size()) {
            DummyLevel level = dummyLevels.get(nextLevelIndex);
            this.currentGameLevel = gameLevelBuilder.buildGame(level.getGameWorldPrototype(), level.getGameType());
            nextLevelIndex++;
        } else {
            this.currentGameLevel = null;
        }
        return currentGameLevel;
    }

    public GamePrivileged getCurrentGameLevel() {
        return currentGameLevel;
    }
}
