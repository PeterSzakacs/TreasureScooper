package szakacs.kpi.fei.tuke.intrfc.game;

import szakacs.kpi.fei.tuke.intrfc.Player;
import szakacs.kpi.fei.tuke.intrfc.game.actorManager.ActorManagerPrivileged;

import java.util.Set;

/**
 * Created by developer on 29.1.2017.
 *
 * An extension to {@link GameLevel} interface exposing functionality
 * necessary for background code running the game.
 */
public interface GameLevelPrivileged extends GameLevel {

    /**
     * Gets a reference to the actor manager with full functionality exposed.
     *
     * @return the {@link ActorManagerPrivileged} single instance of the current game level
     */
    ActorManagerPrivileged getActorManager();

    /**
     * Updates the entire game
     */
    void update();

    /**
     * Starts a new game by setting a new set of updaters and a new player instance.
     *
     * @param updaters a set of {@link GameUpdater} objects responsible for updating
     *                 all logic of the game level.
     * @param player the player playing the current game level.
     */
    void startNewGame(Set<GameUpdater> updaters, Player player);
}
