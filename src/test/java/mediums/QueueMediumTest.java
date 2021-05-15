package mediums;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.queue.BoundedLockQueue;
import ru.mipt.medium.queue.ConcurrentQueue;

import static org.junit.jupiter.api.Assertions.*;

public class QueueMediumTest {
    @Test
    public void mustWaitWhenExecutedSequentially() {
        //given
        int capacity = 10;
        ConcurrentQueue<Token> queue = new BoundedLockQueue<>(capacity);
        QueueMedium queueMedium = new QueueMedium(queue);
        Token actual;
        Token expected = new Token();
        //when
        queueMedium.push(expected);
        actual = queueMedium.poll();
        //then
        assertEquals(actual, expected);
    }

    @Test
    public void mustWaitWhenQueueIsEmpty() {
        int capacity = 10;
        ConcurrentQueue<Token> queue = new BoundedLockQueue<>(capacity);
        QueueMedium queueMedium = new QueueMedium(queue);
        assertDoesNotThrow(() -> ThreadRunner.makeThreadsWaitForPoll(queueMedium));
    }

    @Test
    public void mustWaitWhenTokenSlotIsOccupied() {
        int capacity = 1;
        ConcurrentQueue<Token> queue = new BoundedLockQueue<>(capacity);
        Token token = new Token();
        queue.enq(token);
        QueueMedium queueMedium = new QueueMedium(queue);
        assertDoesNotThrow(() -> ThreadRunner.makeThreadsWaitForPush(queueMedium));
    }

}
