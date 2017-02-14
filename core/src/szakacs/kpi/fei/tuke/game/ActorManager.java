package szakacs.kpi.fei.tuke.game;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.misc.proxies.ActorGameProxy;
import szakacs.kpi.fei.tuke.intrfc.game.GameLevelPrivileged;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 25.1.2017.
 */
public class ActorManager implements ActorManagerPrivileged {
    private List<Actor> actors;
    private List<Actor> searchResults;
    private Map<Actor, Runnable> onDestroyActions;
    private Map<Actor, Integer> unregisteredActors;
    private Pipe pipe;
    private ActorGameProxy actorGameProxy;

    ActorManager(GameLevelPrivileged game) {
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
        this.onDestroyActions = new HashMap<>(20);
        this.actorGameProxy = new ActorGameProxy(game, this);
        this.pipe = new Pipe(actorGameProxy);
    }

    // ActorManagerBasic methods (only queries)

    @Override
    public Pipe getPipe(){
        return this.pipe;
    }

    @Override
    public List<Actor> getActors(){
        return this.actors;
    }

    @Override
    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate){
        this.searchResults.clear();
        for (Actor actor : this.actors){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return this.searchResults;
    }

    // ActorManagerUpdatable methods (adding and removing actors)

    @Override
    public void setOnDestroy(Actor actor, Runnable action) {
        this.onDestroyActions.put(actor, action);
    }

    @Override
    public void registerActor(Actor actor) {
        if (actor != null) {
            System.out.println("Registering: " + actor.toString());
            this.actors.add(actor);
        }
    }

    @Override
    public void unregisterActor(Actor actor) {
        if (actor != null) {
            System.out.println("Unregistering: " + actor.toString());
            this.unregisteredActors.put(actor, 0);
            Runnable action = this.onDestroyActions.get(actor);
            if (action != null) {
                action.run();
                this.onDestroyActions.remove(actor, action);
            }
        }
    }

    // ActorManagerPrivileged methods (updating all actors)

    @Override
    public void update() {
        for (Actor actor : this.actors) {
            actor.act(actorGameProxy);
        }
        for (Iterator<Actor> actorIt = this.unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            Actor unregistered = actorIt.next();
            Integer counter = this.unregisteredActors.get(unregistered);
            if (counter == 0) {
                this.actors.remove(unregistered);
                this.unregisteredActors.put(unregistered, ++counter);
            } else if (counter > 3) {
                actorIt.remove();
            } else {
                this.unregisteredActors.put(unregistered, ++counter);
            }
        }
    }

    @Override
    public Map<Actor, Integer> getUnregisteredActors(){
        return this.unregisteredActors;
    }

    @Override
    public ActorGameInterface getActorGameProxy() {
        return this.actorGameProxy;
    }
}
