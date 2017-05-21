package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Queue;

import java.util.*;
import java.util.function.Predicate;

/**
 * An array-based implementation of the {@link Queue} collection interface.
 */
@SuppressWarnings("unchecked")
public class ArrayQueue<T> implements Queue<T> {

    private class QueueIterator implements Iterator<T> {

        private int currentIndex = front;
        private int iteratedElementCount = 0;

        @Override
        public boolean hasNext() {
            return iteratedElementCount < numElements;
        }

        @Override
        public T next() {
            if ( hasNext() ) {
                T elm = (T) elements[currentIndex % capacity];
                currentIndex++;
                iteratedElementCount++;
                return elm;
            } else {
                throw new NoSuchElementException();
            }
        }
    }



    // The actual type of elements in this array shall be T or a subclass of T.
    // The reason this is declared as an Object array is because generics and
    // arrays don't go well together.
    private Object[] elements;
    // Strictly speaking, capacity is already stored inside the array object
    // as its length property, but the code looks better this way (subjective
    // opinion, admittedly).
    private int capacity;
    private int front;
    private int rear;
    private int numElements;
    private boolean dynamic;



    public ArrayQueue(int initialCapacity, boolean dynamic) {
        this.elements = new Object[initialCapacity];
        this.capacity = initialCapacity;
        this.front = 0;
        this.rear = -1;
        this.numElements = 0;
        this.dynamic = dynamic;
    }



    @Override
    public void enqueue(T element) {
        if (isFull()) {
            if (dynamic) {
                enlarge(capacity * 2);
            } else {
                throw new ArrayStoreException("ArrayQueue is full!");
            }
        }
        rear++; numElements++;
        if (rear == capacity)
            rear = 0;
        this.elements[rear] = element;
    }

    @Override
    public T dequeue() {
        if (isEmpty())
            return null;
        T toReturn = (T) this.elements[front];
        this.elements[front] = null;
        numElements--; front++;
        if (front == capacity)
            front = 0;
        return toReturn;
    }

    @Override
    public T front() {
        if (isEmpty())
            return null;
        else
            return (T) elements[front];
    }

    @Override
    public T rear() {
        if (isEmpty())
            return null;
        else
            return (T) elements[rear];
    }

    @Override
    public boolean isEmpty() {
        return numElements == 0;
    }

    @Override
    public boolean isFull() {
        return numElements == capacity;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getNumElements() {
        return numElements;
    }

    @Override
    public T[] getElements() {
        T[] list = (T[]) new Object[elements.length];
        T[] elements = (T[]) this.elements;
        for (int idx = front, count = 0; count < numElements; idx++, count++) {
            list[count] = elements[idx % capacity];
        }
        return list;
    }

    @Override
    public Set<T> getElementsByCriteria(Predicate<T> criteria){
        if (criteria == null){
            criteria = (T) -> true;
        }
        Set<T> set = new LinkedHashSet<>(
                (int) Math.ceil((float)elements.length/0.75)
        );
        T[] elements = (T[]) this.elements;
        for (int idx = front, count = 0; count < numElements; idx++, count++) {
            T element = elements[idx % capacity];
            if (criteria.test(element)) {
                set.add(element);
            }
        }
        return set;
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }



    // Miscellaneous methods



    private void enlarge(int newSize) {
        if (newSize <= capacity)
            return;
        Object[] elements = new Object[newSize];
        for (int idx = front, count = 0; count < numElements; idx++, count++) {
            elements[count] = this.elements[idx % capacity];
        }
        this.elements = elements;
        this.front = 0;
        this.rear = capacity - 1;
        this.capacity = newSize;
    }
}
