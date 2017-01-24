package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameRenderer;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 24.1.2017.
 */
public abstract class AbstractGameRenderer implements GameRenderer {

    protected SpriteBatch batch;
    protected ManipulableGameInterface world;

    protected AbstractGameRenderer(SpriteBatch batch, ManipulableGameInterface world){
        this.batch = batch;
        this.world = world;
    }
}
