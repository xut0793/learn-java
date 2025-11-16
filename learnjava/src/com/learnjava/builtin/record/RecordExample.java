package com.learnjava.builtin.record;

public class RecordExample {
  public static void main(String[] args) {
    recordBasic();
    recordValidation();
  }

  public static void recordBasic() {
    record Person(String name, int age, String email) {
    }

    Person p = new Person("张三", 18, "zhangsan@example.com");
    System.out.println(p.name()); // 张三
    System.out.println(p.age()); // 18
    System.out.println(p.email()); // zhangsan@example.com
    System.out.println(p); // Person[name=张三, age=18, email=zhangsan@example.com]
  }

  public static void recordValidation() {
    record Person(String name, int age, String email) implements Comparable<Person> {
      public Person {
        if (name == null || name.isBlank()) {
          throw new IllegalArgumentException("姓名不能为空");
        }
        if (age < 0) {
          throw new IllegalArgumentException("年龄不能小于0");
        } else if (age >= 100) {
          throw new IllegalArgumentException("年龄不能大于等于100");
        }
        if (email == null || email.isBlank()) {
          throw new IllegalArgumentException("邮箱不能为空");
        }
      }

      /**
       * compareTo 方法结果
       * 如果返回值小于 0，表示调用对象小于参数对象。
       * 如果返回值等于 0，表示调用对象等于参数对象。
       * 如果返回值大于 0，表示调用对象大于参数对象。
       */
      @Override
      public int compareTo(Person other) {
        // 按年龄比较大小
        return Integer.compare(this.age, other.age);
      }
    }

    // 使用
    Person p1 = new Person("张三", 18, "zhangsan@example.com");
    Person p2 = new Person("李四", 20, "lisi@example.com");
    System.out.println("p1(18) 年龄小于 p2(20): " + p1.compareTo(p2)); // -1
  }
}
