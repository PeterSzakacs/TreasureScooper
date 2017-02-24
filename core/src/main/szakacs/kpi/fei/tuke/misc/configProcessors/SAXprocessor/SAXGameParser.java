package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
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
import java.rmi.activation.UnknownObjectException;
import java.util.*;

/**
 * Created by developer on 2.2.2017.
 */
public class SAXGameParser extends DefaultHandler implements GameConfig {

    private enum ProcessingState{
        INITIALIZING, LEVELS, PLAYERS, ACTORS
    }

    private List<DummyLevel> levels;
    private Map<Class<? extends Actor>, Set<Direction>> actorToDirectionsMap;
    private SAXGameParser.ProcessingState state;
    private SAXParser parser;
    private SAXWorldParser worldConfigProcessor;
    private String playerPackage;
    private String actorPackage;

    SAXGameParser() throws SAXException {
        super();
        this.actorToDirectionsMap = new HashMap<>(12);
        this.state = ProcessingState.INITIALIZING;
        this.levels = new ArrayList<>();
        this.worldConfigProcessor = new SAXWorldParser();
        try {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new SAXException("Error retrieving initializing the game parser", e);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state){
            case INITIALIZING:
                if (qName.equalsIgnoreCase("config")){
                    this.playerPackage = attributes.getValue("player-package");
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
                    DummyLevel level = new DummyLevel(
                            worldConfigProcessor.getWorld(),
                            GameType.valueOf(attributes.getValue("gameType").toUpperCase())
                    );
                    levels.add(level);
                    worldConfigProcessor.reset();
                    this.state = ProcessingState.PLAYERS;
                }
                break;
            case PLAYERS:
                if (qName.equalsIgnoreCase("player")){
                    DummyLevel currentLevel = levels.get(levels.size() - 1);
                    String classname = this.playerPackage + '.' + attributes.getValue("class");
                    Class<? extends Player> playerCls;
                    try {
                        playerCls = (Class<? extends Player>) Class.forName(classname);
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate player class: " + classname, e);
                    }
                    DummyEntrance entrance = currentLevel.getGameWorldPrototype().getDummyEntrances().get(
                            attributes.getValue("entrance")
                    );
                    currentLevel.getEntranceToPlayerMap().put(entrance, playerCls);
                }
                break;
            case ACTORS:
                if (qName.equalsIgnoreCase("actors")){
                    this.actorPackage = attributes.getValue("actor-package");
                } else {
                    Class<? extends Actor> actorClass = null;
                    try {
                        actorClass = (Class<? extends Actor>) Class.forName(
                                actorPackage + "." + attributes.getValue("class")
                        );
                    } catch (ClassNotFoundException e) {
                        throw new SAXException("Could not locate class for actor: " + actorClass, e);
                    }
                    Set<Direction> dirs = new HashSet<>(Direction.values().length);
                    for (Direction dir : Direction.values()){
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
        if (qName.equalsIgnoreCase("level")) {
            this.state = ProcessingState.LEVELS;
        } else if (qName.equalsIgnoreCase("levels")) {
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
