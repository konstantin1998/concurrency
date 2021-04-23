package queue;

import org.junit.Test;
import ru.mipt.boundedQueue.BoundedLockFreeQueue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class LockFreeQueueTest {
    @Test
    public void enqueueProperlyWhenManyThreadsTryToEnqueue() {
        int n = 10;
        int maxQueueSize = 10;
        int expectedQueueSize = 10;
        for(int i = 0; i < n; i++) {
            BoundedLockFreeQueue<Integer> queue = new BoundedLockFreeQueue<>(maxQueueSize);
            Runnable enq = () -> {
                int num = 1;
                queue.enq(num);
            };
            runThreads(enq);
            int size = getSize(queue);
            assertEquals(expectedQueueSize, size);
        }
    }

    private void runThreads(Runnable runnable) {
        List<Thread> threads = new ArrayList<>();
        int n = 10;

        for(int i = 0; i < n; i++) {
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

    private int getSize(BoundedLockFreeQueue<Integer> queue) {
        int size = 0;
        try {
            Field head = queue.getClass().getDeclaredField("head");
            head.setAccessible(true);

            Field tail = queue.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            size = ((AtomicInteger) tail.get(queue)).get() - ((AtomicInteger) head.get(queue)).get();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return size;
    }

    private Integer[] getItems(BoundedLockFreeQueue<Integer> queue) {
        Integer[] items = null;
        try {
            Field  = queue.getClass().getDeclaredField("head");
            .setAccessible(true);

            Field tail = queue.getClass().getDeclaredField("tail");
            tail.setAccessible(true);
            size = ((AtomicInteger) tail.get(queue)).get() - ((AtomicInteger) .get(queue)).get();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
