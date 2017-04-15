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
 * An abstract superclass implementing the {@link Player} interface.
 * This class contains code code and data which is presumed to be
 * common across all player classes and so you can skip directly
 * implementing the Player interface. Just extend this class and
 * implement only the {@link Player#act(PlayerToken)} method or,
 * if you need to set some more member variables on initialization
 * or do some cleanup, you can simply override the other 2 methods
 * {@link Player#initialize(PlayerGameInterface, PipeBasic, PlayerToken)}
 * and {@link Player#deallocate()}, respectively.
 */
public abstract class AbstractPlayer implements Player {

    private PlayerToken token;

    /**
     * The {@link PlayerGameInterface} object passed on initialization
     * to the player to get any game-related information (actors,
     * nuggets, etc.).
     */
    protected PlayerGameInterface gameInterface;
    /**
     * The {@link PipeBasic} object representing the pipe the player controls
     * during a game level. Passed on initialization to the player.
     */
    protected PipeBasic pipe;
    /**
     * The {@link Stack} object representing
     * the pipe passed on initialization to the player.
     */
    protected Stack<PipeSegment> segmentStack;
    /**
     * The {@link PipeHead} object representing the collecting head
     * of the pipe passed on initialization to the player.
     */
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

    /**
     * Checks if the token is set (i.e. not null).
     * Only visible to subclasses of AbstractPlayer.
     *
     * @return boolean true if token is not null | false otherwise.
     */
    protected final boolean isTokenSet(){
        return token != null;
    }

    /**
     * Gets the {@link PlayerToken} object that has been assigned to this player.
     * Only visible to subclasses of AbstractPlayer.
     *
     * @return the token assigned to this player.
     */
    protected final PlayerToken getToken(){
        return token;
    }
}
