# 包装类

## 基本数据类型和引用数据类型

在 Java 语言中，数据类型分为基本数据类型和引用数据类型。

- 基本数据类型（Primitive Types），共 8 种，包括：
  - 整型：byte、short、int、long
  - 浮点型：float、double
  - 字符型：char
  - 布尔型：boolean
- 引用数据类型（Reference Types），包括：
  - 类（Class）
  - 接口（Interface）
  - 数组（Array）

这两种数据类型的区别在于，一句话总结：**基本类型直接存储值，引用类型存的是对象的地址（引用）**。

| 特性            | 基本类型                                             | 引用类型               |
| --------------- | ---------------------------------------------------- | ---------------------- |
| 存储内容        | 直接存储值                                           | 存储对象的地址（引用） |
| 数据位置        | 栈内存（Stack）                                      | 堆内存（Heap）         |
| 初始值          | 对应类型的默认值（如 int 为 0，boolean 为 false 等） | null                   |
| 是否可为 null   | 否（int x; 默认是 0）                                | 是（String s = null;） |
| 性能表现        | 更快、更轻量                                         | 创建和销毁成本高       |
| 是否继承 Object | 否                                                   | 是                     |

但 Java 作为面向对象编程语言，为所有数据类型都提供了对应的包装类。

## 包装类

以下是 Java 中基本数据类型和对应的包装类，以及转换关系：

| 基本数据类型 | 对应包装类 | 转换示例                                                                                                                                            |
| ------------ | ---------- | --------------------------------------------------------------------------------------------------------------------------------------------------- |
| byte         | Byte       | `byte b = 10; Byte byteObj = Byte.valueOf(b); // 基本类型转包装类`<br>`byte b = byteObj.byteValue(); // 包装类转基本类型`                           |
| short        | Short      | `short s = 20; Short shortObj = Short.valueOf(s); // 基本类型转包装类`<br>`short s = shortObj.shortValue(); // 包装类转基本类型`                    |
| int          | Integer    | `int i = 30; Integer intObj = Integer.valueOf(i); // 基本类型转包装类`<br>`int i = intObj.intValue(); // 包装类转基本类型`                          |
| long         | Long       | `long l = 40L; Long longObj = Long.valueOf(l); // 基本类型转包装类`<br>`long l = longObj.longValue(); // 包装类转基本类型`                          |
| float        | Float      | `float f = 5.0f; Float floatObj = Float.valueOf(f); // 基本类型转包装类`<br>`float f = floatObj.floatValue(); // 包装类转基本类型`                  |
| double       | Double     | `double d = 6.0; Double doubleObj = Double.valueOf(d); // 基本类型转包装类`<br>`double d = doubleObj.doubleValue(); // 包装类转基本类型`            |
| char         | Character  | `char c = 'A'; Character charObj = Character.valueOf(c); // 基本类型转包装类`<br>`char c = charObj.charValue(); // 包装类转基本类型`                |
| boolean      | Boolean    | `boolean bool = true; Boolean boolObj = Boolean.valueOf(bool); // 基本类型转包装类`<br>`boolean bool = boolObj.booleanValue(); // 包装类转基本类型` |

包装类都是不可变（Immutable）的，这意味着一旦创建了包装类的对象，就不能改变它的值。

使用 `final` 关键字修饰了 类 和 `value` 字段，确保了包装类的不可变性。这样所有的操作都会生成新的对象，而不是在原对象上进行的。

```java
// Java 中 Integer 类的定义
public final class Integer extends Number implements Comparable<Integer> {
    // 内部存储原始值
    final int value;
    public Integer(int value) {
        this.value = value;
    }
}
```

## 为什么有基本类型和包装类

在 Java 语言中，有一个非常流行的说法“一切皆对象”，既然 java 中一切皆对象，那么又为何又有这些基本数据类型呢。最主要的原因是性能考虑。

