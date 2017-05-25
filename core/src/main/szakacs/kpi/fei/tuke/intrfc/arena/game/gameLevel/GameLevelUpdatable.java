package szakacs.kpi.fei.tuke.intrfc.arena.game.gameLevel;

import szakacs.kpi.fei.tuke.intrfc.arena.game.MethodCallAuthenticator;
import szakacs.kpi.fei.tuke.intrfc.arena.game.world.GameWorldUpdatable;

/**
 * An extension to the {@link GameLevelBasic} that is exposed
 * to game actors via proxy object.
 */
public interface GameLevelUpdatable extends GameLevelBasic {

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
     * Gets a view of the game world that is exposed to game actors.
     *
     * @return the game world.
     */
    GameWorldUpdatable getGameWorld();
}
