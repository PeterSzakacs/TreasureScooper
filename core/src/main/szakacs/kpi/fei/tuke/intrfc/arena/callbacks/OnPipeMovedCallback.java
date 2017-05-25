package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;

/**
 * An interface for defining tasks to execute when the pipe moves.
 */
public interface OnPipeMovedCallback {

    /**
     * Called when a pipe has had a segment pushed on the top of its stack.
     *
     * @param head the head of the affected pipe.
     * @param pushed the segment pushed onto the pipe.
     */
    void onPush(PipeHead head, PipeSegment pushed);

    /**
     * Called when a pipe has had a segment popped off the top of its stack.
     *
     * @param head the head of the affected pipe.
     * @param popped the segment popped off the pipe.
     */
    void onPop(PipeHead head, PipeSegment popped);
}
