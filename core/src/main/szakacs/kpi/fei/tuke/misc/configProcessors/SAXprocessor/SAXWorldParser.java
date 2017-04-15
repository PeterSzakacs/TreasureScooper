package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import szakacs.kpi.fei.tuke.intrfc.misc.GameWorldPrototype;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyTunnel;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyWorld;

import java.util.Map;

/**
 * Created by developer on 2.2.2017.
 */
public class SAXWorldParser extends DefaultHandler {

    private enum ProcessingState{
        INITIALIZING, TUNNELS, INTERCONNECTIONS, ENTRANCES
    }

    private DummyWorld world;
    private SAXWorldParser.ProcessingState state;

    SAXWorldParser(){
        super();
        this.state = SAXWorldParser.ProcessingState.INITIALIZING;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state){
            case INITIALIZING:
                int offsetX = Integer.parseInt(attributes.getValue("offsetX"));
                int offsetY = Integer.parseInt(attributes.getValue("offsetY"));
                this.world = new DummyWorld(
                offsetX * Integer.parseInt(attributes.getValue("width")),
                offsetY * Integer.parseInt(attributes.getValue("height")),
                       offsetX,
                       offsetY
                );
                this.state = SAXWorldParser.ProcessingState.TUNNELS;
                break;
            case TUNNELS:
                if (qName.equalsIgnoreCase("ht")){
                    String id = attributes.getValue("id");
                    DummyTunnel dt = new DummyTunnel(
                            world.getOffsetX() * Integer.parseInt(attributes.getValue("x")),
                            world.getOffsetY() * Integer.parseInt(attributes.getValue("y")),
                            Integer.parseInt(attributes.getValue("width")),
                            id
                    );
                    world.getDummyTunnels().put(id , dt);
                }
                break;
            case INTERCONNECTIONS:
                if (qName.equalsIgnoreCase("ic")){
                    Map<String, DummyTunnel> dummyTunnels = world.getDummyTunnels();
                    DummyTunnel dt = dummyTunnels.get(attributes.getValue("from"));
                    dt.getConnectedTunnelsBelow().put(
                            Integer.parseInt(attributes.getValue("x")) * world.getOffsetX(),
                            dummyTunnels.get(attributes.getValue("to"))
                    );
                }
                break;
            case ENTRANCES:
                if (qName.equalsIgnoreCase("entrance")){
                    Map<String, DummyEntrance> dummyEntrances = world.getDummyEntrances();
                    String id = attributes.getValue("id");
                    DummyEntrance de = new DummyEntrance(
                            world.getOffsetX() * Integer.parseInt(attributes.getValue("x")),
                            world.getOffsetY() * Integer.parseInt(attributes.getValue("y")),
                            world.getDummyTunnels().get(attributes.getValue("to")),
                            id
                    );
                    dummyEntrances.put(id, de);
                }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("htunnels"))
            this.state = SAXWorldParser.ProcessingState.INTERCONNECTIONS;
        else if (qName.equalsIgnoreCase("interconnections"))
            this.state = SAXWorldParser.ProcessingState.ENTRANCES;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public GameWorldPrototype getWorld(){
        return this.world;
    }

    void reset() {
        this.state = ProcessingState.INITIALIZING;
        this.world = null;
    }
}
