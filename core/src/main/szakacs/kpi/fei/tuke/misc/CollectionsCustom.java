package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by developer on 21.4.2017.
 */
public class CollectionsCustom {

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
        public List<E> getElements() { return original.getElements(); }
        // Changes to the list returned do not affect the stack, so safe
        public List<E> getElementsByCriteria(Predicate<E> criteria) {
            return original.getElementsByCriteria(criteria);
        }
        // Iterator does not support element removal, so safe
        public Iterator<E> iterator() { return original.iterator(); }
    }





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
        public List<E> getElements() { return original.getElements(); }
        // Changes to the list returned do not affect the queue, so safe
        public List<E> getElementsByCriteria(Predicate<E> criteria) {
            return original.getElementsByCriteria(criteria);
        }
        // Iterator does not support element removal, so safe
        public Iterator<E> iterator() { return original.iterator(); }
    }
}
