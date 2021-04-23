package queue;

import org.junit.Test;
import ru.mipt.boundedQueue.BoundedLockFreeQueue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LockFreeQueueTest {
    int numberOfCheckIterations = 10;
    int maxQueueSize = 20;
    List<Integer> expectedItems = Arrays.asList(
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            null, null, null, null, null, null, null, null, null, null);
    int numberOfThreads = 10;

    @Test
    public void itemsMustBeCorrectWhenManyThreadsTryToEnqueue() {

        for(int i = 0; i < numberOfCheckIterations; i++) {
            BoundedLockFreeQueue<Integer> queue = new BoundedLockFreeQueue<>(maxQueueSize);
            Runnable enq = () -> {
                int num = 1;
                queue.enq(num);
            };
            runThreads(enq);
            assertTrue(checkItems(expectedItems, getItems(queue)));
        }

    }

    @Test
    public void itemsMustBeCorrectWhenManyThreadsTryToDeque() {

        for(int i = 0; i < numberOfCheckIterations; i++) {
            BoundedLockFreeQueue<Integer> queue = new BoundedLockFreeQueue<>(maxQueueSize);
            for (int j = 0; j < maxQueueSize; j++) {
                queue.enq(1);
            }
            Runnable deq = queue::deq;
            runThreads(deq);
            int expectedNumberOfNotNullElements = 10;
            assertTrue(checkItems(expectedItems, getItems(queue), expectedNumberOfNotNullElements));
        }

    }

    private boolean checkItems(List<Integer> expected, List<Integer> actual) {
        if(expected.size() != actual.size()) {
            return false;
        }
        for(int i = 0; i <= expected.size() - 1; i++) {
            if(expected.get(i) == null) {
                if(actual.get(i) != null) {
                    return false;
                }
                continue;
            }
            if(!expected.get(i).equals(actual.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkItems(List<Integer> expected, List<Integer> actual, int n) {
        return checkItems(expected.subList(0, n), actual.subList(0, n));
    }

    private void runThreads(Runnable runnable) {
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        try {
            for(Thread thread: threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getItems(BoundedLockFreeQueue<Integer> queue) {
        List<Integer> items = null;

        try {
            Field elements  = queue.getClass().getDeclaredField("items");
            elements.setAccessible(true);
            items =  (List<Integer>) elements.get(queue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return items;
    }
}
