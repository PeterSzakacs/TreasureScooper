package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorldPrototype;
import com.szakacs.kpi.fei.tuke.game.misc.levels.CoreConfigProcessor;
import com.szakacs.kpi.fei.tuke.game.misc.levels.DummyLevel;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterEnemies;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterWalls;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterBasic;
import com.szakacs.kpi.fei.tuke.game.player.PlayerA;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by developer on 31.12.2016.
 */
public class TreasureScooperBuilder {

    public TreasureScooperBuilder() {
    }

    public GamePrivileged buildGame(GameWorldPrototype gameWorldPrototype, GameType gameType) {
        TreasureScooper game = new TreasureScooper(gameWorldPrototype);
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

    public GamePrivileged buildNewGame() {
        CoreConfigProcessor processor = new CoreConfigProcessor();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("config.xml"), processor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        List<DummyLevel> levels = processor.getLevels();
        return this.buildGame(levels.get(0).getGameWorldPrototype(), levels.get(0).getGameType());
    }
}
