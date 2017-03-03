package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by developer on 2.2.2017.
 */
public class SAXGameParser extends DefaultHandler implements GameConfig {

    private enum ProcessingState{
        INITIALIZING, LEVELS, ACTORS
    }
    private class PackageNameContainer {
        private String playerPackage;
        private String actorPackage;
        private String updaterPackage;
    }

    private List<DummyLevel> levels;
    private Map<Class<? extends Actor>, Set<Direction>> actorToDirectionsMap;
    private SAXGameParser.ProcessingState state;
    private SAXParser parser;
    private SAXWorldParser worldConfigProcessor;
    private PackageNameContainer packageNames;

    SAXGameParser() throws SAXException {
        super();
        try {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new SAXException("Error initializing the game parser", e);
        }
        this.worldConfigProcessor = new SAXWorldParser();
        this.packageNames = new PackageNameContainer();
        this.levels = new ArrayList<>();
        this.actorToDirectionsMap = new HashMap<>(12);
        this.state = ProcessingState.INITIALIZING;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state) {
            case INITIALIZING:
                if (qName.equalsIgnoreCase("config")){
                    packageNames.playerPackage = attributes.getValue("player-package");
                    packageNames.actorPackage = attributes.getValue("actor-package");
                    packageNames.updaterPackage = attributes.getValue("updater-package");
                }
                this.state = ProcessingState.LEVELS;
                break;
            case LEVELS:
                if (qName.equalsIgnoreCase("level")){
                    String path = attributes.getValue("world");
                    try {
                        parser.parse(new File(path), worldConfigProcessor);
                    } catch (IOException e) {
                        throw new SAXException("Could not initialize level config file: " + path, e);
                    }
                    DummyLevel level = new DummyLevel(worldConfigProcessor.getWorld());
                    levels.add(level);
                    worldConfigProcessor.reset();
                } else if (qName.equalsIgnoreCase("player")) {
                    DummyLevel currentLevel = levels.get(levels.size() - 1);
                    String className = packageNames.playerPackage + '.' + attributes.getValue("class");
                    Class<? extends Player> playerCls;
                    try {
                        playerCls = (Class<? extends Player>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate player class: " + className, e);
                    }
                    DummyEntrance entrance = currentLevel.getGameWorldPrototype().getDummyEntrances().get(
                            attributes.getValue("entrance")
                    );
                    currentLevel.getEntranceToPlayerMap().put(entrance, playerCls);
                } else if (qName.equalsIgnoreCase("updater")) {
                    DummyLevel currentLevel = levels.get(levels.size() - 1);
                    String className = packageNames.updaterPackage + '.' + attributes.getValue("class");
                    Class<? extends GameUpdater> updaterCls;
                    try {
                        updaterCls = (Class<? extends GameUpdater>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate updater class: " + className, e);
                    }
                    currentLevel.getGameUpdaterClasses().add(updaterCls);
                }
                break;
            case ACTORS:
                if (qName.equalsIgnoreCase("actor")) {
                    String className = packageNames.actorPackage + "." + attributes.getValue("class");
                    Class<? extends Actor> actorClass;
                    try {
                        actorClass = (Class<? extends Actor>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate class for actor: " + className, e);
                    }
                    Set<Direction> dirs = new HashSet<>(Direction.values().length);
                    for (Direction dir : Direction.values()) {
                        if (Boolean.parseBoolean(attributes.getValue(dir.name().toLowerCase())))
                            dirs.add(dir);
                    }
                    actorToDirectionsMap.put(actorClass, dirs);
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("levels")) {
            this.state = SAXGameParser.ProcessingState.ACTORS;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    /*
     * GameConfig methods
     */

    @Override
    public List<DummyLevel> getLevels(){
        return levels;
    }

    @Override
    public Map<Class<? extends Actor>, Set<Direction>> getActorToDirectionsMap() {
        return actorToDirectionsMap;
    }
}
