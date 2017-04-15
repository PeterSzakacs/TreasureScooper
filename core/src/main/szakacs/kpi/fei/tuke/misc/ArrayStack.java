package szakacs.kpi.fei.tuke.misc;

import szakacs.kpi.fei.tuke.intrfc.misc.Stack;

import java.util.*;
import java.util.function.Predicate;

/**
 * An array-based implementation of the {@link Stack} collection interface.
 */
@SuppressWarnings("unchecked")
public class ArrayStack<T> implements Stack<T> {

    private class StackIterator implements Iterator<T>{
        private int currentIndex = -1;

        @Override
        public boolean hasNext() {
            if (currentIndex < elements.length - 1) {
                if (elements[currentIndex + 1] != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            if (hasNext())
                return (T) elements[++currentIndex];
            else
                throw new NoSuchElementException();
        }
    }



    // The actual type of elements in this array shall be T or a subclass of T.
    // The reason this is declared as an Object array is because generics and
    // arrays don't go well together.
    private Object[] elements;
    private int top;
    private boolean dynamic;
    private List<T> searchResults;



    public ArrayStack(int initialCapacity, boolean dynamic) {
        this.elements = new Object[initialCapacity];
        this.top = -1;
        this.dynamic = dynamic;
        this.searchResults = new ArrayList<T>(initialCapacity);
    }



    public void push(T element) {
        if (isFull()) {
            if (dynamic) {
                elements = Arrays.copyOf(elements, elements.length * 2);
            } else {
                throw new ArrayStoreException("ArrayStack is full!");
            }
        }
        if (element != null) {
            this.elements[++top] = element;
        }
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        } else {
            T element = (T) elements[top];
            elements[top--] = null;
            return element;
        }
    }

    public T top() {
        if (isEmpty()) {
            return null;
        } else {
            return (T) elements[top];
        }
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == elements.length - 1;
    }

    public int getCapacity() {
        return elements.length;
    }

    public int getNumElements(){
        return top + 1;
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(
                Arrays.asList((T[])
                        Arrays.copyOf(elements, getNumElements())
                )
        );
    }

    public List<T> getElementsByCriteria(Predicate<T> criteria) {
        if (criteria == null){
            return getElements();
        }
        searchResults.clear();
        T[] elements = (T[]) this.elements;
        for (int idx = 0; idx <= top; idx++) {
            if (criteria.test(elements[idx])) {
                searchResults.add(elements[idx]);
            }
        }
        return searchResults;
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }
}
