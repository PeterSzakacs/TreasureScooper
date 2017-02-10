package szakacs.kpi.fei.tuke.game.updaters;

import szakacs.kpi.fei.tuke.intrfc.game.actormanager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.game.GameWorld;

/**
 * Created by developer on 28.1.2017.
 */
public abstract class AbstractGameUpdater implements GameUpdater {

    protected GamePrivileged game;
    protected GameWorld gameWorld;
    protected ActorManagerPrivileged actorManager;

    protected AbstractGameUpdater(GamePrivileged game){
        this.game = game;
        this.gameWorld = game.getGameWorld();
        this.actorManager = game.getActorManager();
    }
}
