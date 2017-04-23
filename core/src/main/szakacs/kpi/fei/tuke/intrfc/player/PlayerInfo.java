package szakacs.kpi.fei.tuke.intrfc.player;

import szakacs.kpi.fei.tuke.intrfc.arena.actors.pipe.PipeBasic;

/**
 * A value object containing info associated with a particular player,
 * such as its current score and the pipe assigned to it.
 */
public interface PlayerInfo {

    /**
     * Gets the current score of this player instance.
     *
     * @return the current score of this player instance.
     */
    int getScore();

    /**
     * Gets the pipe assigned to this player instance.
     *
     * @return the pipe assigned to this player instance.
     */
    PipeBasic getPipe();

    /**
     * Gets this player instance, specifically a view of this instance
     * that is safe to expose to other players.
     *
     * @return this player instance.
     */
    BasePlayer getPlayer();
}
