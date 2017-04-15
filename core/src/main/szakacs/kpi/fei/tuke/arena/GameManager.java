package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.arena.game.TreasureScooperLevel;
import szakacs.kpi.fei.tuke.intrfc.Player;
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

    private class Results implements GameResults{
        private List<Map<Class<? extends Player>, Integer>> levelScores;
        private Map<Class<? extends Player>, Integer> totalGameScores;

        private Results(int numLevels, int playerClassesCount){
            this.totalGameScores = new HashMap<>(playerClassesCount);
            this.levelScores = new ArrayList<>(numLevels);
        }

        public List<Map<Class<? extends Player>, Integer>> getLevelScores() {
            return levelScores;
        }

        public Map<Class<? extends Player>, Integer> getTotalGameScores() {
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
        Set<Class<? extends Player>> playerClasses = config.getPlayerClasses();

        this.results = new Results(dummyLevels.size(), playerClasses.size());
        for (Class<? extends Player> playerCls : playerClasses){
            results.totalGameScores.put(playerCls, 0);
        }
        for (DummyLevel dummyLevel : dummyLevels) {
            results.levelScores.add(new HashMap<>(
                            dummyLevel.getEntranceToPlayerMap().size()
                    )
            );
        }
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
        Map<Class<? extends Player>, Integer> playerClassScores = results.levelScores.get(nextLevelIndex);
        Map<Player, Integer> playerScores = currentGameLevel.getPlayerManager().getPlayersAndScores();
        for (Player player : playerScores.keySet()) {
            Class<? extends Player> playerCls = player.getClass();
            playerClassScores.put(playerCls, playerScores.get(player));
            results.totalGameScores.put(playerCls,
                    results.totalGameScores.get(playerCls) + playerScores.get(player)
            );
        }
    }
}
