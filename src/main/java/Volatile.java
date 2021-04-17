public class Volatile {
    static class PingPong {
        private volatile boolean isPing;
        //private int counter;
        private final int maxIterations;
        private final Thread pingThread;
        private final Thread pongThread;

        PingPong() {
            isPing = true;

            maxIterations = 500_000;

            Runnable ping = () -> {
                int counter = 0;
                int maxIterations = this.maxIterations;
                while(counter <= maxIterations) {
                    if(isPing) {
                        counter++;
                        isPing = false;
                    }
                }

            };

            Runnable pong = () -> {
                int counter = 0;
                int maxIterations = this.maxIterations;
                while(counter <= maxIterations) {
                    if(!isPing) {
                        counter++;
                        isPing = true;

                    }
                }

            };
            pingThread = new Thread(ping);
            pongThread = new Thread(pong);
        }


        public void start() {
            pingThread.start();
            pongThread.start();
            try {
                pingThread.join();
                pongThread.join();
            } catch (InterruptedException ignored) { }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        pingPong.start();

        for (int i = 0; i <= 11; i++) {
            pingPong = new PingPong();
            long time = System.currentTimeMillis();
            pingPong.start();
            long newTime = System.currentTimeMillis();
            System.out.println(newTime - time);
        }

    }
}
