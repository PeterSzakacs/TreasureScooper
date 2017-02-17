package szakacs.kpi.fei.tuke.test;

import org.junit.*;
import szakacs.kpi.fei.tuke.intrfc.misc.Stack;
import szakacs.kpi.fei.tuke.misc.ArrayStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 16.2.2017.
 */
public class StackTest {

    @Test
    public void testDynamicStack(){
        int capacity = 10;
        ArrayStack<Integer> dynamicTestStack = new ArrayStack<>(capacity, true);
        fillStack(dynamicTestStack);

        // The stack should now grow to accommodate more elements.
        // If any exception is thrown, it means it does not work.
        try {
            dynamicTestStack.push(capacity + 1);
        } catch (ArrayStoreException e) {
            System.err.println("Dynamic stack has failed the test");
        }
        Assert.assertEquals(2 * capacity, dynamicTestStack.getCapacity());
        Assert.assertEquals(capacity + 1, dynamicTestStack.getNumElements());

        System.out.println("Dynamic stack has passed the test");
/*        for (int idx = 0; idx < 5; idx++){
            dynamicTestStack.push(idx);
            Assert.assertEquals(capacity + idx + 2, dynamicTestStack.getNumElements());
        }
        Assert.assertFalse(dynamicTestStack.isFull());
        Assert.assertFalse(dynamicTestStack.isEmpty());*/
    }


    @Test
    public void testStaticStack(){
        int capacity = 10;
        ArrayStack<Integer> staticTestStack = new ArrayStack<>(capacity, false);
        Assert.assertTrue(staticTestStack.isEmpty());
        Assert.assertFalse(staticTestStack.isFull());
        for (int idx = 0; idx < capacity; idx++){
            staticTestStack.push(idx);
            Assert.assertEquals(idx + 1, staticTestStack.getNumElements());
        }
        Assert.assertTrue(staticTestStack.isFull());
        Assert.assertFalse(staticTestStack.isEmpty());

        // The stack should refuse having another element pushed onto it and throw an exception
        // If it doesn't, then it does not work properly.
        try {
            staticTestStack.push(capacity + 1);
            System.err.println("Static stack has passed the test");
        } catch (ArrayStoreException e) {
            System.out.println("Static stack has passed the test");
        }
        Assert.assertEquals(capacity, staticTestStack.getCapacity());
        Assert.assertEquals(capacity, staticTestStack.getNumElements());
        emptyStack(staticTestStack);
    }

    @Test
    public void testStackOperation(){
        int capacity = 10;
        Stack<Integer> testStack = new ArrayStack<>(capacity, false);
        fillStack(testStack);
        emptyStack(testStack);
    }


    @Test
    public void push() throws Exception {
        int capacity = 10;
        Stack<Integer> testStack = new ArrayStack<>(capacity, false);
        fillStack(testStack);
    }

    @Test
    public void pop() throws Exception {
        int capacity = 10;
        Stack<Integer> testStack = new ArrayStack<>(capacity, false);
        Assert.assertTrue(testStack.isEmpty());
        for (int idx = 0; idx < capacity; idx++) {
            testStack.push(idx);
        }
        Assert.assertTrue(testStack.isFull());
        Assert.assertFalse(testStack.isEmpty());
        emptyStack(testStack);
    }

    @Test
    public void getElementsByCriteria() throws Exception {
        int capacity = 10;
        Stack<Integer> testStack = new ArrayStack<>(capacity, false);
        fillStack(testStack);
        List<Integer> comparison = new ArrayList<>();

        for (int idx = 0; idx < capacity; idx++){
            comparison.add(idx);
        }
        // if no criteria are given, return a list of all elements from bottom to top of the stack
        List<Integer> queryResults = testStack.getElementsByCriteria(null);
        Assert.assertEquals(capacity, queryResults.size());
        Assert.assertArrayEquals(comparison.toArray(), queryResults.toArray());

        comparison.clear();
        for (int idx = 0; idx < capacity; idx += 2){
            comparison.add(idx);
        }
        // find all even numbers ordered from bottom to top of the stack
        queryResults = testStack.getElementsByCriteria((integer) -> integer % 2 == 0);
        Assert.assertEquals(capacity/2, queryResults.size());
        Assert.assertArrayEquals(comparison.toArray(), queryResults.toArray());
    }


    /*
     * Due to the fact that the below 2 private methods
     * presumably provide more than enough code coverage,
     * we are omitting test methods for top(), isEmpty()
     * and isFull().
     * Because getCapacity() and getNumElements() are
     * just simple getters, we are omitting their tests
     * as well.
     */

    private void fillStack(Stack<Integer> emptyStack) {
        int capacity = emptyStack.getCapacity();
        Assert.assertTrue(emptyStack.isEmpty());
        Assert.assertFalse(emptyStack.isFull());
        Assert.assertEquals(0, emptyStack.getNumElements());
        Assert.assertSame(null, emptyStack.top());
        Assert.assertSame(null, emptyStack.pop());
        Assert.assertEquals(0, emptyStack.getNumElements());
        Assert.assertSame(null, emptyStack.top());
        emptyStack.push(0);
        Assert.assertEquals(1, emptyStack.getNumElements());
        Assert.assertEquals(0, emptyStack.top().intValue());
        Assert.assertFalse(emptyStack.isEmpty());
        Assert.assertFalse(emptyStack.isFull());
        for (int idx = 1; idx < capacity; idx++) {
            Assert.assertFalse(emptyStack.isEmpty());
            Assert.assertEquals(idx, emptyStack.getNumElements());
            Assert.assertEquals(idx - 1, emptyStack.top().intValue());
            emptyStack.push(idx);
            Assert.assertFalse(emptyStack.isEmpty());
            Assert.assertEquals(idx + 1, emptyStack.getNumElements());
            Assert.assertEquals(idx, emptyStack.top().intValue());
        }
        Assert.assertFalse(emptyStack.isEmpty());
        Assert.assertTrue(emptyStack.isFull());
    }

    private void emptyStack(Stack<Integer> stack) {
        int numElements = stack.getNumElements();
        for (int idx = 0; idx < numElements - 1; idx++){
            Assert.assertEquals(numElements - idx - 1, stack.top().intValue());
            Assert.assertEquals(numElements - idx, stack.getNumElements());
            Assert.assertEquals(numElements - idx - 1, stack.pop().intValue());
            Assert.assertEquals(numElements - idx - 2, stack.top().intValue());
            Assert.assertEquals(numElements - idx - 1, stack.getNumElements());
        }
        Assert.assertEquals(0 , stack.top().intValue());
        Assert.assertEquals(1, stack.getNumElements());
        Assert.assertEquals(0 , stack.pop().intValue());
        Assert.assertSame(null, stack.top());
        Assert.assertEquals(0, stack.getNumElements());
        Assert.assertSame(null, stack.pop());
        Assert.assertEquals(0, stack.getNumElements());
        Assert.assertSame(null, stack.top());
        Assert.assertTrue(stack.isEmpty());
        Assert.assertFalse(stack.isFull());
    }
}