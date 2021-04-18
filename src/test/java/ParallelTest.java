import org.junit.Test;
import ru.mipt.boundedQueue.BoundedLockQueue;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelTest {

    @Test
    public void enqTest() {
        int times = 10;
        for(int i = 0; i < times; i++) {
            assertEquals(2, enqAndGetSize());
        }
    }

    @Test
    public void deqTest() {
        int times = 10;
        for(int i = 0; i < times; i++) {
            assertEquals(0, deqAndGetSize());
        }
    }

    private int enqAndGetSize() {
        BoundedLockQueue<Integer> queue = initQueue();
        Runnable enq = () -> {
            queue.enq(1);
        };
        runThreads(enq);
        return getSize(queue);
    }

    private void runThreads( Runnable runnable) {
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BoundedLockQueue<Integer> initQueue() {
        int capacity = 10;
        return new BoundedLockQueue<>(capacity);
    }

    private int getSize(BoundedLockQueue<Integer> queue) {
        int size = 0;
        try {
            Field field = queue.getClass().getDeclaredField("size");
            field.setAccessible(true);
            size = ((AtomicInteger) field.get(queue)).get();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return size;
    }

    private int deqAndGetSize() {
        BoundedLockQueue<Integer> queue = initQueue();
        queue.enq(1);
        queue.enq(1);
        Runnable deq = queue::deq;
        runThreads(deq);
        return getSize(queue);
    }

    @Test
    public void enqAndDeqTest() {
        int times = 10;
        for(int i = 0; i < times; i++) {
            BoundedLockQueue<Integer> queue = initQueue();
            Runnable enq = () -> {
                queue.enq(1);
            };
            Thread enqueuer1 = new Thread(enq);
            Thread enqueuer2 = new Thread(enq);

            Thread dequeuer1 = new Thread(queue::deq);
            Thread dequeuer2 = new Thread(queue::deq);

            enqueuer1.start();
            enqueuer2.start();
            dequeuer1.start();
            dequeuer2.start();
            try {
                enqueuer1.join();
                enqueuer2.join();
                dequeuer1.join();
                dequeuer2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(0, getSize(queue));
        }

    }
}
