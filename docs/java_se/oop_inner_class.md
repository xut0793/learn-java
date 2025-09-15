# 内部类

前面介绍类的章节中说过，通常一个类的内部可以定义以下内容：

- 成员变量
- 方法
- 构造器
- 实例代码块
- 静态变量
- 静态方法
- 静态代码块

另外，类内部还可以直接定义类，这就是内部类。

**内部类，是指在一个类的内部定义的类。包含内部类的类被称为外部类。内部类可以访问外部类的所有成员，包括私有成员。**

根据内部类声明的位置不同，内部类可以分为以下几种：

- 静态内部类
- 成员内部类
- 方法内部类
- 匿名内部类

## 静态内部类

静态内部类与静态变量和静态方法定义的位置一样，也带有 `static`关键字，只是它定义的是类。

在语法上，静态内部类除了位置放在其他类内部以外，它与一个独立的类差别不大，也可以有静态变量、静态方法、成员方法、成员变量、构造方法等。

### 定义

```java
访问修饰符 class 外部类 {
    访问修饰符 static class 静态内部类 {
        // 静态内部类的成员
    }
}
```

### 访问

- 静态内部类访问外部类：
  - 静态内部类只能直接访问外部类的静态成员和方法。
  - 静态内部类不能直接访问外部类的实例成员。
- 外部类访问静态内部类：
  - 外部类可以直接通过 `new 静态内部类` 进行实例化。
- 其它类访问静态内部类：
  - 静态内部类声明为 `public` ，可以被外部使用。
  - 访问方式通过 `new 外部类.静态内部类` 的方式进行实例化。

```java
public class Outer {
  private static int shared = 100;

  // 静态内部类
  public static class StaticInner {
    public void innerMethod () {
      // 静态内部类可以访问外部类的静态成员
      System.out.println("inner access shared: " + shared);
    }
  }

  public void test() {
    // 静态内部类可以直接实例化
    StaticInner inner = new StaticInner();
    inner.innerMethod();
  }
}

// 在其它类中访问静态内部类
public class Other {
  public void test() {
    // 静态内部类可以被其它类直接实例化
    Outer.StaticInner inner = new Outer.StaticInner();
    inner.innerMethod();
  }
}
```

### 实现

内部类只是 Java 编译器的概念，对于 Java 虚拟机而言，它是不知道内部类这回事的，因为每个内部类最后都被编译为一个独立的类，生成一个独立的字节码文件。也就是说，每个内部类其实都被替换为一个独立的类。区别在于各种内部类类名的生成规则和成员访问的方式不同。

静态内部类的实现：

- 静态内部类会被编译为一个独立的类，类名是 `外部类$静态内部类` 。
- 被静态内部访问的外部类静态成员变量，都会在外部类再生成一个对应的静态访问器方法，方法名为 `access$数字`，其中数字是一个从 0 开始的递增数字。

上述代码示例会编译后生成两个类的文件：

- `Outer.class`
- `Outer$StaticInner.class`

```java
// Outer.class
public class Outer {
  private static int shared = 100;

  static int access$0() {
    return shared;
  }

  public void test() {
    Outer$StaticInner inner = new Outer$StaticInner();
    inner.innerMethod();
  }
}

// Outer$StaticInner.class
public class Outer$StaticInner {
  public void innerMethod () {
    System.out.println("inner access shared: " + Outer.access$0());
  }
}
```

内部类访问了外部类的一个私有静态变量 shared，而我们知道私有变量是不能被类外部访问的，Java 的解决方法是：自动为 Outer 生成一个非私有访问方法 `access$0`，它返回这个私有静态变量 shared。

### 应用

静态内部类的使用场景是很多的，如果它与外部类关系密切，且不依赖于外部类实例，则可以考虑定义为静态内部类。

比如，一个类内部，如果既要计算最大值，又要计算最小值，可以在一次遍历中将最大值和最小值都计算出来，但怎么返回呢？可以定义一个类 Pair，包括最大值和最小值，但 Pair 这个名字太普遍，而且它主要是类内部使用的，就可以定义为一个静态内部类。

```java
public class MaxMin {
  public static class Pair {
    public int max;
    public int min;
  }
}
```

在 Java API 中使用静态内部类的例子：

- Integer 类内部有一个私有静态内部类 IntegerCache，用于支持整数的自动装箱。
- 表示链表的 LinkedList 类内部有一个私有静态内部类 Node，表示链表中的每个节点。
- Character 类内部有一个 public 静态内部类 UnicodeBlock，用于表示一个 Unicode block。

