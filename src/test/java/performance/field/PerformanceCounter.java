package performance.field;

import performance.Performance;
import performance.TokenRingInitializer;
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

    private Performance countAveragePerformance(int nNodes, int load) {
        int nIterations = 5;
        List<Performance> scores = new ArrayList<>();

        TokenRingInitializer initializer = new TokenRingInitializer();

        for(int i = 0; i < nIterations; i++) {
            TokenRing tokenRing = initializer.getRing(nNodes, load);
            Performance p = countPerformance(tokenRing);
            scores.add(p);
        }

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

    @Test
    public void showFieldMediumPerformanceWhenLoadIsFixed() {
        warmUpJvm();
        int load = 2;
        List<Performance> scores = new ArrayList<>();
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            scores.add(countAveragePerformance(i, load));
        }

        showResults(scores);
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

    @Test
    public void showFieldMediumPerformanceWhenNumberOfNodesIsFixed() {
        warmUpJvm();
        int nNodes = 16;

        List<Performance> scores = new ArrayList<>();
        int maxLoad = 15;
        for(int i = 1; i <= maxLoad; i++) {
            scores.add(countAveragePerformance(nNodes, i));
        }

        showResults(scores);
    }

    private void warmUpJvm() {
        int n = 4;
        int load = 2;
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

}
