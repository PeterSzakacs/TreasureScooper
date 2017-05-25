package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

import szakacs.kpi.fei.tuke.intrfc.player.PlayerToken;

/**
 * Created by developer on 7.3.2017.
 *
 * <p>The functional interface for a callback method
 * called whenever an action occurs which affects
 * the score of a player, for example when a player
 * buys something such as repairing his/her pipe,
 * or buying a new bullet or when his/her pipe
 * collects a piece of treasure.</p>
 *
 * <p>Purchasing something decrements the players score.</p>
 * <p>Collecting treasure increments the players score.</p>
 */
public interface OnScoreEventCallback {

    /**
     * The callback method. Players are identified by the token passed.
     *
     * @param newScore the new score value for the player.
     * @param token the token identifying the player whom the score change affects.
     */
    void onScoreEvent(int newScore, PlayerToken token);
}
