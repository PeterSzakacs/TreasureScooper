package com.szakacs.kpi.fei.tuke.game.misc;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.szakacs.kpi.fei.tuke.game.arena.game.TreasureScooperBuilder;
import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.Game;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameRenderer;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;
import com.szakacs.kpi.fei.tuke.game.misc.renderers.*;
import org.xml.sax.SAXException;

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
    private AdvancedConfigProcessor configProcessor;
    private List<GameRenderer> renderers;

    private SpriteBatch batch;

    public CoreGameRenderer(GameType gameType, String configFilename){
        this.configProcessor = new AdvancedConfigProcessor();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new File(configFilename), configProcessor);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        this.game = new TreasureScooperBuilder().buildGame(configProcessor, gameType);
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
            Thread.sleep(90);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
