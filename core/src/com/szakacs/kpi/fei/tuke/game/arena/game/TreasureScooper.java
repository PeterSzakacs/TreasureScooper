package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
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
    private Map<Actor, Integer> unregisteredActors;


    public TreasureScooper() {
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>();
        this.unregisteredActors = new HashMap<>();
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
     * begin basicQueryable
     */

    public Pipe getPipe(){
        return this.pipe;
    }

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

    /*
     * end basicQueryable
     *
     * begin basicManipulable
     */

    public void update() {
        if (this.state == GameState.PLAYING) {
            this.player.act();
            this.pipe.allowMovement(this);
        }
        for (Actor actor : this.actors) {
            actor.act(this);
        }
        for (Iterator<Actor> actorIt = this.unregisteredActors.keySet().iterator(); actorIt.hasNext(); ) {
            Actor unregistered = actorIt.next();
            Integer counter = this.unregisteredActors.get(unregistered);
            if (counter == 0)
                this.actors.remove(unregistered);
            else if (counter > 3)
                actorIt.remove();
            else
                this.unregisteredActors.put(unregistered, ++counter);
        }
        //this.unregisteredActors.clear();
        for (GameUpdater updater : this.gameUpdaters)
            updater.update(this);
        if (this.remainingNuggetsCount == 0)
            this.state = GameState.WON;
        if (this.pipe.getHealth() <= 0)
            this.state = GameState.LOST;
    }

    public void onNuggetCollected() {
        this.remainingNuggetsCount--;
    }

    /*
     * end basicManipulable
     *
     * begin enemyQueryable
     */

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
     * end enemyQueryable
     *
     * begin enemyManipulable
     */

    public void registerActor(Actor actor) {
        if (actor != null)
            this.actors.add(actor);
    }

    public void unregisterActor(Actor actor) {
        if (actor != null)
            this.unregisteredActors.put(actor, 0);
    }

    /*
     * end enemyManipulable
     */

    /*
     * end game interface methods
     */

    //Map<Actor, Integer> getUnregisteredActors(){
    //    return this.unregisteredActors;
    //}

    //void setState(GameState state){
    //    this.state = state;
    //}
}