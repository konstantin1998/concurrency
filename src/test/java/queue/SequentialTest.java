package queue;

import org.junit.Test;
import ru.mipt.boundedQueue.BoundedLockQueue;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SequentialTest {
    @Test
    public void enqTest() {

        BoundedLockQueue<Integer> queue = initQueue();

        assertEquals(1, getSize(queue));
    }

    @Test
    public void deqTest() {
        BoundedLockQueue<Integer> queue = initQueue();
        queue.deq();
        assertEquals(0, getSize(queue));
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

    private BoundedLockQueue<Integer> initQueue() {
        int capacity = 10;
        BoundedLockQueue<Integer> queue = new BoundedLockQueue<>(capacity);
        queue.enq(1);
        return queue;
    }
}
