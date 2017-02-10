package szakacs.kpi.fei.tuke.intrfc.game.actormanager;

import szakacs.kpi.fei.tuke.intrfc.arena.Actor;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;

import java.util.Map;

/**
 * Created by developer on 29.1.2017.
 */
public interface ActorManagerPrivileged extends ActorManagerChangeable {

    Map<Actor, Integer> getUnregisteredActors();

    Map<Actor, Runnable> getOnDestroyActions();

    ActorGameInterface getActorGameProxy();
}
