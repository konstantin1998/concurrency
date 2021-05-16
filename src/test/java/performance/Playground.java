package performance;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.TokenRing;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Playground {
    @Test
    public void test() {
        TokenRingInitializer initializer = new TokenRingInitializer();
        TokenRing ring = initializer.getRing(4, 0.5);
        System.out.println(ring);
    }

}
