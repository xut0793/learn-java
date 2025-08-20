# 构造器

构造器是一种特殊的方法，用于创建和初始化对象。它有以下语法规则：

- 构造器的名称与类名相同，大小写也要一样
- 构造器在创建对象（执行 `new` 语句）时调用
- 构造器没有返回值类型，也不能有 `void` 关键字
- 构造器可以有访问修饰符，如果没有声明访问修饰符，默认是 `default` 访问修饰符
- 构造器不会被继承，子类需要通过 `super` 显式调用
- 构造器可以重载，即一个类可以有多个构造器，构造器可以有参数，也可以没有参数
- 如果没有声明构造器，Java 会自动提供一个无参数的构造器
- 构造器可以调用其他构造器，但是必须放在构造器的第一行（java 22 开始可以不在第一行）
- 构造器可以有异常声明，也可以没有异常声明

## 构造器名称与类名相同

```java
class 类名 {
    访问修饰符 类名(参数列表) {
        // 构造器体
    }
}
```

示例代码

```java
class Animal {
    // 构造器
    public Animal() {
        // 构造器体
    }
}
```

## 构造器调用，创建对象

创建对象时，使用 `new` 操作符，后边加上类名及括号，其实就是在调用对应的构造器。

```java
Animal animal = new Animal();
```

如果声明的构造器有参数，那么在创建对象时，就必须传递对应的参数。

```java
class Animal {
    String name;
    public Animal(String name) {
      this.name = name;
    }
}

class TestConstructor {
  public static void main(String[] args) {
    Animal animal = new Animal("动物");
    System.out.println(animal.name);
  }
}
```

## 构造器没有返回值,也不能直接当方法调用

构造器是没有返回值的，所以定义时不需要标识返回值类型，也不能有 void 关键字。

如果将返回类型添加到构造器上，编译不会报错，对 Java 来说，这只是一个“碰巧”跟类同名的方法。

```java
class Animal {
  public void Animal() {
    System.out.println("自认为为是构造器，实际是一个方法");
  }
}

public class Test {
  public static void main(String[] args) {
    Animal animal = new Animal(); // 不会打印任何东西
    animal.Animal(); // 调用方法
  }
}
```

另外，构造器也不可以同方法一样被对象引用，程序编译时就会报错。

```java
class Animal {
  public Animal() {
    System.out.println("正常的构造器");
  }
}

public class TestConstructor {
  public static void main(String[] args) {
    Animal animal = new Animal(); // 打印：正常的构造器
    animal.Animal(); // 编译报错
  }
}
```

构造器可以用任何访问修饰符修饰，包括 public、protected、private、默认（没有修饰符）。

## 默认构造器 default

如果没有显式地定义构造器，编译器会自动生成一个默认的无参构造器，访问修饰符为默认（没有修饰符）。

```java
class Animal {
  // 没有显式定义构造器
}
```

相当于是：

```java
class Animal {
  Animal() {}
}
```

如果一旦定义了构造器，不管是默认的无参构造器，还是其它构造器，java 都不能再自动生成构造器了。

## 保护类型的构造器 protected

通常使用 `protected` 修饰符后，该成员可以在子类和同一个包中的其它类调用，但是不能在不同包中的外部类调用。但因为构造器不能被继承，所以不能在子类中被调用，这样的话，当构造器使用 `protected` 修饰，跟 `default` 默认访问权限一样，只能被同一包中的其它类调用。

要想在不同包中的外部类调用构造器，只能使用 `public` 修饰符。

## 私有构造器 private

如果一个类的构造器使用 `private` 修饰符，那么私有构造器只能在类的内部代码被调用，不能在类的外部被调用，即使同一个包中的类也不行。也就是说对别的类而言，无法直接创建它的实例对象，好像将类锁起来了一样。

```java
class Animal {
  private Animal() {
    System.out.println("私有构造器");
  }
}
```

这种情况下，如果想要得到类的实例对象，则该类必须提供一个静态方法来返回实例对象，通常将这个静态方法称为**静态工厂方法**，好像一个生产实例对象的工厂一样。

```java
class Animal {
  private Animal() {
    System.out.println("私有构造器");
  }

  public static Animal getInstance() {
    return new Animal();
  }
}

class TestConstructor {
  public static void main(String[] args) {
    Animal animal = Animal.getInstance();
  }
}
```

## 构造器的重载

在现实场景中，有时在创建同一个类的多个对象时知道的初始化信息是不同的，比如学生类有姓名、学号、班级、年龄等信息，在创建学生对象时，有时只知道一些，需要用已知的参数先进行对象的初始化，这时就需要不同参数的构造器。

这种允许一个类中定义多个构造器，每个构造器接收不同的参数列表，这就是**构造器的重载**。

