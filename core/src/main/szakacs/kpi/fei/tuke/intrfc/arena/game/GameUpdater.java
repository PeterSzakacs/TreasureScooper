package szakacs.kpi.fei.tuke.intrfc.arena.game;

/**
 * Created by developer on 31.12.2016.
 *
 * An interface required to be implemented by any class
 * updating specific aspects of the game (player, actors,
 * etc.)
 *
 * Anytime one wants to add new functionality
 * to the game (such as new actors) one has to
 * create an updater class which manages it
 * (in addition to, e.g the new actors)
 */
public interface GameUpdater extends ResettableGameClass {

    /**
     * Updates the specific aspects of the game logic particular to the implementing class.
     */
    void update();
}
