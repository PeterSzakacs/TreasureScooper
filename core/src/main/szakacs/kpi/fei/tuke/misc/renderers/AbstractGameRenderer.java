package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;
import szakacs.kpi.fei.tuke.intrfc.game.world.GameWorld;

/**
 * Created by developer on 24.1.2017.
 */
public abstract class AbstractGameRenderer implements GameRenderer {

    protected SpriteBatch batch;
    protected GameLevelPrivileged game;
    protected GameWorld world;
    protected ActorManagerPrivileged actorManager;

    protected AbstractGameRenderer(SpriteBatch batch, GameLevelPrivileged game){
        this.batch = batch;
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }

    @Override
    public void reset(GameLevelPrivileged game){
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }
}
