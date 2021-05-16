package performance;

import ru.mipt.Token;
import ru.mipt.TokenRing;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.QueueMedium;
import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

public class TokenRingInitializer {

    public TokenRing getRing(int n, double load) {
        List<TokenMedium> mediums = getMediums(n);
        TokenRing tokenRing = new TokenRing(mediums);
        initializeWithTokens(tokenRing, (int) (load * (mediums.size() - 1)));
        return tokenRing;
    }

    public TokenRing getRing(int n, double load, int capacity) {
        List<TokenMedium> mediums = getMediums(n,capacity);
        TokenRing tokenRing = new TokenRing(mediums);
        initializeWithTokens(tokenRing, (int) (load * (mediums.size() * capacity - 1)));
        return tokenRing;
    }

    private List<TokenMedium> getMediums(int n) {
        List<TokenMedium> mediums = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            mediums.add(new FieldMedium());
        }
        return mediums;
    }

    private List<TokenMedium> getMediums(int n, int capacity) {
        List<TokenMedium> mediums = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            mediums.add(new QueueMedium(capacity));
        }
        return mediums;
    }

    private void initializeWithTokens(TokenRing tokenRing, int nTokens) {
        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < nTokens; i++) {
            tokens.add(new Token());
        }
        tokenRing.initializeWithTokens(tokens);
    }
}
