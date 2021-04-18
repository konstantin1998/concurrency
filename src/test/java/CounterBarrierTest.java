import org.junit.Test;
import ru.mipt.barriers.CounterBarrier;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class CounterBarrierTest {
    @Test
    public void test() {
        int numThreads = 3;
        CounterBarrier barrier = new CounterBarrier(numThreads);
        barrier.run();
        assertEquals(numThreads, getCounter(barrier));
    }

    private int getCounter(CounterBarrier barrier){
        int counter = 0;
        try {
            Field field = barrier.getClass().getDeclaredField("counter");
            field.setAccessible(true);
            counter =  field.getInt(barrier);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return counter;
    }
}
