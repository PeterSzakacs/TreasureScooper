package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.weapon.AmmoQueue;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;

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
        this.bulletSprite = new Sprite(new Texture(Gdx.files.internal("images/Weapon/BULLET.png")));
        this.queue = new Sprite(new Texture(
                Gdx.files.internal("images/Weapon/QUEUE.png")
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
        AmmoQueue ammoQueue = actorManager.getPipe().getHead().getWeapon().getAmmoQueue();
        List<Bullet> bullets = ammoQueue.getBullets();
        if (!ammoQueue.isEmpty()) {
            for (int i = 0; i < ammoQueue.getCapacity(); i++) {
                if (bullets.get(i) != null) {
                    bulletSprite.setPosition(3744 + i * 32, 1824);
                    bulletSprite.draw(batch);
                }
            }
            Color original = bulletSprite.getColor();
            bulletSprite.setColor(Color.CORAL);
            bulletSprite.setPosition(3744 + ammoQueue.getFrontIndex() * 32, 1824);
            bulletSprite.draw(batch);
            bulletSprite.setColor(Color.GREEN);
            bulletSprite.setPosition(3744 + ammoQueue.getRearIndex() * 32, 1824);
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
