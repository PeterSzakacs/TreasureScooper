package szakacs.kpi.fei.tuke.misc;

/**
 * Thrown by the game level or any class underneath it if there was some problem
 * starting a new game level.
 */
public class GameLevelInitializationException extends Exception{

    public GameLevelInitializationException(String message){
        super(message);
    }

    public GameLevelInitializationException(String message, Throwable cause){
        super(message, cause);
    }
}
