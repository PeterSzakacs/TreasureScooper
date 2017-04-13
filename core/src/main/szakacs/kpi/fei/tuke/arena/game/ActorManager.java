package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 25.1.2017.
 */
public class ActorManager implements ActorManagerPrivileged {

    private Map<ActorPrivileged, Runnable> actorActionMap;
    private Map<ActorBasic, Integer> unregisteredActors;
    private Set<ActorBasic> searchResults;

    private MethodCallAuthenticator authenticator;

    ActorManager(MethodCallAuthenticator authenticator){
        this.authenticator = authenticator;
        this.actorActionMap = new HashMap<>(20);
        this.unregisteredActors = new HashMap<>();
        this.searchResults = new HashSet<>();
    }

    // ActorManagerBasic methods (only queries)

    @Override
    public Set<ActorBasic> getActors() {
        return Collections.unmodifiableSet(actorActionMap.keySet());
    }

    @Override
    public Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> predicate){
        if (predicate == null)
            return Collections.unmodifiableSet(actorActionMap.keySet());
        searchResults.clear();
        for (ActorBasic actor : actorActionMap.keySet()){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return searchResults;
    }

    // ActorManagerUpdatable methods (adding and removing actors)

    @Override
    public void registerActor(ActorPrivileged actor, Runnable action) {
        if (actor != null) {
            //System.out.println("Registering: " + actor.toString());
            actorActionMap.put(actor, action);
        }
    }

    @Override
    public void unregisterActor(ActorBasic actor) {
        if (actor != null) {
            //System.out.println("Unregistering: " + actor.toString());
            this.unregisteredActors.put(actor, 0);
            Runnable action = actorActionMap.get(actor);
            if (action != null) {
                action.run();
            }
        }
    }

    // ActorManagerPrivileged methods (updating all actors and access to internals)

    @Override
    public void update() {
        for (ActorPrivileged actor : actorActionMap.keySet()) {
            actor.act(authenticator);
        }
        for (Iterator<ActorBasic> actorIt = unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            ActorBasic unregistered = actorIt.next();
            Integer counter = unregisteredActors.get(unregistered);
            if (counter == 0) {
                actorActionMap.remove(unregistered);
                unregisteredActors.put(unregistered, ++counter);
            } else if (counter > 3) {
                actorIt.remove();
            } else {
                unregisteredActors.put(unregistered, ++counter);
            }
        }
    }

    @Override
    public Map<ActorBasic, Integer> getUnregisteredActors(){
        return unregisteredActors;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level){
        actorActionMap.clear();
        searchResults.clear();
        unregisteredActors.clear();
    }
}
