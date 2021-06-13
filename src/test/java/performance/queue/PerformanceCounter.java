package performance.queue;

import performance.PerformanceCounterUtils;
import org.junit.Test;


public class PerformanceCounter {

    @Test
    public void saveQueueMediumResultsWhenLoadAndCapacityAreFixed() {
        PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int load = 2;
        int capacity = 10;
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            PerformanceCounterUtils.countAndSavePerformance(i, load, capacity);
        }
    }


    @Test
    public void saveQueueMediumResultsWhenNodesAndCapacityAreFixed() {
        PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int capacity = 1;
        int nNodes = 40;
        int maxLoad = 7;
        for(int i = 1; i <= maxLoad; i++) {
            PerformanceCounterUtils.countAndSavePerformance(nNodes, i, capacity);
        }

    }



    @Test
    public void saveQueueMediumResultsWhenNodesAndLoadAreFixed() {
        PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int load = 150;
        int nNodes = 8;
        int from = 20;
        int to = 120;
        int step = 10;
        for(int i = from; i <= to; i += step) {
            PerformanceCounterUtils.countAndSavePerformance(nNodes, load, i);
        }
    }
}
