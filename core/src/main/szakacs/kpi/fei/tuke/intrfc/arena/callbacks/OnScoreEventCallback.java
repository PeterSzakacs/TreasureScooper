package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

import szakacs.kpi.fei.tuke.intrfc.Player;

/**
 * Created by developer on 7.3.2017.
 *
 * The functional interface for a callback method
 * called whenever an action occurs which affects
 * the score of a player, for example when a player
 * buys something such as repairing his/her pipe,
 * or buying a new bullet or when his/her pipe
 * collects a piece of treasure.
 *
 * Purchasing something decrement the players score.
 * Collecting treasure increments the players score.
 */
public interface OnScoreEventCallback {

    /**
     * The callback method
     *
     * @param newScore the new score value for the player.
     * @param affectedPlayer the player whom the score change affects.
     */
    void onScoreEvent(int newScore, Player affectedPlayer);
}
