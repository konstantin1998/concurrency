package nodes;

import org.junit.Test;
import ru.mipt.Token;
import ru.mipt.TokenRing;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;
import ru.mipt.medium.queue.BoundedLockQueue;
import ru.mipt.medium.queue.ConcurrentQueue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenRingTest {

    @Test
    public void mustGiveValidResultsWhenFieldMediumIsUsed() throws InterruptedException {
        int nMediums = 8;
        TokenRing tokenRing = getTokenRing(nMediums);
        runTokenRing(tokenRing);
    }

    @Test
    public void mustGiveValidResultsWhenQueueMediumIsUsed() throws InterruptedException {
        int nMediums = 4;
        int capacity = 5;
        TokenRing tokenRing = getTokenRing(nMediums, capacity);
        runTokenRing(tokenRing);
    }

    private TokenRing getTokenRing(int nMediums) {

        List<TokenMedium> mediums = new ArrayList<>();
        for(int i = 0; i < nMediums; i++) {
            mediums.add(new FieldMedium());
        }

        TokenRing tokenRing = new TokenRing(mediums);
        int nTokens = 2;
        initializeWithTokens(tokenRing, nTokens);
        return tokenRing;
    }

    private TokenRing getTokenRing(int nMediums, int capacity) {

        List<TokenMedium> mediums = new ArrayList<>();
        for(int i = 0; i < nMediums; i++) {
            //ConcurrentQueue<Token> queue = new BoundedLockQueue<>(capacity);
            mediums.add(new QueueMedium(capacity));
        }

        TokenRing tokenRing = new TokenRing(mediums);
        int nTokens = capacity;
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


    private void runTokenRing(TokenRing tokenRing) throws InterruptedException {
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
}
