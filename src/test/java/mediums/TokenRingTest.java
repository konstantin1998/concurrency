package mediums;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.TokenRing;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenRingTest {

    @Test
    public void test() throws InterruptedException {
        TokenRing tokenRing = getTokenRing();
        tokenRing.start();
        int timeToSleep = 1000;
        Thread.sleep(timeToSleep);
        tokenRing.stop();

        double throughput = tokenRing.getThroughput();
        double latency = tokenRing.getLatency();
        System.out.println("throughput: " + throughput);
        System.out.println("latency: " + latency);

        assertTrue(throughput > 0);
        assertTrue(latency > 0);
    }

    private TokenRing getTokenRing() {
        int nMediums = 4;
        List<TokenMedium> mediums = new ArrayList<>();
        for(int i = 0; i < nMediums; i++) {
            mediums.add(new FieldMedium());
        }

        TokenRing tokenRing = new TokenRing(mediums);
        int nTokens = 2;
        initializeWithTokens(tokenRing, nTokens);
        return tokenRing;
    }

    private void initializeWithTokens(TokenRing tokenRing, int nTokens) {
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < nTokens; i++) {
            tokens.add(new Token());
        }
        tokenRing.initializeWithTokens(tokens);
    }
}
