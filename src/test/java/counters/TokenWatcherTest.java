package counters;

import org.junit.Test;
import ru.mipt.TokenWatcher;
import ru.mipt.Node;
import ru.mipt.Token;

import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;


import static org.junit.jupiter.api.Assertions.*;

public class TokenWatcherTest {
    @Test
    public void mustCountTokensCorrectly() throws InterruptedException {
        //given
        int capacity = 50;
        Token token = new Token();

        TokenMedium input = new QueueMedium(capacity);
        for(int i = 0; i < capacity; i++) {
            input.push(token);
        }

        TokenMedium output = new QueueMedium(capacity);
        TokenWatcher calculator = new TokenWatcher(input, output);

        //when
        Node node = new Node(calculator);
        node.start();
        Thread.sleep(1000);
        node.stop();

        int tokenCount = calculator.getCounter();
        //then
        assertEquals(capacity, tokenCount);
    }
}
