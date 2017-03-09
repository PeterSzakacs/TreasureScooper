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
        INITIALIZING, PLAYERS, UPDATERS, LEVELS, ACTORS
    }
    private final class PackageNameContainer {
        private String playerPackage;
        private String actorPackage;
        private String updaterPackage;
    }
    private final class GameClasses {
        private Map<String, Class<? extends Player>> playerClasses;
        private Map<String, Class<? extends GameUpdater>> updaterClasses;
        private Map<Class<? extends Actor>, Set<Direction>> actorToDirectionsMap;

        private GameClasses(){
            playerClasses = new HashMap<>(5);
            updaterClasses = new HashMap<>(5);
            actorToDirectionsMap = new HashMap<>(12);
        }
    }

    private PackageNameContainer packageNames;
    private GameClasses gameClasses;
    private List<DummyLevel> levels;
    private ProcessingState state;
    private SAXParser parser;
    private SAXWorldParser worldConfigProcessor;

    SAXGameParser() throws SAXException {
        super();
        try {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new SAXException("Error initializing the game parser", e);
        }
        this.worldConfigProcessor = new SAXWorldParser();
        this.packageNames = new PackageNameContainer();
        this.gameClasses = new GameClasses();
        this.levels = new ArrayList<>();
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
                this.state = ProcessingState.PLAYERS;
                break;
            case PLAYERS:
                if (qName.equalsIgnoreCase("player")) {
                    String className = packageNames.playerPackage + '.' + attributes.getValue("class");
                    Class<? extends Player> playerCls;
                    try {
                        playerCls = (Class<? extends Player>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate player class: " + className, e);
                    }
                    gameClasses.playerClasses.put(attributes.getValue("id"), playerCls);
                }
                break;
            case UPDATERS:
                if (qName.equalsIgnoreCase("updater")) {
                    String className = packageNames.updaterPackage + '.' + attributes.getValue("class");
                    Class<? extends GameUpdater> updaterCls;
                    try {
                        updaterCls = (Class<? extends GameUpdater>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate updater class: " + className, e);
                    }
                    gameClasses.updaterClasses.put(attributes.getValue("id"), updaterCls);
                }
                break;
            case LEVELS:
                processLevel(qName, attributes);
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
                    gameClasses.actorToDirectionsMap.put(actorClass, dirs);
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("players")){
            state = ProcessingState.UPDATERS;
        } else if (qName.equalsIgnoreCase("updaters")){
            state = ProcessingState.LEVELS;
        } else if (qName.equalsIgnoreCase("levels")) {
            state = ProcessingState.ACTORS;
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
    public Set<Class<? extends Player>> getPlayerClasses() {
        return new HashSet<>(gameClasses.playerClasses.values());
    }

    @Override
    public Set<Class<? extends GameUpdater>> getUpdaterClasses() {
        return new HashSet<>(gameClasses.updaterClasses.values());
    }

    @Override
    public Map<Class<? extends Actor>, Set<Direction>> getActorToDirectionsMap() {
        return gameClasses.actorToDirectionsMap;
    }

    /*
     * Helper methods
     */

    private void processLevel(String qName, Attributes attributes) throws SAXException {
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
        } else if (qName.equalsIgnoreCase("updater")){
            DummyLevel currentLevel = levels.get(levels.size() - 1);
            Class<? extends GameUpdater> updaterCls = gameClasses.updaterClasses.get(attributes.getValue("id"));
            currentLevel.getGameUpdaterClasses().add(updaterCls);
        } else if (qName.equalsIgnoreCase("player")){
            DummyLevel currentLevel = levels.get(levels.size() - 1);
            Class<? extends Player> playerCls = gameClasses.playerClasses.get(attributes.getValue("id"));
            DummyEntrance entrance = currentLevel.getGameWorldPrototype().getDummyEntrances().get(
                    attributes.getValue("entrance")
            );
            currentLevel.getEntranceToPlayerMap().put(entrance, playerCls);
        }
    }
}
