package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

import java.util.List;

/**
 * Created by developer on 6.11.2016.
 */
public class Bullet implements Actor {
    private Direction dir;
    private int x;
    private int y;
    private int movementDelta;
    private TreasureScooper world;
    private HorizontalTunnel tunnel;
    private Enemy enemy;

    public Bullet(int x, int y, Direction dir, TreasureScooper world) {
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.movementDelta = world.getOffsetX()/2;
        this.world = world;
        this.lockTarget();
    }

    private void lockTarget(){
        this.tunnel = this.world.getTunnelByYCoordinate(this.y);
        List<Enemy> enemies = this.tunnel.getEnemies();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public Direction getDir() {
        return dir;
    }


    //TODO: finish this first of all
    public void act(){
        //if (TreasureScooper.intersects(this,))
        switch (this.dir){
            case UP:
            case DOWN:
                //throw new NotImplementedException();
            case LEFT:
                this.x -= movementDelta;
            case RIGHT:
                this.x += movementDelta;
        }
    }
}
