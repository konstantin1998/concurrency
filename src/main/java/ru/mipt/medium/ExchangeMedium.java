package ru.mipt.medium;

import ru.mipt.Token;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangeMedium implements TokenMedium{
    private final Exchanger<Token> ex;
    private Token token;

    public ExchangeMedium() {
        ex = new Exchanger<>();
    }
    @Override
    public void push(Token token) throws InterruptedException {
        if (this.token == null) {
            this.token = token;
            return;
        }
        this.token = token;
        ex.exchange(token);
    }

    @Override
    public Token poll() throws InterruptedException {
        Token t;
        int timeout = 1;
        try {
            t = ex.exchange(token, timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            if (token != null) {
                new Thread(() -> {
                    try {
                        ex.exchange(token);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }).start();
            }
            t = ex.exchange(token);

        }
        return t;
    }
}
