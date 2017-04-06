package szakacs.kpi.fei.tuke.player.common;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.GameState;
import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

/**
 * Created by developer on 10.3.2017.
 */
public abstract class AbstractPlayer implements Player {

    private PlayerToken token;

    protected PlayerGameInterface gameInterface;
    protected PipeBasic pipe;
    protected Stack<PipeSegment> segmentStack;
    protected PipeHead head;

    @Override
    public final void setPlayerToken(PlayerToken token){
        if (this.token == null) {
            this.token = token;
        }
    }

    @Override
    public void initialize(PlayerGameInterface gameInterface, PipeBasic pipe, PlayerToken token){
        if (this.token.validate(token)) {
            this.gameInterface = gameInterface;
            this.pipe = pipe;
            this.segmentStack = pipe.getSegmentStack();
            this.head = pipe.getHead();
        }
    }

    @Override
    public void deallocate() {
        if (gameInterface.getState() != GameState.PLAYING){
            this.token = null;
        }
    }

    protected final boolean isTokenSet(){
        return token != null;
    }

    protected final PlayerToken getToken(){
        return token;
    }
}
