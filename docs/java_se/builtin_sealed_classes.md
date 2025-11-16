# 密封类

密封类（Sealed Classes）是 Java 15 中引入的一项预览特性，并在 Java 17 中正式成为标准特性。密封类用于限制哪些类可以继承或实现某个类或接口。

## 语法

修饰符说明

- **sealed**：声明密封类
- **permits**：指定允许继承的子类
- **final**：最终类，不能被继承
- **non-sealed**：非密封类，可以被任意继承

```java
// 密封类的声明
public sealed class Person permits Employee, Manager, Customer {
    // 类的成员
}

// 子类声明
public final class Employee extends Person {
    // 员工类
}

public non-sealed class Manager extends Person {
    // 经理类，可以被进一步继承
}

public sealed class Customer extends Person permits VipCustomer, RegularCustomer {
    // 客户类，允许特定子类继承
}

// 具体的最终子类
public final class VipCustomer extends Customer {
    // VIP 客户
}

public final class RegularCustomer extends Customer {
    // 普通客户
}
```

## 特点

### 1. 编译时控制

密封类在编译时就能确定所有可能的子类，编译器可以基于这个信息进行更精确的类型检查。

```java
public sealed interface Shape permits Circle, Rectangle, Triangle {
    double getArea();
}

public final class Circle implements Shape {
    private double radius;

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

public final class Rectangle implements Shape {
    private double width, height;

    @Override
    public double getArea() {
        return width * height;
    }
}

public record Triangle(double base, double height) implements Shape {
    @Override
    public double getArea() {
        return 0.5 * base * height;
    }
}
```

### 2. 模式匹配支持

密封类与模式匹配（Pattern Matching）结合使用，可以实现穷举性检查。

```java
public double calculateArea(Shape shape) {
    return switch (shape) {
        case Circle c -> c.getArea();
        case Rectangle r -> r.getArea();
        case Triangle t -> t.getArea();
        // 编译器会检查是否穷举了所有情况
    };
}
```

### 3. 层级结构控制

允许创建受控的继承层级，确保类的层次结构按照预定义的方式发展。

```java
public sealed abstract class Animal permits Mammal, Bird, Fish {
    public abstract void makeSound();
}

public non-sealed abstract class Mammal extends Animal {
    @Override
    public void makeSound() {
        System.out.println("哺乳动物叫声");
    }

    public abstract void move();
}

public final class Dog extends Mammal {
    @Override
    public void makeSound() {
        System.out.println("汪汪");
    }

    @Override
    public void move() {
        System.out.println("用四条腿跑");
    }
}

public final class Cat extends Mammal {
    @Override
    public void makeSound() {
        System.out.println("喵喵");
    }

    @Override
    public void move() {
        System.out.println("用四条腿跑");
    }
}
```

## 应用场景

### 1. 领域建模

在领域建模中，密封类可以精确表示有限的概念集合。

```java
// 订单状态
public sealed interface OrderState permits Pending, Processing, Shipped, Delivered, Cancelled {
}

public record Pending() implements OrderState {
    public void submit() {
        System.out.println("订单已提交");
    }
}

public record Processing() implements OrderState {
    public void ship() {
        System.out.println("订单已发货");
    }
}

public record Shipped() implements OrderState {
    public void deliver() {
        System.out.println("订单已送达");
    }
}

public record Delivered() implements OrderState {
    // 已完成状态
}

public record Cancelled() implements OrderState {
    // 已取消状态
}
```

### 2. 表达式求值

在构建表达式系统时，密封类可以确保所有表达式类型都被正确处理。

```java
public sealed interface Expression permits NumberExpression, BinaryExpression, UnaryExpression {
    double evaluate();
}

public final class NumberExpression implements Expression {
    private final double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }
}

public final class BinaryExpression implements Expression {
    private final Expression left;
    private final String operator;
    private final Expression right;

    public BinaryExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public double evaluate() {
        double leftValue = left.evaluate();
        double rightValue = right.evaluate();

        return switch (operator) {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> leftValue / rightValue;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
```

## 与其他继承方式的区别

1. 与普通继承的区别
   - **普通继承**：任何类都可以继承父类，无法控制继承关系
   - **密封类**：严格控制哪些类可以继承，实现受控的继承体系
2. 与抽象类的区别
   - **抽象类**：可以被子类任意继承
   - **密封类**：限定继承范围，同时可以包含抽象方法
3. 与接口的区别
   - **接口**：多个类可以实现同一个接口
   - **密封类**：限定哪些类可以作为子类，更强的层次结构控制

## 优势

1. 类型安全：编译时穷举检查，确保所有可能的子类都被处理。
2. 设计控制：明确表达设计意图，限制扩展的方式。
3. 性能优化：编译器可以基于密封类的信息进行优化。
4. 更好的 IDE 支持：IDE 可以基于密封信息提供更精确的代码补全和重构建议。

## 注意事项

1. **Java 版本要求**：需要 Java 17+ 才能使用密封类
2. **迁移成本**：现有代码可能需要重构来使用密封类
3. **设计复杂性**：需要在设计时考虑所有可能的子类

## 总结

密封类为 Java 提供了强大的继承控制机制，特别适合以下场景：

- 需要精确控制继承层次结构
- 存在有限且预定义的类型集合
- 需要配合模式匹配进行穷举性检查

通过合理使用密封类，可以构建更加安全、可维护和类型安全的 Java 应用程序。
