package szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe;

import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeHead;
import szakacs.kpi.fei.tuke.arena.actors.pipe.PipeSegment;
import szakacs.kpi.fei.tuke.arena.game.world.TunnelCell;
import szakacs.kpi.fei.tuke.enums.Direction;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.arena.actors.ActorBasic;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

/**
 * The interface for the pipe exposed to the player
 * to use for collecting treasures (their collection
 * happens automatically as the pipe moves).
 */
public interface PipeBasic {

    /**
     * <p>Returns the head of the pipe which automatically collects treasure
     * when it moves and which possesses a weapon.</p>
     *
     * @return The head of the pipe.
     */
    PipeHead getHead();

    /**
     * <p>Returns a modifiable stack of pipe segments representing the pipe from the base
     * of the pipe (stack bottom) to the segment connecting to the pipe head (stack top).
     * This stack is modifiable via {@link Stack#push(Object)} and {@link Stack#pop()} and
     * changing the stack this way causes the pipe head to move.</p>
     * 
     * @return The stack of {@link PipeSegment}s representing the pipe.
     */
    Stack<PipeSegment> getSegmentStack(PlayerToken token);
    
    /**
     * <p>A method to calculate the segment to push onto the segment stack
     * according to the Direction the player wants to move in.</p>
     *
     * <p>This method is made purely for convenience purposes,
     * to unburden the person calling {@link Stack#push(Object)}
     * from having to implement calculating the segment manually.</p>
     *
     * @param dir the direction the player wishes to move the head to
     * @return the pipe segment required to move the pipe head in the specified direction
     * @throws IllegalArgumentException if null or an unknown Direction enum value
     *         is passed as parameter
     */
    PipeSegment calculateNextSegment(Direction dir) throws IllegalArgumentException;

    /**
     * <p>Checks if there is a wall in front of the pipe head (it just checks if the adjacent
     * {@link TunnelCell}) in the specified Direction from the heads current position is null
     * or not.</p>
     *
     * @param dir the direction to check in.
     * @return boolean true if the adjacent cell is null | false otherwise.
     */
    boolean isWall(Direction dir);

    /**
     * <p>Checks if the given actor intersects with this pipe.</p>
     *
     * @param actor the actor to check.
     * @return boolean true if the actor intersects | false otherwise.
     */
    boolean intersects(ActorBasic actor);

    /**
     * <p>Returns a value in the range 0 to 100 representing the percentage to which the pipe is undamaged.</p>
     * 
     * @return the current degree to which the pipe is undamaged as a percentage. 
     */
    int getHealth();
}
