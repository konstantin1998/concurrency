package ru.mipt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.TokenMedium;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws InterruptedException {

        Stream<Double> numberStream =  Stream.of(-4.0, 3.0, -2.0, 1.0);
        double identity = 0;
        Double result = numberStream.reduce(identity, Double::sum) / 4;
        System.out.println(result);
        Math.round(1.4);
    }
}
