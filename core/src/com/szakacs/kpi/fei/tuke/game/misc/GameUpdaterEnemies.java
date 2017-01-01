package com.szakacs.kpi.fei.tuke.game.misc;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Enemy;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.tunnels.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.GameUpdater;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.world.ManipulableGameInterface;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by developer on 31.12.2016.
 */
public class GameUpdaterEnemies implements GameUpdater {
    private ManipulableGameInterface world;
    private int turnCounter;
    private int turnBound;
    private HorizontalTunnel previous;

    public GameUpdaterEnemies(ManipulableGameInterface world){
        this.world = world;
        this.turnCounter = 0;
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(200);
        } while (this.turnBound < 100);
    }

    public void update(ManipulableGameInterface world){
        if (world != null && world.equals(this.world)) {
            turnCounter++;
            if (turnCounter > turnBound) {
                turnCounter = 0;
                if (world.getActorsBySearchCriteria(actor -> actor.getType() == ActorType.MOLE).size() < 9)
                    createNewEnemy();
            }
        }
    }

    private void createNewEnemy() {
        HorizontalTunnel ht = null;
        for (HorizontalTunnel horizontalTunnel : world.getTunnels()) {
            ht = horizontalTunnel;
            if (!ht.equals(previous)) {
                this.previous = ht;
                break;
            }
        }
        Direction dir;
        TunnelCell startPosition;
        if (new Date().getTime() % 2 == 0) {
            dir = Direction.LEFT;
            startPosition = ht.getCells().get(ht.getCells().size() - 1);
        } else {
            dir = Direction.RIGHT;
            startPosition = ht.getCells().get(0);
        }
        world.registerActor(new Enemy(dir, startPosition, this.world));
    }
}
