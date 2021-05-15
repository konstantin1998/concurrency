package mediums;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.medium.FieldMedium;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        FieldMedium medium = new FieldMedium();
        assertDoesNotThrow(() -> ThreadRunner.makeThreadsWaitForPoll(medium));
    }

    @Test
    public void mustWaitWhenTokenSlotIsOccupied() throws InterruptedException {
        FieldMedium medium = new FieldMedium();
        Token token = new Token();
        medium.push(token);
        assertDoesNotThrow(() -> ThreadRunner.makeThreadsWaitForPush(medium));
    }
}
