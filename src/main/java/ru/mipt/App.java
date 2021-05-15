package ru.mipt;

import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.TokenMedium;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("here");
        });

    }
}
