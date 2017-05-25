package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.arena.actors.pipe.Pipe;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;

import java.util.Map;

/**
 * An extension of the {@link PlayerManagerBasic} interface that is exposed
 * to game actors via proxy object.
 */
public interface PlayerManagerUpdatable extends PlayerManagerBasic {

    /**
     * Gets a set of currently active (not destroyed) pipes
     * identified by the token of the player that owns them.
     *
     * @return the set of currently active pipes identified by
     *         the token of the player owning them.
     */
    Map<PlayerToken, Pipe> getPipesUpdatable();

    /**
     * Gets a set of currently active players and associated info
     * about them identified by their tokens.
     *
     * @return the set of currently active players and associated info
     *         identified by their tokens
     */
    Map<PlayerToken, PlayerInfo> getPlayerTokenMap();
}
