package szakacs.kpi.fei.tuke.intrfc.misc;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The interface for a generic Stack collection whose elements are of type T.
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
     * Checks if stack contains no elements.
     *
     * @return boolean true, if the stack is empty | false otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if the number of elements is equal to the current capacity of the stack.
     *
     * @return boolean true, if the stack is full | false otherwise.
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
     * Returns an array view of all elements of the stack,
     * ordered from bottom to top of the stack. Changes
     * to this view are NOT reflected in the original
     * stack.
     *
     * @return an array view of all elements of the stack.
     */
    T[] getElements();

    /**
     * Returns a list of elements, ordered from bottom to top of the stack,
     * that satisfy the criteria passed as argument to this function.
     * If the given criteria is null, it returns all elements in the stack,
     * ordered from bottom to top.
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether an element should
     *                 be included in the query results. If a null
     *                 value is passed, all elements are returned.
     *
     * @return a list of stack elements satisfying the given criteria.
     */
    List<T> getElementsByCriteria(Predicate<T> criteria);
}
