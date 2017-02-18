package szakacs.kpi.fei.tuke.arena.game.updaters;

import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;

/**
 * Created by developer on 28.1.2017.
 */
public abstract class AbstractGameUpdater implements GameUpdater {

    protected GameLevelPrivileged game;
    protected GameWorld gameWorld;
    protected ActorManagerPrivileged actorManager;

    protected AbstractGameUpdater(GameLevelPrivileged game){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }
}
