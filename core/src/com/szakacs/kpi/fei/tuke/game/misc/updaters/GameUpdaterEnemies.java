package com.szakacs.kpi.fei.tuke.game.misc.updaters;

import com.szakacs.kpi.fei.tuke.game.arena.actors.Enemy;
import com.szakacs.kpi.fei.tuke.game.arena.world.HorizontalTunnel;
import com.szakacs.kpi.fei.tuke.game.arena.world.TunnelCell;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.actors.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by developer on 31.12.2016.
 */
public class GameUpdaterEnemies extends AbstractGameUpdater {

    private int turnCounter;
    private int turnBound;
    private HorizontalTunnel previous;
    private List<Actor> createdEnemies;

    public GameUpdaterEnemies(GamePrivileged game){
        super(game);
        this.turnCounter = 0;
        this.createdEnemies = new ArrayList<>(10);
        Random rand = new Random();
        do {
            this.turnBound = rand.nextInt(200);
        } while (this.turnBound < 100);
    }

    public void update(Game game){
        turnCounter++;
        if (turnCounter > turnBound) {
            turnCounter = 0;
            if (createdEnemies.size() < 9)
                createNewEnemy();
        }
    }

    private void createNewEnemy() {
        HorizontalTunnel ht = null;
        for (HorizontalTunnel horizontalTunnel : gameWorld.getTunnels()) {
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
        actorManager.registerActor(new Enemy(dir, startPosition, this::removeEnemy, super.actorManager.getActorGameProxy()));
    }

    private void removeEnemy(Actor enemy){
        this.createdEnemies.remove(enemy);
    }
}