```java
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

class TestConstructor {
  public static void main(String[] args) {
    // Animal animal = new Animal();
    // animal.Animal();
    // Animal animal = Animal.getInstance();
    // 调用不同版本的构造器创建对象
    Animal a = new Animal();
    Animal b = new Animal("dog");
    Animal c = new Animal("cat", 18);
    System.out.println("动物名叫" + a.name + "，身体长" + a.size + "厘米");
    System.out.println("动物名叫" + b.name + "，身体长" + b.size + "厘米");
    System.out.println("动物名叫" + c.name + "，身体长" + c.size + "厘米");
  }
}
```

每个重载方法都有独一无二的参数类型列表，根据参数的数量、类型、顺序，调用对应的构造器。

## 构造器的级联调用

> 这里有继承语法，后面会介绍，这里先不展开。

下面是一个有继承关系的父子类代码

```java
class Fruit {
  // 无参构造器
  public Fruit() {
    System.out.println("Fruit 无参构造器");
  }
}

class Apple extends Fruit {
  // 子类无参构造器
  public Apple() {
    System.out.println("Apple 无参构造器");
  }
}

class TestConstructor {
  public static void main(String[] args) {
    Apple apple = new Apple();
  }
}

// 执行 TestConstructor，会输出
// Fruit 无参构造器
// Apple 无参构造器
```

虽然程序执行，只调用了子类 Apple 的构造器，但父类 Fruit 的构造器也执行了。

当执行语句 `new Apple()` 时，具体执行步骤如下所示：

1. 执行 `new Apple()` 语句，进入 Apple 构造器体。
2. 级联调用父类 Fruit 的构造器，进入 Fruit 构造器体，因为 Apple 是 Fruit 的子类。
3. 级联调用 Object 类的构造器，进入 Object 构造器体，因为 Fruit 是 Object 的子类。
4. 执行 Object 构造器中的代码直至完成，返回 Fruit 构造器。
5. 执行 Fruit 构造器中的代码直至完成，返回 Apple 构造器。
6. 执行 Apple 构造器中的代码直至完成，返回 main 方法。
7. 执行 main 方法中的代码直至完成，程序结束。

> Object 类是所有类的父类，所有类都直接或间接继承自 Object 类。

![Java_constructor_cascading_call.png](../public/images/Java_constructor_cascading_call.png)

## 显式调用父类构造器 super

上述构造器级联调用过程是默认执行的，并且只会执行父类的无参构造器。

实际上，java 编译器会在子类构造器中默认插入一段代码 `super()`，用于调用父类的无参构造器。

```java
class Apple extends Fruit {
  // 子类无参构造器
  public Apple() {
    // 主动调用父类无参构造器
    super();
    System.out.println("Apple 无参构造器");
  }
}
```

`super` 指代父类的构造器，可以使用 `super()` 调用父类的无参构造器，也可以传入参数 `super(参数列表)` 调用父类的有参构造器。

假如父类构造中显式声明了一个有参数的构造，这样 java 编译器就不会为父类主动生成无参构造器，那子类构造器默认添加的 `super()` 就找不到对应的无参构造器，会编译错误，提示 `Fruit() is undefined` 。

```java
class Fruit {
  // 有参构造器
  public Fruit(String name) {
    System.out.println("Fruit 有参构造器");
  }
}

class Apple extends Fruit {
  public Apple() {
    System.out.println("Apple 无参构造器");
  }
}

class TestConstructor {
  public static void main(String[] args) {
    Apple apple = new Apple();
  }
}

// 执行结果
// 编译错误：Fruit() is undefined
```

此时解决方法是在子类的构造器中显式调用父类的有参构造器。

```java
class Apple extends Fruit {
  public Apple() {
    // 主动调用父类有参构造器
    super("apple");
    System.out.println("Apple 无参构造器");
  }
}
```

到此，可以总结下 Java 构造器的两个默认规则

1. 当类没有声明构造器时，java 编译器会为类自动添加一个无参构造器。
2. 当类中的构造器没有显式调用父类的构造器时，java 编译器会默认添加 `super()` 调用父类的无参构造器。即使类有重载了多个构造器也一样，每个构造器中都默认有 `super()` 调用。

## 调用兄弟构造器 this

上面说了类的构造器可以重载，那么在构造器中是否可以调用其他构造器呢？

答案是可以的，使用 `this()` 调用当前类的其他构造器。

```java
class Apple extends Fruit {
  public Apple() {
    // 主动调用当前类有参构造器
    this("apple");
    System.out.println("Apple 无参构造器");
  }

  public Apple(String name) {
    // 主动调用父类有参构造器
    super(name);
    System.out.println("Apple 有参构造器");
  }
}
```

注意事项，一旦在构造器中使用了 `this(XX)` 语句调用兄弟构造器，在该构造器中编译器将不再自动添加 `super()`，如果同类的其它构造器中也没有显式或默认调用 `super()`，则会编译错误。换句话说，在类的构造器调用链中，必须有一个构造器调用了父类的构造器（不管是显式调用还是默认调用），否则会编译错误。
