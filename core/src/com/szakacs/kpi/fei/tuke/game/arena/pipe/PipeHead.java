package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;
import com.szakacs.kpi.fei.tuke.game.intrfc.game.ManipulableGameInterface;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeHead implements GoldCollector {
    private int x;
    private int y;
    private Direction direction;
    private ManipulableGameInterface world;

    PipeHead(int x, int y, Direction direction, ManipulableGameInterface world) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.world = world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public ActorType getType() {
        return ActorType.PIPEHEAD;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void act(ManipulableGameInterface world) {

    }

    @Override
    public boolean intersects(Actor other) {
        return other != null && other.getX() / world.getOffsetX() == this.x / world.getOffsetX()
                && other.getY() / world.getOffsetY() == this.y / world.getOffsetY();
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    void setDirection(Direction direction) {
        this.direction = direction;
    }
}
