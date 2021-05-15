package mediums;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.medium.FieldMedium;

import static org.junit.jupiter.api.Assertions.*;

public class FieldMediumTest {
    @Test
    public void mustWorkWhenExecutedInMainThread() throws InterruptedException {
        //given
        FieldMedium medium = new FieldMedium();
        Token actual;
        Token expected = new Token();
        //when
        medium.push(expected);
        actual = medium.poll();
        //then
        assertEquals(actual, expected);
    }

    @Test
    public void mustWaitWhenTokenFieldIsEmpty() {
        //given
        FieldMedium medium = new FieldMedium();
        Thread thread = new Thread(() -> {
            try {
                medium.poll();
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
        FieldMedium medium = new FieldMedium();
        Token token = new Token();
        medium.push(token);
        Thread thread = new Thread(() -> {
            try {
                medium.push(token);
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
}
