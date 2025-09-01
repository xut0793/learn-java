# 继承

在[理解面向对象](./oop_index.md)章节说过，软件开发的过程就是使用计算机语言将现实世界（问题域）映射到计算机世界（目标域）的过程。而面向对象技术从组成客观世界的对象着眼，通过抽象，将对象映射到计算机系统中，又通过模拟对象之间的相互作用、相互联系来模拟客观世界，描述客观世界的运动规律。

在现实世界的事物中，有一种非常重要关系，就是分类。分类有个根，然后向下不断细分化，形成一个有层次的分类体系。比如：

- 自然界中，生物有动物和植物，动物又有不同层级分类（界门纲目科属种），比如：动物界 -> 脊索动物门 -> 哺乳纲 -> 灵长目 -> 猴科 -> 金丝猴属 -> 川金丝猴(种)
- 电商网站中，对商品也有不同的分类列表，比如：服装 -> 男装 -> 衬衫 -> 长袖 -> 尺寸 xl -> 颜色 红色

在面向对象编程语言中，使用类的继承关系来描述对象之间的分类关系。在继承关系中，有两种角色：

- 父类（也称为超类或基类）：父类是一个更通用的类，它定义了一些共同的属性和方法，继承子类直接就有这些属性和方法，不需要重新定义。
- 子类（也称为派生类）：子类是一个更具体的类，它继承了父类的属性和方法，并且可以添加自己独特的属性和方法。

父类和子类的角色关系是相对的，一个类 B 可以作为另一个类 A 的子类，也可以作为类 C 的父类。(A -> B -> C)

比如：

- 动物类（Animal）是一个父类，可以定义一个子类：狗类（Dog），然后再细分一种具体的狗类：哈士奇（Husky）
- 服装类（Clothing）是一个父类，它有子类：男装类（MenClothing），然后再细分一种具体的男装类：衬衫类（Shirt）
- 图形类（Shape）是一个父类，它有子类：矩形类（Rectangle），然后再细分一种具体的矩形类：正方形类（Square）

在面向对象编程语言中，继承有两方面的作用：

- 是对现实世界分类体系的抽象和映射
- 代码的复用，子类继承父类的属性和方法，从而实现代码的复用。

在 Java 编程语言中，关于继承有以下几点特征：

- Java 的继承体系采用**单根继承**，一个类只能有一个直接父类。
- Java 中定义的类，默认继承自 `java.lang.Object` 类。也就是说 `Object` 类是所有类的最终的基类。

## 继承的语法

```java
class 子类 extends 父类 {
    // 子类的属性和方法
}
```

代码示例

```java
public class Shape {
    public String toString() {
        return "我是一个形状";
    }
}
public class Rectangle extends Shape {
    public String toString() {
        return "我是一个矩形";
    }
}
public class Square extends Rectangle {
    public String toString() {
        return "我是一个正方形";
    }
}
```

## 基类 Object

Java 中，实际上所有的类均直接或间接地继承自 `java.lang.Object` 类，也可以说 `Object` 类是 Java 中的总根类。

在实际开发中，即使没有显式地继承自某个类，也会默认继承自 `Object` 类。

```java
public class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public static void main(String[] args) {
    Point p = new Point(2, 3);
    System.out.println(p.toString()); // Point@76f9aa66
  }
}
```

调用 p 对象的 `toString` 方法，但是我们在 `Point` 类中并没有定义 `toString` 方法，为啥可以直接调用呢？这个方法就是 `Object` 类中的 `toString` 方法。

```java
public String toString() {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
```

> `@` 符号之前是类名，通过调用 `getClass().getName()` 返回，`@` 符号之后是对象的哈希码（hash code），哈希码是一个十六进制的数字，用于表示当前对象存储的堆内存地址。

那么子类到底能继承父类的哪些内容呢？

- 构造函数不能被继承，但可以通过 `super` 关键字调用父类的构造函数。
- 静态变量和静态方法不能被继承，但是可以通过类名直接访问。
- 成员变量和方法中被 `private` 修饰的不能被继承，但是可以通过 `public` 修饰的 getters 和 setters 方法访问。
- 其它所有非 `private` 修饰的成员变量和方法可以被继承。

## 构造函数不能被继承

子类不能继承父类的构造函数，但是通过 `super` 关键字实现构造函数的级联调用。具体见[构造函数](./oop_constructor.md)章节。

## 静态变量和静态方法不能被继承

静态变量和静态方法是属于类自身的数据和操作，不能被子类继承，但是子类可以通过`类名.xxx`进行访问和操作。

## private 私有变量不能被继承

`private` 修饰的变量和方法不能被继承，但是可以通过 `public` 修饰的 getters 和 setters 方法访问。

## 非 private 变量和方法可以被继承

非 `private` 修饰的变量和方法可以被继承，也就是子类可以继承父类的所有 `public / protected / default` 修饰的成员变量和方法。子类可以直接进行访问。

对于继承的子类中对成员变量和方法的访问规则，可以在 [多态](./oop_polymorphism.md) 章节中学习。

以下是 Java 中可继承和不可继承内容的总结：

