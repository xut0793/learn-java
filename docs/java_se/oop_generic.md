# 泛型 Generic Type

## What 泛型是什么

"**泛型**"从字面意思就是"**广泛的类型**”。英文表述为"**Generic Type**"，其中 `Generic` 也有“通用”的意思。

泛型是一种能够让代码更通用的语言特性。

## Why 为什么需要泛型

泛型的初衷是通过解除类或方法所受数据类型的限制，使得代码更通用，更灵活。普通的类和方法在定义时都需要声明具体的数据类型，要么是基本类型，要么是类类型，如果想要编写跨类型的代码，这种强绑定的机制就会带来很多限制。

泛型的设计能够让类和方法在声明时不再需要绑定某一种具体类型，延迟到类实例化或方法调用时才明确参数的具体类型。这样使得该代码逻辑能接受更广泛的类型，扩展功能的通用性。

> 上述内容综合自 《On Java 基础卷》第二十章 泛型 开篇段落和《Java 编程逻辑》 8.1 泛型的基本概念和原理。

- **提供代码的可复用性**：代码逻辑和数据类型不再强绑定在一起，可以编写一个通用的类或方法，用于处理多种不同类型的数据，而不是为了每种类型都编写一个单独的类或重载方法。
- **增加类型的安全性**：编译器在编译时可以检查类型错误，避免在运行时出现类型转换异常。
- **提高代码的可读性**：使用泛型可以使代码更加清晰和易读，因为它可以将类型参数化，使代码更加通用和灵活。

## How 使用泛型

根据泛型定义的位置，可以分为以下几种：

- 泛型类
- 泛型接口
- 泛型方法

## 泛型参数

**泛型**也被定义为"**参数化类型（Parametersized Type）**"，把类型参数化，将类型作为参数传递。

类似函数定义时使用**形参**，然后函数调用时传入**实参**一样。在定义泛型时的类型参数也可以看作是一种**形参类型**，在使用泛型时实际传入具体的类型参数是**实参类型**。

泛型的类型参数通常使用一个大写的单字母表示，比如 T、E、K、V、R 等，但这只是一种约定，不是强制规范，也可以用具体的单词。

```
T: Type 类型参数，表示任务类型
E: Element 元素，表示集合中的元素
K: Key 键，表示映射中的键
V: Value 值，表示映射中的值
R: Return 返回值，表示方法的返回值

如果 T 不够用，通常也会使用 U S 等其他字母。
```

## 泛型类

泛型类是指在类的定义中使用泛型参数。

泛型类的定义：

- 泛型类的定义格式：`public class 类名<T, U, ...> { }`
- 泛型类的使用格式：`类名<具体类型1, 具体类型2, ...>`，如果在变量定义时声明了具体类型，那么在 `new 类名<>` 中可以省略具体类型，编译器会自动推断。

```java
// 声明泛型类
public class Pair<T, U> {
    T first;
    U second;
    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }
}

// 使用泛型类
Pair<Integer, String> pair = new Pair<Integer, String>(100, "hello");
// 类型推断
Pair<Integer, String> pair2 = new Pair<>(100, "hello");
```

泛型是 Java 5 引入的语法特性，此时可以看到在生成一个泛型类的实例对象时，传入了两次泛型参数的具体类型 `Pair<Integer, String> pair = new Pair<Integer, String>(100, "hello");`，这样的语法要求有点啰唆和多余。所以在 Java 7 之后，引入了**类型推断**的机制，在创建一个泛型对象的时候，只需要在变量声明前的泛型类中传入一次类型参数的具体类型，编译器会根据上下文自动推断出泛型参数的具体类型。但是需要注意 `new` 类名后面需要保留尖括号`<>`，只是不需要再传入具体类型了。

> 《On Java》书里这段内容时，作者应该是嘲讽了一下这个语法特性的变化。原话是“在 Java 5 问世的时候，这种啰唆的方式总被解释为必须这么做，但是到了 Java 7 ，设计者们修正了该问题，而删繁就简随之便被吹嘘为优秀的特性”。

## 泛型接口

泛型接口是指在接口的定义中使用泛型参数。

泛型接口的定义：

- 泛型接口的定义格式：`public interface 接口名<T, U, ...> { }`
- 泛型接口的使用格式：`接口名<具体类型1, 具体类型2, ...>`

```java
// 泛型接口
public interface Pair<T, U> {
    T getFirst();
    U getSecond();
}

// 泛型接口的实现类
public class PairImpl<T, U> implements Pair<T, U> {
    T first;
    U second;
    public PairImpl(T first, U second){
        this.first = first;
        this.second = second;
    }
    @Override
    public T getFirst() {
        return first;
    }
    @Override
    public U getSecond() {
        return second;
    }
}

// 泛型接口实现中也可以直接指定具体的类型，比如匿名实现类
Pair<Integer, String> pair = new Pair<Integer, String>() {
    @Override
    public Integer getFirst() {
        return 100;
    }
    @Override
    public String getSecond() {
        return "hello";
    }
};
System.out.println(pair.getFirst());
System.out.println(pair.getSecond());
```

