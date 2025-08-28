public class Test_extends_poly1 {
  public static void main(String[] args) {
    /*---编译时多态 */
    Person p = new Person();
    Man m = new Man();
    System.out.println(p.toString()); // 调用 Person 类的 toString 方法
    System.out.println(m.toString()); // 调用 Man 类的 toString 方法

    /*---运行时多态 */
    Person p2 = new Man(); // 父类引用指向子类对象
    System.out.println(p2.toString()); // 运行时多态：调用的是 Man 类的 toString 方法

    /*
     * java 从实例所属的类开始寻找匹配的方法执行
     * 如果当当前类没有重写的匹配方法，则沿着继承关系逐级向上寻找，直到 Object 类为止
     * 如果找到匹配方法，则执行该方法
     * 如果没有找到匹配方法，则编译错误
     */
    p2.show(); // 编译错误：Person 类没有 show() 方法
    // 解决方法：将 p2 强制转换为 Man 类
    // ((Man) p2).show(); // 运行时多态：调用的是 Man 类的 show() 方法
  }
}

// 基础版多态
class Person {
  public String toString() {
    return "Person toString()"; // 重写 Object 的 toString 方法
  }
}

class Man extends Person {
  public String toString() {
    return "Man toString()"; // 重写父类的 toString 方法
  }

  public void show() {
    System.out.println("这是子类独有的方法");
  }
}
