package szakacs.kpi.fei.tuke.intrfc.misc;

/**
 * Created by developer on 3.2.2017.
 *
 * Interface implemented by any class that is responsible for extracting
 * game configuration at application start and transforming it into Java
 * value objects for use by the game manager.
 */
public interface GameConfigProcessor {

    /**
     * Upon calling starts retrieving data from external storage and
     * transforms it into a GameConfig object for later retrieval.
     */
    void processGameConfig();

    /**
     * Gets the GameConfig value object.
     *
     * @return the configuration for the game.
     */
    GameConfig getGameConfig();
}
