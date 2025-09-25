package com.learnjava.oop.object;

public class ReferenceType {
  public static void main(String[] args) {
    int a = 123;
    Student stu;
    Student stu1 = new Student("张三", 18);
    Student stu2 = stu1;
    System.out.println("stu1的引用地址：" + stu1); // Student@8efb846
    System.out.println("stu2的引用地址：" + stu2); // Student@8efb846
    System.out.println("stu1内容：name=" + stu1.name + ", age=" + stu1.age); // stu1内容：name=张三, age=18
    System.out.println("stu2内容：name=" + stu2.name + ", age=" + stu2.age); // stu2内容：name=张三, age=18

    stu2.name = "李四";
    stu2.age = 20;
    System.out.println("stu1的引用地址：" + stu1); // Student@8efb846
    System.out.println("stu2的引用地址：" + stu2); // Student@8efb846
    System.out.println("stu1内容：name=" + stu1.name + ", age=" + stu1.age); // stu1内容：name=李四, age=20
    System.out.println("stu2内容：name=" + stu2.name + ", age=" + stu2.age); // stu2内容：name=李四, age=20
    // 说明：stu1和stu2指向的是同一个对象，所以指向的内存地址一样。但是更改 stu2 会直接影响到 stu1
  }
}

class Student {
  String name;
  int age;

  public Student(String name, int age) {
    this.name = name;
    this.age = age;
  }
}
