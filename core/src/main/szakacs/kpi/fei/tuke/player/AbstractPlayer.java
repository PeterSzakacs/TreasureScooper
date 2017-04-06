package szakacs.kpi.fei.tuke.player;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.BasicPipe;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

/**
 * Created by developer on 10.3.2017.
 */
public abstract class AbstractPlayer implements Player {

    protected PlayerGameInterface gameInterface;
    protected BasicPipe pipe;
    protected Stack<PipeSegment> segmentStack;
    protected PipeHead head;

    @Override
    public void initialize(PlayerGameInterface gameInterface, BasicPipe pipe){
        this.gameInterface = gameInterface;
        this.pipe = pipe;
        this.segmentStack = pipe.getSegmentStack();
        this.head = pipe.getHead();
    }

    @Override
    public void deallocate() {
    }
}
