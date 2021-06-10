package performance;

import ru.mipt.TokenRing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceCounterUtils {
    private static String throughputsPath = "src/test/resources/throughputs.csv";
    private static String latenciesPath = "src/test/resources/latencies.csv";

    public static Performance countAveragePerformance(int nNodes, int load, int capacity) {
        int nIterations = 5;
        List<Performance> scores = new ArrayList<>();
        TokenRingInitializer initializer = new TokenRingInitializer();

        for(int i = 0; i < nIterations; i++) {
            TokenRing tokenRing = initializer.getRing(nNodes, load, capacity);
            Performance p = PerformanceCounterUtils.countPerformance(tokenRing);
            scores.add(p);
        }

        return PerformanceCounterUtils.getAveragePerformance(nIterations, scores);
    }

    public static void countAndSavePerformance(int nNodes, int load, int capacity) {
        List<Performance> scores = countScores(nNodes, load, capacity);
        saveScores(scores);
    }

    private static List<Performance> countScores(int nNodes, int load, int capacity) {
        int nIterations = 5;
        List<Performance> scores = new ArrayList<>();
        TokenRingInitializer initializer = new TokenRingInitializer();

        for(int i = 0; i < nIterations; i++) {
            TokenRing tokenRing = initializer.getRing(nNodes, load, capacity);
            Performance p = PerformanceCounterUtils.countPerformance(tokenRing);
            scores.add(p);
        }

        return scores;
    }

    private static void saveScores(List<Performance> scores) {
        try {
            FileWriter throughputsWriter = new FileWriter(throughputsPath, true);
            FileWriter latencyWriter = new FileWriter(latenciesPath, true);

            List<Performance> latencies = List.of(
                    scores.stream().min((p1, p2) -> (int) Math.signum(p1.getLatency() - p2.getLatency())).get(),
                    scores.get(0),
                    scores.get(scores.size() - 1),
                    scores.stream().max((p1, p2) -> (int) Math.signum(p1.getLatency() - p2.getLatency())).get());

            List<Performance> throughtputs = List.of(
                    scores.stream().min((p1, p2) -> (int) (p1.getThroughput() - p2.getThroughput())).get(),
                    scores.get(0),
                    scores.get(scores.size() - 1),
                    scores.stream().max((p1, p2) -> (int) (p1.getThroughput() - p2.getThroughput())).get());

            String throughputRecords = throughtputs
                    .stream()
                    .map(Performance::getThroughput)
                    .map(num -> Double.toString(num))
                    .collect(Collectors.joining(",")) + '\n';

            String latencyRecords = latencies
                    .stream()
                    .map(Performance::getLatency)
                    .map(num -> Double.toString(num))
                    .collect(Collectors.joining(",")) + '\n';

            throughputsWriter.write(throughputRecords);
            throughputsWriter.flush();

            latencyWriter.write(latencyRecords);
            latencyWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeSavedResults() {
        String head = "low,open,close,high" + '\n';

        FileWriter headWriter = null;
        try {
            headWriter = new FileWriter(throughputsPath, false);
            headWriter.write(head);
            headWriter.flush();

            headWriter = new FileWriter(latenciesPath, false);
            headWriter.write(head);
            headWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Performance countPerformance(TokenRing tokenRing) {
        int timeToWait = 1000;
        tokenRing.start();
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tokenRing.stop();
        Performance p = new Performance();
        p.setLatency(tokenRing.getLatency(timeToWait));
        p.setThroughput(tokenRing.getThroughput());
        return p;
    }

    public static Performance countAveragePerformance(int nNodes, int load) {
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

    public static void showResults(List<Performance> scores) {
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

    public static void warmUpJvm() {
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

    public static Performance getAveragePerformance(int nIterations, List<Performance> scores) {
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
}
