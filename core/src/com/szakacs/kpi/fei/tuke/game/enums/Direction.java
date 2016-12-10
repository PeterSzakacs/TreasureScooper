package com.szakacs.kpi.fei.tuke.game.enums;

/**
 * Created by developer on 4.11.2016.
 */
public enum Direction {
    UP (0, 1),
    DOWN (0, -1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    private Direction opposite;
    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }

    private final int xStep;
    private final int yStep;
    Direction(int xStep, int yStep){
        this.xStep = xStep;
        this.yStep = yStep;
    }

    public int getXStep(){
        return this.xStep;
    }

    public int getYStep(){
        return this.yStep;
    }

    public Direction getOpposite(){
        return this.opposite;
    }
}
