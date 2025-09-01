# 接口

面向对象的设计中，类是对现实世界中实体的抽象，封装了实体对象的特征（数据）和行为（方法）。在对事物分类的抽象中，**Java 的类采用的是单继承模式，一个类只能有一个直接父类**（Python 语言中采用多类继承）。

单继承的实现使得类的继承关系变得简单，但也同样限制了 java 类的抽象能力。在对事物分类的抽象中，java 类的继承体系的能力更多的表现在事物的遗传机制这种**纵向深度**的关系链上。但在表现事物**横向关系**的场景中，java 类的继承体系的能力就不足了。

比如要实现拍照功能，很多时候，只要能拍出符合需求的照片就行，至于是用手机拍，还是用 Pad 拍，或者是用单反相机拍，并不重要，即关心的是对象是否有拍出照片的能力，而并不关心对象到底是什么类型，手机、Pad 或单反相机都可以。

又如要计算一组数字，只要能计算出正确结果即可，至于是由人心算，用算盘算，用计算器算，用计算机软件算，并不重要，即关心的是对象是否有计算的能力，而并不关心对象到底是算盘还是计算器。

在以上这种需要表现对象可扩展功能的场景中，对象属于什么类型并不重要，重要的是对象具有的能力。那在面向对象程序中，如何抽象事物的能力呢？就是使用接口。接口相比较继承，最大的特点就是可以定义**一个类同时实现多个不同能力的接口**，表示这个类的对象就拥有多种能力。

接口声明了一组能力，但它并没有实现这个能力，接口只是约定了一个规范，这个规范需要一个实现者，然后需要一个使用者。所以说接口涉及交互的两方对象，一方是实现了接口的类，另一方是调用了接口的类。

比如电脑 USB 接口，USB 标准约定了 USB 设备需要实现的能力，每个 USB 设备都需要实现这些能力，计算机使用 USB 协议与 USB 设备交互，计算机和 USB 设备互不依赖，具体的 USB 设备可以是键盘、鼠标、U 盘、摄像头、手机等，即使用同一种设备也可以是不同厂商生产的，但是都可以通过 USB 接口与电脑交互数据。

## 定义接口

接口的定义使用 `interface` 关键字，接口的定义格式如下：

```java
<访问限制修饰符> [abstract] interface <接口名> {
    // 接口方法声明
    <访问限制修饰符> [abstract] <返回类型> <方法名>([参数列表]);
    // 接口变量声明
    <访问限制修饰符> [static] [final] <数据类型> <变量名> = <值>;
}
```

`interface` 关键字说明定义的是接口，其中 `abstract` 关键字可写可不写，但如果不写，系统也会在进行编译时自动加上。也就是说，接口一定是抽象的。接口名的命名与类名规则一样，首字母大写。

### 定义接口的方法

接口能力的约定通过接口内部方法的声明来表示，声明的方法不需要方法体，只需要方法的签名即可，并且方法的访问修饰符和`abstract`关键字都可以省略，默认是 `public abstract`。

### 定义接口的变量

接口中定义的变量只能是常量，不能是变量。因为接口中的变量默认是 `public static final` 修饰的，即静态常量。即使不写这些修饰符，系统也会默认添加。访问时可以通过 `接口名称.变量名称` 方式使用，类似于类的静态常量。

```java
// 为对象约定一个可参与比较的能力
interface IComparable {
    // 常量定义
    // 返回结果是int类型，-1表示自己小于参数对象，0表示相同，1表示大于参数对象。
    int COMPARE_EQUAL = 0;
    int COMPARE_GREATER = 1;
    int COMPARE_LESS = -1;

    // 方法定义
    int compareTo(Object o);
}
```

## 实现接口

类可以实现接口，表示类的对象具有接口所表示的能力。

Java 使用 `implements` 这个关键字表示实现接口，前面是类名，后面是接口名。实现的接口必须实现接口中的所有方法，否则类必须声明为抽象类。

在此以继承章节中介绍过的 `Point` 类来说明。我们让 `Point` 具备可以比较的能力，点之间怎么比较呢？我们假设按照与原点(0,0)的距离进行比较，Point 类代码如代码所示：

```java
class Point implements IComparable {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance() {
      return Math.sqrt(x * x + y * y);
    }

    @Override
    public int compareTo(Object o) {
      // Point不能与其他类型的对象进行比较，它首先检查要比较的对象是否是Point类型，如果不是，使用throw抛出一个异常，异常将在下一章介绍
      if (!(o instanceof Point)) {
        throw new IllegalArgumentException("参数必须是Point类型");
      }

      // 如果是 Point 类型，则使用强制类型转换将Object类型的参数 o 转换为Point类型的参数p。
      Point p = (Point) o;
      double d = this.distance() - p.distance();
      if (d < 0) {
        return IComparable.COMPARE_LESS;
      } else if (d > 0) {
        return IComparable.COMPARE_GREATER;
      } else {
        return IComparable.COMPARE_EQUAL;
      }
    }

    @Override
    public String toString() {
      return "Point(" + x + "," + y + ")";
    }
}
```

