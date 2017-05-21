package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A class providing custom read-only wrapper views of our custom
 * {@link Stack} and {@link Queue} collections.
 */
public class CollectionsCustom {

    // this class shall not be instantiable
    private CollectionsCustom(){

    }

    /**
     * <p>Returns an unmodifiable view of the stack
     * passed to it as argument.</p>
     *
     * <p>Calling push() or pop() on this view causes
     * an UnsupportedOperationException to be thrown.</p>
     *
     * @param stack the stack for which to create a read-only view.
     * @param <T> the type of elements in the original stack and the read-only view.
     * @return a read-only view of the stack passed as argument.
     */
    public static <T> Stack<T> unmodifiableStack(Stack<T> stack){
        return new UnmodifiableStack<>(stack);
    }



    private static class UnmodifiableStack<E> implements Stack<E> {
        private final Stack<E> original;

        private UnmodifiableStack(Stack<E> original){ this.original = original; }

        public void push(E element) {
            throw new UnsupportedOperationException("Modifying the stack is forbidden!");
        }
        public E pop() {
            throw new UnsupportedOperationException("Modifying the stack is forbidden!");
        }
        public E top() { return original.top(); }
        public boolean isEmpty() { return original.isEmpty(); }
        public boolean isFull() { return original.isFull(); }
        public int getCapacity() { return original.getCapacity(); }
        public int getNumElements() { return original.getNumElements(); }

        // The list returned is unmodifiable, so safe
        public E[] getElements() { return original.getElements(); }
        // Changes to the list returned do not affect the stack, so safe
        public Set<E> getElementsByCriteria(Predicate<E> criteria) {
            return original.getElementsByCriteria(criteria);
        }
        // Iterator does not support element removal, so safe
        public Iterator<E> iterator() { return original.iterator(); }
    }


    /**
     * <p>Returns an unmodifiable view of the queue
     * passed to it as argument.</p>
     *
     * <p>Calling enqueue() or dequeue() on this view causes
     * an UnsupportedOperationException to be thrown.</p>
     *
     * @param queue the queue for which to create a read-only view.
     * @param <T> the type of elements in the original queue and the read-only view.
     * @return a read-only view of the queue passed as argument.
     */
    public static <T> Queue<T> unmodifiableQueue(Queue<T> queue) {
        return new UnmodifiableQueue<>(queue);
    }



    private static class UnmodifiableQueue<E> implements Queue<E> {
        private final Queue<E> original;

        private <T> UnmodifiableQueue(Queue<E> original) {
            this.original = original;
        }

        public void enqueue(E element) {
            throw new UnsupportedOperationException("Modifying the queue is forbidden!");
        }
        public E dequeue() {
            throw new UnsupportedOperationException("Modifying the queue is forbidden!");
        }

        public E front() { return original.front(); }
        public E rear() { return original.rear(); }
        public boolean isEmpty() { return original.isEmpty(); }
        public boolean isFull() { return original.isFull(); }
        public int getCapacity() { return original.getCapacity(); }
        public int getNumElements() { return original.getNumElements(); }

        // The list returned is unmodifiable, so safe
        public E[] getElements() { return original.getElements(); }
        // Changes to the list returned do not affect the queue, so safe
        public Set<E> getElementsByCriteria(Predicate<E> criteria) {
            return original.getElementsByCriteria(criteria);
        }
        // Iterator does not support element removal, so safe
        public Iterator<E> iterator() { return original.iterator(); }
    }
}
