package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
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
    private Map<Actor, Integer> unregisteredActors;
    private Set<Actor> searchResults;

    private MethodCallAuthenticator authenticator;

    ActorManager(MethodCallAuthenticator authenticator){
        this.authenticator = authenticator;
        this.actorActionMap = new HashMap<>(20);
        this.unregisteredActors = new HashMap<>();
        this.searchResults = new HashSet<>();
    }

    // ActorManagerQueryable methods (only queries)

    @Override
    public Set<Actor> getActors() {
        return Collections.unmodifiableSet(actorActionMap.keySet());
    }

    @Override
    public Set<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate){
        if (predicate == null)
            return Collections.unmodifiableSet(actorActionMap.keySet());
        searchResults.clear();
        for (Actor actor : actorActionMap.keySet()){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return searchResults;
    }

    // ActorManagerUpdatable methods (adding and removing actors)

    @Override
    public void registerActor(ActorPrivileged actor, Runnable action) {
        if (actor != null) {
            System.out.println("Registering: " + actor.toString());
            actorActionMap.put(actor, action);
        }
    }

    @Override
    public void unregisterActor(Actor actor) {
        if (actor != null) {
            System.out.println("Unregistering: " + actor.toString());
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
        for (Iterator<Actor> actorIt = unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            Actor unregistered = actorIt.next();
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
    public Map<Actor, Integer> getUnregisteredActors(){
        return unregisteredActors;
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level){
        actorActionMap.clear();
        searchResults.clear();
        unregisteredActors.clear();
    }
}
