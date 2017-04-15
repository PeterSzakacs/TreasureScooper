package szakacs.kpi.fei.tuke.arena.game.updaters;

import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager.PlayerManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

/**
 * Created by developer on 28.1.2017.
 */
public abstract class AbstractGameUpdater implements GameUpdater {

    protected GameLevelPrivileged game;
    protected GameWorldPrivileged gameWorld;
    protected ActorManagerPrivileged actorManager;
    protected PlayerManagerPrivileged playerManager;

    /*protected AbstractGameUpdater(GameLevelPrivileged game){
        this.initialize(game);
    }*/

    protected AbstractGameUpdater(){

    }

    public void startNewGame(GameLevelPrivileged game, DummyLevel level){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
        this.playerManager = game.getPlayerManager();
    }
}
