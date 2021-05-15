package ru.mipt.medium.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedLockQueue<T> implements ConcurrentQueue<T> {

    private final ReentrantLock enqLock = new ReentrantLock();
    private final ReentrantLock deqLock = new ReentrantLock();
    private final Condition notEmptyCondition = deqLock.newCondition();
    private final Condition notFullCondition = enqLock.newCondition();

    private final AtomicInteger size = new AtomicInteger();
    private final int capacity;
    private final List<T> items = new ArrayList<>();

    public BoundedLockQueue(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void enq(T item) throws InterruptedException {
        boolean mustWakeDequeuers = false;
        enqLock.lock();

        try {
            awaitForSlot();
            doEnq(item);
            if(size.getAndIncrement() == 0) {
                mustWakeDequeuers = true;
            }
        }  finally {
            enqLock.unlock();
        }

        if(mustWakeDequeuers) {
            signalDequeuers();
        }
    }

    private void doEnq(T item) {
        items.add(item);
    }

    private void signalDequeuers() {
        deqLock.lock();
        try {
            notEmptyCondition.signalAll();
        } finally {
            deqLock.unlock();
        }
    }

    private void awaitForSlot() throws InterruptedException{
        while(size.get() == capacity) {
            notFullCondition.await();
        }
    }

    @Override
    public T deq() throws InterruptedException {
        T result = null;
        boolean mustWakeEnqueuers = false;
        deqLock.lock();
        try {
            awaitForItems();
            result = doDeq();
            if(size.getAndDecrement() == capacity) {
                mustWakeEnqueuers = true;
            }

        }  finally {
            deqLock.unlock();
        }

        if(mustWakeEnqueuers) {
            signalEnqueuers();
        }
        return result;
    }

    private void signalEnqueuers() {
        enqLock.lock();
        try {
            notFullCondition.signalAll();
        } finally {
            enqLock.unlock();
        }
    }

    private T doDeq() {
        return items.remove(0);
    }

    private void awaitForItems() throws InterruptedException {
        while(size.get() == 0) {
            notEmptyCondition.await();
        }
    }

}
