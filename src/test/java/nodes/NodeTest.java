package nodes;

import org.junit.Test;
import ru.mipt.Node;
import ru.mipt.Token;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.TokenMedium;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    @Test
    public void shouldStopWithoutDeadlocks() throws InterruptedException {
        TokenMedium input = new FieldMedium();
        input.push(new Token());
        TokenMedium output = new FieldMedium();
        Node node = new Node(input, output);
        node.start();
        Thread.sleep(10);
        assertTrue(node.isAlive());
        node.stop();
        assertFalse(node.isAlive());
    }


}
