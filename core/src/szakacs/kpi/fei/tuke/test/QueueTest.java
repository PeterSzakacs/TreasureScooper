package szakacs.kpi.fei.tuke.test;

import org.junit.Assert;
import org.junit.Test;
import szakacs.kpi.fei.tuke.intrfc.misc.Queue;
import szakacs.kpi.fei.tuke.misc.ArrayQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 16.2.2017.
 */
public class QueueTest {

    @Test
    public void testDynamicQueue() {
        int capacity = 10;
        Queue<Integer> dynamicTestQueue = new ArrayQueue<>(10, true);
        fillQueue(dynamicTestQueue);

        // The queue should now grow to accommodate more elements.
        // If any exception is thrown, it means it does not work.
        try {
            dynamicTestQueue.enqueue(capacity + 1);
        } catch (ArrayStoreException e) {
            System.err.println("Dynamic queue has failed the test");
        }
        Assert.assertEquals(2 * capacity, dynamicTestQueue.getCapacity());
        Assert.assertEquals(capacity + 1, dynamicTestQueue.getNumElements());
        System.out.println("Dynamic queue has passed the test");
    }

    @Test
    public void testStaticQueue() {
        int capacity = 10;
        Queue<Integer> staticTestQueue = new ArrayQueue<>(capacity, false);
        fillQueue(staticTestQueue);

        // The queue should refuse enqueuing another element and throw an exception.
        // If it doesn't, then it does not work properly.
        try {
            staticTestQueue.enqueue(capacity + 1);
            System.err.println("Static queue has failed the test");
        } catch (ArrayStoreException e) {
            System.out.println("Static queue has passed the test");
        }
        Assert.assertEquals(capacity, staticTestQueue.getCapacity());
        Assert.assertEquals(capacity, staticTestQueue.getNumElements());
        emptyQueue(staticTestQueue);
    }

    @Test
    public void testQueueOperation() {
        int capacity = 10;
        Queue<Integer> testQueue = new ArrayQueue<>(capacity, false);
        fillQueue(testQueue);
        emptyQueue(testQueue);
    }

    @Test
    public void enqueue() throws Exception {
        int capacity = 10;
        Queue<Integer> testQueue = new ArrayQueue<>(capacity, false);
        fillQueue(testQueue);
    }

    @Test
    public void dequeue() throws Exception {
        int capacity = 10;
        Queue<Integer> testQueue = new ArrayQueue<>(capacity, false);
        Assert.assertTrue(testQueue.isEmpty());
        for (int idx = 0; idx < capacity; idx++) {
            testQueue.enqueue(idx);
        }
        Assert.assertTrue(testQueue.isFull());
        Assert.assertFalse(testQueue.isEmpty());
        emptyQueue(testQueue);
    }

    @Test
    public void getElementsByCriteria() throws Exception {
        int capacity = 10;
        Queue<Integer> testQueue = new ArrayQueue<>(capacity, false);
        fillQueue(testQueue);
        List<Integer> comparison = new ArrayList<>();

        for (int idx = 0; idx < capacity; idx++){
            comparison.add(idx);
        }
        // if no criteria are given, return a list of all elements from front to rear of the queue
        List<Integer> queryResults = testQueue.getElementsByCriteria(null);
        Assert.assertEquals(capacity, queryResults.size());
        Assert.assertArrayEquals(comparison.toArray(), queryResults.toArray());

        comparison.clear();
        for (int idx = 0; idx < capacity; idx += 2){
            comparison.add(idx);
        }
        // find all even numbers ordered from front to rear of the queue
        queryResults = testQueue.getElementsByCriteria((integer) -> integer % 2 == 0);
        Assert.assertEquals(capacity/2, queryResults.size());
        Assert.assertArrayEquals(comparison.toArray(), queryResults.toArray());
    }

    /*
     * Due to the fact that the below 2 private methods
     * presumably provide more than enough code coverage,
     * we are omitting test methods for front(), rear(),
     * isEmpty() and isFull().
     * Because getCapacity() and getNumElements() are
     * just simple getters, we are omitting their tests
     * as well.
     */


    private void fillQueue(Queue<Integer> emptyTestQueue) {
        int capacity = emptyTestQueue.getCapacity();
        Assert.assertTrue(emptyTestQueue.isEmpty());
        Assert.assertFalse(emptyTestQueue.isFull());
        Assert.assertSame(emptyTestQueue.front(), null);
        Assert.assertSame(emptyTestQueue.rear(), null);
        Assert.assertSame(emptyTestQueue.dequeue(), null);
        Assert.assertSame(emptyTestQueue.front(), null);
        Assert.assertSame(emptyTestQueue.rear(), null);
        emptyTestQueue.enqueue(0);
        Assert.assertFalse(emptyTestQueue.isEmpty());
        Assert.assertEquals(1, emptyTestQueue.getNumElements());
        Assert.assertEquals(0, emptyTestQueue.front().intValue());
        Assert.assertEquals(0, emptyTestQueue.rear().intValue());
        for (int idx = 1; idx < capacity; idx++) {
            Assert.assertFalse(emptyTestQueue.isEmpty());
            Assert.assertEquals(idx, emptyTestQueue.getNumElements());
            Assert.assertEquals(0, emptyTestQueue.front().intValue());
            Assert.assertEquals(idx - 1, emptyTestQueue.rear().intValue());
            emptyTestQueue.enqueue(idx);
            Assert.assertFalse(emptyTestQueue.isEmpty());
            Assert.assertEquals(idx + 1, emptyTestQueue.getNumElements());
            Assert.assertEquals(0, emptyTestQueue.front().intValue());
            Assert.assertEquals(idx, emptyTestQueue.rear().intValue());
        }
        Assert.assertTrue(emptyTestQueue.isFull());
        Assert.assertFalse(emptyTestQueue.isEmpty());
    }

    private void emptyQueue(Queue<Integer> testQueue) {
        int numElements = testQueue.getNumElements();
        for (int idx = 0; idx < numElements - 1; idx++) {
            Assert.assertEquals(numElements - 1 , testQueue.rear().intValue());
            Assert.assertEquals(idx, testQueue.front().intValue());
            Assert.assertEquals(idx, testQueue.dequeue().intValue());
            Assert.assertEquals(idx + 1, testQueue.front().intValue());
            Assert.assertEquals(numElements - 1 , testQueue.rear().intValue());
        }
        Assert.assertEquals(numElements - 1 , testQueue.rear().intValue());
        Assert.assertEquals(numElements - 1, testQueue.front().intValue());
        Assert.assertEquals(numElements - 1, testQueue.dequeue().intValue());
        Assert.assertSame(testQueue.front(), null);
        Assert.assertSame(testQueue.rear(), null);
        Assert.assertSame(testQueue.dequeue(), null);
        Assert.assertSame(testQueue.front(), null);
        Assert.assertSame(testQueue.rear(), null);
        Assert.assertTrue(testQueue.isEmpty());
        Assert.assertFalse(testQueue.isFull());
    }
}