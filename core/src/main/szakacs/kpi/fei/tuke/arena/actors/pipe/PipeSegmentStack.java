package szakacs.kpi.fei.tuke.arena.actors.pipe;

import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnStackUpdatedCallback;
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
    private final OnStackUpdatedCallback<PipeSegment> callback;



    PipeSegmentStack(int capacity, boolean dynamic, int pushLimit, int popLimit,
                     OnStackUpdatedCallback<PipeSegment> callback){
        super(capacity, dynamic);
        this.pushLimit = pushLimit;
        this.popLimit = popLimit;
        this.pushCounter = 0;
        this.popCounter = 0;
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
        if (pushCounter < pushLimit) {
            super.push(segment);
            pushCounter++;
            callback.onPush();
        }
    }

    /**
     * Moves pipe as well as head one step back from its current position
     *
     * @return the removed pipe segment
     * (in conformance with the return value definition of the pop() method
     * for the ArrayStack ADT)
     */
    @Override
    public PipeSegment pop() {
        if (popCounter < popLimit) {
            PipeSegment popped = super.pop();
            popCounter++;
            callback.onPop(popped);
            return popped;
        } else {
            return null;
        }
    }

    void unlock(){
        this.pushCounter = 0;
        this.popCounter = 0;
    }
}
