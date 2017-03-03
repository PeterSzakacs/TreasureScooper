package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.game.TreasureScooperLevelBuilder;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.List;

/**
 * Created by developer on 3.2.2017.
 */
public class GameManager {

    private final List<DummyLevel> dummyLevels;
    private final TreasureScooperLevelBuilder gameLevelBuilder;
    private GameLevelPrivileged currentGameLevel;
    private int nextLevelIndex;

    public GameManager(GameConfig config) {
        this.dummyLevels = config.getLevels();
        this.gameLevelBuilder = new TreasureScooperLevelBuilder();
        this.nextLevelIndex = 0;
    }

    public GameLevelPrivileged getNextGameLevel() throws ConfigProcessingException {
        if (nextLevelIndex < dummyLevels.size()) {
            DummyLevel level = dummyLevels.get(nextLevelIndex);
            this.currentGameLevel = gameLevelBuilder.buildGameLevel(level);
            nextLevelIndex++;
        } else {
            this.currentGameLevel = null;
        }
        return currentGameLevel;
    }

    public GameLevelPrivileged getCurrentGameLevel() {
        return currentGameLevel;
    }
}
