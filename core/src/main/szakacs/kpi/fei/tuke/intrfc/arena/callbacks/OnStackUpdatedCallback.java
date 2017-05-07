package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

/**
 * An interface for defining tasks to execute when an element
 * is added to or removed from a stack collection.
 */
public interface OnStackUpdatedCallback<T> {

    void onPush(T pushed);

    void onPop(T popped);
}
