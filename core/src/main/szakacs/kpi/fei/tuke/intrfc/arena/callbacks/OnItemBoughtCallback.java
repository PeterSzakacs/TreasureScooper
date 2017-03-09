package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

import szakacs.kpi.fei.tuke.intrfc.Player;

/**
 * Created by developer on 6.2.2017.
 *
 * The functional interface for a callback method
 * called when the player purchases something
 * from the game store, e.g. repairing the pipe,
 * or buying a new bullet.
 *
 * Purchasing something results in decrementing the game score.
 */
public interface OnItemBoughtCallback {

    /**
     * The callback method, the decrement to the game score
     * is determined by the actual price parameter passed.
     *
     * @param price the price payed for whatever was bough from the store
     *              (equals the decrement of the game score).
     */
    void onItemBought(int price, Player buyer);
}