## 泛型方法

泛型方法是指在方法的定义中使用泛型参数。

泛型方法的定义：

- 泛型方法的定义格式：`public <T, U, ...> 返回类型 方法名(参数列表) { }`
- 泛型方法的使用格式：`方法名<具体类型1, 具体类型2, ...>(参数列表)`

```java
// 泛型方法
public class Pair<T, U> {
    T first;
    U second;
    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }
    // 泛型方法
    public T getFirst() {
        return first;
    }
    public U getSecond() {
        return second;
    }
    public <T, U> void print(T t, U u) {
        System.out.println(t);
        System.out.println(u);
    }

    // 泛型方法也可以单独定义类型参数，与类的类型参数无关
    public <V> void print(V v) {
        System.out.println(v);
    }
}
```

如果方法接收可变参数，那么可变参数的类型参数也需要与方法的类型参数保持一致。

```java
// 泛型方法，接收可变参数
public <T> void print(T... arr) {
    for (T t : arr) {
        System.out.println(t);
    }
}
```

## 限定类型 Bounding type

声明类型形参 `<T>`后，这时编译器并不知道 T 具体是什么类型，所以在使用 T 类型的变量时，编译器并不知道 T 类型的方法和属性。

```java
public <T> void print(T t) {
    // 假设我们预期 T 类型是 String 类型，我们在 print 方法中想调用它的 charAt 方法，编译器会报错
    // The method intValue is undefined for the type T
    System.out.println(t.intValue());

    // 为什么 toString 方法可以正常调用？见下面类型擦除的原理
    System.out.println(t.toString());
}
```

为了让编译器知道 T 类型是 Number 类型，我们可以使用**边界限定**的方式来指定 T 类型的上限。

```java
public <T extends Number> void print(T t) {
    System.out.println(t.intValue());
}
```

`extend` 语法限定类型边界，也可以有多个限定条件，使用 `&` 相连，但如果有多个限定条件，有如下限制：

- 多个限定类型可以都是接口，但类只能有一个。
- 如果限定条件是类和接口混合，则第一个必须是类，后面是接口。

```java
public <T extends Number & Comparable<T>> void print(T t) {
    System.out.println(t.intValue());
}
```

## 类型约束的弊端

看下这个例子：

```java
class Fruit {
    String name;
    public Fruit(String name) {
        this.name = name;
    }
}

class Apple extends Fruit {
    public Apple(String name) {
        super(name);
    }
}

class Pair<T> {
    T first;
    T second;
    public Pair(T first, T second){
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }
    public T getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }
    public void setSecond(T second) {
        this.second = second;
    }

    public static void main(String[] args) {
        Pair<Apple> applePair = new Pair<>(new Apple("青苹果"), new Apple("红苹果"));

        Pair<Fruit> fruitPair1 = applePair; // 编译错误：Type mismatch: cannot convert from Pair<Apple> to Pair<Fruit>Java(16777233)
        Pair<Fruit> fruitPair2 = new Pair<Apple>(new Apple("青苹果"), new Apple("红苹果")); // 编译错误：Type mismatch: cannot convert from Pair<Apple> to Pair<Fruit>Java(16777233)
    }
}
```

常理上讲，一对 `Apple` 是 `Fruit` 的子类，所以可以放 `Apple` 苹果的组合，自然也可以赋值给 `Fruit` 水果的组合中，为什么在编译期间会报错呢？

从语法层面看，即使 `Apple` 是 `Fruit` 的子类，但是 `Pair<Apple>` 不是 `Pair<Fruit>` 的子类。语法规则是：无论 S 和 T 有什么关系，`Pair<S>` 与 `Pair<T>` 都没有任何关系。所以在编译期间会报错。

```
+-------+       +-------------+
| Fruit |       | Pair<Fruit> |
+---^---+       +-------------+
    |
    |                   没有任何关系
    |
+---+---+       +-------------+
| Apple |       | Pair<Apple> |
+-------+       +-------------+
```

这看起来是一个很严格的限制，但是实际上它对于类型安全是非常有必要的。

如果允许将 `Pair<Apple>` 赋值给 `Pair<Fruit>`，那下面代码就会将一个`Orange`橙子也插入 `Pair<Apple>` 苹果对中。

```java
class Orange extends Fruit {
    public Orange(String name) {
        super(name);
    }
}

Pair<Apple> applePair = new Pair<>(new Apple("青苹果"), new Apple("红苹果"));
Pair<Fruit> fruitPair = applePair; // 假设这是合法的
fruitPair.setFirst(new Orange("橙子"));
```