## 成员内部类

成员内部类是直接定义在外部类的成员位置的类，与静态内部类的区别就是没有 `static` 修饰符，并且在**成员内部类中不可以定义静态变量和静态方法**。

> 为什么成员内部类不能定义静态成员变量和静态方法？可以这么理解，这些内部类是与外部实例相连的，不应独立使用，而静态变量和方法作为类型的属性和方法，一般是独立使用的。

### 定义

```java
访问修饰符 class 外部类 {
    访问修饰符 class 成员内部类 {
        // 成员内部类的成员
    }
}
```

### 访问

- 成员内部类访问外部类：
  - 成员内部类可以直接访问外部类的所有实例成员，包括私有成员。
  - 如果遇到同名变量名，则可以通过 `外部类.this.变量名` 的方式访问外部类的变量。
- 外部类访问成员内部类：
  - 外部类可以直接通过 `new 成员内部类` 进行实例化。
- 其它类访问成员内部类：
  - 成员内部类声明为 `public` ，可以被外部使用。
  - 访问方式通过 `new 外部类().new 成员内部类()` 的方式进行实例化。

```java
public class Outer {
  private int a = 100;
  private String action() {
    return "outer action";
  }

  // 成员内部类
  public class MemberInner {
    public void innerMethod () {
      // 成员内部类可以访问外部类的所有实例成员
      System.out.println("inner access a: " + a);

      // 访问外部类的方法
      System.out.println("inner access outer method: " + Outer.this.action());
    }
  }

  public void test() {
    // 成员内部类可以直接实例化
    MemberInner inner = new MemberInner();
    inner.innerMethod();
  }
}

// 在其它类中访问成员内部类
public class Other {
  public void test() {
    Outer outer = new Outer();
    // 成员内部类可以通过外部类的实例，进行实例化
    Outer.MemberInner inner = outer.new MemberInner();
    inner.innerMethod();
  }
}
```

### 实现

成员内部类的实现：

- 成员内部类会被编译为一个独立的类，类名是 `外部类$成员内部类` 。
- 被成员内部类访问的外部类实例成员变量，都会在外部类再生成一个对应的访问器方法，方法名为 `access$数字`，其中数字是一个从 0 开始的递增数字。
- `access$数字` 方法的入参是外部类的实例对象，比如 `access$0(Outer outer)`，这点与静态内部类实现的区别。

```java
public class Outer {
  private int a = 100;
  private String action() {
    return "outer action";
  }
  public void test() {
    Outer$MemberInner inner = new Outer$MemberInner(this);
    inner.innerMethod();
  }

  // 生成访问器方法
  static int access$0(Outer outer) {
    return outer.a;
  }
  static String access$1(Outer outer) {
    return outer.action();
  }
}

public class Outer$MemberInner {

  private final Outer outer;

  public Outer$MemberInner(Outer outer) {
    this.outer = outer;
  }

  public void innerMethod () {
    System.out.println("inner access a: " + Outer.access$0(outer));
    System.out.println("inner access outer method: " + Outer.access$1(outer));
  }
}
```

- 生成的成员内部类中，会有一个 `final` 类型的字段，用于存储外部类的实例，它会在构造函数中被初始化。外部类在实例化成员内部类时会传入当前外部类的实例对象。
- 由于内部类访问了外部类的私有变量和方法，外部类 Outer 生成了两个非私有静态方法：`access$0` 用于访问变量 a, `access$1` 用于访问方法 action，方法的入参是外部类的实例对象。

### 应用

如果内部类与外部类关系密切，需要访问外部类的实例变量或方法，则可以考虑定义为成员内部类。

在 Java API 的类 LinkedList 中，它的两个方法 listIterator 和 descendingIterator 的返回值都是接口 Iterator，调用者可以通过 Iterator 接口对链表遍历，listIterator 和 descendingIterator 内部分别使用了成员内部类 ListItr 和 DescendingIterator，这两个内部类都实现了接口 Iterator。

## 方法内部类

方法内部类是指定义在外部类的方法中的类，包括定义在静态方法和实例方法中。

### 定义

```java
访问修饰符 class 外部类 {
    访问修饰符 返回值类型 方法名(参数列表) {
        访问修饰符 class 方法内部类 {
            // 方法内部类的成员
        }
    }
}
```

### 访问