| 内容类型                          | 是否可继承 | 说明                                                                               |
| --------------------------------- | ---------- | ---------------------------------------------------------------------------------- |
| 构造函数                          | 否         | 不能被继承，但可通过 `super` 关键字调用父类的构造函数                              |
| 静态变量和静态方法                | 否         | 属于类自身的数据和操作，不能被子类继承，但子类可通过 `类名.xxx` 进行访问和操作     |
| `private` 修饰的成员变量和方法    | 否         | 不能被继承，但可通过 `public` 修饰的 getters 和 setters 方法访问                   |
| 非 `private` 修饰的成员变量和方法 | 是         | 子类可继承父类所有 `public / protected / default` 修饰的成员变量和方法，可直接访问 |

## 方法重写

当继承的父类的方法不能满足子类现在的需求时，子类可以对方法进行重写。

方法重写（Override）是指在子类中重新定义父类的方法，以实现子类特有的行为。

方法重写的规则：

- 方法名、参数列表、返回类型必须与父类的方法完全相同。
- 访问权限不能比父类的方法更严格。
- 不能抛出比父类方法更多的异常。
- 可以在子类方法中使用 `@Override` 注解来显式地表示这是一个重写方法。

> @Override 是 Java 中的一个注解，用于标识子类方法重写了父类中的方法，并在编译时进行正确性检查。虽然不强制使用，但使用 @Override 注解可以提高代码的安全性、可读性和可维护性。可以在 [注解](./oop_annotation.md) 章节学习更多关于注解的知识。

```java
public class Point {
    private int x;
    private int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }
}
```

> 注意区分方法重写和方法重载的区别。
>
> - 方法的重载是指在同一个类中定义多个方法，方法名相同，但参数列表不同。
> - 方法的重写是指在子类中重新定义父类的方法，方法名、参数列表、返回类型必须与父类的方法完全相同或兼容。

## 防止继承 final

如果不希望某个类被继承，或者不希望某个方法被重写，就可以使用 `final` 关键字进行修饰。

- 类：使用 `final` 关键字修饰的类，不能被继承。
- 方法：使用 `final` 关键字修饰的方法，不能被重写。

## 代码示例：图形类的继承体系

<<< ../../exercise/oop/ShapeInheritance.java#ShapeManager

执行

<<< ../../exercise/oop/ShapeInheritance.java#execute

执行结果

```
Drawing a circle at Point(4, 4)with radius 2, using color: black
Drawing a line from Point(2, 3) to Point(2, 3), using color: green
Drawing a line from Point(1, 2) to Point(1, 2), using color: red
Drawing a end arrow
```

## 继承的缺点

> 摘自 《Java 编程的逻辑》 4.4

继承其实是把双刃剑：

- 一方面继承是非常强大的，使用都可以通过继承复用大量代码，提高代码的复用率。不管是在各种 Java 内部类中，还是各种框架和类库中。
- 另一方面继承的破坏力也是很强的。继承为什么会有破坏力呢？主要是因为继承可能破坏封装，而封装可以说是程序设计的第一原则；另外，继承可能没有反映出 is-a 关系。

### 继承破坏封装

封装就是隐藏实现细节，提供简化接口。使用者只需要关注怎么用，而不需要关注内部是怎么实现的。实现细节可以随时修改，而不影响使用者。

继承可能破坏封装是因为子类和父类之间可能存在着实现细节的依赖。子类在继承父类的时候，往往不得不关注父类的实现细节，而父类在修改其内部实现的时候，如果不考虑子类，也往往会影响到子类。

```java
/**
 * 继承破坏封装例子
 * 出处：《Java 编程的逻辑》4.4中代码清单 4-10
 */
class Base {
  private static final int MAX_NUM = 1000;
  private int[] arr = new int[MAX_NUM];
  private int count;

  public void add(int number) {
    if (count < MAX_NUM) {
      arr[count++] = number;
    }
  }

  public void addAll(int[] numbers) {
    for (int number : numbers) {
      add(number);
    }
  }
}

class Child extends Base {
  private long sum;

  @Override
  public void add(int number) {
    super.add(number);
    sum += number;
  }

  @Override
  public void addAll(int[] numbers) {
    super.addAll(numbers);
    for (int number : numbers) {
      sum += number;
    }
  }

  public long getSum() {
    return sum;
  }
}

public class Inheritance_violates_encapsulation {
  public static void main(String[] args) {
    Child c = new Child();
    c.addAll(new int[] { 1, 2, 3 });
    System.out.println(c.getSum()); // 期望输出是 6（1+2+3)，但实际输出12
  }
}
```

期望的输出是 1+2+3=6，实际输出为 12。为什么是 12 呢？

查看代码不难看出，同一个数字被汇总了两次。子类的 addAll 方法首先调用了父类的 add-All 方法，而父类的 addAll 方法通过 add 方法添加，由于动态绑定，在执行父类的 addAll 方法中调用的 add 方法，实际执行的子类的 add 方法会执行，子类的 add 也会做了汇总操作，所以结果错误。

可以看出，**如果子类不知道基类方法的实现细节，它就不能正确地进行扩展**。

