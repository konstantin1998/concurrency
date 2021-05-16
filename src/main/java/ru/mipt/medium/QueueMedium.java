package ru.mipt.medium;

import ru.mipt.Token;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueMedium implements TokenMedium {

    private final BlockingQueue<Token> queue;
    public QueueMedium(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }
    @Override
    public void push(Token token) throws InterruptedException {
        queue.put(token);
    }

    @Override
    public Token poll() throws InterruptedException {
        return queue.take();
    }
}
