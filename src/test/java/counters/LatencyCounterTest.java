package counters;

import org.junit.Test;
import ru.mipt.LatencyCounter;
import ru.mipt.Node;
import ru.mipt.Token;

import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;
import ru.mipt.medium.queue.BoundedLockQueue;
import ru.mipt.medium.queue.ConcurrentQueue;

import static org.junit.jupiter.api.Assertions.*;

public class LatencyCounterTest {
    @Test
    public void mustCountThroughPutCorrectly() throws InterruptedException {
        //given
        int capacity = 50;
        //ConcurrentQueue<Token> inputQueue = new BoundedLockQueue<>(capacity);
        TokenMedium input = new QueueMedium(capacity);
        for(int i = 0; i < capacity; i++) {
            Token token = new Token();
            token.setTime(System.currentTimeMillis());
            input.push(token);
        }

        //ConcurrentQueue<Token> outputQueue = new BoundedLockQueue<>(capacity);
        TokenMedium output = new QueueMedium(capacity);


        int offset = 0;
        //ThroughPutCalculator calculator = new ThroughPutCalculator(input, output, offset);
        LatencyCounter calculator = new LatencyCounter(input, output, offset);
        //when
        Node node = new Node(calculator);
        node.start();
        Thread.sleep(1000);
        node.stop();

        double bigLatency = 50;
        double latency = calculator.countLatency();
        //then
        assertTrue(latency < bigLatency);
    }
}