此处 `fruitPair` 实际是指向 `Pair<Apple>` 苹果对的，如果插入 `Orange` 橙子在预期是不允许的，但语句 `fruitPair.setFirst(new Orange("橙子"));` 却是合法的。

## 通配符

`Pair<Apple>`与`Pair<Fruit>` 这种严格的泛型参数限定规则，会让类型抽象与现实预期不符，导致代码编写起来比较麻烦。所以 Java 设计了**通配符类型（wildcard type）**来解决此类问题。

主要有三种通配符形式：

- 子类型限定通配符 `<? extend T>`
- 超类型限定通配符 `<? super T>`
- 无限定通配符 `<?>`

### 子类型限定通配符 `<? extend T>`

上述 `Pair<Apple>`与`Pair<Fruit>` 不兼容的问题，可以使用子类型限定通配符解决。

```java
// 下列代码不再报错
Pair<? extends Fruit> fruitPair = applePair;
Pair<? extends Fruit> fruitPair =  new Pair<Apple>(new Apple("青苹果"), new Apple("红苹果"));
```

`Pair<Apple>`与`Pair<Fruit>`之间没有关系，但是 `Pair<? extends Fruit>` 与 `Pair<Apple>` 是有关系的，如下图所示。

```
+-------+                    +-----------+
| Fruit |                    |   Pair    |
+---^---+                    |   (raw)   |
    |                        +-----^-----+
    |                              |
    |                              |
    |                              |
    |                   +----------+------------+
    |                   | Pair<? extends Fruit> |
    |                   +----------^------------+
    |                              |
    |                              |
    |                    +-------->+---------+
    |                    |                   |
+---+---+        +-------+-----+       +-------------+
| Apple |        | Pair<Apple> |       | Pair<Fruit> |
+-------+        +-------------+       +-------------+
```

并且此时编译器也能很好的阻止我们插入 `Orange` 橙子，保证类型安全。

```java
fruitPair.setFirst(new Orange("橙橘子")); // 报错
fruitPair.setFirst(new Apple("白苹果")); // 同样也报错
```

但是此时我们会发现，属于`applePair` 苹果对的引用对象 `fruitPair` 调用 `setFirst` 方法传入 `new Apple("白苹果")` 实例时，居然也报错了，这与我们预期也是不相符的。

深究下类型参数的传参，我们在声明类 `Pair` 时的形参类型是 `T`，然后在定义 `Pair<? extends Fruit> fruitPair` 时传入的实参类型是 `? extends Fruit`，此时 pair 类中方法的类型标识，类似这样：

```java
public ? extends Fruit getFirst() {return first;}
void setFirst(? extends Fruit first) {this.first = first;}
```

对于 `setFirst` 方法，编译器知道它参数有一个某个未知的特定的类型，这个类型继承自 `Fruit`，只能是 `Fruit` 的子类型，但是这个类型具体是 `Apple`？还是 `Orange`？编译器无法知道，至于`Fruit`之外的类型更是不能接受，所以编译器干脆拒绝接收的所有参数，以保证安全性。

对于 `getFirst` 方法，编译器知道它返回的是一个未知的特定的类型，这个类型继承自 `Fruit`，可以是 `Fruit` 及其子类型，但是这个类型具体是 `Fruit`？还是 `Apple`？还是 `Orange`？编译器不关心，因为它可以保证 `getFirst()`的返回值，赋值给 `Fruit` 类型的变量是安全的。

```java
Pair<? extends Fruit> fruitPair = applePair;
Fruit first = fruitPair.getFirst();
```

### 超类型限定通配符 `<? super T>`

对于持有 `applePair` 对象引用的 `fruitPair` 不能写入 `Apple` 实例的问题 `fruitPair.setFirst(new Apple("白苹果"));`，Java 语言实现了超类型限定通配符。

```java
Pair<? super Apple> fruitPair = applePair;
fruitPair.setFirst(new Apple("白苹果")); // 合法
```

深究下类型参数的传参，我们在声明类 `Pair` 时的形参类型是 `T`，然后在定义 `Pair<? super Apple> fruitPair` 时传入的实参类型是 `? super Apple`，此时 pair 类中方法的类型标识，类似这样：

```java
public ? super Apple getFirst() {return first;}
void setFirst(? super Apple first) {this.first = first;}
```

`? super Apple` 超类型限定通配符，限制参数未知的类型只能为 `Apple`及其超类型，具体的类型可以是 `Apple`、`Fruit`或者`Object`。对于 `setFirst` 方法而言，此时接受一个 `Apple` 类型或者它的子类型的实例对象，是安全的。

