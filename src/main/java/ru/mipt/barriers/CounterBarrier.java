package ru.mipt.barriers;

import java.util.ArrayList;
import java.util.List;

public class CounterBarrier {
    private volatile int counter = 0;
    private final int n;
    TTASLock lock = new TTASLock();
    List<Thread> threads = new ArrayList<>();
    public CounterBarrier(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            threads.add(new Thread(() -> {
                lock.lock();
                try {
                    counter ++;
                } finally {
                    lock.unlock();
                }

                while(counter != n) { }
            }));
        }
    }

    public void run() {
        for(Thread thread: threads) {
            thread.start();
        }

        try {
            for(Thread thread: threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
