package szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * The SAX (simple API for XML) implementation of the {@link GameConfigProcessor}
 * interface for parsing game configuration stored as XML files.
 */
public class SAXConfigProcessor extends DefaultHandler implements GameConfigProcessor {

    private String baseDir;
    private SAXGameParser SAXprocessor;

    public SAXConfigProcessor() throws ConfigProcessingException {
        String dirSeparator = System.getProperty("file.separator");
        this.baseDir = "configurations" + dirSeparator + "XML" + dirSeparator;
        try {
            this.SAXprocessor = new SAXGameParser(baseDir);
        } catch (SAXException e) {
            throw new ConfigProcessingException("Could not initialize parser", e);
        }
    }

    @Override
    public void processGameConfig() throws ConfigProcessingException {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File(baseDir + "game.xml"), SAXprocessor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new ConfigProcessingException("Could not initialize game", e);
        }
    }

    @Override
    public GameConfig getGameConfig() {
        return SAXprocessor;
    }
}
