package ru.mipt;

import ru.mipt.medium.FieldMedium;
import ru.mipt.medium.TokenMedium;

import java.util.ArrayList;
import java.util.List;

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
    }
}
