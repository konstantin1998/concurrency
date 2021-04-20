package ru.mipt.barriers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Barrier {

    private final List<Thread> threads;
    private final AtomicIntegerArray queue;

    public Barrier(int n) {
        if(n < 2) {
            throw new RuntimeException("number of threads must be at least 2");
        }

        queue = new AtomicIntegerArray(n);

        threads = new ArrayList<>();
        Thread firstThread = new Thread(() -> {
            queue.set(0, 1);
        });

        threads.add(firstThread);

        for(int i = 1; i <= n - 2; i++) {
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
            queue.set(n - 1, 2);
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

/*
При работе каждого из барьеров есть моменты, когда кэш потоков инвалидируется и нужно его обновлять. Допустим каждый
барьер запускается в три потока. Посчитаем сколько инвалдаций кэшей будет в каждом барьере. Начнем с CounterBarrier.
Каждый захват лока заставляет 2 потока(все потоки, кроме того, который захватил лок) инвалидировать свой кэш, метод
захвата лока вызывается 3 раза, это дает 3 * 2 = 6 инвалидаций кэшей, аналогично отпускание лока дает 6 инвалидаций
кэшей. Также каждый поток увеличивает volatile переменную counter, это тоже вызывает инвалидацию кешей. Аналогично
захвату лока это дает 6 инвалидаций кэшей. Итого суммарное количество инвалидаций кэшей: 6(захват лока) +
6(увеличение счетчика) + 6(отпускание лока) = 18. Теперь рассмотрим случай барьера, основанного на массиве. При запуске
на 3 потоках этот барьер будет использовать AtomicIntegerArray из 3 элементов. Когда один из потоков записывает единицу
в свою ячейку, 2 других потока инвалидируют свой кеш. При этом за время работы единица записывается в я чейку массива 3
раза, что дает 6 инвалидаций кэшей. Аналогично получаем 6 инвалидаций, при записывании двойки в массив. Итого в сумме
получаем 12 инвалидаций кэшей. Это меньше, чем в случае с CounterBarrier, поэтому при малом числе потоков барьер,
основанный на массиве, будет работать быстрее. Теперь допустим у нас есть много потоков с id = 0...n. В каждый момент
времени может выполняться только несколько потоков, поскольку количество ядер ограничено. В случае с барьером,
основанным на массиве, операционная система забросит на ядра несколько потоков и, в общем случае, им придется ждать
пока потоки с меньшим id не положат в свои ячейки единицы или двойки. Время ожидания в общем случае может быть достаточно
долгим. В то время как CounterBarrier не имеет этих недостатков и, когда операционная система забросит несколько потоков
на ядро, один из них гарантированно начнет работу, не будет такого, что все потоки сидят на ядре и ждут.
* */