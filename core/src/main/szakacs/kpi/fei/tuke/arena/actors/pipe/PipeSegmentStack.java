package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.enums.TunnelCellType;
import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldPrivileged;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.ActorGameInterface;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
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
                     OnStackUpdatedCallback<PipeSegment> callback, ActorGameInterface gameInterface, PlayerToken token){
        super(capacity, dynamic);
        this.pushLimit = pushLimit;
        this.popLimit = popLimit;
        this.pushCounter = 0;
        this.popCounter = 0;
        this.randomSegment = new PipeSegment(
                // Simply: create a random tunnel cell
                new TunnelCell(-1, -1,
                        TunnelCellType.CROSSROAD,
                        null,
                        (GameWorldPrivileged) gameInterface.getGameWorld()),
                Direction.LEFT,
                Direction.RIGHT,
                gameInterface,
                token
        );
        this.segmentToPush = randomSegment;
        this.callback = callback;
    }


    /**
     * Adds a segment to the top of the stack, if it is that which is expected
     * and the limit on the number of segments that can be pushed has not been
     * reached and calls back the pipe to perform appropriate actions.
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
     * Removes a segment from the top of the stack, if the limit on the number
     * of segments that can be popped off it has not been reached and calls back
     * the pipe to perform appropriate actions.
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

    /**
     * Resets the operation counters to their default values to enable
     * this stack to be pushed to or popped from again.
     */
    void unlock(){
        this.pushCounter = 0;
        this.popCounter = 0;
    }

    /**
     * Sets the next expected segment to be pushed onto this stack.
     *
     * @param segmentToPush the next expected segment to be pushed onto this stack
     */
    void setSegmentToPush(PipeSegment segmentToPush){
        this.segmentToPush = segmentToPush;
    }
}
