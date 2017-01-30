package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.Game;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;

/**
 * Created by developer on 24.1.2017.
 */
public class BackgroundRenderer extends AbstractGameRenderer {

    private Sprite bgSprite;

    public BackgroundRenderer(SpriteBatch batch, GamePrivileged game) {
        super(batch, game);
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