## 使用接口

与类不同，接口不能 `new`，不能直接创建一个接口对象，对象只能通过类来创建。但可以声明接口类型的变量，引用实现了接口的类对象。

比如 `IComparable` 接口，它不能直接创建对象，只能通过实现了 `IComparable` 接口的类来创建对象。

```java
IComparable p1 = new Point(1, 2);
IComparable p2 = new Point(3, 4);
int result = p1.compareTo(p2);
System.out.println(result); // 输出 -1
```

在这个例子中，`p1` 和 `p2` 都被声明为 `IComparable` 类型的变量，但是它们引用的对象分别是 `Point` 类型的对象。

为什么 `Point` 类型的对象非要赋值给 `IComparable` 类型的变量呢？在以上代码中，确实没必要。但更多的场景中，往往代码逻辑并不知道具体的实例对象的类型，但又知道需要实现怎样的功能，这时才是接口发挥威力的地方。

比如要开发一组工具类，这些工具类实现获取最大值的 max 方法，和按升序排序的 sort 方法。

```java
public class ComparableUtils {
  public static IComparable max(IComparable[] arr) {
    if (arr == null || arr.length == 0) {
      return null;
    }

    IComparable max = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i].compareTo(max) > 0) {
        max = arr[i];
      }
    }
    return max;
  }

  /**
   * 从小到大升序排序
   */
  public static void sort(IComparable[] arr) {
    if (arr == null || arr.length == 0) {
      return;
    }

    for (int i = 0; i < arr.length - 1; i++) {
      for (int j = 0; j < arr.length - 1 - i; j++) {
        if (arr[j].compareTo(arr[j + 1]) > 0) {
          IComparable temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }
  }
}
```

可以看出，这个类 `ComparableUtils` 是针对 `IComparable` 接口编程，它并不知道具体的对象类型是什么，也并不关心，但却可以对任意实现了 `IComparable` 接口的类型进行操作。

```java
Point[] points = new Point[] {
  new Point(2, 3),
  new Point(3, 4),
  new Point(1, 2)
};

IComparable max = ComparableUtils.max(points);
System.out.println("max: " + max); // max: Point(3,4)

ComparableUtils.sort(points);
System.out.println("sort: " + Arrays.toString(points)); // sort: [Point(1,2), Point(2,3), Point(3,4)]
```

这里代码示例的是对 Point 数组操作，实际上可以针对任何实现了 IComparable 接口的类型数组进行操作。这就是接口的威力，可以说，针对接口而非具体类型进行编程，是计算机程序的一种重要思维方式。接口很多时候反映了对象以及对对象操作的本质。

接口的优点：

- 首先是代码复用，同一套代码可以处理多种不同类型的对象，只要这些对象都有相同的能力，如 ComparableUtils。
- 接口更重要的是降低了耦合，提高了灵活性。使用接口的代码依赖的是接口本身，而不是具体的实现类。程序可以根据情况替换接口的实现，而不影响使用接口的代码。

## 实现多个接口

接口与继承最大的区别是，类可以实现多个接口，而继承只能继承一个类。

一个类如果要实现多个接口，可以使用逗号将想要实现的各个接口分隔开。

```java
interface IComparable {
  int compareTo(Object o);
}

interface ICloneable {
  Object clone();
}

interface IMovable {
  void move();
}

class Point implements IComparable, ICloneable, IMovable {
  // ...
}
```

## 接口的多态

接口的多态，实现机制上也是通过接口方法的重写实现，在多态的表现为变量使用接口类型进行声明，然后在运行时赋值实现了该接口的类的对象，调用重写了接口定义方法的实现。

```java
// 支付接口
interface IPayment {
    void pay(double amount);
}

// 微信支付实现
class WeChatPay implements IPayment {
    @Override
    public void pay(double amount) {
        System.out.println("使用微信支付：" + amount + " 元");
    }
}

// 支付宝支付实现
class AliPay implements IPayment {
    @Override
    public void pay(double amount) {
        System.out.println("使用支付宝支付：" + amount + " 元");
    }
}

// 新增信用卡支付实现
class CreditCardPay implements IPayment {
    @Override
    public void pay(double amount) {
        System.out.println("使用信用卡支付：" + amount + " 元");
    }
}

// 测试支付系统
public class PaymentTest {
    public static void main(String[] args) {
        Payment payment;

        payment = new WeChatPay();
        payment.pay(100); // 输出：使用微信支付：100 元

        payment = new AliPay();
        payment.pay(200); // 输出：使用支付宝支付：200 元

        payment = new CreditCardPay();
        payment.pay(300); // 输出：使用信用卡支付：300 元
    }
}
```