```java
class FujiApple extends Apple {
    public FujiApple(String name) {
        super(name);
    }
}
fruitPair.setFirst(new Apple("白苹果")); // 合法
fruitPair.setFirst(new FujiApple("红富士")); // 合法
```

但是对于 `getFirst` 方法，返回类型可以是 `Apple`、`Fruit`或者`Object`，编译器无法知道它返回的具体类型，只能返回 `Object` 类型，所以只有把它的返回值赋值给 Object 类型的变量才不会报错，其它类型接收一律报错处理。

```
                             +-----------+
                             |   Pair    |
                             |   (raw)   |
                             +-----^-----+
                                   |
                                   |
+-------+                    +-----+-----+
| Fruit |                    |  Pair<?>  |
+---^---+                    +-----^-----+
    |                              |
    |                              |
    |                   +----------+------------+
    |                   |  Pair<? super Apple>  |
    |                   +----------^------------+
    |                              |
    |                              |
+---+---+                   +------+------+
| Apple |                   | Pair<Apple> |
+-------+                   +-------------+
```

这就是引入有限定通配符的关键之外，可以让我们有办法区分安全的访问器方法和更改器方法。

- 带有子类型限定的通配符 `<? extends T>` 允许读取一个泛型对象，但是不允许写入。
- 带有超类型限定的通配符 `<? super T>` 允许写入一个泛型对象，但是不允许读取。

### 无限定通配符 `<?>`

无界通配符 `<?>` 表示未知的类型参数 `?` 可以是任意类型。通常无限定通配符都可以转换成类型参数具体限定的写法：

```java
public void printPair(Pair<?> pair) {}

public <T> void printPair(Pair<T> pair) {}
```

无限定通配符`<?>`和类型参数`<T>`的区别是,对于编译器来说所有的`T`都代表同一种类型，但是通配符`?`则表示有类型，但是具体类型不确定，是什么不知道。

无限定通配符一个使用场景是来用判断对象是否为 null，因为无限定通配符可以表示任意类型，所以可以用来判断对象是否为 null。

```java
public static boolean hasNull(Pair<?> p) {
    return p.getFirst() == null || p.getSecond() == null;
}
```

### PECS 原则

PECS（Producer Extends Consumer Super）原则是指在使用泛型时，根据读写的方向来选择通配符的方向。

- 生产者（Producer）：如果需要频繁从泛型类型中读取数据，而不写入数据，那么就应该使用上界通配符 `<? extend T>`。
- 消费者（Consumer）：如果需要频繁向泛型类型中写入数据，而不读取数据，那么就应该使用下界通配符 `<? super T>`。

### 通配符限定和具体限定的区别

泛型限定分类：

- 具体类型限定： `<T extends Fruit>`
- 通配符限定
  - 有限定通配符：
    - 子类限定通配符（上界通配符） `<? extends T>`
    - 超类限定通配符（下界通配符） `<? super T>`
  - 无限定通配符： `<?>`

具体类型限定和通配符限定的区别：

- 具体类型限定：适用于定义泛型类或方法，并且在定义方法时可以标识方法的返回值类型。
- 通配符限定：通常用于使用泛型类或方法，并且不可以用于方法返回值类型。（**可以把通配符限定的整体当做一个实参类型来考虑**）。

```java
public <T extends Fruit> T printPair(Pair<T> pair) {
    return pair.getFirst();
}

public printPair(Pair<T extend Fruit> pair) {
    return pair.getFirst();
}


Pair<Apple> applePair = new Pair<>(new Apple("青苹果"), new Apple("红苹果"));

// 取值场景
Pair<? extends Fruit> fruitPair = applePair;
Fruit first = fruitPair.getFirst();

// 赋值场景
Pair<? super Apple> fruitPair = applePair;
fruitPair.setFirst(new Apple("白苹果")); // 合法
```

## 类型擦除

Java 程序会先通过 Java 编译器将代码转换为 `.class` 文件，然后 Java 虚拟机加载并执行 `.class` 文件。

对于具有泛型的代码，Java 编译器在编译时会泛型代码转换为普通的非泛型代码，并在必要位置插入强制类型转换的代码，以确保类型安全。Java 虚拟机实际执行的时候，它是不知道有泛型这回事的，这就是泛型的实现原理**类型擦除**。

类型擦除的过程：

- 将泛型类型参数替换为其限定的第一个边界，如果有多个边界，取第一个。如果没有限定边界，类型参数会被替换为 Object 类型。
- 编译器在必要位置插入强制类型转换的代码，以确保类型安全。
- 虚拟机加载并执行 `.class` 文件时，它是不知道有泛型这回事的，只知道它是一个普通的类，所以在运行时会丢失类型信息。

泛型类的类型擦除：

