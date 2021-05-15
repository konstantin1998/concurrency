package ru.mipt.medium.queue;

public interface ConcurrentQueue<T> {
    void enq(T item);
    T deq();
}
