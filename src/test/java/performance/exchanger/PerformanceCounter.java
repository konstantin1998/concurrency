package performance.exchanger;

import org.junit.Test;
import performance.PerformanceCounterUtils;

public class PerformanceCounter {
    @Test
    public void saveExchangerMediumPerformanceWhenLoadIsFixed() {
        //PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int load = 2;
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            PerformanceCounterUtils.countAndSaveExchangerPerformance(i, load);
        }
    }

    @Test
    public void saveExchangerMediumPerformanceWhenNumberOfNodesIsFixed() {
        //PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int nNodes = 20;
        int maxLoad = 7;
        for(int i = 1; i <= maxLoad; i++) {
            PerformanceCounterUtils.countAndSaveExchangerPerformance(nNodes, i);
        }
    }
}
