package szakacs.kpi.fei.tuke.misc.configProcessors.world;

import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * Created by developer on 2.2.2017.
 */
public class GameWorldConfigProcessor extends DefaultHandler {

    private enum ProcessingState{
        INITIALIZING, TUNNELS, INTERCONNECTIONS, ENTRANCES
    }

    private DummyWorld world;
    private GameWorldConfigProcessor.ProcessingState state;

    public GameWorldConfigProcessor(){
        super();
        this.state = GameWorldConfigProcessor.ProcessingState.INITIALIZING;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state){
            case INITIALIZING:
                this.world = new DummyWorld();
                world.offsetX = Integer.parseInt(attributes.getValue("offsetX"));
                world.offsetY = Integer.parseInt(attributes.getValue("offsetY"));
                world.width = world.offsetX * Integer.parseInt(attributes.getValue("width"));
                world.height = world.offsetY * Integer.parseInt(attributes.getValue("height"));
                this.state = GameWorldConfigProcessor.ProcessingState.TUNNELS;
                break;
            case TUNNELS:
                if (qName.equalsIgnoreCase("ht")){
                    String id = attributes.getValue("id");
                    DummyTunnel dt = new DummyTunnel();
                    dt.id = id;
                    dt.x = world.offsetX * Integer.parseInt(attributes.getValue("x"));
                    dt.y = world.offsetY * Integer.parseInt(attributes.getValue("y"));
                    dt.numCells = Integer.parseInt(attributes.getValue("width"));
                    world.dummyTunnels.put(id , dt);
                }
                break;
            case INTERCONNECTIONS:
                if (qName.equalsIgnoreCase("ic")){
                    Map<String, DummyTunnel> dummyTunnels = world.dummyTunnels;
                    DummyTunnel dt = dummyTunnels.get(attributes.getValue("from"));
                    dt.connectedTunnelsBelow.put(
                            Integer.parseInt(attributes.getValue("x")),
                            dummyTunnels.get(attributes.getValue("to"))
                    );
                }
                break;
            case ENTRANCES:
                if (qName.equalsIgnoreCase("entrance")){
                    List<DummyEntrance> dummyEntrances = world.dummyEntrances;
                    DummyEntrance de = new DummyEntrance();
                    de.x = world.offsetX * Integer.parseInt(attributes.getValue("x"));
                    de.y = world.offsetY * Integer.parseInt(attributes.getValue("y"));
                    de.tunnel = world.dummyTunnels.get(attributes.getValue("to"));
                    dummyEntrances.add(de);
                }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("htunnels"))
            this.state = GameWorldConfigProcessor.ProcessingState.INTERCONNECTIONS;
        else if (qName.equalsIgnoreCase("interconnections"))
            this.state = GameWorldConfigProcessor.ProcessingState.ENTRANCES;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public GameWorldPrototype getWorld(){
        return this.world;
    }

    public void reset(){
        this.state = ProcessingState.INITIALIZING;
        this.world = null;
    }
}
