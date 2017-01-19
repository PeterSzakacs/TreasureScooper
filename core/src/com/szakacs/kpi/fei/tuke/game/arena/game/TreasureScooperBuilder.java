package com.szakacs.kpi.fei.tuke.game.arena.game;

import com.szakacs.kpi.fei.tuke.game.arena.pipe.Pipe;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.GameType;
import com.szakacs.kpi.fei.tuke.game.enums.TunnelCellType;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;
import com.szakacs.kpi.fei.tuke.game.misc.AdvancedConfigProcessor;
import com.szakacs.kpi.fei.tuke.game.misc.DummyTunnel;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterEnemies;
import com.szakacs.kpi.fei.tuke.game.misc.updaters.GameUpdaterWalls;
import com.szakacs.kpi.fei.tuke.game.player.PlayerA;

import java.util.*;

/**
 * Created by developer on 31.12.2016.
 */
public class TreasureScooperBuilder {

    private TreasureScooper world;

    public TreasureScooperBuilder() {
    }

    public ManipulableGameInterface buildGameWorld(AdvancedConfigProcessor configProcessor, GameType gameType) {
        this.world = new TreasureScooper();
        this.initialize(configProcessor);
        Set<GameUpdater> updaters = new HashSet<>(3);
        updaters.add(new TreasureScooperBaseUpdater(this.world));
        switch (gameType) {
            case STACK:
                this.world.setGameUpdaters(updaters);
                break;
            case QUEUE:
                updaters.add(new GameUpdaterWalls(this.world));
                this.world.setGameUpdaters(updaters);
                break;
            case ENEMIES:
                updaters.add(new GameUpdaterEnemies(this.world));
                this.world.setGameUpdaters(updaters);
                break;
            case ULTIMATE:
                updaters.add(new GameUpdaterWalls(this.world));
                updaters.add(new GameUpdaterEnemies(this.world));
                this.world.setGameUpdaters(updaters);
                break;
        }
        return this.world;
    }

    private void initialize(AdvancedConfigProcessor configProcessor) {
        world.setDimensions(configProcessor.getWidth(), configProcessor.getHeight(),
                configProcessor.getOffsetX(), configProcessor.getOffsetY());
        this.buildTunnelGraph(configProcessor);
        Pipe pipe = new Pipe(this.world);
        world.setNewPlayer(new PlayerA(new GameProxy(this.world), pipe), pipe);
    }

    private void buildTunnelGraph(AdvancedConfigProcessor configProcessor) {
        Collection<DummyTunnel> dummyTunnels = configProcessor.getDummyTunnels().values();
        Map<String, HorizontalTunnel> tunnels = new HashMap<>(dummyTunnels.size());
        for (DummyTunnel dt : dummyTunnels) {
            tunnels.put(dt.getId(),
                    new HorizontalTunnel(dt, this.world));
        }
        Map<DummyTunnel, Map<Integer, DummyTunnel>> dummyInterconnects = configProcessor.getInterconnects();
        Map<HorizontalTunnel, Map<Integer, HorizontalTunnel>> interconnects = new HashMap<>();
        for (DummyTunnel dt : dummyInterconnects.keySet()) {
            HorizontalTunnel ht = tunnels.get(dt.getId());
            Map<Integer, HorizontalTunnel> tunnelsExitMap = new HashMap<>(3);
            Map<Integer, DummyTunnel> dummiesExitMap = dummyInterconnects.get(dt);
            for (Integer exit : dummiesExitMap.keySet()) {
                tunnelsExitMap.put(exit, tunnels.get(
                        dummiesExitMap.get(exit).getId()
                ));
            }
            interconnects.put(ht, tunnelsExitMap);
        }

        TunnelCell rootCell = new TunnelCell(configProcessor.getInitX(),
                configProcessor.getInitY(), TunnelCellType.INTERCONNECT, null, this.world);;
        TunnelCell newCell, prevCell = rootCell;
        HorizontalTunnel rootTunnel = tunnels.get(configProcessor.getRootTunnel());
        int offsetY = this.world.getOffsetY();
        for (int y = rootCell.getY() - offsetY; y > rootTunnel.getY(); y -= offsetY) {
            newCell = new TunnelCell(rootCell.getX(), y, TunnelCellType.INTERCONNECT, null, this.world);
            newCell.setAtDirection(this.world, Direction.UP, prevCell);
            prevCell.setAtDirection(this.world, Direction.DOWN, newCell);
            prevCell = newCell;
        }
        rootTunnel.setEntrance(prevCell);
        for (HorizontalTunnel ht : tunnels.values()) {
            ht.addInterconnects(interconnects.get(ht));
        }

        world.setTunnels(new ArrayList<>(tunnels.values()), rootCell);
    }
}
