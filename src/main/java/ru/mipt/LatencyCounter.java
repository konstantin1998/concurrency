package ru.mipt;

import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

public class LatencyCounter implements Runnable{
    private final List<Long> latencies;
    private int offset = 1000;
    private int counter;
    private final int maxCounter = 10_000;

    private final TokenMedium input;
    private final TokenMedium output;

    public LatencyCounter(TokenMedium input, TokenMedium output) {
        this.latencies = new ArrayList<>(maxCounter);
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
        counter ++;
        if(counter < offset || counter > maxCounter) {
            return;
        }
        latencies.add(System.currentTimeMillis() - t.getTimeStamp());
    }

    public double countLatency() {
        double avgLatency = 0;
        for(Long item: latencies) {
            long latency = item;
            avgLatency += latency;
        }

        return avgLatency / latencies.size();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
