package ru.mipt.boundedQueue;

public interface Queue<T> {
    void enq(T item);
    T deq();
}
