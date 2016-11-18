package com.szakacs.kpi.fei.tuke.game.misc;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 7.11.2016.
 */
public class ConfigurationProcessor extends DefaultHandler {

    private List<List<Integer>> levelEntrances;
    private List<Integer> yCoordinatesOfLevels;
    private int current_level;
    private boolean entranceFound;
    //private boolean std_offset_found;

    public ConfigurationProcessor(){
        super();
        this.levelEntrances = new ArrayList<List<Integer>>();
        this.yCoordinatesOfLevels = new ArrayList<Integer>();
        this.entranceFound = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        if (qName.equalsIgnoreCase("htunnel")){
            this.current_level = Integer.parseInt(attributes.getValue("level"));
            this.levelEntrances.add(this.current_level, new ArrayList<Integer>());
            this.yCoordinatesOfLevels.add(this.current_level, Integer.parseInt(attributes.getValue("y")));
        } else if (qName.equalsIgnoreCase("entrance")){
            this.entranceFound = true;
        }/* else if (qName.equalsIgnoreCase("std_offset")){
            this.std_offset_found = true;
        }*/
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Uncomment if debugging the parser
        /*if (qName.equalsIgnoreCase("config")){
            for (int idx = 0; idx < yCoordinatesOfLevels.size(); idx++){
                System.out.println("Y: " + yCoordinatesOfLevels.get(idx));
                for (int entrance : levelEntrances.get(idx)) {
                    System.out.println("Entrance: " + entrance);
                }
            }
        }*/
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (this.entranceFound){
            this.levelEntrances.get(this.current_level).add(Integer.parseInt(new String(ch, start, length)));
            this.entranceFound = false;
        } /*else if (this.std_offset_found){
            this.STD_OFFSET = Integer.parseInt(new String(ch, start, length));
            this.std_offset_found = false;
        }*/
    }

/*    public int getSTD_OFFSET() {
        return STD_OFFSET;
    }*/

    public List<List<Integer>> getLevelEntrances() {
        return levelEntrances;
    }

    public List<Integer> getyCoordinatesOfLevels() {
        return yCoordinatesOfLevels;
    }
}
