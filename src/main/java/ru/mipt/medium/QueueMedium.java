package ru.mipt.medium;

import ru.mipt.Token;
import ru.mipt.medium.queue.ConcurrentQueue;

public class QueueMedium implements TokenMedium {
    private final ConcurrentQueue<Token> queue;

    public QueueMedium(ConcurrentQueue<Token> queue) {
        this.queue = queue;
    }

    @Override
    public void push(Token token) {
        queue.enq(token);
    }

    @Override
    public Token poll() {
        return queue.deq();
    }
}
