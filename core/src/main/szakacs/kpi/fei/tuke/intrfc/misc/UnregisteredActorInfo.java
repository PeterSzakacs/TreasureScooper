package szakacs.kpi.fei.tuke.intrfc.misc;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;

/**
 * Created by developer on 4.5.2017.
 */
public interface UnregisteredActorInfo {

    int getTurnCountSinceUnregister();

    ActorBasic getActor();
}
