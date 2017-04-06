package szakacs.kpi.fei.tuke.intrfc.arena.actors;

/**
 * Created by developer on 5.4.2017.
 */
public interface ActorPrivileged extends ActorBasic {

    /**
     * Called on every iteration of the game loop (the argument passed is
     * a sort of method caller authentication).
     */
    void act(Object authToken);
}
