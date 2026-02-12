package com.learnjava.collection.list;

import java.util.ArrayList;
import java.util.List;

public class ListDemo {
    public static void main(String[] args) {
        // List 集合类中的元素是有序的，即添加顺序和取出顺序是一致的，并且是可重复的。
        List<String> list = new ArrayList<>();
        list.add("jack");
        list.add("tom");
        list.add("mary");
        list.add("tom"); // 元素可重复

        System.out.println("list = " + list);
        System.out.println("list.get(2) = " + list.get(2)); // 元素有序

        List<Integer> listNumbers = List.of(1, 2);
        System.out.println("listNumbers = " + listNumbers);


        String lastItem = list.getLast();
        System.out.println("lastItem = " + lastItem);

    }
}
