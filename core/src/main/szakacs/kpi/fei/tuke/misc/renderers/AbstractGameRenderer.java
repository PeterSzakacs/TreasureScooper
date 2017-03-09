package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldQueryable;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;

/**
 * Created by developer on 24.1.2017.
 */
public abstract class AbstractGameRenderer implements GameRenderer {

    protected SpriteBatch batch;
    protected GameLevelPrivileged game;
    protected GameWorldQueryable world;
    protected ActorManagerPrivileged actorManager;
    protected PlayerManagerPrivileged playerManager;

    protected AbstractGameRenderer(SpriteBatch batch, GameLevelPrivileged game){
        this.batch = batch;
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }

    @Override
    public void reset(GameLevelPrivileged game){
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }
}
