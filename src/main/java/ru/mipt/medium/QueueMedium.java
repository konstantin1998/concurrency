package ru.mipt.medium;

import ru.mipt.Token;
import ru.mipt.medium.queue.ConcurrentQueue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueMedium implements TokenMedium {
//    private final ConcurrentQueue<Token> queue;
//
//    public QueueMedium(ConcurrentQueue<Token> queue) {
//        this.queue = queue;
//    }
//
//    @Override
//    public void push(Token token) throws InterruptedException {
//        queue.enq(token);
//    }
//
//    @Override
//    public Token poll() throws InterruptedException {
//        return queue.deq();
//    }
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
