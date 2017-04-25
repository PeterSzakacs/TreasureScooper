package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractActor;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * The class representing a single segment of the collecting pipe.
 */
public class PipeSegment extends AbstractActor {

    private PipeSegmentType segmentType;
    private Direction from;

    PipeSegment(TunnelCellUpdatable currentPosition, Direction from, Direction to, ActorGameInterface gameInterface) {
        super(currentPosition, ActorType.PIPE, to, gameInterface);
        this.from = from;
        this.setSegmentType(from, to);
    }

    @Override
    public void act(Object authToken) {

    }

    /**
     * Gets the segment type enum value of this pipe segment.
     *
     * @see PipeSegmentType for further details about the values of this enum.
     * @return the segment type value of this pipe segment.
     */
    public PipeSegmentType getSegmentType() {
        return segmentType;
    }

    /**
     * Gets the direction of this pipe segment toward the bottom of the stack or pipe.
     *
     * @return the direction of this pipe segment away from the collection head.
     */
    public Direction getDirectionFrom() {
        return from;
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
