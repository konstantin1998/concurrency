public class Synchronized {
    static class PingPong {
        static class State {
            private boolean isPing = true;

            boolean isPing() {
                return isPing;
            }

            void switchState(){
                isPing = !isPing;
            }
        }

        private final State state;

        private final int maxIterations;
        private final Thread pingThread;
        private final Thread pongThread;

        PingPong() {

            state = new State();
            maxIterations = 500_000;

            Runnable ping = () -> {
                int counter = 0;
                int maxIterations = this.maxIterations;
                while(counter <= maxIterations) {
                    synchronized (state) {
                        while(!state.isPing()) {
                            try {
                                state.wait();
                            } catch (InterruptedException ignored) {

                            }
                        }
                        counter++;
                        state.switchState();
                        state.notify();
                    }
                }
            };

            Runnable pong = () -> {
                int counter = 0;
                int maxIterations = this.maxIterations;
                while(counter <= maxIterations) {
                    synchronized (state) {
                        while(state.isPing()) {
                            try {
                                state.wait();
                            } catch (InterruptedException ignored) {

                            }

                        }
                        counter++;
                        state.switchState();
                        state.notify();
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

        for (int i = 0; i < 15; i++) {
            pingPong = new PingPong();
            long time = System.currentTimeMillis();
            pingPong.start();
            long newTime = System.currentTimeMillis();
            System.out.println(newTime - time);
        }
    }
}

