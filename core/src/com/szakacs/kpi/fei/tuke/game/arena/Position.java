package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;

import java.util.Map;

/**
 * Created by developer on 5.11.2016.
 */
public class Position {
    private int x;
    private int y;
    private GoldNugget nugget;
    private Map<Direction, Position> fourDirections;

    public Position(int x, int y, GoldNugget nugget) {
        this.x = x;
        this.y = y;
        this.nugget = nugget;
    }

    public boolean hasNugget(){
        return this.nugget != null;
    }

    public GoldNugget getNugget(){
        return this.nugget;
    }
}
