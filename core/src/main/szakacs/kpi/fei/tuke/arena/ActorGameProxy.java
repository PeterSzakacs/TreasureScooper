package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 28.1.2017.
 */
public class ActorGameProxy extends PlayerGameProxy implements ActorGameInterface {

    public ActorGameProxy(GameLevelPrivileged game){
        super(game);
    }

    @Override
    public void registerActor(ActorPrivileged actor, Runnable action) {
        actorManager.registerActor(actor, action);
    }

    @Override
    public void unregisterActor(ActorBasic actor) {
        actorManager.unregisterActor(actor);
    }

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return game.getAuthenticator();
    }
}
