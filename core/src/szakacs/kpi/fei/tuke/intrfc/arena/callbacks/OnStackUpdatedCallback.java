package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

/**
 * Created by developer on 15.2.2017.
 */
public interface OnStackUpdatedCallback<T> {

    void onPush();

    void onPop(T popped);
}
