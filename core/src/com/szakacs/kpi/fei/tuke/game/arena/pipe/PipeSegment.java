package com.szakacs.kpi.fei.tuke.game.arena.pipe;

import com.szakacs.kpi.fei.tuke.game.enums.Direction;
import com.szakacs.kpi.fei.tuke.game.enums.PipeSegmentType;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeSegment {
    private int x;
    private int y;
    private PipeSegmentType segmentType;

    PipeSegment(int x, int y, Direction from, Direction to) {
        this.x = x;
        this.y = y;
        this.setSegmentType(from, to);
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

    private void setSegmentType(Direction from, Direction to){
        if (from.getOpposite() == to){
            switch (from){
                case UP:
                case DOWN:
                    this.segmentType = PipeSegmentType.VERTICAL;
                    break;
                case RIGHT:
                case LEFT:
                    this.segmentType = PipeSegmentType.HORIZONTAL;
                    break;
            }
        } else {
            this.setEdge(from, to);
        }
    }

    private void setEdge(Direction from, Direction to) {
        if (from == Direction.DOWN || to == Direction.DOWN){
            if (from == Direction.LEFT || to == Direction.LEFT) {
                this.segmentType = PipeSegmentType.BOTTOM_LEFT;
            } else {
                this.segmentType = PipeSegmentType.BOTTOM_RIGHT;
            }
        } else {
            if (from == Direction.LEFT || to == Direction.LEFT) {
                this.segmentType = PipeSegmentType.TOP_LEFT;
            } else {
                this.segmentType = PipeSegmentType.TOP_RIGHT;
            }
        }
    }
}