```java
public class Pair<T, U> {
    T first;
    U second;
    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }
    public U getSecond() {
        return second;
    }
}
```

在编译后，Pair 类的字节码文件中，类型参数 T 和 U 都被替换为了 Object 类型。类似这样：

```java
public class Pair {
    Object first;
    Object second;
    public Pair(Object first, Object second){
        this.first = first;
        this.second = second;
    }
    public Object getFirst() {
        return first;
    }
    public Object getSecond() {
        return second;
    }
}
```

在使用 `Pair` 的代码

```java
Pair<Integer, String> pair = new Pair<>(100, "hello");
Integer first = pair.getFirst();
String second = pair.getSecond();
```

类型擦除后，必要位置插入强制类型转换的代码，以确保类型安全。

```java
// 类型擦除后
Pair pair = new Pair(100, "hello");
Integer first = (Integer) pair.getFirst();
String second = (String) pair.getSecond();
```

## 为什么 Java 使用类型擦除实现泛型

Java 使用类型擦除来实现泛型主要是为了支持向后兼容性的折中实现。泛型并不是 Java 与生俱来的一部分，如果泛型在 Java 1.0 的时候就实现的一部分语言特性，可能就不会通过类型擦除来实现了，而是通过具体化来将类型参数保持为第一类实体信息，这样就可以对类型参数执行基于类型的语言操作和反射操作了。

泛型是在 Java 5 中引入的，在设计实现时必须考虑支持向后兼容性，保证之前已有的代码和类文件都依旧是合法的，并且能继续保持原有的含义，而且还必须支持迁移兼容性。这样库才能按它们自己的节奏变得通用,并且一旦某个库变得通用了，便不会对依赖它的代码和程序造成破坏。将此定为目标后，Java 设计者们和各个相关团队便决定了类型擦除是唯一可行的方案。通过允许非泛型的代码和泛型代码共存，类型擦除实现了向泛型的迁移。

如果没有某种兼容的迁移的途径，那么所有已经存在了很久的库都有可能要和选择迁移到 Java 泛型上的开发者们说再见了。类库可以说是一门编程语言最有影响力的组成部分，因此这种代价是不可接受的。类型擦除是否是最佳甚至唯一的迁移途径，只有时间能给我们答案了。

## 不完全的类型擦除

Java 泛型的突出特性之一是在虚拟机中擦除泛型类型。但是，擦除的类仍然保留了原始泛型的一些微弱记忆。例如，原始 `Pair` 类知道它源于泛型类 `Pair<T>`，尽管无法准确区分它的构造为 `Pair<String>` 还是 `Pair<Fruit>`>。

- `java.lang.Class<T>` 是 Java 反射机制中的核心类，用于表示运行时类的信息。`Class<T>` 是一个泛型类，`T` 代表该 `Class` 对象所表示的类的类型，每个类在 JVM 中都有唯一的 `Class` 实例，可通过该实例获取类的各种信息。
- `java.lang.reflect`利用 java 反射（Reflection）机制可以在运行时获取泛型类的类型信息的接口定义。

**获取 `Class` 对象**

- `类名.class`：编译时获取，如 `String.class`。
- `对象.getClass()`：运行时获取，如 `"hello".getClass()`。
- `Class.forName("全限定类名")`：通过类名动态加载，如 `Class.forName("java.lang.String")`。

`java.lang.Class<T>` 常用方法签名

```java
public class Class<T> {
    public Class() {
    }
    // 已弃用，旧版本用于创建类的实例。
    public T newInstance() throws InstantiationException, IllegalAccessException {
        return null;
    }
    // `getDeclaredConstructor().newInstance()`：推荐使用，可创建带参数的实例
    public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes) throws NoSuchMethodException {
    }
    // 如果 obj 为null或者可以转换成类型 T，则返回 obj，否则抛出一个 BadCastException 异常
    public <T> T cast(Object obj) {
    }
}
```

`java.lang.reflect`常用接口，声明 `public static <T extends Comparable<? super T>> T min(T[] a)`，下述接口返回以下信息：

- `java.lang.reflect.Class`: 表示类的具体类型
- `java.lang.reflect.TypeVariable`：表示类型变量，如 `T extends Comparable<? super T>` 等。
- `java.lang.reflect.WildcardType`：表示通配符类型，如 `? super T>` 等。
- `java.lang.reflect.ParameterizedType`：表示参数化类型，如 `Comparable<? super T>` 等。
- `java.lang.reflect.GenericArrayType`：表示泛型数组类型，如 `T[]` 等。

以下是 《Java 核心技术卷中》8.9.3 的综合示例

