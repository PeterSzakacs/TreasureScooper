package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractActor;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.TunnelCellUpdatable;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;

/**
 * The class representing a single segment of the collecting pipe.
 */
public class PipeSegment extends AbstractActor {

    private PipeSegmentType segmentType;
    private Direction from;
    private PlayerToken token;

    PipeSegment(TunnelCellUpdatable currentPosition, Direction from, Direction to, ActorGameInterface gameInterface, PlayerToken token) {
        super(currentPosition, ActorType.PIPESEGMENT, to, gameInterface);
        this.from = from;
        this.token = token;
        this.setSegmentType(from, to);
    }

    /**
     * Not implemented.
     *
     * @param authToken An authentication token to verify the caller
     */
    @Override
    public void act(Object authToken) {

    }

    /**
     * Gets the pipe of which this segment is a part.
     *
     * @return the pipe of which this segment is a part
     */
    public PipeBasic getPipe(){
        return gameInterface.getPlayerTokenMap().get(token).getPipe();
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
