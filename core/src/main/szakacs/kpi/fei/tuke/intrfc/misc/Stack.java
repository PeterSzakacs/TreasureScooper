package szakacs.kpi.fei.tuke.intrfc.misc;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 16.2.2017.
 */
public interface Stack<T> extends Iterable<T> {

    /**
     * Add an element to the top of the stack
     *
     * @param element the element to add
     */
    void push(T element);

    /**
     * Remove an element from the top of the stack
     *
     * @return the element removed from the stack
     */
    T pop();

    /**
     * Also sometimes referred to as peek()
     * Shows the element at the top of the stack,
     * without removing it.
     *
     * @return the element at the top of the stack
     */
    T top();

    /**
     * Self-explanatory
     */
    boolean isEmpty();

    /**
     * Self-explanatory
     */
    boolean isFull();

    /**
     * Returns the number of elements the stack is currently able to hold.
     * In the case of a dynamic stack, every time the number of elements
     * increases beyond the stack's capacity, the capacity is doubled.
     *
     * @return the capacity of the stack
     */
    int getCapacity();

    /**
     * Returns the current number of elements stored in the stack.
     *
     * @return the number of elements in the stack
     */
    int getNumElements();

    /**
     * Returns a list of elements, ordered from bottom to top of the stack,
     * that satisfy the criteria passed as argument to this function.
     * If the given criteria is null, it returns all elements in the stack,
     * ordered from bottom to top.
     *
     * @return a List of stack elements satisfying the given criteria.
     */
    List<T> getElementsByCriteria(Predicate<T> criteria);
}
