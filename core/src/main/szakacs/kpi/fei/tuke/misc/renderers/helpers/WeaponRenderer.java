package szakacs.kpi.fei.tuke.misc.renderers.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.arena.actors.Bullet;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Weapon;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.intrfc.misc.Rectangle;
import szakacs.kpi.fei.tuke.misc.renderers.AbstractGameRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders the state of each player's weapon (number of bullets) as a circular queue.
 */
public class WeaponRenderer extends AbstractGameRenderer {

    private final class BulletQueuePosition {
        private int x;
        private int y;
        private PlayerToken token;

        private BulletQueuePosition(int x, int y, PlayerToken token){
            this.x = x; this.y = y; this.token = token;
        }
    }

    private Sprite bulletSprite;
    private Sprite queue;
    private Map<PipeBasic, BulletQueuePosition> bulletQueuePositions;

    public WeaponRenderer(SpriteBatch batch, GameLevelPrivileged game) {
        super(batch, game);
        this.bulletSprite = new Sprite(new Texture(Gdx.files.internal("images/Weapon/BULLET.png")));
        this.queue = new Sprite(new Texture(
                Gdx.files.internal("images/Weapon/QUEUE.png")
        ));
        this.bulletQueuePositions = new HashMap<>(3);
        this.reset(game);
    }

    public void renderWeapon(PipeBasic pipe){
        BulletQueuePosition queueInfo = bulletQueuePositions.get(pipe);
        queue.setPosition(queueInfo.x, queueInfo.y);
        queue.draw(batch);
        Weapon weapon = pipe.getHead().getWeapon();
        Queue<Bullet> bulletQueue = weapon.getBulletQueue(queueInfo.token);
        if ( ! bulletQueue.isEmpty() ) {
            int front = weapon.getFrontIndex();
            int rear = weapon.getRearIndex();
            int numBullets = bulletQueue.getNumElements();
            for (int idx = front, counter = 0; counter < numBullets; idx++, counter++) {
                bulletSprite.setPosition(queueInfo.x + 32 + (idx % bulletQueue.getCapacity()) * 32, queueInfo.y + 32);
                bulletSprite.draw(batch);
            }

            Color original = bulletSprite.getColor();
            bulletSprite.setColor(Color.CORAL);
            bulletSprite.setPosition(queueInfo.x + 32 + front * 32, queueInfo.y + 32);
            bulletSprite.draw(batch);
            bulletSprite.setColor(Color.GREEN);
            bulletSprite.setPosition(queueInfo.x + 32 + rear * 32, queueInfo.y + 32);
            bulletSprite.draw(batch);
            bulletSprite.setColor(original);
        }
    }

    @Override
    public void render() {
        for (PipeBasic pipe : playerManager.getPipesUpdatable().values()) {
            renderWeapon(pipe);
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
        Map<PlayerToken, Pipe> pipeMap = playerManager.getPipesUpdatable();
        for (PlayerToken token : pipeMap.keySet()){
            PipeBasic pipe = pipeMap.get(token);
            Rectangle headRectangle = pipe.getHead().getActorRectangle();
            bulletQueuePositions.put(pipe,
                    new BulletQueuePosition(
                            headRectangle.getRectangleX() + world.getOffsetX(),
                            headRectangle.getRectangleY(),
                            token
                    )
            );
        }
    }
}
