package com.learnjava.collection.list;

import java.util.ArrayList;

public class ArrayListSource {
    public static void main(String[] args) {
        final ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        for (int i = 11; i < 15; i++) {
            list.add(i);
        }

        list.add(100);
        list.add(200);
        list.add(null);

        for (Integer i : list) {
            System.out.println(i);
        }
    }
}