此时针对错误，我们修改子类的 addAll 方法的实现，去掉多余的累加语句。

```java
@Override
public void addAll(int[] numbers) {
  super.addAll(numbers);
}
```

再执行，输出正确的结果 6。

但是如果此时，基类 Base 决定重构 addAll 方法的实现，它不再通过调用 add 方法添加元素，而是直接添加。

```java
public void addAll(int[] numbers) {
  for (int number : numbers) {
    // add(number);
    if (count < MAX_NUM) {
      arr[count++] = number;
    }
  }
}
```

修改了基类的内部实现后，子类程序的调用结果又错了。所以可以看出，子类和父类之间是细节依赖，子类扩展父类，仅仅知道父类能做什么是不够的，还需要知道父类是怎么做的，而父类的实现细节也不能随意修改，否则可能影响子类。

也就是说，**当子类需要重写父类方法时，需要清楚地知识父类方法的实现细节，包括方法之间的依赖关系，而且这个依赖关系不能随意改变。一旦这个依赖关系改变，子类的封装就会被破坏。**

另外一个场景，将上述示例代码恢复到初始状态，子类的 getSum 方法能正确地计算总和。此时基类 Base 类需要增加一个清空的方法 clear。

```java
class Base {
  private static final int MAX_NUM = 1000;
  private int[] arr = new int[MAX_NUM];
  private int count;

  public void add(int number) {
    if (count < MAX_NUM) {
      arr[count++] = number;
    }
  }

  public void addAll(int[] numbers) {
    for (int number : numbers) {
      add(number);
    }
  }

  public void clear() {
    for (int i = 0; i < count; i++) {
      arr[i] = 0;
    }
    count = 0;
  }
}
```

基类添加一个方法不需要告诉子类，Child 类不知道 Base 类添加了这么一个方法，但因为继承关系，Child 类却自动拥有了这么一个方法。因此，Child 类的使用者可能会这么使用 Child 类的 clear 方法。此时执行以下代码：

```java
public class Inheritance_violates_encapsulation {
  public static void main(String[] args) {
    Child c = new Child();
    c.addAll(new int[] { 1, 2, 3 });
    System.out.println(c.getSum()); // 6
    c.clear();
    c.addAll(new int[] { 1, 2, 3 });
    System.out.println(c.getSum()); // 12
  }
}
```

先添加一次，之后调用 clear() 清空，又添加一次，最后输出 sum, 期望输出 6，实际输出 12。这时因为 clear 方法仅有清空 Base 类的逻辑，并没有重置子类 Child 内部的 sum 值。

为了解决这个问题，我们需要在子类中重写 clear 方法，添加重置 sum 值的逻辑。

```java
class Child extends Base {
  private long sum;

  @Override
  public void add(int number) {
    super.add(number);
    sum += number;
  }

  @Override
  public void addAll(int[] numbers) {
    super.addAll(numbers);
    for (int number : numbers) {
      sum += number;
    }
  }

  @Override
  public void clear() {
    super.clear();
    sum = 0;
  }

  public long getSum() {
    return sum;
  }
}
```

可以看出，父类不能随意增加公开方法，因为给父类增加就是给所有子类增加，而子类可能必须要重写该方法才能确保方法的正确性。

总结：

- 对于子类而言，通过继承实现是没有安全保障的，因为父类修改内部实现细节，它的功能就可能会被破坏；
- 而对于基类而言，让子类能继承和随意重写方法，也是破坏了基类封装的目的，而且也让父类丧失了能自由修改内部实现的自由。

### 继承没有反映 is-a 关系

继承关系是设计用来反映 is-a 关系的，子类是父类的一种，子类对象也属于父类，父类的属性和行为也适用于子类。就像橙子是水果一样，水果有的属性和行为，橙子也必然都有。

但现实中，设计完全符合 is-a 关系的继承是困难的。在某些分类中，可能绝大多数子类都会遵循同一种行为，但是就是有个别特例会违背这种统一行为规范。比如绝大部分鸟都会飞，可能就会给鸟类添加一个 fly 方法表示飞行，但是就有一些鸟不会飞，比如驼鸟。

还有一点，Java 并没有办法强约束父子类严格符合 is-a 关系，父类有的属性和行为，子类并不一定都适用，子类还可以重写方法，实现与父类预期完全不一样的行为。

## 应对继承的双面性

继承既强大又有破坏性，那怎么办呢？

1. 避免使用继承：
   1. 使用 final 关键字修饰类，防止被继承；
   2. 优先使用组合而非继承；
   3. 使用抽象类约束父类的行为；
   4. 使用接口。
2. 正确使用继承：
   1. 如果基类是别人写的，我们写子类。此时需要注意，重写的方法不要改变预期的行为；阅读文档说明，理解可重写方法的实现机制，特别是方法之前的依赖关系；在基类修改的情况下，及时更新子类代码。
   2. 如果我们写基类，别人可能写子类。此时需要注意，尽量反映真正的 is-a 关系，只将真正公共的部分放到基类；对不希望被子类重写的公开方法添加 final 修饰符阻止被重写；完善文档，明确子类应该如何重写；基类修改时，写修改说明；
