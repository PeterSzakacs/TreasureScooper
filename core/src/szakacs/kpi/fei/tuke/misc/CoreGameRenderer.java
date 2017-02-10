package szakacs.kpi.fei.tuke.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.game.GameManager;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;
import szakacs.kpi.fei.tuke.misc.configProcessors.levels.CoreConfigProcessor;
import org.xml.sax.SAXException;
import szakacs.kpi.fei.tuke.misc.renderers.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by developer on 2.12.2016.
 */
public class CoreGameRenderer implements ApplicationListener {

    private GamePrivileged game;
    private GameManager manager;
    private CoreConfigProcessor configProcessor;
    private List<GameRenderer> renderers;

    private SpriteBatch batch;
    private int counter = 0;

    public CoreGameRenderer(String configFilename){
        this.configProcessor = new CoreConfigProcessor();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File(configFilename), configProcessor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        this.manager = new GameManager(this.configProcessor);
        this.game = manager.getNextGameLevel();
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.renderers = new ArrayList<>();
        this.renderers.add(new BackgroundRenderer(batch, game));
        this.renderers.add(new TunnelsRenderer(batch, game));
        this.renderers.add(new ActorRenderer(batch, game, configProcessor.getActorToDirectionsMap()));
        this.renderers.add(new PlayerRenderer(batch, game));
        this.renderers.add(new PlayerInfoRenderer(batch, game));
        //let the garbage collector now do its job on the config processor
        this.configProcessor = null;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.update();

        batch.begin();
        for (GameRenderer renderer : this.renderers)
            renderer.render();
        batch.end();
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (game.getState() != GameState.PLAYING){
            counter++;
            if (counter > 30) {
                counter = 0;
                GamePrivileged game = manager.getNextGameLevel();
                if (game == null) {
                    Gdx.app.exit();
                } else {
                    this.game = game;
                    for (GameRenderer renderer : renderers) {
                        renderer.reset(game);
                    }
                }
            }
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        for (GameRenderer renderer : this.renderers){
            renderer.dispose();
        }
    }

    public GameWorld getWorld() {
        return game.getGameWorld();
    }

    /*
     * helper rendering methods
     */
}
