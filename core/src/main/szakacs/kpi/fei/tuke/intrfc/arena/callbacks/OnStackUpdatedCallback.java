package szakacs.kpi.fei.tuke.intrfc.arena.callbacks;

/**
 * An interface for defining tasks to execute when an element
 * is added to or removed from a stack collection.
 */
public interface OnStackUpdatedCallback<T> {

    /**
     * Called when an element has been pushed onto the stack.
     *
     * @param pushed the element pushed onto the stack.
     */
    void onPush(T pushed);

    /**
     * Called when an element has been popped off the stack.
     *
     * @param popped the element poped off the stack.
     */
    void onPop(T popped);
}