变量 payment 可以被赋值任何实现了 IPayment 接口的对象，然后在运行时，根据被赋值对象的实现类型，分别实现 WeChatPay、AliPay 或 CreditCardPay 的支付功能，这就是接口实现的多态。

对于变量 payment，同样有静态类型和动态类型：

- 静态类型（编译时类型）：IPayment
- 动态类型（运行时类型）：WeChatPay、AliPay 或 CreditCardPay

调用 `payment.pay()` 方法时，同样会根据对象的动态类型，调用对应的 pay 方法，这就是接口多态的表现。

## 接口的继承

接口也可以继承，一个接口可以继承其他接口，同样使用 `extends` 关键字实现接口继承。但与类的继承不一样的是，接口继承可以有多个父接口，用逗号隔开。

```java
interface IPowered extends IMovable, ICloneable {
  void powerOn();
  void powerOff();
}
```

## 类的继承和接口实现共存

类的继承与接口可以共存，换句话说，类可以在继承基类的情况下，同时实现一个或多个接口，语法规则：关键字 `extends` 要放在 `implements` 之前。如下所示：

```java
class Circle extends Shape implements IComparable, ICloneable, IMovable {
  // ...
}
```

## 使用接口和组合代替继承

类的继承有两个好处：

- 复用代码，子类可以直接使用父类的方法和属性，避免重复编写代码。
- 多态，利用动态绑定原理，将变量声明为父类型，统一一处理多种不同子类的对象。

但类的继承也有两大缺点：

- 脆弱的基类，当基类发生变化时，所有子类都必须受到影响，这是一种脆弱的设计。
- 限制了类的灵活性，因为类只能继承一个父类，这限制了类的功能。

解决办法：

- 使用组合替代继承，可以复用代码，但不能统一处理。
- 使用接口替代继承，可以实现统一处理不同类型的对象，但因为接口没有代码实现，无法复用代码。

所以将组合和接口结合起来替代继承，既可以统一处理，又可以复用代码了。可以简单理解：`组合 + 接口 = 继承`

```java
public interface IAdd {
  void add(int number);
  void addAll(int[] numbers)
}

public class Base implements IAdd {
  @Override
  public void add(int number) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addAll(int[] numbers) {
    // TODO Auto-generated method stub
  }
}

public class Child implements IAdd {
  public Child(Base base) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void add(int number) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addAll(int[] numbers) {
    // TODO Auto-generated method stub
  }
}

```

Child 复用了 Base 的代码，又都实现了 IAdd 接口，这样，既复用代码，又可以统一处理，还不用担心破坏封装，因为接口约束了 Base 不能再随意接口约定的方法。

## 增强的接口：静态方法和默认方法

Java8 和 Java 9 对接口做了一些增强。在 Java 8 之前，接口中的方法都是抽象方法，都没有实现体，Java 8 允许在接口中定义两类新方法：**静态方法**和**默认方法**，它们有实现体。

```java
public interface IDemo {
  void hello();

  // 静态方法
  public static void test() {
    System.out.println("test static");
  }

  // 默认方法
   default void hi() {
    System.out.println("hi Default");
  }
}
```

- `test` 是一个接口静态方法，可以通过 `IDemo.test()` 调用。
- `hi()`是一个默认方法，用关键字 `default` 表示。默认方法与抽象方法都是接口的方法，不同在于，默认方法有默认的实现，实现类可以改变它的实现，也可以不改变。引入默认方法主要是函数式数据处理的需求，是为了便于给接口增加功能。关于函数式数据处理，后面会有专题介绍。

> 在没有默认方法之前，Java 是很难给接口增加功能的，比如 `List` 接口 ​，因为有太多非 Java JDK 控制的代码实现了该接口，如果给接口增加一个方法，则那些接口的实现就无法在新版 Java 上运行，必须改写代码，实现新的方法，这显然是无法接受的。

## instanceof 运算符

instanceof 不仅可以用于类，也可以用于接口。功能是检查引用指向的对象是否可以看作指定的类或接口。

```java
对象 instanceof <类或接口>
```

当引用指向的对象可以看作指定的类或接口类型时返回 true，否则返回 false

## 总结

1. 接口中定义的变量是常量，隐含是 `public static final` 的，并且在声明时必须初始化。
2. 接口中定义的方法是抽象方法，隐含是 `public abstract` 的。
3. Java 8 允许在接口中定义静态方法和默认方法，它们有实现体。
4. 类实现接口时，必须实现接口中的所有抽象方法，否则类必须是抽象类。
5. 接口可以继承其他接口，使用`extends`关键字。
6. 类可以实现多个接口，使用`implements`关键字。
7. 可以使用 `instanceof` 运算符检查对象是否是某个类或接口的实例。
