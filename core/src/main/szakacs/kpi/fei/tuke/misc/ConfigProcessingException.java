package szakacs.kpi.fei.tuke.misc;

/**
 * An exception thrown when there was some problem processing
 * the game configuration (i.e. file not found etc.).
 */
public class ConfigProcessingException extends Exception {

    public ConfigProcessingException(String message){
        super(message);
    }

    public ConfigProcessingException(String message, Throwable cause){
        super(message, cause);
    }
}
