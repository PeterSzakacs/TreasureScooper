package com.szakacs.kpi.fei.tuke.game.arena;

import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;
import com.szakacs.kpi.fei.tuke.game.intrfc.Actor;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeSegment implements Actor {
    private int x;
    private int y;
    private PipeSegmentType segmentType;

    public PipeSegment(int x, int y, PipeSegmentType segmentType) {
        this.x = x;
        this.y = y;
        this.segmentType = segmentType;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public PipeSegmentType getSegmentType() {
        return segmentType;
    }

    void setSegmentType(PipeSegmentType segmentType){
        this.segmentType = segmentType;
    }
}
