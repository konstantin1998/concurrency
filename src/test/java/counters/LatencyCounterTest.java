package counters;

import org.junit.Test;
import ru.mipt.LatencyCounter;
import ru.mipt.Node;
import ru.mipt.Token;

import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;


import static org.junit.jupiter.api.Assertions.*;

public class LatencyCounterTest {
    @Test
    public void mustCountThroughPutCorrectly() throws InterruptedException {
        //given
        int capacity = 50;
        TokenMedium input = new QueueMedium(capacity);
        for(int i = 0; i < capacity; i++) {
            Token token = new Token();
            token.setTimeStamp(System.currentTimeMillis());
            input.push(token);
        }

        TokenMedium output = new QueueMedium(capacity);


        int offset = 0;
        LatencyCounter calculator = new LatencyCounter(input, output);
        calculator.setOffset(offset);
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
