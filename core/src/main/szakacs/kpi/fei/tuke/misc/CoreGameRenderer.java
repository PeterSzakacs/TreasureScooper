package szakacs.kpi.fei.tuke.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.GdxRuntimeException;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.arena.GameManager;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.misc.renderers.*;

import java.util.*;

/**
 * Created by developer on 2.12.2016.
 */
public class CoreGameRenderer implements ApplicationListener {

    private final GameManager manager;
    private GameConfig config;
    private GameLevelPrivileged currentGameLevel;

    private List<GameRenderer> renderers;
    private GameRenderer scoreRenderer;
    private SpriteBatch batch;
    private int counter = 0;

    public CoreGameRenderer(GameManager manager, GameConfig config) throws GameLevelInitializationException {
        this.manager = manager;
        this.config = config;
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
        this.config = null;
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

            if (currentGameLevel.getState() != GameState.PLAYING) {
                startNewGameLevel();
            }
        } else {
            batch.begin();
            scoreRenderer.render();
            batch.end();
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

    public GameWorldBasic getWorld() {
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
                // no need to update the game any longer
                Gdx.graphics.setContinuousRendering(false);
                currentGameLevel = null;
            } else {
                currentGameLevel = level;
                for (GameRenderer renderer : renderers) {
                    renderer.reset(currentGameLevel);
                }
            }
        }
    }

    public GameManager getManager() {
        return manager;
    }
}
