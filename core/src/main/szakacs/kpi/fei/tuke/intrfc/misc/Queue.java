package szakacs.kpi.fei.tuke.intrfc.misc;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 16.2.2017.
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
     * Self-explanatory
     */
    boolean isEmpty();

    /**
     * Self-explanatory
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
     * @return the number of elements in the queue
     */
    int getNumElements();

    /**
     * Returns a list of elements, ordered from front to rear of the queue,
     * that satisfy the criteria passed as argument to this function.
     * If the given criteria is null, it returns all elements in the queue,
     * ordered from front to rear.
     *
     * @return a List of queue elements satisfying the given criteria.
     */
    List<T> getElementsByCriteria(Predicate<T> criteria);
}
