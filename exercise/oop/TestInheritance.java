class Base {
  public static int s;
  public int a;
  protected int b = 3;

  static {
    System.out.println("基类静态代码块，s: " + s);
    s = 1;
  }

  {
    System.out.println("基类实例代码块，a: " + a);
    a = 1;
  }

  public Base() {
    System.out.println("基类构造方法，a: " + a);
    a = 2;
  }

  protected void step() {
    System.out.println("基类 step 方法, base s: " + s + ", a: " + a);
  }

  public void action() {
    System.out.println("start");
    step();
    System.out.println("end");
  }
}

class Child extends Base {
  public static int s;
  private int a;

  static {
    System.out.println("子类静态代码块，s: " + s);
    s = 10;
  }

  {
    System.out.println("子类实例代码块，a: " + a);
    a = 10;
  }

  public Child() {
    System.out.println("子类构造方法，a: " + a);
    a = 20;
  }

  @Override
  protected void step() {
    System.out.println("子类 step 方法, child s: " + s + ", a: " + a);
  }

  public int getB() {
    return b;
  }
}

public class TestInheritance {
  public static void main(String[] args) {
    System.out.println("---------- new Child()---------");
    Child c = new Child();
    System.out.println("\n---------- c.action() ---------");
    c.action();
    System.out.println("\n---------- b.action() ---------");
    Base b = c;
    b.action();
    System.out.println("---------- b.s: " + b.s);
    System.out.println("---------- c.s: " + c.s);
    System.out.println("---------- c.b: " + c.b);
  }
}

// 输出内容

/*
 * ---------- new Child()---------
 * 基类静态代码块，s: 0
 * 子类静态代码块，s: 0
 * 基类实例代码块，a: 0
 * 基类构造方法，a: 1
 * 子类实例代码块，a: 0
 * 子类构造方法，a: 10
 * 
 * ---------- c.action() ---------
 * start
 * 子类 step 方法, child s: 10, a: 20
 * end
 * 
 * ---------- b.action() ---------
 * start
 * 子类 step 方法, child s: 10, a: 20
 * end
 * 
 * ---------- b.s: 1
 * 
 * ---------- c.s: 10
 * 
 */