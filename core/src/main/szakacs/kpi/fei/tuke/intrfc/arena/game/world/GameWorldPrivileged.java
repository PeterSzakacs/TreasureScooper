package szakacs.kpi.fei.tuke.intrfc.arena.game.world;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.ResettableGameClass;

/**
 * An extension to the {@link GameWorldUpdatable} interface that is exposed
 * to the core backend classes managing the game logic as well as
 * any classes external to the game logic.
 */
public interface GameWorldPrivileged extends GameWorldUpdatable, ResettableGameClass {

    /**
     * Gets the authenticator object to be used to verify the caller
     * of a method with a signature containing an {@code Object authToken}
     * in their signatures by passing the authenticator to the method
     * as that parameter.
     *
     * @return the authenticator object.
     */
    MethodCallAuthenticator getAuthenticator();

    /**
     * Called when a nugget has been collected
     * to update the nugget count of the world.
     *
     * @param pipe the pipe which collected the nugget.
     * @param val the value of the nugget.
     */
    void onNuggetCollected(Pipe pipe, int val);
}