```java
package genericReflection;

import java.lang.reflect.*;
import java.util.*;

/**
 * @version 1.13 2023-12-20
 * @author Cay Horstmann
 */
public class GenericReflectionTest
{
   public static void main(String[] args)
   {
      // read class name from command line args or user input
      String name;
      if (args.length > 0) name = args[0];
      else
      {
         try (var in = new Scanner(System.in))
         {
            System.out.println("Enter class name (e.g., java.util.Collections): ");
            name = in.next();
         }
      }

      try
      {
         // print generic info for class and public methods
         Class<?> cl = Class.forName(name);
         printClass(cl);
         for (Method m : cl.getDeclaredMethods())
            printMethod(m);
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   }

   public static void printClass(Class<?> cl)
   {
      System.out.print(cl);
      printTypes(cl.getTypeParameters(), "<", ", ", ">", true);
      Type sc = cl.getGenericSuperclass();
      if (sc != null)
      {
         System.out.print(" extends ");
         printType(sc, false);
      }
      printTypes(cl.getGenericInterfaces(), " implements ", ", ", "", false);
      System.out.println();
   }

   public static void printMethod(Method m)
   {
      String name = m.getName();
      System.out.print(Modifier.toString(m.getModifiers()));
      System.out.print(" ");
      printTypes(m.getTypeParameters(), "<", ", ", "> ", true);

      printType(m.getGenericReturnType(), false);
      System.out.print(" ");
      System.out.print(name);
      System.out.print("(");
      printTypes(m.getGenericParameterTypes(), "", ", ", "", false);
      System.out.println(")");
   }

   public static void printTypes(Type[] types, String prefix, String separator,
         String suffix, boolean isDefinition)
   {
      if (prefix.equals(" extends ")
            && Arrays.equals(types, new Type[] { Object.class })) return;
      if (types.length > 0) System.out.print(prefix);
      for (int i = 0; i < types.length; i++)
      {
         if (i > 0) System.out.print(separator);
         printType(types[i], isDefinition);
      }
      if (types.length > 0) System.out.print(suffix);
   }

   public static void printType(Type type, boolean isDefinition)
   {
      if (type instanceof Class<?> t)
      {
         System.out.print(t.getName());
      }
      else if (type instanceof TypeVariable<?> t)
      {
         System.out.print(t.getName());
         if (isDefinition)
            printTypes(t.getBounds(), " extends ", " & ", "", false);
      }
      else if (type instanceof WildcardType t)
      {
         System.out.print("?");
         printTypes(t.getUpperBounds(), " extends ", " & ", "", false);
         printTypes(t.getLowerBounds(), " super ", " & ", "", false);
      }
      else if (type instanceof ParameterizedType t)
      {
         Type owner = t.getOwnerType();
         if (owner != null)
         {
            printType(owner, false);
            System.out.print(".");
         }
         printType(t.getRawType(), false);
         printTypes(t.getActualTypeArguments(), "<", ", ", ">", false);
      }
      else if (type instanceof GenericArrayType t)
      {
         System.out.print("");
         printType(t.getGenericComponentType(), isDefinition);
         System.out.print("[]");
      }
   }
}
```

如果输入 `Pair<T>` 类，那么会输出如下内容

```
class Pair<T> extends java.lang.Object
public T getFirst()
public T getSecond()
public void setFirst(T)
public void secSecond(T)
```

## 类型擦除带来的问题和补偿措施

将泛型融入 Java 语言中，在不破坏现有库的情况下，使得从现存的非泛型代码过渡到泛型化代码的中间过程可以在不用修改的情况下可以继续使用，直至调用方做好了用泛型来重写代码的准备。这个动机非常有意义，因为它不会突然间破坏掉所有已有代码。

但是选择类型擦除来实现泛型的代价也很大，导致 Java 为此引入了很多语法特性来补偿类型擦除带来的损失，增加了在 Java 使用泛型的复杂性和用户的心智负担。

类型擦除带来的问题：

- 在定义泛型参数时
  - 不能通过类型参数创建对象
  - 泛型类的类型参数不能用于静态变量和方法
- 在使用泛型参数时
  - 泛型参数的实参类型不能是基本类型
  - 运行时类型信息不支持泛型的类型参数
  - 运行时类型擦除可能会引发一些冲突
- 不支持创建泛型数组

### 不能使用泛型类创建对象

```java
T elm = new T(); // 错误
T[] arr = new T[10]; // 错误
```

如果允许，那么用户会以为创建的就是对应类型的对象，但由于类型擦除，Java 只能创建 Object 类型的对象，而无法创建 `T` 类型的对象，容易引起误解，所以 Java 干脆禁止这么做。

如果确实希望根据类型创建对象，那么可以使用反射来创建对象。

```java
public static <T> T createInstance(Class<T> cls) {
    try {
        return cls.newInstance();
    } catch (Exception e) {
        return null;
    }
}

// 使用
Date date = create(Date.class) // 等价于 Date date = new Date()
Pair<Integer> pair = createInstance(Pair<Integer>.class); // 等价于 Pair<Integer> pair = new Pair<Integer>()
```

