import org.junit.Test;
import ru.mipt.barriers.Barrier;
import ru.mipt.barriers.CounterBarrier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BarrierTest {
    @Test
    public void test() {
        int n = 8;
        Barrier barrier = new Barrier(n);
        barrier.run();
        List<Integer> queue = getQueue(barrier);
        for(int i = 2; i < n + 2; i++) {
            assertEquals(queue.get(i), 2);
        }

    }

    private List<Integer> getQueue(Barrier barrier){
        List<Integer> result = new ArrayList<>();
        try {
            Field field = barrier.getClass().getDeclaredField("queue");
            field.setAccessible(true);
            result = (List<Integer>) field.get(barrier);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }
}
