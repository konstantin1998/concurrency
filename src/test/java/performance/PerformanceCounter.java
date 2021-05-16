package performance;

import org.jetbrains.annotations.NotNull;
import ru.mipt.TokenRing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;


public class PerformanceCounter {

    private Performance countPerformance(TokenRing tokenRing) {
        int timeToWait = 1000;
        tokenRing.start();
        try {

            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stop();
        Performance p = new Performance();
        p.setLatency(tokenRing.getLatency());
        p.setThroughput(tokenRing.getThroughput());
        return p;
    }



    private Performance countAveragePerformance(int nNodes, double load, int capacity) {
        int nIterations = 5;
        List<Performance> scores = new ArrayList<>();
        TokenRingInitializer initializer = new TokenRingInitializer();

        for(int i = 0; i < nIterations; i++) {
            TokenRing tokenRing = initializer.getRing(nNodes, load, capacity);
            Performance p = countPerformance(tokenRing);
            scores.add(p);
        }

        return getAveragePerformance(nIterations, scores);
    }

    @NotNull
    private Performance getAveragePerformance(int nIterations, List<Performance> scores) {
        double averageThroughput = scores
                .stream()
                .map(Performance::getThroughput)
                .reduce((double) 0, Double::sum) / nIterations;
        double averageLatency = scores
                .stream()
                .map(Performance::getLatency)
                .reduce((double) 0, Double::sum) / nIterations;

        Performance p = new Performance();
        p.setThroughput(averageThroughput);
        p.setLatency(averageLatency);

        System.out.println("=================================");
        System.out.println("average throughput: " + averageThroughput);
        System.out.println("average latency: " + averageLatency);
        return p;
    }



    private void showResults(List<Performance> scores) {
        List<Double> throughputs = scores
                .stream()
                .map(Performance::getThroughput)
                .map((x) -> (double) Math.round(x))
                .collect(Collectors.toList());
        List<Double> latencies = scores
                .stream()
                .map(Performance::getLatency).map((x) -> {
                    double a = x * 1000;
                    return (double) (Math.round(a)) / 1000;
                })
                .collect(Collectors.toList());
        System.out.println(" ");
        System.out.println("throughputs: " + throughputs);
        System.out.println("latencies: " + latencies);
    }



    private void warmUpJvm() {
        int n = 4;
        double load = 0.5;
        TokenRingInitializer initializer = new TokenRingInitializer();
        TokenRing tokenRing = initializer.getRing(n, load);
        tokenRing.start();
        int timeToSleep = 500;
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stop();
    }

    @Test
    public void showQueueMediumResultsWhenLoadAndCapacityAreFixed() {
        warmUpJvm();
        double load = 0.5;
        int capacity = 20;
        List<Performance> scores = new ArrayList<>();
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            scores.add(countAveragePerformance(i, load, capacity));
        }

        showResults(scores);
    }

    @Test
    public void showQueueMediumResultsWhenNodesAndCapacityAreFixed() {
        warmUpJvm();

        int capacity = 20;
        List<Performance> scores = new ArrayList<>();
        int nIterations = 10;
        int nNodes = 4;
        for(int i = 1; i <= nIterations - 1; i++) {
            double load = (double) i / nIterations;
            scores.add(countAveragePerformance(nNodes, load, capacity));
        }

        showResults(scores);
    }

    @Test
    public void showQueueMediumResultsWhenNodesAndLoadAreFixed() {
        warmUpJvm();
        double load = 0.5;
        int nNodes = 8;
        List<Performance> scores = new ArrayList<>();
        int from = 10;
        int to = 100;
        int step = 10;
        for(int i = from; i <= to; i += step) {
            scores.add(countAveragePerformance(nNodes, load, i));
        }

        showResults(scores);
    }
}
