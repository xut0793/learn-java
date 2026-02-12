package com.learnjava.collection.array;

import java.util.Arrays;

public class ArrayDemo {
    public static void main(String[] args) {
        int[] intArr = new int[1];
        intArr[0] = 10;
        System.out.println(intArr[0]);

        String[] strArr = new String[] { "a", "b", "c" };
        System.out.println(Arrays.toString(strArr));

        Person[] personArr = new Person[1];
        personArr[0] = new Person("Tom", 20);

        // 数组长度开始初始化时指定，并且后面不能更改。
        // 数组内的元素类型必须为同一种类型
        // 数组元素的增删比较麻烦
        // Person[] personArr2 = new Person[personArr.length + 1];
        // System.arraycopy(personArr, 0, personArr2, 0, personArr.length);
        Person[] personArr2 = Arrays.copyOf(personArr, personArr.length + 1);
        personArr2[personArr2.length - 1] = new Person("Jerry", 30);
        System.out.printf("Array personArr length: %s; personArr2 length: %s.", personArr.length, personArr2.length);
    }
}

class Person {
    String name;
    int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