- 如果定义在静态方法中：则方法内部类只能访问外部类的静态变量和方法；
- 如果定义在实例方法中：则方法内部类可以访问外部类的所有实例成员和方法，包括私有成员。
- 方法内部类还可以访问方法的参数和局部变量，但是这些变量**必须是 final 修饰**。
- 方法内部类只能在定义它的方法中实例化，不能在外部实例化。不管外部类其它成员还是其它类都无法访问方法内部类

```java
public class Outer {
    private int a = 100;
    public void test(final int param){
        final String str = "hello";
        class MethodInner {
            public void innerMethod(){
                System.out.println("outer a " +a);
                System.out.println("param " +param);
                System.out.println("local var " +str);
            }
        }

        // 只在方法内使用
        MethodInner inner = new MethodInner();
        inner.innerMethod();
    }
}
```

### 实现

- 与成员内部类类似，方法内部类也有一个实例变量 outer 指向外部对象，然后在构造方法中被初始化。
- 对外部私有实例变量的访问也是通过 Outer 添加的方法 `access$0(Outer outer)` 来进行的。
- 对方法中的参数和局部变量的访问，是通过在构造方法中传递参数来实现的。
- str 并没有被作为参数传递，这是因为它被定义为了常量，在生成的代码中，会直接使用常量值。

至于需要被方法内部类访问的参数，必须声明为 `final` 的原因是因为实际上，方法内部类操作的并不是外部的变量，而是它自己的实例变量，只是这些变量的值和外部一样，对这些变量赋值，并不会改变外部的值，为避免混淆，所以干脆强制规定必须声明为 final。

```java
public class Outer {
    private int a = 100;
    public void test(final int param) {
        final String str = "hello";
        Outer$MethodInner inner = new Outer$MethodInner(this, param);
        inner.innerMethod();
    }
    static int access$0(Outer outer){
        return outer.a;
    }
}


public class Outer$MethodInner {
    Outer outer;
    int param;
    OuterInner(Outer outer, int param){
        this.outer = outer;
        this.param = param;
    }
    public void innerMethod() {
        System.out.println("outer a " + Outer.access$0(this.outer));
        System.out.println("param " + param);
        System.out.println("local var " + "hello");
    }
}
```

从上面代码可以看出，内部类访问方法的参数 param 时，并不是直接访问的，而是构造函数传入的，作为自身的成员变量。从原始代码的角度看，如果内部类改变了该变量的值，外部方法的使用这个形参时应该也是更新后的值，但实际上却没有改变，这种不一致性会让人难以理解和接受，所以为了保持参数的一致性，就规定了使用 `final` 来保持形参的不变性，在编译器层面也会校验，要求被内部类引用的变量必须是 `final` 的。

## 匿名内部类

与前面介绍的内部类不同，匿名内部类没有单独的类定义，它在创建对象的同时定义类。

### 定义

```java
new 父类构造器(参数列表) | 实现接口() {
    // 匿名内部类实例的代码
}
```

匿名内部类是与 `new` 关联的，在创建对象的同时定义类，`new` 后面是父类或者父接口，然后是圆括号`()`，里面可以是传递给父类构造方法的参数，最后是大括号`{}`，里面是类的定义。

匿名内部类只能被使用一次，用来创建一个对象。它没有名字，没有构造方法，但可以根据参数列表，调用对应的父类构造方法（new 后面接的父类构造函数接受入参）。它可以定义实例变量和方法，可以有初始化代码块，初始化代码块可以起到构造方法的作用，只是构造方法可以有多个，而初始化代码块只能有一份。

- 匿名内部类没有类名，所以它不能定义静态变量和静态方法，即不能在匿名内部类中使用 `static` 关键字。
- 匿名内部类可以定义实例变量和方法。
- 匿名内部类可以有初始化代码块，初始化代码块可以起到构造方法的作用，只是构造方法可以有多个，而初始化代码块只能有一份。

### 访问

- 如果定义在静态方法中：则匿名内部类只能访问外部类的静态变量和方法；
- 如果定义在实例方法中：则匿名内部类可以访问外部类的所有实例成员和方法，包括私有成员。
- 匿名内部类还可以访问方法的参数和局部变量，但是这些变量**必须是 final 修饰**。
- 匿名内部类只能被使用一次，用来创建一个对象。不管外部类其它成员还是其它类都无法访问匿名内部类，因为匿名内部类没有类名。

