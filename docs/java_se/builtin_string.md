# 字符串

String 是 Java 中最常用的引用类型之一，用于表示不可变的字符序列。

## String 的创建方式

在 Java 中，创建 String 对象有两种主要方式：

1. **字符串字面量方式**：

   ```java
   String s1 = "hello";
   ```

2. **使用 new 关键字**：
   ```java
   String s2 = new String("hello");
   ```
   `String` 构造函数的重载方法签名

```java
// 从字节数组创建字符串
public String(byte[] bytes)
// 从字符数组创建字符串，指定字符编码
public String(char[] value, String charsetName) throws UnsupportedEncodingException
// 从字节数组的指定范围创建字符串
public String(byte[] bytes, int offset, int length)
// 从字符数组的指定范围创建字符串
public String(char[] value, int offset, int count)
// 从字符串的指定范围创建字符串
public String(char[] value, int beginIndex, int endIndex)
// 从字符串创建字符串
public String(String original)
```

## String 常用方法

<<< ../../learnjava/src/com/learnjava/builtin/string/StringExample.java#stringMethods

## String 的不可变性

String 的不可变性是其核心特性之一，意味着一旦 String 对象被创建，其内容就不能被修改。String 类中提供了很多看似修改的方法，其实是通过创建新的 String 对象来实现的，原来的 String 对象不会被修改。

主要原因是 String 类被声明为 final，并且其内部存储字符的数组（JDK 9 前为 char[]，JDK 9 后为 byte[]）字段也被声明为 final。

不可变性设计的基本原则：

1. **字段不可变**：使用 final 修饰所有字段，确保初始化后不可更改。
2. **无修改方法**：不提供 setter 或任何修改状态的公开方法；修改操作返回新对象，而不是改变原有对象。
3. **构造时初始化**：所有字段在对象创建时就被初始化，后续不能修改。
4. **类不可继承**：使用 final 修饰类，防止子类破坏不可变性。

不可变性的优势：

1. **安全性**：可以安全地在多线程环境中共享
2. **性能优化**：允许字符串常量池的实现
3. **缓存哈希码**：可以缓存哈希码，提高在哈希表中的性能
4. **线程安全**：不需要额外的同步机制

## String 类不可继承

String 类被声明为 final，这意味着它不能被继承。这是为了确保字符串的不可变性，以及它的方法不能被重写，防止子类修改字符串的行为。

## 字符串常量池

JVM 运行的时候，将内存分为两个部分，一部分是堆，另一部分是栈。堆中存放的是创建的对象，而栈中存放的则是方法调用过程中的局部变量或引用。

而设计 Java 字符串对象内存实现的时候，在堆中又开辟了一块很小的内存，其被称为字符串常量池，专门用来存放已创建的字符串对象。主要目的是重用相同内容的字符串，减少内存消耗。

### 通过字面量形式创建字符串的基本逻辑：

- 当创建一个字符串字面量时，JVM 会先检查字符串常量池中是否已经存在相同内容的字符串
- 如果存在，则返回池中的字符串引用
- 如果不存在，则创建新的字符串并放入池中

示例：

```java
String s1 = "hello";
String s2 = "hello";  // 复用池中已有的 "hello"
System.out.println(s1 == s2);  // true，引用同一个对象
```

### 通过 `new` 关键字创建字符串的基本逻辑：

- 当使用 `new` 关键字创建字符串时，JVM 首先会在堆中（非字符串常量池的区域）为新字符串对象分配内存，并将引用返回
- 然后同样会去检查字符串常量池中是否已经存在相同内容的字符串
- 如果存在，则将新字符串对象与池中的字符串对象联系起来
- 如果不存在，则将新字符串对象放入字符串常量池，并将池中的字符串对象与新字符串对象 联系起来

比如以下例子

```java
String s1 = "hello";
String s3 = new String("hello");  // 总是创建新对象
System.out.println(s1 == s3);  // false，引用不同的对象
```

虽然字符串 s1 与 s3 因为没有指向同一个对象，所以 `==` 运算符返回 false。但是 new 出来的对象与字符串常量池中的对象还是有联系的，可以通过 `intern()` 方法从普通堆中的对象来获取字符串常量池中对应的对象。

```java
/**
 * public String intern()
 * 此方法将指定字符串对象在字符串常量池中对应对象的引用返回。
 * 若该字符串本身就在字符串常量池中，则直接将自己的引用返回；
 * 若该字符串在堆中，则返回在常量池中与其有联系对象的引用。
 */
String s4 = s3.intern();  // s3 本身是堆中的字符串对象，然后获取与它有联系的字符串常量池中的 "hello" 对象的引用
System.out.println(s1 == s4);  // true，引用同一个对象
```

