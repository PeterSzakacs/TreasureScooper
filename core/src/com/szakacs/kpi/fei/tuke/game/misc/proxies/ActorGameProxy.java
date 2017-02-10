package com.szakacs.kpi.fei.tuke.game.misc.proxies;

import com.szakacs.kpi.fei.tuke.game.enums.GameState;
import com.szakacs.kpi.fei.tuke.game.intrfc.Player;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManager;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.ActorManagerPrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameWorld;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

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
