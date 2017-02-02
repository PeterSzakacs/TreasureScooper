package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.misc.AdvancedConfigProcessor;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterEnemies;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterWalls;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterBasic;
import com.szakacs.kpi.fei.tuke.game.player.PlayerA;
import com.szakacs.kpi.fei.tuke.game.player.PlayerNativeA;

import java.util.*;

/**
 * Created by developer on 31.12.2016.
 */
public class TreasureScooperBuilder {

    public TreasureScooperBuilder() {
    }

    public GamePrivileged buildGame(AdvancedConfigProcessor configProcessor, GameType gameType) {
        TreasureScooper game = new TreasureScooper(configProcessor);
        Set<GameUpdater> updaters = new HashSet<>(3);
        updaters.add(new GameUpdaterBasic(game));
        switch (gameType) {
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
        game.startNewGame(updaters, new PlayerA());
        return game;
    }
}
