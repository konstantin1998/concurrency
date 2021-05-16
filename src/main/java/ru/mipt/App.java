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

public class App {
    public static void main(String[] args) throws InterruptedException {
        List<TokenMedium> list = new ArrayList<>();

        list.add(new FieldMedium());
        list.add(new FieldMedium());
        list.add(new FieldMedium());
        list.add(new FieldMedium());
        list.add(new FieldMedium());

        TokenRing ring = new TokenRing(list);
        System.out.println(ring);

        Queue<Object> queue = new ArrayBlockingQueue<>(1);
    }
}
