package ru.mipt;

public interface Queue<T> {
    void enq(T item);
    T deq();
}
