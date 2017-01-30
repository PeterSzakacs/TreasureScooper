package com.szakacs.kpi.fei.tuke.game.intrfc.actors;

/**
 * Created by developer on 29.1.2017.
 */
public interface ActorManagerChangeable extends ActorManager {
    void setOnDestroy(Actor actor, Runnable action);

    void registerActor(Actor actor);

    void unregisterActor(Actor actor);
}
