import java.util.concurrent.Semaphore;

public class OneSemaphore {
    static class PingPong {
        private final Semaphore sem = new Semaphore(1);
        private int counter = 0;
        private boolean isPing = true;
        private final int maxIterations = 1_000_000;
        private final Thread pingThread;
        private final Thread pongThread;

        PingPong() {
            Runnable ping = () -> {
                while(counter < maxIterations) {
                    try {
                        sem.acquire();
                        if(isPing) {
                            //System.out.println("ping iteration number: " + counter);
                            counter++;
                            isPing = false;
                        }
                        sem.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Runnable pong = () -> {
                while(counter < maxIterations) {
                    try {
                        sem.acquire();
                        if(!isPing) {
                            //System.out.println("pong iteration number: " + counter);
                            counter++;
                            isPing = true;
                        }
                        sem.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread pingThread = new Thread(ping);
            Thread pongThread = new Thread(pong);
            this.pingThread = pingThread;
            this.pongThread = pongThread;
        }

        public void start() {
            pingThread.start();
            pongThread.start();
            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        pingPong.start();
        pingPong = new PingPong();
        long startTime = System.currentTimeMillis();
        pingPong.start();
        System.out.println(System.currentTimeMillis() - startTime);
    }

}
