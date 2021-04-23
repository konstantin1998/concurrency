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

        while(true) {
            if(!isFull()) {
                int n = tail.get();
                if(tail.compareAndSet(n, n + 1)) {
                    items.set(n % maxSize, item);
                    return;
                }
            }
        }
    }

    private void waitForSlots() {
        while(tail.get() - head.get() >= maxSize) { }
    }

    @Override
    public T deq() {
        while(true) {
            if(!isEmpty()) {
                int n = head.get();
                if(head.compareAndSet(n, n + 1)) {
                    return items.get(n % maxSize);
                }
            }
        }
    }

    private boolean isEmpty() {
        return tail.get() - head.get() <= 0;
    }

    private boolean isFull() {
        return tail.get() - head.get() >= maxSize;
    }

    private void waitForItems() {
        while(tail.get() - head.get() <= 0) { }
    }
}
