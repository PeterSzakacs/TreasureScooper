package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerPrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManager;

/**
 * Created by developer on 24.1.2017.
 */
public abstract class AbstractGameRenderer implements GameRenderer {

    protected SpriteBatch batch;
    protected GamePrivileged game;
    protected GameWorld world;
    protected ActorManagerPrivileged actorManager;

    protected AbstractGameRenderer(SpriteBatch batch, GamePrivileged game){
        this.batch = batch;
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }

    @Override
    public void reset(GamePrivileged game){
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }
}
