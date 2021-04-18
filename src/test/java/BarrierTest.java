import org.junit.Test;
import ru.mipt.barriers.Barrier;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicIntegerArray;
import static org.junit.jupiter.api.Assertions.*;

public class BarrierTest {
    @Test
    public void test() {
        int n = 8;
        Barrier barrier = new Barrier(n);
        barrier.run();
        AtomicIntegerArray queue = getQueue(barrier);
        for(int i = 1; i < n; i++) {
            assertEquals(queue.get(i), 2);
        }

    }

    private AtomicIntegerArray getQueue(Barrier barrier){
        AtomicIntegerArray result = null;
        try {
            Field field = barrier.getClass().getDeclaredField("queue");
            field.setAccessible(true);
            result = (AtomicIntegerArray) field.get(barrier);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }
}
