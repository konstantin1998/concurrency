package performance.field;

import performance.Performance;
import performance.PerformanceCounterUtils;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PerformanceCounter {
    @Test
    public void showFieldMediumPerformanceWhenLoadIsFixed() {
        PerformanceCounterUtils.warmUpJvm();
        int load = 2;
        List<Performance> scores = new ArrayList<>();
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            scores.add(PerformanceCounterUtils.countAveragePerformance(i, load));
        }

        PerformanceCounterUtils.showResults(scores);
    }


    @Test
    public void showFieldMediumPerformanceWhenNumberOfNodesIsFixed() {
        PerformanceCounterUtils.warmUpJvm();
        int nNodes = 35;

        List<Performance> scores = new ArrayList<>();
        int maxLoad = nNodes - 1;
        for(int i = 1; i <= maxLoad; i++) {
            scores.add(PerformanceCounterUtils.countAveragePerformance(nNodes, i));
        }

        PerformanceCounterUtils.showResults(scores);
    }



}
