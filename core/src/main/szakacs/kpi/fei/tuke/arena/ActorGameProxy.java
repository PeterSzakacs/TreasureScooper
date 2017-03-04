package szakacs.kpi.fei.tuke.arena;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 28.1.2017.
 */
public class ActorGameProxy extends PlayerGameProxy implements ActorGameInterface {

    public ActorGameProxy(GameLevelPrivileged game, ActorManagerPrivileged actorManager){
        super(game, actorManager);
        super.actorManager = actorManager;
    }

    @Override
    public void setOnDestroy(Actor actor, Runnable action) {
        actorManager.setOnDestroy(actor, action);
    }

    @Override
    public void registerActor(Actor actor) {
        actorManager.registerActor(actor);
    }

    @Override
    public void unregisterActor(Actor actor) {
        actorManager.unregisterActor(actor);
    }

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return actorManager.getAuthenticator();
    }
}
