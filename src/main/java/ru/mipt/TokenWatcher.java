package ru.mipt;

import ru.mipt.medium.TokenMedium;



public class TokenWatcher implements Runnable{


    private int counter;

    private Token tokenToWatch = null;

    private final TokenMedium input;
    private final TokenMedium output;

    public TokenWatcher(TokenMedium input, TokenMedium output) {

        this.input = input;
        this.output = output;

    }

    @Override
    public void run() {
        while(true) {
            try {
                Token t = input.poll();
                consume(t);
                output.push(t);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void consume(Token t) {
        if (tokenToWatch == null) {
            tokenToWatch = t;
            counter ++;
            return;
        }
        if (tokenToWatch.equals(t)){
            counter ++;
        }
    }

    public int getCounter() {
        return counter;
    }
}
