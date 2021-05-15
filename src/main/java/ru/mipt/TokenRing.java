package ru.mipt;

import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

public class TokenRing {
    private final List<Node> nodes;
    private final List<TokenMedium> mediums;
    private LatencyCounter latencyCounter;
    private ThroughputCounter throughputCounter;


    public TokenRing(List<TokenMedium> mediums) {
        this.mediums = mediums;
        this.nodes = new ArrayList<>();
        for(int i = 0; i < mediums.size(); i++) {

            if(i == 0) {
                TokenMedium input = mediums.get(mediums.size() - 1);
                TokenMedium output = mediums.get(i);
                throughputCounter = new ThroughputCounter(input, output, mediums.size());
                Node node = new Node(throughputCounter);
                nodes.add(node);
                continue;
            }

            TokenMedium input = mediums.get(i - 1);
            TokenMedium output = mediums.get(i);

            if(i == mediums.size() - 1) {
                latencyCounter = new LatencyCounter(input, output, mediums.size());
                Node node = new Node(latencyCounter);
                nodes.add(node);
                continue;
            }

            Node node = new Node(input, output);
            nodes.add(node);
        }
    }

    public void initializeWithTokens(List<Token> tokens) {
        for(int i = 0; i < tokens.size(); i++) {
            TokenMedium medium = mediums.get(i % mediums.size());
            try {
                medium.push(tokens.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        for(Node node : nodes) {
            node.start();
        }
    }

    public void stop() {
        for(Node node : nodes) {
            node.stop();
        }
    }

    public double getThroughput() {
        return throughputCounter.countThroughput();
    }

    public double getLatency() {
        return latencyCounter.countLatency();
    }
}
