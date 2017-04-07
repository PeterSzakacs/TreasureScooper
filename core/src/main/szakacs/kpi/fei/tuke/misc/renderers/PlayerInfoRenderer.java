package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Weapon;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 24.1.2017.
 */
public class PlayerInfoRenderer extends AbstractGameRenderer {

    private final class QueuePosition {
        private int x;
        private int y;

        private QueuePosition(int x, int y){
            this.x = x; this.y = y;
        }
    }

    private BitmapFont score;
    private Sprite bulletSprite;
    private Sprite queue;
    private Map<PipeBasic, QueuePosition> bulletQueuePositions;

    public PlayerInfoRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.score = new BitmapFont();
        this.score.setColor(Color.YELLOW);
        this.score.getData().setScale(5);
        this.bulletSprite = new Sprite(new Texture(Gdx.files.internal("images/Weapon/BULLET.png")));
        this.queue = new Sprite(new Texture(
                Gdx.files.internal("images/Weapon/QUEUE.png")
        ));
        this.bulletQueuePositions = new HashMap<>(playerManager.getPipes().size());
        this.reset(game);
    }

    @Override
    public void render() {
        String displayMsg = "Remaining nuggets: " + Integer.toString(game.getGameWorld().getNuggetCount());
        switch (game.getState()) {
            case PLAYING:
                break;
            case WON:
                displayMsg += "\nSuccess!";
                break;
            case LOST:
                displayMsg += "\nFailure!";
                break;
            default:
                throw new IllegalStateException("Illegal game state: " + game.getState());
        }
        score.draw(batch, displayMsg, 128, 2000);
        queue.draw(batch);
        for (PipeBasic pipe : playerManager.getPipes()) {
            QueuePosition queuePosition = bulletQueuePositions.get(pipe);
            queue.setPosition(queuePosition.x, queuePosition.y);
            queue.draw(batch);
            Weapon weapon = pipe.getHead().getWeapon();
            Queue<Bullet> bulletQueue = weapon.getBulletQueue();
            if ( ! bulletQueue.isEmpty() ) {
                int front = weapon.getFrontIndex();
                int rear = weapon.getRearIndex();
                int numBullets = bulletQueue.getNumElements();
                for (int idx = front, counter = 0; counter < numBullets; idx++, counter++) {
                    bulletSprite.setPosition(queuePosition.x + 32 + (idx % bulletQueue.getCapacity()) * 32, queuePosition.y + 32);
                    bulletSprite.draw(batch);
                }

                Color original = bulletSprite.getColor();
                bulletSprite.setColor(Color.CORAL);
                bulletSprite.setPosition(queuePosition.x + 32 + front * 32, queuePosition.y + 32);
                bulletSprite.draw(batch);
                bulletSprite.setColor(Color.GREEN);
                bulletSprite.setPosition(queuePosition.x + 32 + rear * 32, queuePosition.y + 32);
                bulletSprite.draw(batch);
                bulletSprite.setColor(original);
            }
        }
    }

    @Override
    public void dispose() {
        bulletSprite.getTexture().dispose();
        queue.getTexture().dispose();
    }

    @Override
    public void reset(GameLevelPrivileged game) {
        super.reset(game);
        for (PipeBasic pipe : playerManager.getPipes()){
            bulletQueuePositions.put(pipe,
                    new QueuePosition(
                            pipe.getHead().getActorRectangle().getRectangleX() + world.getOffsetX(),
                            pipe.getHead().getActorRectangle().getRectangleY()
                    )
            );
        }
    }
}