字符串这种特殊的内存机制带来的好处，即不管字符串多长，其比较速度都是一样的。因为在比较两个字符串内容是否相同时，不必真去考察内容，只需比较两个字符串联系的常量池中的对象是否为同一个即可。这也就将对内容的比较转化为对引用的比较，大大提高了比较操作的速度。String 类所提供的 equals 方法就是这样实现的，这样在实际开发中就不必自己去调用两个 intern 了。

所以在实际开发中，如果没有特殊需要应该避免使用 new 来创建字符串对象，尽量使用字面量形式创建字符串，这样可以节省内存，不会在堆中再分配内存，直接从常量池中返回已存在字符串对象的引用即可。

## String 源码实现

**JDK 9 之前**：

- String 类内部使用 `char[]` 数组存储字符
- 每个 char 占用 2 个字节，无论字符是否需要

**JDK 9 之后**：

- String 类内部使用 `byte[]` 数组存储字符
- 增加了一个 coder 字段来表示编码方式

这种改进主要是为了提升字符串的内存使用效率，减少内存开销。

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    @Stable
    private final byte[] value;
    private final byte coder;
    private int hash;

    // 省略其他方法
}
```

可以看到类和字段都是 final 修饰的，确保了字符串的不可变性。

### 不可变性的弊端

字符串特殊的内存机制（不可变性）带来了好处，同时也需要付出一定的代价。因为字符串是永远不变的，在一些需要大量连接字符串的代码中，其性能将大幅下降。这是因为其中创建了大量无用的中间对象。

比如以下代码，每一次循环中的 `+` 连接操作便会创建一个新对象、丢弃了一个老对象，这样会造成性能的急剧下降。

```java
String s = "";

for (int i = 0; i < 100000; i++) {
    s += i;
}
```

在 Java 中创建一个新的对象一般要比修改一个已经存在的对象耗时大得多，这是字符串特殊内存机制付出的代价，但是这样的付出是值得的。因为在大多数情况下，字符串比较操作比字符串修改操作要多得多。所以总的来说，Java 中 String 类的特殊内存机制使得代码的执行效率得到了极大的提高。

为了弥补在连接操作中 String 类的缺点，Java 中专门提供了一个 java.lang.StringBuffer 类，它是一个可变的字符串序列，适用于在循环中连接字符串的场景。

## StringBuilder

### 基本用法

通过 new 新建 StringBuilder 对象，通过 append 方法添加字符串，然后通过 toString 方法获取构建完成的字符串。

```java
StringBuilder sb = new StringBuilder();
sb.append("hello");
sb.append(" world");
System.out.println(sb.toString());  // "hello world"
```

### 主要方法

StringBuilder 类的主要方法包括：

- `append(String str)`：在字符串末尾追加指定字符串
- `insert(int offset, String str)`：在指定位置插入字符串
- `delete(int start, int end)`：删除指定范围内的字符
- `replace(int start, int end, String str)`：用指定字符串替换指定范围内的字符
- `reverse()`：反转字符串
- `toString()`：将 StringBuilder 对象转换为字符串

### 实现原理

StringBuilder 类的实现原理与 String 类类似，都是使用 char[] 数组来存储字符。但是与 String 有一些不同点：

- StringBuilder 类没有使用 final 修饰，所以可以修改。
- StringBuilder 能够动态调整数组大小，避免了字符串拼接时频繁创建新对象的问题。

```java
/**
 *  StringBuilder 类的实现原理
 */
public final class StringBuilder extends AbstractStringBuilder {
    int count;
    /**
     * 构造一个空的 StringBuilder，初始容量为 16 个字符
     */
    public StringBuilder() {
        super(16);
    }

    /**
     * 追加字符串
     * @param str 要追加的字符串
     * @return 当前 StringBuilder 对象
     */
    @Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }
}

/**
 * AbstractStringBuilder 类的实现原理
 */
public abstract class AbstractStringBuilder implements Appendable, CharSequence {
    char[] value;
    int count;

    /**
     * 构造一个空的 AbstractStringBuilder，初始容量为 16 个字符
     */
    AbstractStringBuilder() {
        value = new char[16];
    }