> [Java 为什么需要保留基本数据类型](https://www.cnblogs.com/bdqczhl/p/9291761.html)

- 内存占用：基本数据类型直接在栈中存储值，而引用数据类型在堆中存储值，然后需要在栈中存储堆空间的地址（引用）。这样一个 double 类型的变量，占用 8 个字节的内存空间，而一个 Double 类型的对象，占用 16 个字节的内存空间（8 个字节存储值，8 个字节存储引用）。并且存储在堆内存空间的对象还需要额外的内存来支持对象的垃圾回收，但是基本类型不需要。
- 运行性能：基本数据类型在栈中存储值，栈的操作只有入栈和出栈，但是引用类型需要频繁的创建对象，每一次 new 一个对象都是对堆内存的消耗，并且这些对象还需要被垃圾回收，而且还要在栈里持有引用变量，并且有可能出现空指针错误。

所以“用基本类型处理数据，用引用类型组织世界。”，在项目开发中，频繁的数据操作首选基本类型。

> 引用[Java 编程之美-04. 基本类型：既然 Java 一切皆对象，那么又为何保留基本类型？](https://zhuanlan.zhihu.com/p/643192163)
>
> Java 真的想要做到一切皆对象，也是有可能的。它可以只提供包装类给开发者，而不提供基本类型。编译器在底层将包装类转换为基本类型再进行处理。这样就相当于包装类是基本类型的语法糖。既兼顾了符合一切皆对象的设计理念，又兼顾了性能。实际上，像 Groovy, Scala 等语言也正是这么做的。而 Java 之所以没有这么做，我猜测，很可能是历史的原因，毕竟 Java 发明于上个世纪 90 年代，当时没有考虑那么全面，而之后大家已经习惯了使用基本类型，如果再将其废弃，那么影响过于大。

为了弥补基本类型和包装类型之间的差距，Java 语言也一些优化的措施：

- 自动装箱和自动拆箱机制
- 缓存池机制

## 包装类应用场景

包装类的实现，统一了 Java 语言其它类对象的操作，比如集合、泛型、反射等。

## 自动装箱和自动拆箱

基本类型和包装类之间可以转换，这种转换可以显式执行，也可以隐式执行。

- 基本数据类型转换为包装类：使用对应包装类的 `valueOf()` 方法，如 `Integer.valueOf(i)`。
- 包装类转换为基本数据类型：使用对应基本类型的 `xxxValue()` 方法，如 `intObj.intValue()`。

除了以上显式转换方式之外，Java 还提供了基本类型和包装类之间的隐式转换方法，即自动装箱（autoboxing）和自动拆箱（unboxing）。

- 自动装箱是指自动将基本类型转换为包装类。
- 自动拆箱是指自动将包装类转换为基本类型。

```java
Integer iobj = 12; //自动装箱
int i = iobj; //自动拆箱
```

Java 中的自动装箱和自动拆箱只是语法糖，在编译器层面自动处理，实际执行时，相当于执行了 Integer 类的 `valueof()` 方法。

```java
Integer iobj = 12;
// 底层实现为：
Integer iobj = Integer.valueof(12);
```

自动装箱和自动拆箱的触发场景有以下几种。

1. 将基本类型数据赋值给包装类变量（包括参数传递）时，触发自动装箱。
2. 将包装类对象赋值给基本类型变量（包括参数传递）时，触发自动拆箱。
3. 当包装类对象参与算术运算时，触发自动拆箱操作。
4. 当包装类对象参与关系运算（<、>）时，触发自动拆箱操作。
5. 当包装类对象参与关系运算（==），并且另一方是基本类型数据时，触发拆箱操作。

```java
//第一种情况：赋值
int i1 = 4;
Integer iobj1 = 5; //自动装箱
iobj1 = i1; //自动装箱
List list = new ArrayList<>();
list.add(i1); //自动装箱

//第二种情况：赋值
Integer iobj2 = new Integer(6);
int i2 = iobj2; //自动拆箱

//第三种情况：算术运算
Integer iobj3 = iobj1 + iobj2; //自动拆箱
System.out.println(iobj3); //输出10

//第四种情况：大于小于关系运算
boolean bl = (iobj1 < iobj2); //自动拆箱
System.out.println(bl); //输出true
bl = (iobj1 < 2); //自动拆箱
System.out.println(bl); //输出false

//第五种情况：==关系运算
Integer iobj4 = new Integer(1345);
bl = (iobj4 == 1345); //自动拆箱
System.out.println(bl); //输出true
```

自动装箱和自动拆箱让基本类型与引用类型在编码层面没有心智负担，但不恰当的使用，也会导致性能问题也引入了性能问题。

- 装箱会创建对象，频繁装箱会带来堆内存压力，甚至引发 GC 风暴。
- 如果对 null 进行拆箱，会直接抛出空指针异常。实际开发中应当合理规避，比如使用基本类型、AtomicInteger 代替包装类等。

```java
// 1. 这段代码悄悄创建了 100 万个 Integer 对象！增加了对象创建压力，以及垃圾回收压力
List<Integer> list = new ArrayList<>();
for (int i = 0; i < 1000000; i++) {
    list.add(i); // 每次循环都装箱：int ➜ Integer
}


// 2 == 与 equals 混淆，一不留神，“自动装箱”就让你踩进“对象引用比较”的坑！
Integer x = 100;
Integer y = 100;
System.out.println(x == y); // true（缓存池命中）

Integer m = 1000;
Integer n = 1000;
System.out.println(m == n); // false（超出缓存范围）

System.out.println(m.equals(n)); // true（值相等）


// 拆箱 null 直接 NPE！
// map.get("key") 返回的是 null，然后拆箱操作：null.intValue() 报错 NullPointerException！
Map<String, Integer> map = new HashMap<>();
int val = map.get("key"); // NullPointerException！
```

常见拆装箱陷阱小结（务必牢记！）

| 场景                           | 危险说明                        |
| ------------------------------ | ------------------------------- |
| `Integer a = null; int b = a;` | NullPointerExceptionInteger     |
| `a = 1000; a == 1000`          | false，引用地址不一致           |
| `map.get("key") + 1`           | 若为 null 会 NPE，且有装箱开销  |
| `List list = ...`              | 每个元素都可能是对象，GC 压力大 |
| Set 比较性能                   | 基于对象 equals/hashCode，慢    |

自动拆装箱和自动拆箱是基本类型和包装类之间的桥梁，写起来爽，但要懂得“隐患背后的机制” 。

## 缓存池机制

上面的一个 `==` 和 `equals` 比较的例子

```java
Integer x = 100;
Integer y = 100;
System.out.println(x == y); // true（缓存池命中）

Integer m = 1000;
Integer n = 1000;
System.out.println(m == n); // false（超出缓存范围）

System.out.println(m.equals(n)); // true（值相等）
```

为什么是这样？这是 Java 为了优化性能，引入的缓存池机制。

- 装箱时，若数值在 `[-128, 127]` 范围内，会直接从缓存池返回对象，避免重复创建。
- 超出缓存范围时，会创建新的 Integer 对象。

在 `Integer x = 100;` 中，编译器编译后，实际执行的是 `Integer x = Integer.valueOf(100);`。

在 `Integer.valueOf()` 方法的源码实现：

```java
public static Integer valueOf(int i) {
  // IntegerCache.low 为 -128，high 为 127
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

`IntegerCache.cache[]` 是一个缓存数组，提前创建好了 -128 到 127 范围的 Integer 对象。这个范围内的数调用 `valueOf()` 会返回已有对象引用，而不是新建对象，超出这个范围就会 new 一个新对象。

为啥是 -128 到 127？，这不是拍脑袋随便定的，而有有讲究的，是 **“JVM 规范 + 二进制效率 + 内存权衡”**共同决定的。

- 原因 1，与 byte 类型范围一致：byte 类型的范围就是 -128 到 127。Java 是强类型语言，很多常量表达式都用 byte 表示，提升执行效率。
- 原因 2，Integer 最常用的数值集中在这个范围。大量统计发现，开发中   大部分整型常量都集中在 -128 到 127，包括下标、状态码、布尔映射值、分页编号、枚举 ID……，缓存这部分能   极大减少对象创建次数。
- 原因 3：减少堆内存压力 + 减少 GC。每次 new 一个 Integer 对象都是对堆的消耗，GC 也更频繁。通过缓存对象（享元模式），可以极大优化性能。

但是这个范围也能更改，JVM 启动时允许你通过参数设置缓存上限（只能设置上限）：`-Djava.lang.Integer.IntegerCache.high=512`。这样可以让 512 值以下都缓存起来，而 512 以上的数值就会创建新对象。但是这个改动只影响通过 `valueOf()` 方法装箱的场景，手动 `new Integer(i)` 无效，永远是新对象；这样做对于第三方库、多人协作项目，改缓存值容易埋雷，建议只在性能敏感 + 你能全程掌控代码的场景下调整。
