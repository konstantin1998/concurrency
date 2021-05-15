package ru.mipt;

import ru.mipt.medium.TokenMedium;

public class Node {
    private final TokenMedium input;
    private final TokenMedium output;
    private final Thread thread;


    public Node(TokenMedium input, TokenMedium output) {
        this.input = input;
        this.output = output;
        thread = new Thread(() -> {
            while(true) {
                try {
                    Token t = input.poll();
                    output.push(t);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    public Node(TokenMedium input, TokenMedium output, Runnable task) {
        this.input = input;
        this.output = output;
        thread = new Thread(task);
    }

    public void run() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return thread.isAlive();
    }
}
