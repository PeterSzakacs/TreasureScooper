package com.szakacs.kpi.fei.tuke.game.enums;

/**
 * Created by developer on 4.11.2016.
 */
public enum Direction {
    UP (0, 1),
    DOWN (0, -1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    static {
        UP.opposite = DOWN;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
    }

    private final int xStep;
    private final int yStep;
    private Direction opposite;

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

    public static Direction getDirectionByDeltas(int dx, int dy){
        if (dx + dy != dx && dx + dy != dy)
            throw new IllegalArgumentException("Illegal values of dx and dy. One has to be zero, the other nonzero!");
        if (dy == 0) {
            if (dx/Math.abs(dx) == 1)
                return RIGHT;
            else
                return LEFT;
        } else {
            if (dy/Math.abs(dy) == 1)
                return UP;
            else
                return DOWN;
        }
    }
}
