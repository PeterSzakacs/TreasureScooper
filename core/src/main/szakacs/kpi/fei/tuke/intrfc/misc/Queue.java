package szakacs.kpi.fei.tuke.intrfc.misc;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The interface for a generic Queue collection whose elements are of type T.
 */
public interface Queue<T> extends Iterable<T> {

    /**
     * Adds an element to the rear of the queue.
     *
     * @param element the element to add to the queue.
     */
    void enqueue(T element);

    /**
     * Removes an element from the front of the queue.
     *
     * @return the element removed from the queue.
     */
    T dequeue();

    /**
     * Returns the element at the front of the queue,
     * without removing it.
     *
     * @return the element at the front of the queue.
     */
    T front();

    /**
     * Returns the element at the rear of the queue,
     * without removing it.
     *
     * @return the element at the rear of the queue.
     */
    T rear();

    /**
     * Checks if queue contains no elements.
     *
     * @return boolean true, if the queue is empty | false otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if the number of elements is equal to the current capacity of the queue.
     *
     * @return boolean true, if the queue is full | false otherwise.
     */
    boolean isFull();

    /**
     * Returns the number of elements the queue is currently able to hold.
     * In the case of a dynamic queue, every time the number of elements
     * increases beyond the queue's capacity, the capacity is doubled.
     *
     * @return the capacity of the queue
     */
    int getCapacity();

    /**
     * Returns the current number of elements stored in the queue.
     *
     * @return the number of elements in the queue.
     */
    int getNumElements();

    /**
     * Returns an array view of all elements of the queue,
     * ordered from front to rear of the queue. Changes
     * to this view are NOT reflected in the original
     * queue.
     *
     * @return an array view of all elements of the queue.
     */
    T[] getElements();

    /**
     * Returns a set of elements, ordered from front to rear of the queue,
     * that satisfy the criteria passed as argument to this function.
     * If the given criteria is null, it returns all elements in the queue,
     * ordered from front to rear.
     *
     * @param criteria a functional interface or lambda function
     *                 used in evaluating whether an element should
     *                 be included in the query results. If a null
     *                 value is passed, all elements are returned.
     *
     * @return a set of queue elements satisfying the given criteria.
     */
    Set<T> getElementsByCriteria(Predicate<T> criteria);
}
