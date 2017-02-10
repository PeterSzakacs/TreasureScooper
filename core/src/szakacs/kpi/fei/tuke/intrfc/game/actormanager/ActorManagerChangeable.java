package szakacs.kpi.fei.tuke.intrfc.game.actormanager;

import szakacs.kpi.fei.tuke.intrfc.arena.Actor;

/**
 * Created by developer on 29.1.2017.
 */
public interface ActorManagerChangeable extends ActorManager {
    void setOnDestroy(Actor actor, Runnable action);

    void registerActor(Actor actor);

    void unregisterActor(Actor actor);
}
