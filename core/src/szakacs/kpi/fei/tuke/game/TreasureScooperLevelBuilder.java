package szakacs.kpi.fei.tuke.game;

import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.GameUpdater;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterEnemies;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterWalls;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterBasic;
import szakacs.kpi.fei.tuke.player.PlayerA;

import java.util.*;

/**
 * Created by developer on 31.12.2016.
 */
public class TreasureScooperLevelBuilder {

    public TreasureScooperLevelBuilder() {
    }

    public GameLevelPrivileged buildGameLevel(DummyLevel level) {
        TreasureScooper game = new TreasureScooper(level.getGameWorldPrototype());
        Set<GameUpdater> updaters = new HashSet<>(3);
        updaters.add(new GameUpdaterBasic(game));
        switch (level.getGameType()) {
            case STACK:
                break;
            case QUEUE:
                updaters.add(new GameUpdaterWalls(game));
                break;
            case ENEMIES:
                updaters.add(new GameUpdaterEnemies(game));
                break;
            case ULTIMATE:
                updaters.add(new GameUpdaterWalls(game));
                updaters.add(new GameUpdaterEnemies(game));
                break;
        }
        /*try {
            Class playerClass = Class.forName("szakacs.kpi.fei.tuke.player.PlayerA");
            Player player = (Player) playerClass.newInstance();
            game.startNewGame(updaters, player);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }*/
        game.startNewGame(updaters, new PlayerA());
        return game;
    }
}
