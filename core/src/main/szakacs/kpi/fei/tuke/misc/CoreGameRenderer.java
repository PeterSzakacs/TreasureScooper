package szakacs.kpi.fei.tuke.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.arena.GameManager;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;
import szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor.SAXConfigProcessor;
import szakacs.kpi.fei.tuke.misc.renderers.*;

import java.util.*;

/**
 * Created by developer on 2.12.2016.
 */
public class CoreGameRenderer implements ApplicationListener {

    private GameLevelPrivileged game;
    private GameManager manager;
    private GameConfig config;

    private List<GameRenderer> renderers;
    private SpriteBatch batch;
    private int counter = 0;

    public CoreGameRenderer(String configFilename) throws ConfigProcessingException {
        GameConfigProcessor configProcessor = new SAXConfigProcessor();
        configProcessor.processGameConfig();
        this.config = configProcessor.getGameConfig();
        this.manager = new GameManager(config);
        this.game = manager.getNextGameLevel();
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.renderers = new ArrayList<>();
        this.renderers.add(new BackgroundRenderer(batch, game));
        this.renderers.add(new TunnelsRenderer(batch, game));
        this.renderers.add(new ActorRenderer(batch, game, config));
        this.renderers.add(new PlayerRenderer(batch, game));
        this.renderers.add(new PlayerInfoRenderer(batch, game));
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
                GameLevelPrivileged game = manager.getNextGameLevel();
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
