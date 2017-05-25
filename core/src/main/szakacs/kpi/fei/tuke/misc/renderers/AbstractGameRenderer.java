package szakacs.kpi.fei.tuke.misc.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.GameRenderer;

/**
 * An abstract base class partially implementing the {@link GameRenderer}
 * interface for the purpose of storing and updating commonly used member
 * variables.
 */
public abstract class AbstractGameRenderer implements GameRenderer {

    protected SpriteBatch batch;
    protected GameLevelPrivileged game;
    protected GameWorldBasic world;
    protected ActorManagerPrivileged actorManager;
    protected PlayerManagerPrivileged playerManager;

    protected AbstractGameRenderer(SpriteBatch batch, GameLevelPrivileged game){
        this.batch = batch;
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }

    /**
     * {@inheritDoc}
     *
     * This implementation should be called before
     * executing code of an overriding method.
     */
    @Override
    public void reset(GameLevelPrivileged game){
        this.game = game;
        this.world = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }
}
