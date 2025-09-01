class TestConstructor {
  public static void main(String[] args) {
    // Animal animal = new Animal();
    // animal.Animal();
    // Animal animal = Animal.getInstance();
    // 调用不同版本的构造器创建对象
    // Animal a = new Animal();
    // Animal b = new Animal("dog");
    // Animal c = new Animal("cat", 18);
    // System.out.println("动物名叫" + a.name + "，身体长" + a.size + "厘米");
    // System.out.println("动物名叫" + b.name + "，身体长" + b.size + "厘米");
    // System.out.println("动物名叫" + c.name + "，身体长" + c.size + "厘米");

    // 测试实例代码块执行顺序
    // TestCodeBlock t1 = new TestCodeBlock();
    // TestCodeBlock t2 = new TestCodeBlock("李四");

    // 测试构造器不能继承的代码
    // Apple apple = new Apple();
  }
}

class Animal {
  String name;
  int size;

  // 无参构造器
  public Animal() {
    System.out.println("无参构造器");
  }

  // 一个参数的构造器
  public Animal(String name) {
    this.name = name;
    System.out.println("一个参数的有参构造器，name: " + name);
  }

  // 两个参数的构造器
  public Animal(String name, int size) {
    this.name = name;
    this.size = size;
    System.out.println("两个参数的有参构造器，name: " + name + ", size: " + size);
  }
}

class TestCodeBlock {
  static int count;
  private String name;

  // 静态代码块，有 static 修饰
  static {
    System.out.println("静态代码块");
    count = 100;
  }

  // 实例代码块，无 static 修饰
  {
    System.out.println("实例代码块");
    name = "张三";
  }

  // 无参构造器
  public TestCodeBlock() {
    System.out.println("无参构造器");
  }

  // 有参构造器
  public TestCodeBlock(String name) {
    System.out.println("有参构造器");
    this.name = name;
  }
}

// 测试类的构造器不能继承父类的构造器
class Fruit {
  // 无参构造器
  // public Fruit() {
  // System.out.println("Fruit 无参构造器");
  // }

  // 提供一个有参构造器
  public Fruit(String name) {
    System.out.println("Fruit 构造器执行，name: " + name);
  }
}

class Apple extends Fruit {
  // 子类的无参构造器
  // public Apple() {
  // System.out.println("Apple 无参构造器");
  // }

  // 子类的有参构造器
  public Apple(String name) {
    // 调用父类的有参构造器
    super(name);
    System.out.println("Apple 有参构造器，name: " + name);
  }
}