```java
public class Outer {
  public void test(final int x, final int y){

    // 调用父类构造方法，传入参数
    Point p = new Point(2,3){
      @Override
      public double distance() {
          // 访问外部类方法的参数
          return super.distance(new Point(x, y));
      }
    };
    System.out.println(p.distance());
  }
}
```

或者使用匿名内部类实现接口

```java
interface MyInterface {
    void doSomething();
}

public class Main {
    public static void main(String[] args) {
        // 通过匿名内部类实现接口
        MyInterface myObject = new MyInterface() {
            @Override
            public void doSomething() {
                System.out.println("This is an implementation of MyInterface using an anonymous inner class.");
            }
        };
        myObject.doSomething();
    }
}
```

### 实现

每个匿名内部类也都被生成为一个独立的类，只是类的名字以`外部类$数字编号`，没有有意义的类名。

```java
public class Outer {
    public void test(final int x, final int y){
        Point p = new Outer$1(this, 2, 3, x, y);
        System.out.println(p.distance());
    }
}


public class Outer$1 extends Point {
    int x2;
    int y2;
    Outer outer;
    Outer$1(Outer outer, int x1, int y1, int x2, int y2){
        super(x1, y1);
        this.outer = outer;
        this.x2 = x2;
        this.y2 = y2;
    }
    @Override
    public double distance() {
        return super.distance(new Point(this.x2, y2));
    }
}
```

在匿名内部类实现化时，外部实例 this、方法参数 x 和 y、new 时的参数 2 和 3 都作为参数传递给了内部类构造方法。此外，new 时的参数 2 和 3 在内部类构造方法又将它们传递给了父类构造方法。

匿名内部类能做的，方法内部类都能做。但如果对象只会创建一次，且不需要构造方法来接受参数，则可以使用匿名内部类，这样代码书写上更为简洁。

### 应用

匿名内部类在实际开发中应用非常广泛，通常作为方法的参数、事件的回调函数使用。但在 Lambda 函数出现后，大部分场景都被替代了。

## 内部类的继承

内部类和普通类一样，也是可以被继承，这样给本来就十分灵活的内部类增加了更好的灵活性和代码复用性，只是内部类的继承和普通类有一些不同之处，是在使用时要多加注意的，因为内部类的创建需要持有外部类的引用。

在上述介绍的内部类的实现中，除了静态内部类之后，其它内部类的实现中都通过内部类的构造函数传入了外部类实例的引用。因为在构建内部类对象时，需要一个指向外部类对象的引用，才能访问外部类的成员变量和方法。如果编译器访问不到这个引用就会报错。

所以在内部类实例时，通过 `外部类实例对象.new` 语法传递了外部类实例的引用。

```java
public class Outer {
  class Inner {}

  public static void main(string[] args) {
    Outer outer = new Outer();
    Outer.Inner inner = outer.new Inner();
  }
}
```

由于内部类保持外部类的引用是通过构造方法完成的，所以在继承内部类时的情况就变得复杂。如果按默认继承的语法，通过 `super()` 调用父类构造器时，会报错，因为内部类无法引用到外部类实例对象。

```java
public class Outer {
  class Inner {}
}

class Test extends Outer.Inner {
  public Test() {
    super();
  }

  public static void main(string[] args) {
    // 编译器报错提示：需要包含 Outer.Inner 的封装实例引用 enclosingClassReference.super()
    Test test = new Test();
  }
}
```

为了解决内部类继承的问题，Java 提供了一种特殊的语法 `enclosingClassReference.super()` 来建立内部类和外部类之间建立引用关系。这是一种特殊语法，任何非内部类中使用会报错。

```java
public class Outer {
  class Inner {}
}

class Test extends Outer.Inner {
  public Test(Outer o) {
    // enclosingClassReference.super() 特殊语法
    o.super();
  }

  public static void main(string[] args) {
    Outer outer = new Outer();
    Test test = new Test(outer);
  }
}
```

`enclosingClassReference.super()` 是 java 处理内部类继承时的特殊语法，会外部类的实例引用 `enclosingClassReference` 在内部类构造函数中传入。与常规类的继承语法中 `super()` 作用一样，都控制了对象实例化的流程。

## 内部类的重写

既然说到了继承，很自然的就会联想到，内部类会被覆盖吗？答案是不会的。

```java
public class Egg {
    private Yolk y;
    protected class Yolk{
        public Yolk() {
            System.out.println("Egg.Yolk!");
        }
    }

    public Egg(){
        System.out.println("New Egg");
        y = new Yolk();
    }
}

public class BigEgg extends Egg {

    public class Yolk{
        public Yolk() {
            System.out.println("BigEgg yolk");
        }
    }

    public static void main(String[] args) {
        new BigEgg(); // 输出： New Egg Egg.Yolk!
    }
}
```

