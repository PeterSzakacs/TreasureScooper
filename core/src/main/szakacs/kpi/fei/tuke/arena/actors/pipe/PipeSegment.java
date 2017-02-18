package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeSegment {
    private TunnelCell currentPosition;
    private PipeSegmentType segmentType;

    PipeSegment(TunnelCell currentPosition, Direction from, Direction to) {
        this.currentPosition = currentPosition;
        this.setSegmentType(from, to);
    }

    public int getX() {
        return currentPosition.getX();
    }

    public int getY() {
        return currentPosition.getY();
    }

    public PipeSegmentType getSegmentType() {
        return segmentType;
    }

    public TunnelCell getCurrentPosition(){
        return currentPosition;
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
