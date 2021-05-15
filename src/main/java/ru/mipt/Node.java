package ru.mipt;

import ru.mipt.medium.TokenMedium;

public class Node {
    private final TokenMedium input;
    private final TokenMedium output;
    private Thread thread;


    public Node(TokenMedium input, TokenMedium output) {
        this.input = input;
        this.output = output;
    }

    public void run() {
        thread = new Thread(() -> {
            while(!Thread.interrupted()) {
                Token t = input.poll();
                output.push(t);
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}
