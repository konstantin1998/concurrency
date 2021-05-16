package counters;

import org.junit.Test;
import ru.mipt.Node;
import ru.mipt.ThroughputCounter;
import ru.mipt.Token;

import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;


import static org.junit.jupiter.api.Assertions.*;

public class ThroughPutCounterTest {
    @Test
    public void mustCountThroughPutCorrectly() throws InterruptedException {
        //given
        int capacity = 30;
        TokenMedium input = new QueueMedium(capacity);
        for(int i = 0; i < capacity; i++) {
            input.push(new Token());
        }
        TokenMedium output = new QueueMedium(capacity);
        int offset = 0;
        ThroughputCounter calculator = new ThroughputCounter(input, output);
        calculator.setOffset(offset);
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
