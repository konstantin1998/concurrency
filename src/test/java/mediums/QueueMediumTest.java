package mediums;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.medium.QueueMedium;

import static org.junit.jupiter.api.Assertions.*;

public class QueueMediumTest {
    @Test
    public void mustWaitWhenExecutedSequentially() throws InterruptedException {
        //given
        int capacity = 10;
        QueueMedium queueMedium = new QueueMedium(capacity);
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
        //given
        int capacity = 10;
        QueueMedium medium = new QueueMedium(capacity);
        Thread thread = new Thread(() -> {
            try {
                medium.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //when
        thread.start();
        await();
        //then
        assertTrue(thread.isAlive());
    }

    private void await() {
        int time = 10;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mustWaitWhenTokenSlotIsOccupied() throws InterruptedException {
        //given
        int capacity = 1;
        Token token = new Token();

        QueueMedium medium = new QueueMedium(capacity);
        medium.push(token);
        Thread thread = new Thread(() -> {
            try {
                medium.push(token);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        //when
        await();
        //then
        assertTrue(thread.isAlive());
    }

}
