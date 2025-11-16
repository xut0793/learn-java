package com.learnjava.builtin.array;

import java.util.Arrays;
import java.util.Collections;

public class ArrayExample {
  public static void main(String[] args) {
    // arrayBase();
    // getArrayItem();
    // arrayIterate();
    arrayUtilSort();
  }

  public static void arrayBase() {
    // 方式一：静态初始化，在声明时直接赋值
    int[] numbers = { 1, 2, 3, 4, 5 };
    String[] names = new String[] { "张三", "李四", "王五" };
    System.out.println("numbers: " + Arrays.toString(numbers));
    System.out.println("names: " + Arrays.toString(names));

    // 方式二：动态初始化，先指定长度，然后赋值
    int[] data = new int[3];
    data[0] = 10;
    data[1] = 20;
    data[2] = 30;
    System.out.println("data: " + Arrays.toString(data));
  }

  public static void getArrayItem() {
    int[] numbers = { 1, 2, 3, 4, 5 };

    // 获取数组长度
    int length = numbers.length; // length = 5
    System.out.println("length: " + length);

    // 访问元素（索引从0开始）
    int first = numbers[0]; // 第一个元素：10
    int last = numbers[4]; // 最后一个元素：50
    System.out.println("first: " + first);
    System.out.println("last: " + last);

    // 修改元素
    numbers[2] = 300; // 修改第三个元素为300
    System.out.println("modified numbers: " + Arrays.toString(numbers));
  }

  public static void arrayIterate() {
    int[] numbers = { 1, 2, 3, 4, 5 };

    // 方式一：普通for循环
    for (int i = 0; i < numbers.length; i++) {
      System.out.println("numbers[" + i + "] = " + numbers[i]);
    }

    // 方式二：增强for循环（foreach）
    for (int num : numbers) {
      System.out.println("num = " + num);
    }

    // 方式三：Java 8 Stream API
    Arrays.stream(numbers).forEach(System.out::println);
  }

  public static void arrayUtilSort() {
    // Arrays.sort(array); // 升序排序
    // Arrays.sort(array, Collections.reverseOrder()); // 降序排序（Java 8+）
    // Arrays.sort(array, fromIndex, toIndex); // 对指定范围排序
    // Arrays.sort(array, (a, b) -> b - a); // 自定义排序规则
    int[] numbers = { 5, 2, 8, 1, 9 };
    int[] copiedNumbers = Arrays.copyOf(numbers, numbers.length);
    Arrays.sort(numbers);
    System.out.println("numbers: " + Arrays.toString(numbers)); // {1, 2, 5, 8, 9}

    Arrays.sort(copiedNumbers, 1, 4); // 对索引1到3的元素排序
    System.out.println("copiedNumbers: " + Arrays.toString(copiedNumbers)); // {9, 8, 5, 2, 1}

    // Arrays.sort 中如果传入比较器时，不能是基本类型，必须包装类型，否则会报错
    Integer[] integers = { 5, 2, 8, 1, 9 };
    Arrays.sort(integers, (a, b) -> a - b); // 升序排序
    System.out.println("integers: " + Arrays.toString(integers)); // {1, 2, 5, 8, 9}
    Arrays.sort(integers, Collections.reverseOrder());
    System.out.println("numbers: " + Arrays.toString(numbers)); // {9, 8, 5, 2, 1}
  }
}
