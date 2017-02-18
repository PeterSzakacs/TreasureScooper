package szakacs.kpi.fei.tuke.misc;

/**
 * Created by developer on 17.2.2017.
 */
public class ConfigProcessingException extends Exception {

    public ConfigProcessingException(String message){
        super(message);
    }

    public ConfigProcessingException(String message, Throwable cause){
        super(message, cause);
    }
}
