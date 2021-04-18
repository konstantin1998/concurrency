package ru.mipt.barriers;

import java.util.ArrayList;
import java.util.List;

public class Barrier {

    private final List<Thread> threads;
    private volatile List<Integer> queue;

    public Barrier(int n) {
        queue = new ArrayList<>();
        for(int i = 0; i < n + 2; i++) {
            queue.add(0);
        }

        threads = new ArrayList<>();
        Thread firstThread = new Thread(() -> {
            queue.set(0, 1);
        });

        threads.add(firstThread);

        for(int i = 1; i <= n; i++) {
            int a  = i;
            Runnable runnable = () -> {
                while(queue.get(a - 1) != 1) { }
                queue.set(a, 1);

                while(queue.get(a + 1) != 2) { }
                queue.set(a, 2);
            };

            Thread thread = new Thread(runnable);
            threads.add(thread);
        }


        Thread lastThread = new Thread(() -> {
            queue.set(n + 1, 2);
        });
        threads.add(lastThread);
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
