package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.GameType;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
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
        LEVELS, ACTORS
    }

    private List<DummyLevel> levels;
    private Map<ActorType, Set<Direction>> actorToDirectionsMap;
    private SAXGameParser.ProcessingState state;
    private SAXParser parser;
    private SAXWorldParser worldConfigProcessor;


    SAXGameParser(){
        super();
        this.actorToDirectionsMap = new EnumMap<>(ActorType.class);
        this.state = SAXGameParser.ProcessingState.LEVELS;
        this.levels = new ArrayList<>();
        this.worldConfigProcessor = new SAXWorldParser();
        try {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state){
            case LEVELS:
                if (qName.equalsIgnoreCase("level")){
                    String path = attributes.getValue("world");
                    try {
                        parser.parse(new File(path), worldConfigProcessor);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new SAXException("Could not initialize level config file: " + path);
                    }
                    DummyLevel level = new DummyLevel(
                            worldConfigProcessor.getWorld(),
                            GameType.valueOf(attributes.getValue("gameType").toUpperCase())
                    );
                    levels.add(level);
                    worldConfigProcessor.reset();
                }
                break;
            case ACTORS:
                if (qName.equalsIgnoreCase("actor")){
                    ActorType at = ActorType.valueOf(attributes.getValue("name").toUpperCase());
                    Set<Direction> dirs = new HashSet<>(Direction.values().length);
                    for (Direction dir : Direction.values()){
                        if (Boolean.parseBoolean(attributes.getValue(dir.name().toLowerCase())))
                            dirs.add(dir);
                    }
                    actorToDirectionsMap.put(at, dirs);
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("levels"))
            this.state = SAXGameParser.ProcessingState.ACTORS;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    /*
     * ConfigProcessorMethods
     */

    @Override
    public List<DummyLevel> getLevels(){
        return levels;
    }

    @Override
    public Map<ActorType, Set<Direction>> getActorToDirectionsMap() {
        return actorToDirectionsMap;
    }
}
