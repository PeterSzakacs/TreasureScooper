package szakacs.kpi.fei.tuke.game;

import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.misc.configProcessors.levels.CoreConfigProcessor;
import szakacs.kpi.fei.tuke.misc.configProcessors.levels.DummyLevel;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterEnemies;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterWalls;
import szakacs.kpi.fei.tuke.game.updaters.GameUpdaterBasic;
import org.xml.sax.SAXException;
import szakacs.kpi.fei.tuke.player.PlayerA;

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