### 泛型类的类型参数不能用于静态变量和方法

因为静态变量和方法是与类相关的，而泛型类的类型参数是用来约束实例对象的类型。

比如下面代码是非法无效的。

```java
public class Singleton<T> {
    private static T instance;
    public static T getInstance() {
        if (instance == null) {
            instance = createInstance(cls);
        }
        return instance;
    }
}
```

对于静态方法，它可以是泛型方法，因为方法可以声明自己独立的类型参数，但需要注意这个类型参数不要与泛型类的类型参数同名，否则会导致编译错误。比如上面的 `createInstance` 方法。

### 泛型的类型参数的实参类型不能是基本类型

泛型的类型参数的实参类型只能是引用类型，不能是基本类型。因为在编译后，类型参数会被替换为 Object，然后在特定的位置需要插入强制类型转换的代码，基本类型不能与 Object 类型相互转换，所以 Java 泛型中不能使用基本数据类型，需要使用包装类型来代替。

```java
// 错误
Pair<int, String> pair = new Pair<>(100, "hello");
```

需要使用包装类来代替基本类型。

```java
// 正确
Pair<Integer, String> pair = new Pair<>(100, "hello");
```

### 运行时类型信息不适用于泛型的类型参数

泛型的类型参数在运行时会被擦除，所以不能使用 `instanceof` 运算符判断对象是否是某个泛型类的实例。

比如 `instanceof` 运算符后面可以是接口或类名，用于运行时判断对象是否是某个类的实例，但不能判断对象是否是某个泛型类的实例。

```java
if (p1 instanceof Pair<Integer>) {} // 错误
```

需要改为

```java
if (p1 instanceof Pair) {} // 正确
// 或者使用无限定通配符
if (p1 instanceof Pair<?>) {} // 正确
```

另一个例子，比如在运行时，动态获取每个类的类型对象，可以通过以下两种形式：

- `类名.class` 来获取，比如 `String.class`、`Integer.class`。但是不支持 `Pair<Integer>.class` 的写法，只能是 `Pair.class`。因为泛型只与编译器有关，运行时根本没有泛型相关的任何信息。
- `对象.getClass()` 来获取，比如如下代码。

```java
Class<?> cls = 'Hello'.getClass();

Pair<Integer> p1 = new Pair<>(100, 200);
// 一个泛型对象的 `getClass` 方法的返回值与原始类型对象也是相同的。
System.out.println(Pair.class == p1.getClass()); // true
```

> 在介绍继承的实现原理时，我们提到 java 在内存中有一块方法区，在这里保存着每个类的类型信息，而每个对象也都持有着它对应类的类型信息的引用。这个类型信息实际上也是一个对象，它的类型为 Class, Class 本身也是一个泛型类，如果用类型参数表示 `Class<T>`，或者使用无限定通配符的形式 `Class<?>`。

### 运行时类型擦除可能会引发一些冲突

比如你可能认为可以定义如下重载方法

```java
public static void test(DynamicArray<Integer> intArr)
public static void test(DynamicArray<String> strArr)
```

表面上看，这两个方法的参数类型不同，一个是 `DynamicArray<Integer>`，一个是 `DynamicArray<String>`，所以理论上应该可以重载这两个方法。

但实际上，由于类型擦除，这两个方法的参数类型都被擦除为 `DynamicArray<Object>`，同一个方法签名被声明了两次，所以会报错。

### 不支持创建泛型数组

java 引入泛型后，为了保持向后兼容（backward compatibility），不能创建泛型数组。

```java
Integer[] ints = new Integer[10];
// 或者
Integer[] ints = new Integer [] {1, 2, 3};

// 然后使用它
Number[] numbers = ints;
Object[] objs = ints;
```

上述代码是合法的。如果想创建自定义类型的数组，可能会想当然使用下面代码：

```java
// 想这么创建数组
Pair<Integer>[] pairs = new Pair<Integer>[10];
// 或者
Pair<Integer>[] pairs = new Pair<>[] {new Pair<>(1, 2), new Pair<>(3, 4)};

// 想这么使用
Pair<Number>[] numberPairs = pairs;
Pair<Object>[] objectPairs = pairs;
```

java 编译器会报错，提示不能创建泛型数组。为什么呢？

因为数组是支持协变的，而泛型是不变的。数组是 Java 直接支持的内置类型，在运行时创建数组，java 虚拟机知道数组元素的实际类型，比如上面代码，jvm 知道 `Number`和`Object`都是 `Integer` 的父类型，`Integer` 可以向上转型。

