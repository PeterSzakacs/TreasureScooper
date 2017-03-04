package szakacs.kpi.fei.tuke.intrfc.arena.game;

import szakacs.kpi.fei.tuke.intrfc.arena.game.actorManager.ActorManagerPrivileged;

/**
 * Created by developer on 29.1.2017.
 *
 * An extension to {@link GameLevelQueryable} interface exposing functionality
 * necessary for background code running the game.
 */
public interface GameLevelPrivileged extends GameLevelQueryable {

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
}
