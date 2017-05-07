package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.misc.ArrayStack;

/**
 * Created by developer on 15.2.2017.
 *
 * An extension to the base {@link ArrayStack} class providing customized modifications
 * to better suit its functionality requirements to the Pipe.
 */
public class PipeSegmentStack extends ArrayStack<PipeSegment> {

    private int pushLimit;
    private int popLimit;
    private int pushCounter;
    private int popCounter;
    private PipeSegment segmentToPush;
    private PipeSegment randomSegment;
    private final OnStackUpdatedCallback<PipeSegment> callback;



    PipeSegmentStack(int capacity, boolean dynamic, int pushLimit, int popLimit,
                     OnStackUpdatedCallback<PipeSegment> callback, ActorGameInterface gameInterface){
        super(capacity, dynamic);
        this.pushLimit = pushLimit;
        this.popLimit = popLimit;
        this.pushCounter = 0;
        this.popCounter = 0;
        this.randomSegment = new PipeSegment(
                // Simply: find a LEFT_EDGE cell of some tunnel;
                new TunnelCell(-1, -1,
                        TunnelCellType.CROSSROAD,
                        null,
                        (GameWorldPrivileged) gameInterface.getGameWorld()),
                Direction.LEFT,
                Direction.RIGHT,
                gameInterface
        );
        this.segmentToPush = randomSegment;
        this.callback = callback;
    }


    /**
     * Adds a segment to the top of the pipe and moves the pipe head in
     * that direction according to the segment
     *
     * @param segment the segment to add to the pipe
     */
    @Override
    public void push(PipeSegment segment) {
        if (pushCounter < pushLimit && segmentToPush.equals(segment)) {
            super.push(segment);
            pushCounter++;
            callback.onPush(segment);
            segmentToPush = randomSegment;
        }
    }

    /**
     * Moves pipe as well as head one step back from its current position.
     *
     * @return the removed pipe segment
     */
    @Override
    public PipeSegment pop() {
        if (popCounter < popLimit) {
            PipeSegment popped = super.pop();
            popCounter++;
            callback.onPop(popped);
            return popped;
        } else {
            return top();
        }
    }

    void unlock(){
        this.pushCounter = 0;
        this.popCounter = 0;
    }

    void setSegmentToPush(PipeSegment segmentToPush){
        this.segmentToPush = segmentToPush;
    }
}
