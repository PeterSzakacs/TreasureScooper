package szakacs.kpi.fei.tuke.intrfc.misc;

/**
 * Containing information about the results (score etc.)
 * of a particular player class for either one level or
 * for the whole game.
 */
public interface GameResult {

    /**
     * Gets the number of points the player has scored.
     *
     * @return the number of points the player has scored
     */
    int getScore();

    /**
     * Checks if the player has failed in either a given level
     * or in the game as a whole.
     *
     * @return boolean true if the player has failed to reach
     *         the objective of either a particular level or
     *         the game as a whole | false otherwise.
     */
    boolean hasFailed();

    /**
     * Checks if the player has thrown an exception.
     *
     * @return boolean true if the player has ever
     *         thrown an exception | false otherwise.
     */
    boolean hasCrashed();

    /**
     * Gets the reason why this player has crashed,
     * if he/she has crashed.
     *
     * @return the exception raised by the player | null
     *         if {@code hasCrashed()} returns false.
     */
    Throwable getCrashReason();
}
