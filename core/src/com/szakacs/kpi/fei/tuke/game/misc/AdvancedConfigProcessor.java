package com.szakacs.kpi.fei.tuke.game.misc;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.eventHandlers.GameInitializer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * Created by developer on 16.11.2016.
 */
public class AdvancedConfigProcessor extends DefaultHandler {

    private int width;
    private int height;
    private int offsetX;
    private int offsetY;
    private int initX;
    private int initY;
    private String rootTunnel;

    private Map<String, DummyTunnel> dummyTunnels;
    private Map<DummyTunnel, Map<Integer, DummyTunnel>> interconnects;
    private Map<ActorType, List<Direction>> actorToDirectionsMap;
    private ProcessingState state;

    private enum ProcessingState{
        INITIALIZING, TUNNELS, INTERCONNECTIONS, ACTORS
    }

    public AdvancedConfigProcessor(){
        super();
        this.dummyTunnels = new HashMap<>(6);
        this.interconnects = new HashMap<>(5);
        this.actorToDirectionsMap = new EnumMap<>(ActorType.class);
        this.state = ProcessingState.INITIALIZING;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (this.state){
            case INITIALIZING:
                this.offsetX = Integer.parseInt(attributes.getValue("offsetX"));
                this.offsetY = Integer.parseInt(attributes.getValue("offsetY"));
                this.width = Integer.parseInt(attributes.getValue("width"));
                this.height = Integer.parseInt(attributes.getValue("height"));
                this.initX = Integer.parseInt(attributes.getValue("initX"));
                this.initY = Integer.parseInt(attributes.getValue("initY"));
                this.width *= this.offsetX;
                this.height *= this.offsetY;
                this.initX *= this.offsetX;
                this.initY *= this.offsetY;
                this.rootTunnel = attributes.getValue("to");
                this.state = ProcessingState.TUNNELS;
                break;
            case TUNNELS:
                if (qName.equalsIgnoreCase("ht")){
                    String id = attributes.getValue("id");
                    dummyTunnels.put(id , new DummyTunnel(
                            Integer.parseInt(attributes.getValue("x")),
                            Integer.parseInt(attributes.getValue("y")),
                            Integer.parseInt(attributes.getValue("width")),
                            id
                            )
                    );
                }
                break;
            case INTERCONNECTIONS:
                if (qName.equalsIgnoreCase("ic")){
                    DummyTunnel dt = dummyTunnels.get(attributes.getValue("from"));
                    Map<Integer, DummyTunnel> exitMap = interconnects.get(dt);
                    if (exitMap == null) {
                        exitMap = new HashMap<>(3);
                        interconnects.put(dt, exitMap);
                    }
                    exitMap.put(
                            Integer.parseInt(attributes.getValue("x")) * this.offsetX,
                            dummyTunnels.get(attributes.getValue("to") )
                    );
                }
                break;
            case ACTORS:
                if (qName.equalsIgnoreCase("actor")){
                    ActorType at = ActorType.valueOf(attributes.getValue("name").toUpperCase());
                    List<Direction> dirs = new ArrayList<>(Direction.values().length);
                    if (Boolean.parseBoolean(attributes.getValue("left")))
                        dirs.add(Direction.LEFT);
                    if (Boolean.parseBoolean(attributes.getValue("right")))
                        dirs.add(Direction.RIGHT);
                    if (Boolean.parseBoolean(attributes.getValue("up")))
                        dirs.add(Direction.UP);
                    if (Boolean.parseBoolean(attributes.getValue("down")))
                        dirs.add(Direction.DOWN);
                    actorToDirectionsMap.put(at, dirs);
                }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("htunnels"))
            this.state = ProcessingState.INTERCONNECTIONS;
        else if (qName.equalsIgnoreCase("interconnections"))
            this.state = ProcessingState.ACTORS;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public String getRootTunnel() {
        return rootTunnel;
    }

    public Map<String, DummyTunnel> getDummyTunnels() {
        return dummyTunnels;
    }

    public Map<DummyTunnel, Map<Integer, DummyTunnel>> getInterconnects() {
        return interconnects;
    }

    public Map<ActorType, List<Direction>> getActorToDirectionsMap() {
        return actorToDirectionsMap;
    }
}
