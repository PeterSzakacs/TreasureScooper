package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.misc.AdvancedConfigProcessor;
import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;
import com.szakacs.kpi.fei.tuke.game.player.PlayerA;

import java.util.*;
import java.util.function.Predicate;

public class TreasureScooper implements ManipulableGameInterface {
	private int width = 4096;
    private int height = 2048;
    private int offsetX = 128;
    private int offsetY = 128;
    private GameState state;

    private int remainingNuggetsCount;
    private Player player;
    private Pipe pipe;
    private TunnelCell rootCell;
    private Map<String, HorizontalTunnel> tunnels;
    private List<Actor> searchResults;
    private List<Actor> actors;
    private Map<Actor, Integer> unregisteredActors;


    public TreasureScooper(AdvancedConfigProcessor configProc) {
        this.width = configProc.getWidth();
        this.height = configProc.getHeight();
        this.offsetX = configProc.getOffsetX();
        this.offsetY = configProc.getOffsetY();
        this.rootCell = new TunnelCell(configProc.getInitX(),
                configProc.getInitY(), TunnelCellType.INTERCONNECT, null, this);
        Map<String, DummyTunnel> dummyTunnels = configProc.getDummyTunnels();
        this.buildTunnelGraph(dummyTunnels.get(configProc.getRootTunnel()),
                dummyTunnels.values(), configProc.getInterconnects());
        this.pipe = new Pipe(this);
        this.player = new PlayerA(new GameProxy(this), this.pipe);
        this.actors = new ArrayList<>();
        this.searchResults = new ArrayList<>(3 * this.tunnels.size());
        this.unregisteredActors = new HashMap<>();
        this.state = GameState.PLAYING;
    }

    private void buildTunnelGraph(DummyTunnel rootDummy,
                                  Collection<DummyTunnel> dummyTunnels,
                                  Map<DummyTunnel, Map<Integer, DummyTunnel>> dummyInterconnects) {
        this.tunnels = new HashMap<>(dummyTunnels.size());
        for (DummyTunnel dt : dummyTunnels){
            this.tunnels.put(dt.getId(),
                    new HorizontalTunnel(dt, this));
        }
        Map<HorizontalTunnel, Map<Integer, HorizontalTunnel>> interconnects = new HashMap<>();
        for (DummyTunnel dt : dummyInterconnects.keySet()) {
            HorizontalTunnel ht = this.tunnels.get(dt.getId());
            Map<Integer, HorizontalTunnel> tunnelsExitMap = new HashMap<>(3);
            Map<Integer, DummyTunnel> dummiesExitMap = dummyInterconnects.get(dt);
            for (Integer exit : dummiesExitMap.keySet()) {
                tunnelsExitMap.put(exit, this.tunnels.get(
                        dummiesExitMap.get(exit).getId()
                ));
            }
            interconnects.put(ht, tunnelsExitMap);
        }
        TunnelCell newCell, prevCell = this.rootCell;
        HorizontalTunnel root = this.tunnels.get(rootDummy.getId());
        for (int y = rootCell.getY() - offsetY; y > root.getY(); y -= offsetY){
            newCell = new TunnelCell(rootCell.getX(), y, TunnelCellType.INTERCONNECT, null, this);
            newCell.setAtDirection(this, Direction.UP, prevCell);
            prevCell.setAtDirection(this, Direction.DOWN, newCell);
            prevCell = newCell;
        }
        root.setEntrance(prevCell);
        for (HorizontalTunnel ht : this.tunnels.values()) {
            ht.addInterconnects(interconnects.get(ht));
            this.remainingNuggetsCount += ht.getNuggetCount();
        }
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

    public Collection<HorizontalTunnel> getTunnels(){
        return Collections.unmodifiableCollection(this.tunnels.values());
    }

    public GameState getState(){
        return this.state;
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
        for (HorizontalTunnel ht : tunnels.values())
            ht.act(this);
        for (Actor actor : this.actors) {
            actor.act(this);
        }
        for (Iterator<Actor> actorIt = this.unregisteredActors.keySet().iterator(); actorIt.hasNext(); ){
            Actor unregistered = actorIt.next();
            Integer counter = this.unregisteredActors.get(unregistered);
            if (counter == 0)
                this.actors.remove(unregistered);
            else if (counter > 3)
                actorIt.remove();
            else
                this.unregisteredActors.put(unregistered, ++counter);
        }
        this.unregisteredActors.clear();
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
}