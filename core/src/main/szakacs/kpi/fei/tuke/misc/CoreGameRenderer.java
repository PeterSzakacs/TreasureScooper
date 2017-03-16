package szakacs.kpi.fei.tuke.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.arena.GameManager;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldQueryable;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfigProcessor;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.misc.configProcessors.SAXprocessor.SAXConfigProcessor;
import szakacs.kpi.fei.tuke.misc.renderers.*;

import java.util.*;

/**
 * Created by developer on 2.12.2016.
 */
public class CoreGameRenderer implements ApplicationListener {

    private GameManager manager;
    private GameLevelPrivileged currentGameLevel;
    private GameConfig config;

    private List<GameRenderer> renderers;
    private GameRenderer scoreRenderer;
    private SpriteBatch batch;
    private int counter = 0;

    public CoreGameRenderer() throws ConfigProcessingException, GameLevelInitializationException {
        GameConfigProcessor configProcessor = new SAXConfigProcessor();
        configProcessor.processGameConfig();
        this.config = configProcessor.getGameConfig();
        this.manager = new GameManager(config);
        this.currentGameLevel = manager.getNextGameLevel();
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.renderers = new ArrayList<>();
        this.renderers.add(new BackgroundRenderer(batch, currentGameLevel));
        this.renderers.add(new TunnelsRenderer(batch, currentGameLevel));
        this.renderers.add(new ActorRenderer(batch, currentGameLevel, config));
        this.renderers.add(new PlayerRenderer(batch, currentGameLevel));
        this.renderers.add(new PlayerInfoRenderer(batch, currentGameLevel));
    }

    @Override
    public void resize(int width, int height) {}

    // Yes, this is the main game loop
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (currentGameLevel != null) {
            // update game logic in current level
            currentGameLevel.update();

            // render current game level
            batch.begin();
            for (GameRenderer renderer : this.renderers)
                renderer.render();
            batch.end();
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (currentGameLevel.getState() != GameState.PLAYING) {
                startNewGameLevel();
            }
        } else {
            batch.begin();
            scoreRenderer.render();
            batch.end();
            Gdx.graphics.setContinuousRendering(false);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        for (GameRenderer renderer : renderers){
            renderer.dispose();
        }
        if (scoreRenderer != null) {
            scoreRenderer.dispose();
        }
    }

    public GameWorldQueryable getWorld() {
        return currentGameLevel.getGameWorld();
    }

    /*
     * helper methods
     */

    private void startNewGameLevel(){
        counter++;
        if (counter > 30) {
            counter = 0;
            GameLevelPrivileged level;
            try {
                level = manager.getNextGameLevel();
            } catch (GameLevelInitializationException e) {
                throw new GdxRuntimeException("Failed to start new game level", e);
            }
            if (level == null) {
                this.scoreRenderer = new FinalScoreRenderer(batch, manager.getResults(),
                        currentGameLevel.getGameWorld().getWidth(),
                        currentGameLevel.getGameWorld().getHeight()
                );
            } else {
                for (GameRenderer renderer : renderers) {
                    renderer.reset(currentGameLevel);
                }
            }
            currentGameLevel = level;
        }
    }
}
