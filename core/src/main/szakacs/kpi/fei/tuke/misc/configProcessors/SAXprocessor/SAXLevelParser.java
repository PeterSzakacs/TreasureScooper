package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 25.3.2017.
 */
public class SAXLevelParser extends DefaultHandler {

    private String worldDir;
    private SAXParser parser;
    private SAXWorldParser worldConfigProcessor;
    private List<DummyLevel> levels;

    SAXLevelParser(String baseDir) throws SAXException {
        try {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new SAXException("Error initializing the game parser", e);
        }
        String dirSeparator = System.getProperty("file.separator");
        this.worldDir = baseDir + "worlds" + dirSeparator;
        this.worldConfigProcessor = new SAXWorldParser();
        this.levels = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("level")){
            String path = worldDir + attributes.getValue("world");
            try {
                parser.parse(new File(path), worldConfigProcessor);
            } catch (IOException e) {
                throw new SAXException("Could not initialize level config file: " + path, e);
            }
            DummyLevel level = new DummyLevel(worldConfigProcessor.getWorld());
            levels.add(level);
            worldConfigProcessor.reset();
        } else if (qName.equalsIgnoreCase("updater")){
            DummyLevel currentLevel = levels.get(levels.size() - 1);
            Class<? extends GameUpdater> updaterCls = SAXGameParser.gameClasses.updaterClasses.get(attributes.getValue("id"));
            currentLevel.getGameUpdaterClasses().add(updaterCls);
        } else if (qName.equalsIgnoreCase("player")){
            DummyLevel currentLevel = levels.get(levels.size() - 1);
            Class<? extends Player> playerCls = SAXGameParser.gameClasses.playerClasses.get(attributes.getValue("id"));
            DummyEntrance entrance = currentLevel.getGameWorldPrototype().getDummyEntrances().get(
                    attributes.getValue("entrance")
            );
            currentLevel.getEntranceToPlayerMap().put(entrance, playerCls);
        }
    }

    List<DummyLevel> getLevels(){
        return levels;
    }
}
