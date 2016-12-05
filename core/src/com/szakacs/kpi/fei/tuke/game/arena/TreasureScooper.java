package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.*;
import com.szakacs.kpi.fei.tuke.game.intrfc.callbacks.TunnelEventHandler;
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
    private List<ManipulableActor> actors;

    private class worldCallback implements TunnelEventHandler {
        @Override
        public void onEnemyDestroyed(Enemy enemy) {
            TreasureScooper.this.actors.remove(enemy);
        }
        @Override
        public void onNuggetCollected(GoldCollector collector) {
            TreasureScooper.this.remainingNuggetsCount--;
        }
    }
    private TunnelEventHandler callback;

    public TreasureScooper(AdvancedConfigProcessor configProc) {
        this.width = configProc.getWidth();
        this.height = configProc.getHeight();
        this.offsetX = configProc.getOffsetX();
        this.offsetY = configProc.getOffsetY();
        this.rootCell = new TunnelCell(configProc.getInitX(),
                configProc.getInitY(), TunnelCellType.INTERCONNECT, null);
        Map<String, DummyTunnel> dummyTunnels = configProc.getDummyTunnels();
        this.callback = new worldCallback();
        this.buildTunnelGraph(dummyTunnels.get(configProc.getRootTunnel()),
                dummyTunnels.values(), configProc.getInterconnects());
        this.pipe = new Pipe(this);
        this.player = new PlayerA(new GameProxy(this), this.pipe);
        this.searchResults = new ArrayList<>(3 * this.tunnels.size());
        this.actors = new ArrayList<>();
        this.state = GameState.PLAYING;
    }

    private void buildTunnelGraph(DummyTunnel rootDummy,
                                  Collection<DummyTunnel> dummyTunnels,
                                  Map<DummyTunnel, Map<Integer, DummyTunnel>> dummyInterconnects) {
        this.tunnels = new HashMap<>(dummyTunnels.size());
        for (DummyTunnel dt : dummyTunnels){
            this.tunnels.put(dt.getId(),
                    new HorizontalTunnel(dt, this, this.callback));
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
            newCell = new TunnelCell(rootCell.getX(), y, TunnelCellType.INTERCONNECT, null);
            newCell.setAtDirection(Direction.UP, prevCell);
            prevCell.setAtDirection(Direction.DOWN, newCell);
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
     * begin basic
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

    public void update() {
        if (this.state == GameState.PLAYING) {
            this.player.act();
            this.pipe.setOperationApplied(false);
        }
        for (HorizontalTunnel ht : tunnels.values())
            ht.act();
        for (int i = 0; i < actors.size(); i++) {
            ManipulableActor actor = actors.get(i);
            actor.act();
        }
        if (this.remainingNuggetsCount == 0)
            this.state = GameState.WON;
        if (this.pipe.getHealth() <= 0)
            this.state = GameState.LOST;
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * end basic
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

    public boolean intersects(Actor actorA, Actor actorB){
        if (actorA != null && actorB != null) {
            return actorA.getX() / offsetX == actorB.getX() / offsetX
                    && actorA.getY() / offsetY == actorB.getY() / offsetY;
        }
        return false;
    }

    public void registerActor(ManipulableActor actor) {
        if (actor != null)
            this.actors.add(actor);
    }

    public void unregisterActor(ManipulableActor actor) {
        if (actor != null)
            this.actors.remove(actor);
    }

    /*
     * end game interface methods
     */
}