import performance.Performance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class PlayGound {
    public static void main(String[] args) {
        try {
            FileWriter throughputsWriter = new FileWriter("D:/projectsOnJava/hw1Concurrency/src/test/resources/throughputs.csv", true);
            FileWriter latencyWriter = new FileWriter("D:/projectsOnJava/hw1Concurrency/src/test/resources/latencies.csv", true);


            throughputsWriter.write("throughputs");
            throughputsWriter.flush();
            latencyWriter.write("latencies");
            latencyWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