由输出可以看出，重写的内部类并没有被调用，说明了在不同的外部类中的内部类是相互独立的实体，他们存在于自己的命名空间中，如果想要实现覆盖的话，可以直接使用继承语法，将子类的内部类继承自父类的内部类。

```java
// 正确继承内部类
public class Egg {
  protected class Yolk{
    public Yolk() {
      System.out.println("Egg.Yolk!");
    }
    public void f() {
      System.out.println("Egg.Yolk.f()");
    }
  }

  private Yolk y = new Yolk();

  public Egg() {
    System.out.println("New Egg");
  }

  public void insertYolk(Yolk yolk) {
    this.y = yolk;
  }
  public void g() {y.f();}
}

public class BigEgg extends Egg {
  public class Yolk extends Egg.Yolk {
    public Yolk() {
      System.out.println("BigEgg yolk");
    }

    @Override
    public void f() {
      System.out.println("BigEgg yolk f()");
    }
  }

  public BigEgg() {
    insertYolk(new Yolk());
  }

  public static void main(String[] args) {
    Egg e = new BigEgg(); // 输出： New Egg Egg.Yolk! BigEgg yolk
    e.g(); // 输出： BigEgg yolk f()
  }
}
```

## 为什么需要内部类

java 类的继承机制是单继承，一个类只能有一个直接父类。如果一个类需要实现多种不同类型特征时，单继承就无能为力了。这个时候，可以使用内部类的形式来解决“多重继承”的问题。

一个外部类可以有多个内部类，每个内部类可以以不同的方式实现同一个接口，或者继承于同一个类或多个不同的类。这样不同功能的类就与通过内部类的形式与外部类进行了关联，变向实现了“多重继承”的效果。

理由就是：每个内部类都可以独立地继承自一个实现，因此外部类是否已经继承了某个实现，对内部并没有限制，内部类完善了多重继承问题的解决方案。接口解决了多重继承的一部分问题，内部类也解决了一部分问题（继承多个非接口类型）。

## 总结

在理解内部类时，应该从不同的角度去进行理解。

从外部类的角度来看，根据内部类定义的位置，扮演对应位置的角色。比如静态内部类同静态成员一样，成员内部类同实例成员一样，方法内部类和匿名内部类同方法的局部变量一样，都是局部的。

从内部类的角度来看，自己是一个独立的类，可以定义和使用自己的成员变量和方法。然后根据所处位置不同，还能访问到额外的成员变量和方法。

| 种类       | 可以使用的修饰符                                    | 主要特征                                                                                                                                                                                                                                                            |
| ---------- | --------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 静态内部类 | static、final、abstract、public、protected、private | 1. 定义位置与静态变量和静态方法类似，带 `static` 关键字 <br> 2. 只能直接访问外部类的静态成员和方法 <br> 3. 编译后类名是 `外部类$静态内部类` <br> 4. 外部访问通过 `new 外部类.静态内部类` 实例化                                                                     |
| 成员内部类 | final、abstract、public、protected、private         | 1. 定义在外部类成员位置，无 `static` 修饰符 <br> 2. 不能定义静态变量和静态方法 <br> 3. 可直接访问外部类所有实例成员，同名时用 `外部类.this.变量名` 访问 <br> 4. 编译后类名是 `外部类$成员内部类` <br> 5. 外部访问通过 `new 外部类().new 成员内部类()` 实例化        |
| 方法内部类 | final、abstract                                     | 1. 定义在外部类的方法中 <br> 2. 静态方法中的只能访问外部类静态成员，实例方法中的可访问外部类所有实例成员 <br> 3. 可访问方法参数和局部变量，这些变量必须是 `final` 修饰 <br> 4. 只能在定义它的方法中实例化 <br> 5. 编译后类名类似 `外部类$方法内部类`                |
| 匿名内部类 | 无                                                  | 1. 创建对象同时定义类，无单独类定义 <br> 2. 无类名、无构造方法，只能使用一次 <br> 3. 静态方法中只能访问外部类静态成员，实例方法中可访问外部类所有实例成员 <br> 4. 可访问方法参数和局部变量，这些变量必须是 `final` 修饰 <br> 5. 编译后类名以 `外部类$数字编号` 命名 |
