package szakacs.kpi.fei.tuke.misc.proxies;

import szakacs.kpi.fei.tuke.intrfc.arena.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.actormanager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

/**
 * Created by developer on 28.1.2017.
 */
public class ActorGameProxy extends PlayerGameProxy implements ActorGameInterface {

    public ActorGameProxy(GamePrivileged game, ActorManagerPrivileged actorManager){
        super(game);
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
}