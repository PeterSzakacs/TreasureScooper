package com.szakacs.kpi.fei.tuke.game.misc;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Enemy;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.Wall;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 1.1.2017.
 */
public class GameUpdaterWalls implements GameUpdater {

    private final ManipulableGameInterface world;
    private int turnCounter;
    private int turnBound;
    private HorizontalTunnel previous;
    private int previousIndex;
    private List<HorizontalTunnel> tunnels;

    public GameUpdaterWalls(ManipulableGameInterface world) {
        this.world = world;
        this.tunnels = world.getTunnels();
        this.turnCounter = 0;
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(201);
        } while (this.turnBound < 100);
    }

    @Override
    public void update(ManipulableGameInterface world) {
        if (world != null && world.equals(this.world)) {
            turnCounter++;
            if (turnCounter > turnBound) {
                turnCounter = 0;
                if (world.getActorsBySearchCriteria(actor -> actor.getType() == ActorType.WALL).size() < 9)
                    addWall();
            }
        }
    }

    private void addWall() {
        HorizontalTunnel ht = null;
        for (HorizontalTunnel horizontalTunnel : world.getTunnels()) {
            ht = horizontalTunnel;
            if (!ht.equals(previous)) {
                this.previous = ht;
                break;
            }
        }
        Direction dir;
        int idx = 1;
        List<TunnelCell> cells = ht.getCells();
        Random rand = new Random();
        do {
            idx = 1 + rand.nextInt(cells.size() - 1);
        } while (idx == previousIndex);
        world.registerActor(new Wall(cells.get(idx), this.world));
    }
}
