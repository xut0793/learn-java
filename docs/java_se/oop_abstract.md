# 抽象类

顾名思意，抽象类就是抽象的类。抽象是相对于具体而言的，一般来说，具体类的都有对应实例对象的具体实现，而抽象类则不能直接使用 new 进行实例化。

从面向对象的角度来看，类是现实世界中事物分类在计算机世界的一种映射。现实世界中，对具体事物的分类之上也都会有一个抽象的类别，比如，狗是具体的对象，而动物是抽象的类别；苹果是具体的对象，而水果是抽象的类别。

在 java 语言层面上，抽象类是一种特殊的类，它有如下特征：

1. 使用 `abstract` 关键字修饰类，表示该类是抽象类；
2. 抽象类不能使用 `new` 关键字实例化；只能用于被继承
3. 抽象类可以包含抽象方法，也可以没有抽象方法。但如果一个类包含了抽象方法，那么该类必须被声明为抽象类；
4. 抽象类不能被 `final` 修饰，因为 `final` 修饰的类不能被继承，但抽象类只能被继承使用，二者矛盾；

## 抽象类定义

```java
public abstract class 类名 {
  // 其它代码
}
```

抽象类的命名一般以 `Abstract` 或 `Base` 开头，比如 `AbstractAnimal`、`BaseAnimal` 等。

在具体类内部可以定义的内容，抽象类也可以定义，包含静态变量、静态方法、静态代码块、实例变量、实例方法、构造方法、实例代码块等。

但作为抽象类最主要的特征是可以定义抽象方法，抽象方法是一种只有方法签名，没有方法体的方法，要求必须在子类中被实现。

## 抽象方法

抽象方法同抽象类一样，通过 `abstract` 关键字定义修饰，定义格式如下：

```java
public abstract 返回值类型 方法名(参数列表);
```

抽象方法的特征是：

1. 没有方法体，只有方法签名，要求必须在子类中被实现。
2. 抽象方法必须被 `public`、`protected` 或 `default` 访问修饰符修饰，不能被 `private` 访问修饰符修饰。
3. 抽象方法不能被 `final` 或 `static` 关键字修饰，因为 `final` 和 `static` 修饰的方法不能被重写，但抽象方法要求被子类重写实现。

## 抽象类的使用

抽象类不能直接使用 `new` 关键字实例化，只能用于被继承。

```java
public class 子类名 extends 抽象类名 {
  // 实现抽象方法
  @Override
  public 返回值类型 方法名(参数列表) {
    // 方法体
  }
}
```

子类必须实现抽象类中的所有抽象方法，除非子类也是抽象类。

## 代码示例

```java
/**
 * 定义一个抽象类
 */
abstract class Shape {
    // 抽象类可以有成员变量
    protected String color;

    // 构造函数
    public Shape(String color) {
        this.color = color;
    }

    // 抽象类可以有非抽象方法
    public void displayColor() {
        System.out.println("Color of the shape is: " + color);
    }

    // 抽象方法，没有方法体
    public abstract double calculateArea();
}

/**
 * 继承抽象类
 */
class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

/**
 * 实例化子类
 */
public class TestAbstractDemo {
    public static void main(String[] args) {
        // 生成一个圆对象
        Circle circle = new Circle("Red", 5.0);
        // 调用非抽象方法，访问成员变量
        circle.displayColor();
        // 调用抽象方法
        double area = circle.calculateArea();
        System.out.println("Area of the circle is: " + area);
    }
}
```

## 为什么需要抽象类

在 [继承](oop_inheritance.md) 章节中，我们提到继承的缺点之一，**继承破坏了封装性**，主要体现在：

- 对于子类而言，通过继承实现是没有安全保障的，因为父类修改内部实现细节，它的功能就可能会被破坏；
- 而对于基类而言，让子类能继承和随意重写方法，也是破坏了基类封装的目的，而且也让父类丧失了能自由修改内部实现的自由。

引入抽象方法和抽象类，是 Java 提供的一种语法工具，用来约束了类的行为，引导使用者正确使用它们，减少误用。当类继承于某个抽象类时，子类就知道它必须要实现所有的抽象方法，而不可能忽略，如果被忽略 Java 编译器会提示错误。

无论是编写程序，还是平时做其他事情，每个人都可能会犯错，减少错误不能只依赖人的优秀素质，还需要一些机制，使得一个普通人都容易把事情做对，而难以把事情做错。抽象类就是 Java 提供的这样一种机制，就像 `@Override` 注解对方法重写的安全性和正确性进行检查一样，java 在语言层面提供这些约束机制，让开发者把事情做对，不容易做错。
