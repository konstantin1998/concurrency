package mediums;

import org.junit.Test;
import ru.mipt.Node;
import ru.mipt.ThroughputCounter;
import ru.mipt.Token;

import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;
import ru.mipt.medium.queue.BoundedLockQueue;
import ru.mipt.medium.queue.ConcurrentQueue;

import static org.junit.jupiter.api.Assertions.*;

public class ThroughPutCounterTest {
    @Test
    public void mustCountThroughPutCorrectly() throws InterruptedException {
        //given
        int capacity = 30;
        ConcurrentQueue<Token> inputQueue = new BoundedLockQueue<>(capacity);
        TokenMedium input = new QueueMedium(inputQueue);
        for(int i = 0; i < capacity; i++) {
            input.push(new Token());
        }

        ConcurrentQueue<Token> outputQueue = new BoundedLockQueue<>(capacity);
        TokenMedium output = new QueueMedium(outputQueue);


        int offset = 0;
        ThroughputCounter calculator = new ThroughputCounter(input, output, offset);
        //when
        Node node = new Node(calculator);
        node.start();
        Thread.sleep(1000);
        input.push(new Token());
        node.stop();

        double expectedThroughput = 31;
        double actualThroughput = calculator.countThroughput();
        double eps = 1;
        //then
        assertTrue(Math.abs(expectedThroughput - actualThroughput) < eps);
    }
}
