package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.ActorType;
import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.intrfc.GoldCollector;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeHead implements GoldCollector {
    private int x;
    private int y;
    private Direction direction;

    PipeHead(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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
