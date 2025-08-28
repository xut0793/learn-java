# 方法

方法通常用来描述一个对象的一种行为，一种功能，一个操作。决定了这个对象能够接收什么样的消息，做出什么样的反应。

## 方法的定义

方法的定义格式为：

```java
访问修饰符 数据类型 方法名(参数列表) {
  // 方法体
}
```

- 方法只能定义在类中，但是不能在方法中定义方法。
- 访问修饰符：用于控制方法的被访问权限，常用的有 `public`、`private`、`protected`、`default` 等，统一在[访问修饰符](./oop_access_modifier.md)中介绍。
- 数据类型：用于指定方法的返回值类型，如果方法没有返回值，则使用 `void` 关键字。
- 方法名：用于标识方法的名称，遵循小驼峰命名法。
- 参数列表：用于指定方法的参数，参数之间用逗号隔开，每个参数由**数据类型和参数名**组成。
- 方法体：用于实现方法的功能，包含一系列的语句。要返回一个值，需要使用 `return` 语句。

代码示例

```java
public class MethodDemo {
  // 定义方法
  public int add(int a, int b) {
    int result = a + b;
    return result;
  }
}
```

## 方法的调用

方法的调用格式为：

```java
对象名.方法名(参数列表);
```

- 对象名：用于指定调用方法的对象。
- 方法名：用于指定调用的方法名称。
- 参数列表：用于指定调用方法时传递的参数，参数之间用逗号隔开。

代码示例

```java
public class MethodDemo {
  // 定义方法
  public int add(int a, int b) {
    int result = a + b;
    return result;
  }
  // 调用方法
  public static void main(String args[]) {
    MethodDemo demo = new MethodDemo();
    int result = demo.add(10, 20);
    System.out.println(result);
  }
}
```

## 方法的参数传递

方法的参数传递是指调用方法时，将参数传递给方法。

参数传递有两种方式：值传递和引用传递。

- 值传递：将参数的值复制一份传递给方法，方法内部对参数的修改不会影响到方法外部的参数。
- 引用传递：将参数的引用传递给方法，方法内部对参数的修改会影响到方法外部的参数。

> 也有一种说法：Java 只有值传递，因为引用传递也是将引用对象的地址值进行传递，使得新变量指向同一个对象。

```java
public class MethodDemo {
  int[] arr = {1, 2, 3, 4, 5};
  int a = 10;
  // 定义方法
  public void append(int[] arr, int a) {
    arr[arr.length - 1] = a;
    a++;
    System.out.println("append 方法中的 a = " + a); // 11
    System.out.println("append 方法中的 arr = " + arr); // [1, 2, 3, 4, 10]
  }

  // 调用方法
  public static void main(String args[]) {
    MethodDemo demo = new MethodDemo();
    demo.append(demo.arr, demo.a);
    System.out.println("main 方法中的 a = " + demo.a); // 10
    System.out.println("main 方法中的 arr = " + demo.arr); // [1, 2, 3, 4, 10]
  }
}
```

引用类型的参数 arr 被更改了，但是基本类型的参数 a 没有被更改。

这是因为引用类型的参数传递的是引用对象的地址值，而基本类型的参数传递的是参数的值。

## 静态方法 static

类的静态方法是指使用 `static` 关键字修饰的方法。

- 静态方法可以直接通过类名调用，不需要创建对象。
- 静态方法不能访问非静态的成员变量和非静态的方法，即不能访问实例对象的成员变量和方法。
- 静态方法只能访问其它静态的成员变量和静态的方法。

```java
public class MethodDemo {
  // 定义静态方法
  public static void print() {
    System.out.println("Hello World!");
  }
  public static void main(String args[]) {
    // 不需要通过实例对象，就可以调用静态方法
    MethodDemo.print();
  }
}
```

## 方法签名

方法签名是指方法的名称和参数列表，用于唯一标识一个方法。

比如 String 类有 4 个名为 indexOf 的公共方法，它们的方法签名分别为：

- `int indexOf(int ch)`
- `int indexOf(int ch, int fromIndex)`
- `int indexOf(String str)`
- `int indexOf(String str, int fromIndex)`

注意，修饰符和返回值类型不是方法签名的一部分，只有方法名和参数列表才是方法签名的一部分。

因为在程序中方法可以直接调用，不需要创建对象，也不需要明确返回值，所以这种情况下，对编译器来说来确定具体执行哪个方法也只有方法名和参数列表。

```java
public class MethodDemo {
  public int a = 0;

  public void increment() {
    a++;
  }

  public void decrement() {
    a--;
  }

  public int getValue() {
    // 此时编译器只能根据方法名和参数列表来确定调用哪个方法
    increment();
    decrement();
    return a;
  }
}
```

## 方法的重载

方法的重载是指在同一个类中定义多个方法，方法名相同，但参数列表不同。

参数列表不同指的是参数的类型、参数的个数、参数的顺序不同。

> 在实践项目中，如果仅仅重载方法仅仅只是参数顺序不同，是不推荐的。因为这样会导致调用方法时，参数的顺序错误，从而导致程序运行错误。
>
> 比如有一个方法 `add(int a, int b)`，如果又重载一个为 `add(int b, int a)`，那么在调用 `add(1, 2)` 时，就会导致程序运行错误。

代码示例

```java
public class MethodDemo {
  // 定义方法
  public int add(int a, int b) {
    int result = a + b;
    return result;
  }
  // 定义方法
  public double add(double a, int b) {
    double result = a + b;
    return result;
  }
}
```

## 方法的隐藏参数 this

在 Java 中，每个方法都有一个隐藏的参数 `this`，用于引用当前对象。

- 当方法被调用时，`this` 会自动传递给方法。
- 可以在方法中使用 `this` 来引用当前对象的成员变量和方法。

代码示例

```java
public class MethodDemo {
  int a = 10;
  // 定义方法
  public void append(int[] arr, int a) {
    this.a = a;
    arr[arr.length - 1] = a;
    System.out.println("append 方法中的 a = " + this.a);
    System.out.println("append 方法中的 arr = " + arr);
  }
  // 调用方法
  public static void main(String args[]) {
    MethodDemo demo = new MethodDemo();
    demo.append(demo.arr, demo.a);
  }
}
```
