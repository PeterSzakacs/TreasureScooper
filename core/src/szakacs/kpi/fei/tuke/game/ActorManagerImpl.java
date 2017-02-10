package szakacs.kpi.fei.tuke.game;

import szakacs.kpi.fei.tuke.arena.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.Actor;
import szakacs.kpi.fei.tuke.intrfc.game.actormanager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.misc.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.misc.proxies.ActorGameProxy;
import szakacs.kpi.fei.tuke.intrfc.game.GamePrivileged;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 25.1.2017.
 */
public class ActorManagerImpl implements ActorManagerPrivileged {
    private List<Actor> actors;
    private List<Actor> searchResults;
    private Map<Actor, Runnable> onDestroyActions;
    private Map<Actor, Integer> unregisteredActors;
    private Pipe pipe;
    private ActorGameProxy actorGameProxy;

    public ActorManagerImpl(GamePrivileged game){
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
        this.onDestroyActions = new HashMap<>(20);
        this.actorGameProxy = new ActorGameProxy(game, this);
        this.pipe = new Pipe(actorGameProxy);
    }

    public Pipe getPipe(){
        return this.pipe;
    }

    public List<Actor> getActors(){
        return this.actors;
    }

    public Map<Actor, Integer> getUnregisteredActors(){
        return this.unregisteredActors;
    }

    public Map<Actor, Runnable> getOnDestroyActions(){
        return this.onDestroyActions;
    }

    @Override
    public ActorGameInterface getActorGameProxy() {
        return this.actorGameProxy;
    }

    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate){
        this.searchResults.clear();
        for (Actor actor : this.actors){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return this.searchResults;
    }

    /**
     * Sets a method (an action) that is to be called (performed) when
     * the actor specified is unregistered, that is, removed from the
     * game world.
     *
     * @param actor: The actor
     * @param action: The action to be performed when the actor is unregistered,
     *              must be a method returning void and taking no arguments
     */
    public void setOnDestroy(Actor actor, Runnable action) {
        this.onDestroyActions.put(actor, action);
    }


    /**
     * Registers an actor, thereby adding the actor to the game world
     *
     * @param actor Not Null: The actor to add to the game world
     *
     */
    public void registerActor(Actor actor) {
        if (actor != null) {
            System.out.println("Registering: " + actor.toString());
            this.actors.add(actor);
        }
    }

    /**
     * Unregisters an actor for later removal from the list of arena,
     * (late removal to prevent ConcurrentModificationException) thereby
     * removing the actor from the game world. Optionally also executes
     * a method that is to be called when the actor is destroyed (removed)
     * if such a method has been set, otherwise it does nothing.
     *
     * @param actor Not Null: The actor to remove
     */
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
}
