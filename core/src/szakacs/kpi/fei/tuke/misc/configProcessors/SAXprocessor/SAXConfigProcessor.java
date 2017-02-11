package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.xml.sax.SAXException;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by developer on 11.2.2017.
 */
public class SAXConfigProcessor implements GameConfigProcessor {

    private SAXGameParser SAXprocessor;

    public SAXConfigProcessor() {
        this.SAXprocessor = new SAXGameParser();
    }

    @Override
    public void processGameConfig() {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File("config.xml"), SAXprocessor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameConfig getGameConfig() {
        return SAXprocessor;
    }
}
