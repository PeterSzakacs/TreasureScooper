package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeSegment {
    private int x;
    private int y;
    private PipeSegmentType segmentType;

    public PipeSegment(int x, int y, PipeSegmentType segmentType) {
        this.x = x;
        this.y = y;
        this.segmentType = segmentType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PipeSegmentType getSegmentType() {
        return segmentType;
    }
}
