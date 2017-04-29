package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.misc.ActorInfo;
import szakacs.kpi.fei.tuke.intrfc.player.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.ActorInfoImpl;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

/**
 * Created by developer on 2.2.2017.
 */
public class SAXGameParser extends DefaultHandler implements GameConfig {

    final static class GameClasses {
        Map<String, Class<? extends Player>> playerClasses = new HashMap<>(5);
        Map<String, Class<? extends GameUpdater>> updaterClasses = new HashMap<>(5);
        Map<Class<? extends ActorBasic>, ActorInfo> actorToDirectionsMap = new HashMap<>(12);
    }
    static final GameClasses gameClasses = new GameClasses();


    private enum ProcessingState{
        INITIALIZING, PLAYERS, UPDATERS, LEVEL, ACTORS
    }
    private ProcessingState state;


    private final class PackageNameContainer {
        private String playerPackage;
        private String actorPackage;
        private String updaterPackage;
    }
    private final PackageNameContainer packageNames;



    private final String scenariosDir;
    private String levelFile;
    private SAXLevelParser levelParser;
    private Class<? extends ActorBasic> currentActorClass = null;



    SAXGameParser(String baseDir) throws SAXException {
        super();
        this.levelParser = new SAXLevelParser(baseDir);
        String dirSeparator = System.getProperty("file.separator");
        this.scenariosDir = baseDir + "scenarios" + dirSeparator;
        this.packageNames = new PackageNameContainer();
        this.state = ProcessingState.INITIALIZING;
    }



    @Override
    @SuppressWarnings("unchecked")
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state) {
            case INITIALIZING:
                if (qName.equalsIgnoreCase("game")){
                    packageNames.playerPackage = attributes.getValue("player-package");
                    packageNames.actorPackage = attributes.getValue("actor-package");
                    packageNames.updaterPackage = attributes.getValue("updater-package");
                    levelFile = scenariosDir + attributes.getValue("level-file");
                }
                this.state = ProcessingState.PLAYERS;
                break;
            case PLAYERS:
                if (qName.equalsIgnoreCase("player")) {
                    String className = packageNames.playerPackage + '.' + attributes.getValue("class");
                    Class<? extends Player> playerCls;
                    try {
                        playerCls = (Class<? extends Player>) Class.forName(className);
                    } catch (ClassNotFoundException | ClassCastException e) {
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
                    } catch (ClassNotFoundException | ClassCastException e) {
                        throw new SAXException("Could not locate updater class: " + className, e);
                    }
                    gameClasses.updaterClasses.put(attributes.getValue("id"), updaterCls);
                }
                break;
            case ACTORS:
                if (qName.equalsIgnoreCase("actor")) {
                    String className = packageNames.actorPackage + "." + attributes.getValue("class");
                    Class<? extends ActorBasic> actorCls;
                    try {
                        actorCls = (Class<? extends ActorBasic>) Class.forName(className);
                    } catch (ClassNotFoundException | ClassCastException e) {
                        throw new SAXException("Could not locate class for actor: " + className, e);
                    }
                    ActorInfo info = new ActorInfoImpl();
                    for (Direction dir : Direction.values()) {
                        if (Boolean.parseBoolean(attributes.getValue(dir.name().toLowerCase())))
                            info.getActorDirections().add(dir);
                    }
                    currentActorClass = actorCls;
                    gameClasses.actorToDirectionsMap.put(actorCls, info);
                } else if (qName.equalsIgnoreCase("property")){
                    ActorInfo info = gameClasses.actorToDirectionsMap.get(currentActorClass);
                    for (int idx = 0; idx < attributes.getLength(); idx++) {
                        info.getProperties().put(attributes.getQName(idx), attributes.getValue(idx));
                    }
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("players")){
            state = ProcessingState.UPDATERS;
        } else if (qName.equalsIgnoreCase("updaters")){
            state = ProcessingState.ACTORS;
        } else if (qName.equalsIgnoreCase("actors")) {
            state = ProcessingState.LEVEL;
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse(levelFile, levelParser);
            } catch (IOException | ParserConfigurationException e) {
                throw new SAXException("Error parsing level file: " + levelFile, e);
            }
        }
    }

    /*
     * GameConfig methods
     */

    @Override
    public List<DummyLevel> getLevels(){
        return levelParser.getLevels();
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
    public Map<Class<? extends ActorBasic>, ActorInfo> getActorInfoMap() {
        return gameClasses.actorToDirectionsMap;
    }
}
