package com.learnjava.builtin.object;

import java.util.Arrays;
import java.util.Objects;

public class ObjectExample {
  public static void main(String[] args) {
    // testEquals();
    // testHashCode();
    testToString();
  }

  public static void testToString() {
    int i = 100;
    Integer iObj = i;
    // System.out.println(i.toString()); // Error: Cannot invoke toString() on the
    // primitive type int
    System.out.println(iObj.toString()); // 100

    System.out.println(i);

    String s = "OK";
    System.out.println(s.toString()); // OK

    int[] luckyNumbers = { 2, 3, 5, 7, 11, 13 };
    String s1 = "" + luckyNumbers;
    System.out.println(s1); // [I@6d06d69c

    String s2 = Arrays.toString(luckyNumbers);
    System.out.println(s2); // [2, 3, 5, 7, 11, 13]
  }

  /**
   * 测试 hashCode 方法
   * 字符串 s 与 t 有相同的散列码，这是因为字符串的散列码是由字符值导出的。
   * 而字符串构建器 sb 与 tb 却有着不同的散列码. 因为在 StringBuilder 类中没有定义 hashCode 方法，继承了 Object
   * 类的默认 hashCode 方法，它会从对象的存储地址得出散列码。
   */
  public static void testHashCode() {
    // String s = "OK";
    // StringBuilder sb = new StringBuilder(s);
    // System.out.println("s.hashCode() = " + s.hashCode() + " sb.hashCode() = " +
    // sb.hashCode());

    var t = new String("OK");
    var tb = new StringBuilder(t);
    // System.out.println("t.hashCode() = " + t.hashCode() + " tb.hashCode() = " +
    // tb.hashCode());

    // var s1 = "Aa";
    // var s2 = "BB";
    // System.out.println("s1.hashCode() = " + s1.hashCode() + " s2.hashCode() = " +
    // s2.hashCode());

    // 打印对象HashCode和内存地址
    System.out.println("HashCode: " + t.hashCode() + " IdentityHashCode: " + System.identityHashCode(t));
    System.out.println("HashCode: " + tb.hashCode() + " IdentityHashCode: " + System.identityHashCode(tb));
  }

  public static void testEquals() {
    Person person1 = new Person("张三", 18, "男");
    Person person2 = new Person("张三", 18, "男");
    System.out.println("person1.equals(person2) = " + person1.equals(person2));
  }

  static class Person {
    private String name;
    private int age;
    private String gender;

    public Person(String name, int age, String gender) {
      this.name = name;
      this.age = age;
      this.gender = gender;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Person other = (Person) obj;
      return age == other.age && Objects.equals(name, other.name) && Objects.equals(gender, other.gender);
    }

    /**
     * 通常重写 equals 方法时，也应该重写 hashCode 方法。
     * 并且不参与 equals()比较的字段不应参与HashCode计算。
     */
    // @Override
    // public int hashCode() {
    // return Objects.hash(age, gender, name);
    // }
  }
}
