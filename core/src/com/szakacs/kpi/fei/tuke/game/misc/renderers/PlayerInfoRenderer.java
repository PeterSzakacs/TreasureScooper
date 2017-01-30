package com.szakacs.kpi.fei.tuke.game.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szakacs.kpi.fei.tuke.game.arena.actors.Bullet;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.AmmoQueue;
import com.szakacs.kpi.fei.tuke.game.arena.weapon.Weapon;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.Game;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;

import java.util.List;

/**
 * Created by developer on 24.1.2017.
 */
public class PlayerInfoRenderer extends AbstractGameRenderer {

    private BitmapFont score;
    private Sprite bulletSprite;
    private Sprite queue;

    public PlayerInfoRenderer(SpriteBatch batch, GamePrivileged game) {
        super(batch, game);
        this.score = new BitmapFont();
        this.score.setColor(Color.YELLOW);
        this.score.getData().setScale(5);
        this.bulletSprite = new Sprite(new Texture(Gdx.files.internal("images/128/Objects/bullet.png")));
        this.queue = new Sprite(new Texture(
                Gdx.files.internal("images/128/Objects/queue.png")
        ));
        this.queue.setPosition(3712, 1792);
    }

    @Override
    public void render() {
        String displayMsg = Integer.toString(game.getScore());
        switch (game.getState()) {
            case PLAYING:
                break;
            case WON:
                displayMsg += "\nYou won the game!";
                break;
            case LOST:
                displayMsg += "\nGame over!";
                break;
            default:
                throw new IllegalStateException("Illegal game state: " + game.getState());
        }
        score.draw(batch, displayMsg, 128, 2000);
        queue.draw(batch);
        Weapon weapon = actorManager.getPipe().getHead().getWeapon();
        List<Bullet> bullets = weapon.getBullets();
        if (!weapon.isEmpty()) {
            for (int i = 0; i < weapon.getCapacity(); i++) {
                if (bullets.get(i) != null) {
                    bulletSprite.setPosition(3744 + i * 32, 1824);
                    bulletSprite.draw(batch);
                }
            }
            Color original = bulletSprite.getColor();
            bulletSprite.setColor(Color.CORAL);
            bulletSprite.setPosition(3744 + weapon.getFrontIndex() * 32, 1824);
            bulletSprite.draw(batch);
            bulletSprite.setColor(Color.GREEN);
            bulletSprite.setPosition(3744 + weapon.getRearIndex() * 32, 1824);
            bulletSprite.draw(batch);
            bulletSprite.setColor(original);
        }
    }

    @Override
    public void dispose() {
        bulletSprite.getTexture().dispose();
        queue.getTexture().dispose();
    }
}
