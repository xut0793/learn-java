package com.learnjava.builtin.string;

public class StringExample {
  public static void main(String[] args) {
    // stringCreate();
    // stringMethods();
    stringIntern();
  }

  public static void stringCreate() {
    // #region createString
    // 创建字符串的两种方法
    String s1 = "Hello";
    String s2 = new String("World");
    System.out.println(s1);
    System.out.println(s2);
    // #endregion createString
  }

  public static void stringMethods() {
    // #region stringMethods
    String s1 = "Hello";
    String s2 = new String("World");
    // =============== 字符串常用方法============
    // 1. 长度和获取字符
    System.out.println("s1.length() = " + s1.length()); // 5
    // 2. 获取指定索引位置的字符
    System.out.println("s1.charAt(1) = " + s1.charAt(1)); // e
    // 3. 字符串裁剪
    System.out.println("s1.substring(1, 4) = " + s1.substring(1, 4)); // ell
    System.out.println("s1.trim() = " + s1.trim()); // Hello 去除首尾空格
    // 4. 字符串拼接
    System.out.println("s1 + s2 = " + s1 + s2); // HelloWorld
    System.out.println("s1.concat(s2) = " + s1.concat(s2)); // HelloWorld
    // 5. 字符串比较
    System.out.println("s1.equals(s2) = " + s1.equals(s2)); // false
    System.out.println("s1.equalsIgnoreCase(s2) = " + s1.equalsIgnoreCase(s2)); // false 忽略大小写比较
    System.out.println("s1.compareTo(s2) = " + s1.compareTo(s2)); // -15 按字典顺序比较
    System.out.println("s1.compareToIgnoreCase(s2) = " + s1.compareToIgnoreCase(s2)); // -15 忽略大小写比较
    System.out.println("s1.isEmpty() = " + s1.isEmpty()); // false 检查字符串是否为空
    System.out.println("s1.startsWith(\"He\") = " + s1.startsWith("He")); // true 检查字符串是否以指定前缀开头
    System.out.println("s1.endsWith(\"lo\") = " + s1.endsWith("lo")); // true 检查字符串是否以指定后缀结尾

    // 6. 字符串查找
    System.out.println("s1.indexOf('l') = " + s1.indexOf('l')); // 2
    System.out.println("s1.lastIndexOf('l') = " + s1.lastIndexOf('l')); // 3
    System.out.println("s1.indexOf('l', 3) = " + s1.indexOf('l', 3)); // 3 从索引3开始查找
    System.out.println("s1.contains(\"lo\") = " + s1.contains("lo")); // true 包含子字符串
    // 7. 字符串转换
    System.out.println("s1.toUpperCase() = " + s1.toUpperCase()); // HELLO
    System.out.println("s1.toLowerCase() = " + s1.toLowerCase()); // hello
    // replace方法用于替换字符串中的字符或字符序列
    System.out.println("s1.replace('l', 'x') = " + s1.replace('l', 'x')); // Hexxo
    // replaceFirst方法用于替换字符串中的第一个匹配子字符串
    System.out.println("s1.replaceFirst(\"l\", \"x\") = " + s1.replaceFirst("l", "x")); // Hexlo 替换第一个匹配的子字符串
    // replaceAll方法用于基于正则表达式的替换，首个参数是正则表达式
    System.out.println("s1.replaceAll(\"l\", \"x\") = " + s1.replaceAll("l", "x")); // Hexxo 替换所有匹配的子字符串
    // #endregion stringMethods
  }

  public static void stringIntern() {
    String s1 = "hello";
    String s3 = new String("hello"); // 总是创建新对象
    System.out.println(s1 == s3); // false，引用不同的对象

    /**
     * public String intern()
     * 此方法将指定字符串对象在字符串常量池中对应对象的引用返回。
     * 若该字符串本身就在字符串常量池中，则直接将自己的引用返回；
     * 若该字符串在堆中，则返回在常量池中与其有联系对象的引用。
     */
    String s4 = s3.intern(); // s3 本身是堆中的字符串对象，然后获取与它有联系的字符串常量池中的 "hello" 对象的引用
    System.out.println(s1 == s4); // true，引用同一个对象
  }
}