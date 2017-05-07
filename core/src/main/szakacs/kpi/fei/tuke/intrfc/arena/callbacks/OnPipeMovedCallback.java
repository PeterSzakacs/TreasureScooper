package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;

/**
 * Created by developer on 6.5.2017.
 */
public interface OnPipeMovedCallback {

    void onPush(PipeHead head, PipeSegment pushed);

    void onPop(PipeHead head, PipeSegment popped);
}
