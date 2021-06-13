import performance.Performance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.stream.Collectors;

public class PlayGound {
    public static void main(String[] args) throws InterruptedException {
        Exchanger<String> ex = new Exchanger<>();
        String str = "";
        str = ex.exchange(str);
        str = ex.exchange("bue");
        System.out.println(str);
    }
}
