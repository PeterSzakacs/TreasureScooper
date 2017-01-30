package com.szakacs.kpi.fei.tuke.game.intrfc.actors;

import com.szakacs.kpi.fei.tuke.game.intrfc.game.GamePrivileged;
import com.szakacs.kpi.fei.tuke.game.intrfc.proxies.ActorGameInterface;

import java.util.List;
import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 */
public interface ActorManagerPrivileged extends ActorManagerChangeable {

    Map<Actor, Integer> getUnregisteredActors();

    Map<Actor, Runnable> getOnDestroyActions();

    ActorGameInterface getActorGameProxy();
}
