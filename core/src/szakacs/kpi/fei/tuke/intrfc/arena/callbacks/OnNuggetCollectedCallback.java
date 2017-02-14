package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

/**
 * Created by developer on 28.1.2017.
 *
 * The functional interface for a callback method
 * called when the pipe collects a piece of treasure.
 *
 * Collecting treasures increments the game score.
 */
public interface OnNuggetCollectedCallback {

    /**
     * The callback method, the increment to the game score
     * is determined by the actual argument passed.
     *
     * @param nuggetValue the value of the piece of treasure collected
     *                    (equals the increment to the score)
     */
    void onNuggetCollected(int nuggetValue);
}
