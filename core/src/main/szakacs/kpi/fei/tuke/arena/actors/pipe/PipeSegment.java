package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.AbstractActor;
import szakacs.kpi.fei.tuke.enums.ActorType;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.PipeSegmentType;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;

/**
 * Created by developer on 4.11.2016.
 */
public class PipeSegment extends AbstractActor {
    private PipeSegmentType segmentType;
    private Direction from;

    PipeSegment(TunnelCell currentPosition, Direction from, Direction to, ActorGameInterface gameInterface) {
        super(currentPosition, ActorType.PIPE, to, gameInterface);
        this.from = from;
        this.setSegmentType(from, to);
    }

    @Override
    public void act(Object authToken) {

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

    Direction getOriginDirection() {
        return from;
    }
}
