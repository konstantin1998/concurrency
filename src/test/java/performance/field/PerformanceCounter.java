package performance.field;

import performance.Performance;
import performance.PerformanceCounterUtils;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PerformanceCounter {


    @Test
    public void saveFieldMediumPerformanceWhenLoadIsFixed() {
        PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int load = 2;
        int from = 4;
        int to = 16;
        for(int i = from; i <= to; i++) {
            PerformanceCounterUtils.countAndSavePerformance(i, load);
        }
    }

    @Test
    public void saveFieldMediumPerformanceWhenNumberOfNodesIsFixed() {
        PerformanceCounterUtils.warmUpJvm();
        PerformanceCounterUtils.removeSavedResults();

        int nNodes = 16;
        int maxLoad = nNodes - 1;
        for(int i = 1; i <= maxLoad; i++) {
            PerformanceCounterUtils.countAndSavePerformance(nNodes, i);
        }


    }

}
