package szakacs.kpi.fei.tuke.enums;

/**
 * An enum representing the current state of a particular game level.
 */
public enum GameState {

    /**
     * The state before a new game level is fully started.
     */
    INITIALIZING,

    /**
     * The state when at least one player in the game level
     * still has a pipe with a health value greater than 0.
     */
    PLAYING,

    /**
     * The state when all pieces of treasure are collected and
     * at least one player still has a pipe with a health value
     * greater than 0.
     */
    WON,

    /**
     * The state when at not all pieces of treasure are collected
     * and no player has a pipe with a health value of 0.
     */
    LOST
}
