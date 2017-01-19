package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.Wall;
import com.szakacs.kpi.fei.tuke.game.enums.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.*;
import java.util.function.Predicate;

public class TreasureScooper implements ManipulableGameInterface {
	private int width = 4096;
    private int height = 2048;
    private int offsetX = 128;
    private int offsetY = 128;
    private GameState state;
    private Set<GameUpdater> gameUpdaters;

    private int remainingNuggetsCount;
    private Player player;
    private Pipe pipe;
    private TunnelCell rootCell;
    private List<HorizontalTunnel> tunnels;
    private List<Actor> actors;
    private List<Actor> searchResults;
    private Map<Actor, Runnable> onDestroyActions;
    private Map<Actor, Integer> unregisteredActors;

    public TreasureScooper() {
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
        this.onDestroyActions = new HashMap<>(20);
    }

    void setGameUpdaters(Set<GameUpdater> updaters){
        this.gameUpdaters = updaters;
    }

    void setTunnels(List<HorizontalTunnel> tunnels, TunnelCell rootCell){
        this.tunnels = tunnels;
        this.rootCell = rootCell;
        for (HorizontalTunnel ht : tunnels)
            this.remainingNuggetsCount += ht.getNuggetCount();
    }

    void setDimensions(int width, int height, int offsetX, int offsetY){
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    void setNewPlayer(Player player, Pipe pipe){
        this.player = player;
        this.pipe = pipe;
        this.state = GameState.PLAYING;
    }

    /*
     * begin game interface methods
     */

    /*
     * begin QueryableGameInterface
     */

    public TunnelCell getRootCell() {
        return this.rootCell;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getRemainingNuggetsCount(){
        return this.remainingNuggetsCount;
    }

    public List<HorizontalTunnel> getTunnels(){
        return Collections.unmodifiableList(this.tunnels);
    }

    public GameState getState(){
        return this.state;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Pipe getPipe(){
        return this.pipe;
    }

    public List<Actor> getActors(){
        return Collections.unmodifiableList(this.actors);
    }

    public List<Actor> getActorsBySearchCriteria(Predicate<Actor> predicate){
        this.searchResults.clear();
        for (Actor actor : this.actors){
            if (predicate.test(actor))
                searchResults.add(actor);
        }
        return this.searchResults;
    }

    /*
     * end QueryableGameInterface
     *
     * begin ManipulableGameInterface
     */

    public void update() {
        for (GameUpdater updater : this.gameUpdaters)
            updater.update(this);
    }

    public void onNuggetCollected() {
        this.remainingNuggetsCount--;
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
     * Unregisters an actor for later removal from the list of actors,
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

    /*
     * end ManipulableGameInterface
     */

    /*
     * end game interface methods
     */


    /*
     * Methods for accessing internals of the actual game world object.
     * Restricted only to package local classes which need to have access
     * to these member variables for their functionality which is tightly
     * bound to this object (updaters & builders, though the proxy object
     * currently doesn't seem to need any such functionality, therefore,
     * we currently do not pass it a reference to the actual object type).
     */

    Map<Actor, Integer> getUnregisteredActors(){
        return this.unregisteredActors;
    }

    List<Actor> getActorsList(){
        return this.actors;
    }

    Map<Actor, Runnable> getOnDestroyActions(){
        return this.onDestroyActions;
    }

    void setState(GameState state){
        this.state = state;
    }
}