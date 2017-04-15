package szakacs.kpi.fei.tuke.intrfc.arena.actors;

/**
 *
 */
public interface ActorPrivileged extends ActorUpdatable {

    void act(Object authToken);
}
