package ru.mipt.medium;

import ru.mipt.Token;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FieldMedium implements TokenMedium{
    private Token token;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition occupied = lock.newCondition();

    @Override
    public void push(Token token) throws InterruptedException {
        lock.lock();
        try {
            waitForSlot();
            this.token = token;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public Token poll() throws InterruptedException {
        Token t;
        lock.lock();
        try {
            waitForToken();
            t = token;
            token = null;
            occupied.signalAll();
        } finally {
            lock.unlock();
        }

        return t;
    }

    private void waitForSlot() throws InterruptedException {
        while(token != null) {
            occupied.await();
        }
    }

    private void waitForToken() throws InterruptedException {
        while(token == null) {

            notEmpty.await();
        }
    }
}
