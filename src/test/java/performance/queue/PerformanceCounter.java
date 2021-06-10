package performance.queue;

import performance.Performance;
import performance.PerformanceCounterUtils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class PerformanceCounter {

    @Test
    public void showQueueMediumResultsWhenLoadAndCapacityAreFixed() {
        PerformanceCounterUtils.warmUpJvm();
        int load = 2;
        int capacity = 10;
        List<Performance> scores = new ArrayList<>();
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            scores.add(PerformanceCounterUtils.countAveragePerformance(i, load, capacity));
        }

        PerformanceCounterUtils.showResults(scores);
    }

    @Test
    public void showQueueMediumResultsWhenNodesAndCapacityAreFixed() {
        PerformanceCounterUtils.warmUpJvm();

        int capacity = 3;
        List<Performance> scores = new ArrayList<>();

        int nNodes = 16;
        int maxLoad = nNodes * capacity - 1;
        for(int i = 1; i <= maxLoad; i++) {
            scores.add(PerformanceCounterUtils.countAveragePerformance(nNodes, i, capacity));
        }

        PerformanceCounterUtils.showResults(scores);
    }

    @Test
    public void showQueueMediumResultsWhenNodesAndLoadAreFixed() {
        PerformanceCounterUtils.warmUpJvm();
        int load = 150;
        int nNodes = 8;
        List<Performance> scores = new ArrayList<>();
        int from = 20;
        int to = 120;
        int step = 10;
        for(int i = from; i <= to; i += step) {
            scores.add(PerformanceCounterUtils.countAveragePerformance(nNodes, load, i));
        }

        PerformanceCounterUtils.showResults(scores);
    }
}
