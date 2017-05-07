package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.game.TreasureScooperLevel;
import szakacs.kpi.fei.tuke.intrfc.misc.GameResult;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameResults;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.GameLevelInitializationException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;

/**
 * Created by developer on 3.2.2017.
 */
public class GameManager {

    public class GameResultImpl implements GameResult {
        private Class<? extends Player> playerClass;
        private int score;
        private boolean failed;

        private GameResultImpl(
                Class<? extends Player> playerClass,
                int score, boolean failed){
            this.playerClass = playerClass;
            this.score = score;
            this.failed = failed;
        }

        public Class<? extends Player> getPlayerClass() { return playerClass; }
        public int getScore(){ return score; }
        public boolean hasFailed(){ return failed; }
    }

    private class Results implements GameResults{
        private List<Map<Class<? extends Player>, GameResult>> levelScores;
        private Map<Class<? extends Player>, GameResult> totalGameScores;

        private Results(GameConfig config){
            Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();
            this.totalGameScores = new HashMap<>(playerClasses.size());
            this.levelScores = new ArrayList<>(config.getLevels().size());
            for (Class<? extends Player> playerCls : playerClasses){
                totalGameScores.put(
                        playerCls,
                        new GameResultImpl(playerCls, 0, false)
                );
            }
            for (DummyLevel dummyLevel : dummyLevels) {
                levelScores.add(
                        new HashMap<>(dummyLevel.getEntranceToPlayerMap().size())
                );
            }
        }

        public List<Map<Class<? extends Player>, GameResult>> getLevelScores() {
            return levelScores;
        }

        public Map<Class<? extends Player>, GameResult> getTotalGameScores() {
            return totalGameScores;
        }
    }
    private Results results;

    private final List<DummyLevel> dummyLevels;
    private GameLevelPrivileged currentGameLevel;
    private int nextLevelIndex;

    public GameManager(GameConfig config) throws ConfigProcessingException {
        this.dummyLevels = config.getLevels();
        this.currentGameLevel = new TreasureScooperLevel(config);
        this.nextLevelIndex = -1;
        this.results = new Results(config);
    }

    public GameLevelPrivileged getNextGameLevel() throws GameLevelInitializationException {

        if (nextLevelIndex >= 0) {
            archiveScores();
        }

        // Start the next game level
        nextLevelIndex++;
        if (nextLevelIndex < dummyLevels.size()) {
            this.currentGameLevel.startNewGame(dummyLevels.get(nextLevelIndex));
        } else {
            this.currentGameLevel = null;
        }
        return currentGameLevel;
    }

    public GameLevelPrivileged getCurrentGameLevel() {
        return currentGameLevel;
    }

    public GameResults getResults() {
        return results;
    }

    private void archiveScores(){
        // Archive the scores of players for the previous level
        Map<Class<? extends Player>, GameResult> playerClassScores = results.levelScores.get(nextLevelIndex);
        Set<PlayerInfo> playerScores = currentGameLevel.getPlayerManager().getPlayerInfo();
        for (PlayerInfo info : playerScores) {
            // Casting in the current context is safe.
            Class<? extends Player> playerCls = ((Player)info.getPlayer()).getClass();
            GameResultImpl result = new GameResultImpl(playerCls, info.getScore(), false);
            playerClassScores.put(playerCls, result);
            GameResultImpl previous = (GameResultImpl) results.totalGameScores.get(playerCls);
            if (previous != null){
                previous.score += info.getScore();
                previous.failed |= false;
            }
        }
        Collection<PlayerInfo> failedPlayers = currentGameLevel.getPlayerManager()
                .getUnregisteredPlayers().values();
        for (PlayerInfo info : failedPlayers){
            Class<? extends Player> playerCls = ((Player)info.getPlayer()).getClass();
            GameResultImpl result = new GameResultImpl(playerCls, info.getScore(), true);
            playerClassScores.put(playerCls, result);
            GameResultImpl previous = (GameResultImpl) results.totalGameScores.get(playerCls);
            if (previous != null){
                previous.score += info.getScore();
                previous.failed |= true;
            }
        }
    }
}
