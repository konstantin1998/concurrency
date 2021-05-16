package ru.mipt;

import ru.mipt.medium.TokenMedium;

public class ThroughputCounter implements Runnable{
    private final int offset;
    private int counter;
    private long startTime;
    private long currentTime;
    private final TokenMedium input;
    private final TokenMedium output;

    public ThroughputCounter(TokenMedium input, TokenMedium output, int offset) {
        this.input = input;
        this.output = output;
        this.offset = offset;
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
        long currTime = System.currentTimeMillis();
        t.setTimeStamp(currTime);

        if (counter == offset) {
            startTime = currTime;
        }
        counter++;
        currentTime = currTime;
    }

    public double countThroughput() {
        if(offset > counter) {
            throw new RuntimeException("there were too few iterations, unable to count throughput");
        }
        return (counter - offset) / ((double)(currentTime - startTime) / 1000);
    }
}
