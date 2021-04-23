package ru.mipt.boundedQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockFreeQueue<T> implements Queue<T> {
    private final AtomicInteger head;
    private final AtomicInteger tail;
    private final List<T> items;
    private final int maxSize;

    public BoundedLockFreeQueue(int maxSize) {
        head = new AtomicInteger(0);
        tail = new AtomicInteger(0);
        this.maxSize = maxSize;
        items = new ArrayList<>();
        for(int i = 0; i < maxSize; i++) {
            items.add(null);
        }
    }

    @Override
    public void enq(T item) {
        waitForSlots();
        int n = tail.getAndIncrement();
        items.set(n % maxSize, item);
    }

    private void waitForSlots() {
        while(tail.get() - head.get() >= maxSize) { }
    }

    @Override
    public T deq() {
        waitForItems();
        int n = head.getAndIncrement();
        return items.get(n % maxSize);
    }

    private void waitForItems() {
        while(tail.get() - head.get() <= 0) { }
    }
}
