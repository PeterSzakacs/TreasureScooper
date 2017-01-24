package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameRenderer;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

/**
 * Created by developer on 24.1.2017.
 */
public class BackgroundRenderer extends AbstractGameRenderer {

    private Sprite bgSprite;
    private BitmapFont score;

    public BackgroundRenderer(SpriteBatch batch, ManipulableGameInterface world) {
        super(batch, world);
        this.bgSprite = new Sprite(new Texture(Gdx.files.internal("images/128/background.png")));
    }

    @Override
    public void render() {
        bgSprite.setPosition(0, 0);
        bgSprite.draw(batch);
    }

    @Override
    public void dispose() {
        bgSprite.getTexture().dispose();
    }
}
