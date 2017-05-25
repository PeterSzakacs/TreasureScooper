package szakacs.kpi.fei.tuke.misc;

/**
 * An exception type used as a wrapper around the actual exception
 * thrown by a player during the game. It is never thrown, it just
 * serves to wrap the actual exception thrown by the player with
 * a message stating which player class threw it and in which level.
 */
public class PlayerException extends Exception {

    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
