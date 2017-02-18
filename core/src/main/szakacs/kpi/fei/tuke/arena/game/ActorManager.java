package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.arena.ActorGameProxy;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;

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
    private Map<Player, Pipe> pipes;
    private ActorGameProxy actorGameProxy;

    ActorManager(GameLevelPrivileged game) {
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
        this.onDestroyActions = new HashMap<>(20);
        this.actorGameProxy = new ActorGameProxy(game, this);
        this.pipes = new HashMap<>(3);
    }

    // ActorManagerBasic methods (only queries)

    @Override
    public Pipe getPipeOfPlayer(Player player){
        return pipes.get(player);
    }

    @Override
    public List<Actor> getActors(){
        return actors;
    }

    @Override
    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate){
        searchResults.clear();
        for (Actor actor : actors){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return searchResults;
    }

    // ActorManagerUpdatable methods (adding and removing actors)

    @Override
    public void setOnDestroy(Actor actor, Runnable action) {
        onDestroyActions.put(actor, action);
    }

    @Override
    public void registerActor(Actor actor) {
        if (actor != null) {
            System.out.println("Registering: " + actor.toString());
            actors.add(actor);
        }
    }

    @Override
    public void unregisterActor(Actor actor) {
        if (actor != null) {
            System.out.println("Unregistering: " + actor.toString());
            this.unregisteredActors.put(actor, 0);
            Runnable action = onDestroyActions.get(actor);
            if (action != null) {
                action.run();
                onDestroyActions.remove(actor, action);
            }
        }
    }

    // ActorManagerPrivileged methods (updating all actors)

    @Override
    public void update() {
        for (Pipe pipe : pipes.values()){
            pipe.allowMovement(actorGameProxy);
        }
        for (Actor actor : actors) {
            actor.act(actorGameProxy);
        }
        for (Iterator<Actor> actorIt = unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            Actor unregistered = actorIt.next();
            Integer counter = unregisteredActors.get(unregistered);
            if (counter == 0) {
                actors.remove(unregistered);
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
    public ActorGameInterface getActorGameProxy() {
        return actorGameProxy;
    }

    @Override
    public Map<Player, Pipe> getPlayerToPipeMap() {
        return pipes;
    }
}
