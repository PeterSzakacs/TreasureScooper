package szakacs.kpi.fei.tuke.misc;

/**
 * Created by developer on 15.3.2017.
 */
public class GameLevelInitializationException extends Exception{

    public GameLevelInitializationException(String message){
        super(message);
    }

    public GameLevelInitializationException(String message, Throwable cause){
        super(message, cause);
    }
}
