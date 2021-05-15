package ru.mipt.medium.queue;

public interface ConcurrentQueue<T> {
    void enq(T item) throws InterruptedException;
    T deq() throws InterruptedException;
}
