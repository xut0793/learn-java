package com.learnjava.collection.list;

import java.util.Vector;

public class VectorSource {
    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>();

        for (int i = 0; i < 10; i++) v.add(i);
        v.add(100);
        System.out.println("Vector: " + v);
    }
}
