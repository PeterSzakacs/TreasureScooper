package szakacs.kpi.fei.tuke.arena.game;

import szakacs.kpi.fei.tuke.arena.PlayerGameProxy;
import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorld;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.arena.ActorGameProxy;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.misc.ConfigProcessingException;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyEntrance;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

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
    private MethodCallAuthenticator authenticator;

    ActorManager(GameLevelPrivileged game, DummyLevel level, MethodCallAuthenticator authenticator) throws ConfigProcessingException {
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
        this.onDestroyActions = new HashMap<>(20);
        this.actorGameProxy = new ActorGameProxy(game, this);
        this.authenticator = authenticator;
        this.setPipes(game, level);
    }

    private void setPipes(GameLevelPrivileged game, DummyLevel level) throws ConfigProcessingException{
        Map<DummyEntrance, Class<? extends Player>> entranceToPlayerMap = level.getEntranceToPlayerMap();
        GameWorld gameWorld = game.getGameWorld();
        this.pipes = new HashMap<>(entranceToPlayerMap.size());
        for (DummyEntrance de : entranceToPlayerMap.keySet()) {
            try {
                Player player = entranceToPlayerMap.get(de).newInstance();
                Pipe pipe = new Pipe(actorGameProxy, gameWorld.getEntrances().get(de.getId()), player);
                pipes.put(player, pipe);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new ConfigProcessingException("Failed to initialize game", e);
            }
        }
        PlayerGameInterface playerInterface = new PlayerGameProxy(game, this);
        for (Player player : pipes.keySet()) {
            player.initialize(playerInterface);
        }
    }

    // ActorManagerQueryable methods (only queries)

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

    @Override
    public Map<Player, Pipe> getPlayerToPipeMap() {
        return Collections.unmodifiableMap(pipes);
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

    @Override
    public MethodCallAuthenticator getAuthenticator() {
        return authenticator;
    }

    // ActorManagerPrivileged methods (updating all actors and access to internals)

    @Override
    public void update() {
        for (Player player : pipes.keySet()){
            Pipe pipe = pipes.get(player);
            if (pipe.getHealth() > 0) {
                player.act();
                pipe.allowMovement(authenticator);
            }
        }
        for (Actor actor : actors) {
            actor.act(authenticator);
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
}
