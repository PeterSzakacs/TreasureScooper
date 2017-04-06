package szakacs.kpi.fei.tuke.intrfc;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.BasicPipe;
import szakacs.kpi.fei.tuke.intrfc.arena.proxies.PlayerGameInterface;

/**
 * Created by developer on 2.12.2016.
 *
 * The interface for all player classes which contains code
 * that the student can implement.
 */
public interface Player {

    /**
     * Called on every iteration of the game loop. This is also
     * where the student implements code to control the pipe.
     */
    void act();

    /**
     * For practical purposes (ability to create class instances
     * only through calling class->newInstance()) instantiation
     * of any member variables the player wants to use is reserved
     * for this method (and the constructor has no parameters).
     *
     * @param gameInterface the interface to the game functionality for the player
     * @param pipe
     */
    void initialize(PlayerGameInterface gameInterface, BasicPipe pipe);

    /**
     * Frees resources that are not taken care of by the garbage
     * collector. This will probably only be used by native coded
     * players to delete any global references in native code.
     */
    void deallocate();
}
