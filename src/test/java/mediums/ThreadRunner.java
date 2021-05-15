package mediums;

import ru.mipt.Token;
import ru.mipt.medium.TokenMedium;

public class ThreadRunner {
    private static int timeToWait = 100;
    private static int nIterations = 10;

    public static void makeThreadsWaitForPoll(TokenMedium medium) {
        int n = nIterations;
        for(int i = 0; i < n; i++) {
            Token token = new Token();
            Thread t1 = new Thread(() -> {
                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                medium.push(token);
            });
            Thread t2= new Thread(medium::poll);
            t2.start();
            t1.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void makeThreadsWaitForPush(TokenMedium medium) {
        int n = nIterations;
        for(int i = 0; i < n; i++) {
            Token token = new Token();
            Thread t1 = new Thread(() -> {
                medium.push(token);
            });
            Thread t2= new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                medium.poll();
            });
            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
