package szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.Actor;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

/**
 * Created by developer on 5.4.2017.
 * 
 * The interface for the pipe implementation that is currently exposed to the player
 * to use for collecting treasures.
 */
public interface BasicPipe {

    /**
     * Returns the head of the pipe which automatically collects treasure 
     * and possesses a weapon.
     */
    PipeHead getHead();

    /**
     * Returns a modifiable stack of {@link PipeSegment}s representing 
     * the pipe in the direction from the base of the pipe (stack bottom)
     * to the segment connecting to the pipe head (stack top).
     * 
     * @return The stack of {@link PipeSegment}s representing the pipe.
     */
    Stack<PipeSegment> getSegmentStack();
    
    /**
     * A method to calculate the segment to push onto the segmentStack
     * according to the direction the player wants to move to.
     * This method is made purely for convenience purposes,
     * to unburden the person calling push() from having to
     * implement calculating the segment manually.
     *
     * @param dir the direction the player wishes to move the head to
     * @return the pipe segment required to move the pipe head in the specified direction
     * @throws IllegalArgumentException if null or an unknown Direction enum value passed as parameter
     */
    PipeSegment calculateNextSegment(Direction dir) throws IllegalArgumentException;

    /**
     * A method to check if there is a wall in front of the pipe head (it just checks if the adjacent 
     * {@link szakacs.kpi.fei.tuke.arena.game.world.TunnelCell}) in the specified {@link Direction}
     * from the head is null or not.
     * 
     * @return boolean true if the adjacent cell is null | false otherwise
     */
    boolean isWall(Direction dir);

    /**
     * Checks if the given actor intersects with this pipe. Since an actor should only make contact 
     * with the pipe at the segments where the pipe bends, it just checks those).
     * 
     * @return boolean true if the actor intersects | false otherwise
     */
    boolean intersects(Actor actor);

    /**
     * Returns a value in the range 0 to 100 representing the percentage to which the pipe is undamaged.
     * 
     * @return the current degree to which the pipe is undamaged as a percentage. 
     */
    int getHealth();
}
