package szakacs.kpi.fei.tuke.arena.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel.GameLevelPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.UnregisteredActorInfo;
import szakacs.kpi.fei.tuke.misc.configProcessors.gameValueObjects.DummyLevel;

import java.util.*;
import java.util.function.Predicate;

/**
 * The managing class for all actors except pipe heads and pipe segments.
 */
public class ActorManager implements ActorManagerPrivileged {

    private class ActorInfo implements UnregisteredActorInfo {
        ActorPrivileged actor;
        Runnable onUnregisterAction;
        int turnCountSinceUnregistered;

        public int getTurnCountSinceUnregister() { return turnCountSinceUnregistered; }
        public ActorBasic getActor() { return actor; }
    }



    private MethodCallAuthenticator authenticator;
    private BiMap<String, ActorInfo> actors;
    private BiMap<String, ActorInfo> unregisteredActors;
    // used for collision detection.
    private Map<TunnelCellBasic, Set<ActorBasic>> positionToActorsMap;
    // Collections derived from and synchronized with actors and unregisteredActors.
    // Used for queries and collision detection, cached for performance reasons.
    private Set<ActorBasic> actorsSet;
    private EnumMap<ActorType, Set<ActorBasic>> actorsByType;



    ActorManager(MethodCallAuthenticator authenticator){
        this.authenticator = authenticator;
        this.actors = HashBiMap.create();
        this.unregisteredActors = HashBiMap.create();
        this.positionToActorsMap = new HashMap<>();
        this.actorsSet = new HashSet<>();
        this.actorsByType = new EnumMap<>(ActorType.class);
        for (ActorType actorType : ActorType.values()) {
            if (actorType.name().startsWith("PIPE")){
                // no such actors will be stored here
                actorsByType.put(actorType, new HashSet<>(0));
            } else {
                actorsByType.put(actorType, new HashSet<>(20));
            }
        }
    }



    // ActorManagerBasic methods (only queries)



    @Override
    public Set<ActorBasic> getActors() {
        return Collections.unmodifiableSet(actorsSet);
    }

    @Override
    public Set<ActorBasic> getActorsBySearchCriteria(Predicate<ActorBasic> predicate){
        if (predicate == null)
            return Collections.unmodifiableSet(actorsSet);
        Set<ActorBasic> searchResults = new HashSet<>(actors.size());
        for (ActorBasic actor : actorsSet){
            if (predicate.test(actor)){
                searchResults.add(actor);
            }
        }
        return searchResults;
    }

    @Override
    public Set<ActorBasic> getActorsByType(ActorType type){
        return Collections.unmodifiableSet(actorsByType.get(type));
    }

    @Override
    public Map<TunnelCellBasic, Set<ActorBasic>> getPositionToActorsMap() {
        return Collections.unmodifiableMap(positionToActorsMap);
    }



    // ActorManagerUpdatable methods (adding and removing actors)



    @Override
    public void registerActor(ActorPrivileged actor, Runnable action) {
        addActor(actor, action);
    }

    @Override
    public void unregisterActor(ActorBasic actor) {
        removeActor(actor);
    }



    // ActorManagerPrivileged methods (updating all actors and access to internals)



    @Override
    public void update() {
        for (ActorInfo info : actors.values()) {
            TunnelCellBasic formerPosition = info.actor.getCurrentPosition();
            info.actor.act(authenticator);
            TunnelCellBasic currentPosition = info.actor.getCurrentPosition();
            if (formerPosition != currentPosition){
                positionToActorsMap.get(formerPosition).remove(info.actor);
                positionToActorsMap.get(currentPosition).add(info.actor);
            }
        }
        Iterator<String> actorIt = unregisteredActors.keySet().iterator();
        while( actorIt.hasNext() ) {
            String unregisteredActorString = actorIt.next();
            ActorInfo info = unregisteredActors.get(unregisteredActorString);
            if (info.turnCountSinceUnregistered == 0) {
                info.turnCountSinceUnregistered++;
                actors.remove(unregisteredActorString, info);
            } else if (info.turnCountSinceUnregistered > 3) {
                actorIt.remove();
            } else {
                info.turnCountSinceUnregistered++;
            }
        }
    }

    @Override
    public Set<UnregisteredActorInfo> getUnregisteredActors(){
        return Collections.unmodifiableSet(unregisteredActors.values());
    }

    @Override
    public void startNewGame(GameLevelPrivileged gameLevel, DummyLevel level){
        actors.clear(); unregisteredActors.clear();
        actorsSet.clear();
        for (Set<ActorBasic> actorsOfTypeSet : actorsByType.values()) {
            actorsOfTypeSet.clear();
        }
        initializePositionToActorsMap(gameLevel.getGameWorld());
    }



    // helper methods



    private void addActor(ActorPrivileged actor, Runnable action){
        if (actor != null) {
            ActorInfo info = new ActorInfo();
            info.actor = actor;
            info.onUnregisterAction = action;
            actors.put(actor.toString(), info);
            actorsSet.add(actor);
            actorsByType.get(actor.getType()).add(actor);
        }
    }

    private void removeActor(ActorBasic actor){
        if (actor != null){
            String actorString = actor.toString();
            ActorInfo info = actors.get(actorString);
            if (info != null) {
                unregisteredActors.put(actorString, info);
                if (info.onUnregisterAction != null) {
                    info.onUnregisterAction.run();
                    info.onUnregisterAction = null;
                }
                //actors.remove(actorString, info);
                positionToActorsMap.get(actor.getCurrentPosition()).remove(actor);
                actorsSet.remove(actor);
                actorsByType.remove(actor.getType(), actor);
            }
        }
    }

    private void initializePositionToActorsMap(GameWorldBasic world){
        positionToActorsMap.clear();
        Set<TunnelCellBasic> cellsSet = world.getCells();
        for (TunnelCellBasic cell : cellsSet) {
            positionToActorsMap.put(cell, new HashSet<>(3));
        }
    }
}