    /**
     * 构造一个空的 AbstractStringBuilder，初始容量为指定值
     * @param capacity 初始容量
     */
    AbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }

    /**
     * 直接复制字符到内部的字符数组中，如果字符数组长度不够，会进行扩展，实际使用的长度用 count 体现
     * - `ensureCapacityInternal(count+len)` 会确保数组的长度足以容纳新添加的字符
     * - `str.getChars(0, len, value, count)` 会复制新添加的字符到字符数组中
     * - `count+=len` 会增加实际使用的长度，体现了字符串的实际长度。
     */
    @Override
    public AbstractStringBuilder append(String str) {
        if (str == null) {
            str = "null";
        }
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }

    /**
     * 扩展的逻辑是：分配一个足够长度的新数组，然后将原内容复制到这个新数组中，最后让内部的字符数组指向这个新数组
     * 参数minimumCapacity表示需要的最小长度，需要多少分配多少不就行了吗？
     * 不行，因为那就跟String一样了，每append一次，都会进行一次内存分配，效率低下。
     * 这里的扩展策略是跟当前长度相关的，当前长度乘以2，再加上2，如果这个长度不够最小需要的长度，才用minimumCapacity。
     * 比如，默认长度为16，长度不够时，会先扩展到16*2+2即34，然后扩展到34*2+2即70，然后是70*2+2即142，这是一种指数扩展策略。
     * 为什么要加2？这样，在原长度为0时也可以一样工作。
     * 为什么要这么扩展呢？这是一种折中策略，一方面要减少内存分配的次数，另一方面要避免空间浪费。
     * 在不知道最终需要多长的情况下，指数扩展是一种常见的策略，广泛应用于各种内存分配相关的计算机程序中。
     *
     * 如果预先就知道需要多长，那么可以调用 StringBuilder 的另外一个构造方法：
     * `public StringBuilder(int capacity)`
     */
    void expandCapacity(int minimumCapacity) {
        int newCapacity = value.length ＊ 2 + 2;
        if(newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if(newCapacity < 0) {
            if (minimumCapacity < 0) //overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }

    /**
     * 基于内部数组新建了一个String。
     * 这里要注意，这个 String 构造方法不会直接用value数组，而会新建一个，以保证 String 的不可变性。
     */
    public String toString() {
        return new String(value, 0, count);
    }

    // 省略其它方法
}
```

### `+`和`+=`运算符和 StringBuilder

Java 中，String 可以直接使用 `+`和 `+=` 运算符，这是 Java 编译器提供的支持，背后，Java 编译器一般会将 `+`和`+=`操作转换为 `StringBuilder` 的 `append` 方法。

比如：

```java
String str = "Hello";
str += " World";
```

会被转换为：

```java
String str = "Hello";
str = new StringBuilder(str).append(" World").toString();
```

但是这里要注意一种情况，在循环中拼接字符串时，不要使用 `+=` 运算符，因为每次循环都会创建一个新的 StringBuilder 对象，效率低下。

正确的做法是，在循环外部创建一个 StringBuilder 对象，然后在循环中调用其 `append` 方法，最后调用 `toString` 方法获取最终的字符串。

比如：

```java
String str = "Hello";
for (int i = 0; i < 1000; i++) {
    str += i;
}
System.out.println(str);
```

编译后，大致会转换为如下逻辑：

```java
String str = "Hello";
for (int i = 0; i < 1000; i++) {
    str = new StringBuilder(str).append(i).toString();
}
System.out.println(str);
```

正确的做法是：

```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String str = sb.toString();
```

所以，对于简单的情况，可以直接使用 String 的 `+`和`+=`运算符，对于复杂的情况，尤其是有循环的时候，应该直接使用 StringBuilder。

## StringBuffer

StringBuffer 类的用途与 StringBuilder 类相同，当字符串需要大量修改、连接时，也可以使用该类。

两者之间的区别如下：

1. StringBuilder 类的执行效率要比 StringBuffer 类稍高一些，因为 StringBuilder 类的字符串编辑方法并没有进行同步锁限定，在多线程同时操作时可能会产生问题，应该在单线程情况下使用。
2. StringBuffer 类对其内容进行编辑、修改的方法进行了同步，多线程同时使用不会产生问题。

所以实际开发中，如果是在一个线程中对字符串进行编辑，则建议使用 StringBuilder 类。如果是多线程同时编辑，则应该使用 StringBuffer 类。

两者使用方法基本相同，只是 StringBuffer 类的修改方法在实现中都添加了同步锁，以确保线程安全。

## 总结

- **String**：不可变，适用于字符串不经常修改的场景
- **StringBuilder**：可变，非线程安全，性能更好，适用于单线程环境
- **StringBuffer**：可变，线程安全（方法同步），适用于多线程环境
