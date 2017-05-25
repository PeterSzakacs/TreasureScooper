package szakacs.kpi.fei.tuke.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import szakacs.kpi.fei.tuke.arena.GameManager;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.GameConfig;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.misc.renderers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The game's implementation of the ApplicationListener interface of libGDX.
 * This is the class called first on every iteration of the game loop.
 */
public class CoreGameRenderer implements ApplicationListener {

    private final GameManager manager;
    private GameConfig config;
    private GameLevelPrivileged currentGameLevel;
    private Timer timer;
    private TimerTask task;

    private List<GameRenderer> renderers;
    private GameRenderer scoreRenderer;
    private SpriteBatch batch;
    private int counter = 0;

    public CoreGameRenderer(GameManager manager, GameConfig config) throws GameLevelInitializationException {
        this.manager = manager;
        this.config = config;
        this.currentGameLevel = manager.getNextGameLevel();
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        };
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.renderers = new ArrayList<>();
        renderers.add(new BackgroundRenderer(batch, currentGameLevel));
        renderers.add(new TunnelsRenderer(batch, currentGameLevel));
        renderers.add(new ActorRenderer(batch, currentGameLevel, config));
        renderers.add(new PlayerRenderer(batch, currentGameLevel));
        this.config = null;
    }

    @Override
    public void resize(int width, int height) {}

    /**
     * <p>Main game loop. If the game is still running,
     * calls update on the current game level, waits
     * for a while after the level finishes and then
     * starts the new level.</p>
     *
     * <p>Once the final level is finished and it has
     * finished waiting, it turns off continuous
     * rendering and then displays the scoreboard
     * for one minute and then closes the application.</p>
     */
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

    /**
     * Not implemented.
     */
    @Override
    public void pause() {}

    /**
     * Not implemented.
     */
    @Override
    public void resume() {}

    /**
     * Disposes of all rendering resources used by this class
     * and any of its helper {@link GameRenderer} classes.
     */
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

    /**
     * Only used during initialization for outside classes
     * to be able to get the width and height to use for
     * the LwjglApplicationConfiguration.
     *
     * @return the game world of the current level
     */
    public GameWorldBasic getWorld() {
        return currentGameLevel.getGameWorld();
    }

    /**
     * Gets the game manager instance containing scores of players
     *
     * @return the game manager
     */
    public GameManager getManager() {
        return manager;
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
                timer.schedule(task, 60000);
            } else {
                currentGameLevel = level;
                for (GameRenderer renderer : renderers) {
                    renderer.reset(currentGameLevel);
                }
            }
        }
    }
}
