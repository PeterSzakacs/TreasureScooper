package szakacs.kpi.fei.tuke.intrfc.arena.game.playerManager;

import szakacs.kpi.fei.tuke.intrfc.arena.callbacks.OnScoreEventCallback;
import szakacs.kpi.fei.tuke.intrfc.arena.game.GameUpdater;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerInfo;
import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;

import java.util.Map;

/**
 * An extension to the {@link PlayerManagerUpdatable} interface that is exposed
 * to the core backend classes managing the game logic as well as
 * any classes external to the game logic.
 */
public interface PlayerManagerPrivileged extends PlayerManagerUpdatable, GameUpdater {

    /**
     * Gets a callback object to use when setting player scores after some events.
     *
     * @return a callback for manipulating player scores.
     */
    OnScoreEventCallback getScoreChangeCallback();

    /**
     * Gets a set of players identified by their tokens,
     * that have failed in the current game level. This
     * could be due to throwing an exception or having
     * had their pipes destroyed.
     *
     * @return the set of failed players during the current
     *         game level identified by their tokens.
     */
    Map<PlayerToken, PlayerInfo> getUnregisteredPlayers();

    /**
     * Gets the reason why a player has crashed during the game level,
     * if he/she has crashed, otherwise it returns null.
     *
     * @param token the token identifying the player
     * @return the reason the player crashed (the exception that was thrown)
     */
    Throwable getCrashReason(PlayerToken token);
}