虽然允许这种转换，但如果使用不当，虽然不会在编译时报错，但在运行时可能会报错。比如下面代码：

```java
Integer[] ints = new Integer[10];
Object[] objs = ints;
objs[0] = "hello";
```

编译时没有问题，运行时会抛出 `ArrayStoreException` 异常。因为 jvm 知道 `ints` 数组元素的每个类型是 `Integer`，写入 `String` 时就会抛出异常。

但如果是允许创建泛型数组，假设下面代码是合法的：

```java
Pair<Integer>[] pairs = new Pair<Integer>[10];
Object[] objs = pairs;
objs[0] = new Pair<String>("hello");
```

如果允许创建泛型数组 pairs，那它就可以赋值给 `Object[] objs`，因为经过类型擦除后，实际运行时，`pairs` 数组元素的类型是 `Pair`，而 `objs` 数组元素的类型是 `Object`，它是所有类型的父类型。然后 `new Pair<String>("hello")` 产生的对象也是 `Pair` 类型，自然可以赋值给 `Object` 类型。所以上述操作不会引起编译错误，也不会在运行时抛出异常，但当我们把 `objs[0]` 当作 `Integer` 使用时就会报错了。这种允许这种情况（既不会引起编译错误，也不会立即触发运行时异常），相当于在逻辑中埋下了一颗炸弹，不定什么时候爆发，为避免这种情况，Java 干脆就禁止创建泛型数组。

但实际业务场景中，我们需要创建泛型数组，怎么办呢？有以下几种方法：

- 创建原始类型的数组
- 使用支持泛型的容器类
- 使用反射机制，获取运行时类型信息动态创建

#### 创建原始类型的数组

```java
Pair[] pairs = new Pair[] {
    new Pair<>(1, 2),
    new Pair<>(3, 4),
};
```

#### 使用支持泛型的容器类

```java
List<Pair<Integer>> list = new ArrayList<>();
list.add(new Pair<>(1, 2));
list.add(new Pair<>(3, 4));
```

至于为什么支持泛型容器类，而不支持泛型数组，是因为容器内部实现的数组是 `Object` 类型，并且在一些操作的结果中使用了类型强制转换，比如上述 `DynamicArray` 类的实现。

#### 使用反射机制，获取运行时类型信息动态创建

```java
Class<Pair<Integer>> cls = Pair.class;
Pair<Integer>[] pairs = (Pair<Integer>[]) Array.newInstance(cls, 10);
```

## 延伸概念：泛化

泛型是对程序代码逻辑进行泛化的手段之一。

在程序中，代码泛化的目标是为了让代码更通用，尽量复用代码逻辑，减少代码冗余，也让程序员编写更少的代码。在 Java 语言中很多语法特征都有一层泛化的思想。

> 以下内容源自 《On Java 基础卷》第二十章 泛型 开篇段落的理解

比如我们实现了一段通用的算法，可以把算法核心逻辑（包括实现算法需要的数据和方法）封装为一个类。然后外部场景需要使用该算法时，可以定义一个函数 fn 以实现该类的对象作为参数，在方法内部通过这个对象来调用实现了核心逻辑的方法 method，这是**封装**。

当需要扩展该算法的功能时，比如能支持更多的数据类型，或者支持更多的操作，但又不想重复书写原来那段代码，此时可以通过继承来实现，将原来的类作为基类，扩展算法的类继承基类即可，这是**继承**。

此时，原来的功能函数 fn （功能类的某个成员方法或构造器方法）是以基类作为参数的，在实际使用时，可以传入该基类的任何子类的对象，不会导致程序报错，因为子类对象要么继承了原来的方法 method，要么重写了原来的方法 `@Override method`，这是**多态**。

但是 Java 语言的单一继承层次结构的规则限制太多，因为你必须在原有的固定的继承层次中来生成符合需要的子类对象。如果将方法的参数类型从基类改成**接口**，可以使得功能函数 fn 更为通用，这样函数参数可以接爱任何实现了该接口的类的实例对象，而不会被限制在原来基类的继承层次结构中。接口能跨越类的继承层次结构，使得代码更为通用。

从功能函数 fn 角度来看，在声明的时候，形参的数据类型不管是类还是接口，形参的数据类型与参数都是强绑定的，不能改变。能够改变的是函数调用时传入的实参的数据类型，它可以是基类型的子类对象，或者是实现接口的类对象。

此时很自然的想法，既然现在函数的实参数据类型通过继承或接口实现了多态（多态是泛化的手段之一），那么我们是不是可以考虑让函数的形参的类型也进行泛化呢？

这就是**泛型**的概念，让函数形参的数据类型在声明时不再指定某一种具体类型，延迟到函数调用时才明确该参数的具体数据类型。使得该功能函数能接受更广泛的类型，也就扩展了函数功能通用性。
