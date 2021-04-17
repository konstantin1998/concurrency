import java.util.concurrent.Semaphore;



public class Concurrent {
    enum Position {
        Ping,
        Pong
    }

    private static final Semaphore pingSem = new Semaphore(1);
    private static final Semaphore pongSem = new Semaphore(0);
    private static final int maxIterations = 500_000;
    private static Thread pingThread;
    private static Thread pongThread;

    private static class PingPongRunnable implements Runnable {
        private long count;
        private final long countBound;
        private final Position position;
        private final Semaphore ownSem;
        private final Semaphore foreignSem;

        public PingPongRunnable(Position position, long countBound, Semaphore ownSem, Semaphore foreignSem) {
            this.countBound = countBound;
            this.position = position;
            this.ownSem = ownSem;
            this.foreignSem = foreignSem;
        }

        @Override
        public void run() {
            while (count <= countBound) {
                try {
                    ownSem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    count++;
                    foreignSem.release();
                }
            }
        }
    }

    private static void start() {
        pingThread.start();
        pongThread.start();
        try {
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        pingThread = new Thread(new PingPongRunnable(Position.Ping, maxIterations, pingSem, pongSem));
        pongThread = new Thread(new PingPongRunnable(Position.Pong, maxIterations, pongSem, pingSem));

        start();
        for (int i = 0; i < 15; i++) {
            pingThread = new Thread(new PingPongRunnable(Position.Ping, maxIterations, pingSem, pongSem));
            pongThread = new Thread(new PingPongRunnable(Position.Pong, maxIterations, pongSem, pingSem));
            long time = System.currentTimeMillis();
            start();
            long newTime = System.currentTimeMillis();
            System.out.println(newTime - time);
        }

    }

}
