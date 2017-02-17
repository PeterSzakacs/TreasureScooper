package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Queue;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by developer on 14.2.2017.
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
    private List<T> searchResults;



    public ArrayQueue(int initialCapacity, boolean dynamic) {
        this.elements = new Object[initialCapacity];
        this.capacity = initialCapacity;
        this.front = 0;
        this.rear = -1;
        this.numElements = 0;
        this.dynamic = dynamic;
        this.searchResults = new ArrayList<T>();
    }



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
        //return true;
    }

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

    public T front() {
        if (isEmpty())
            return null;
        else
            return (T) elements[front];
    }

    public T rear() {
        if (isEmpty())
            return null;
        else
            return (T) elements[rear];
    }

    public boolean isEmpty() {
        return numElements == 0;
    }

    public boolean isFull() {
        return numElements == capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumElements() {
        return numElements;
    }

    public List<T> getElementsByCriteria(Predicate<T> criteria){

        // if no criteria are specified, all elements from the queue are returned
        if (criteria == null){
            criteria = t -> true;
        }
        searchResults.clear();
        T[] elements = (T[]) this.elements;
        for (int idx = front, count = 0; count < numElements; idx++, count++) {
            T element = elements[idx % capacity];
            if (criteria.test(element)) {
                searchResults.add(element);
            }
        }
        return searchResults;
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
