package szakacs.kpi.fei.tuke.intrfc;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;

/**
 * The interface for all player classes which contains code
 * that the student can implement.
 */
public interface Player {

    /**
     * <p>Called as first thing after player instantiation</p>
     *
     * <p>Sets the authentication token used during a game level to check in other methods defined here
     * whether another player instance is attempting to call them to sabotage other players.</p>
     *
     * @param token the token to set on player creation.
     */
    void setPlayerToken(PlayerToken token);

    /**
     * <p>Called when the player is created and after the PlayerToken is set.</p>
     *
     * <p>For practical purposes (ability to create class instances
     * only through calling {@link Class#newInstance()}. Instantiation
     * of any member variables the player wants to use is reserved
     * for this method (and the constructor has no parameters).</p>
     *
     * <p><b>The code in this method should only be called if the player's token successfully validates the token passed as argument)</b></p>
     *
     * @param gameInterface the interface to the game functionality for the player
     * @param pipe the pipe assigned to the player
     * @param token a PlayerToken used to authenticate that the caller is allowed
     *              to call this method.
     */
    void initialize(PlayerGameInterface gameInterface, PipeBasic pipe, PlayerToken token);

    /**
     * <p>Called on every iteration of the game loop. This is also
     * where the student implements code to control the pipe.</p>
     *
     * <p><b>This is the main bit of code that has to be implemented</b></p>
     *
     * <p><b>The code in this method should only be called if the player's token successfully validates the token passed as argument)</b></p>
     *
     * @param token a PlayerToken used to authenticate that the caller is allowed
     *              to call this method.
     */
    void act(PlayerToken token);

    /**
     * <p>Called when the current game level ends.</p>
     *
     * <p>Could be used by native players to free resources
     * that are not taken care of by the garbage collector.</p>
     *
     * <p>Currently no plans to implement native interface,
     * but keeping this method for now.</p>
     */
    void deallocate();
}
