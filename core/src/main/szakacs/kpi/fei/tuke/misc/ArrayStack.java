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



    public ArrayStack(int initialCapacity, boolean dynamic) {
        this.elements = new Object[initialCapacity];
        this.top = -1;
        this.dynamic = dynamic;
    }



    @Override
    public void push(T element) {
        if (isFull()) {
            if (dynamic) {
                elements = Arrays.copyOf(elements, elements.length * 2);
            } else {
                throw new ArrayStoreException("ArrayStack is full!");
            }
        }
        this.elements[++top] = element;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        } else {
            T element = (T) elements[top];
            elements[top--] = null;
            return element;
        }
    }

    @Override
    public T top() {
        if (isEmpty()) {
            return null;
        } else {
            return (T) elements[top];
        }
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public boolean isFull() {
        return top == elements.length - 1;
    }

    @Override
    public int getCapacity() {
        return elements.length;
    }

    @Override
    public int getNumElements(){
        return top + 1;
    }

    @Override
    public T[] getElements() {
        T[] list = (T[]) new Object[elements.length];
        T[] source = (T[]) elements;
        System.arraycopy(source, 0, list, 0, top + 1);
        return list;
    }

    @Override
    public Set<T> getElementsByCriteria(Predicate<T> criteria) {
        if (criteria == null){
            criteria = (T) -> true;
        }
        T[] elements = (T[]) this.elements;
        Set<T> set = new LinkedHashSet<>(
                (int) Math.ceil((float)(top+1)/0.75)
        );
        for (int idx = 0; idx <= top; idx++) {
            if (criteria.test(elements[idx])) {
                set.add(elements[idx]);
            }
        }
        return set;
    }

    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }
}
