# 基类 Object

Object 类是 Java 中所有类的始祖，Java 中的每一个类都扩展了 Object 类。

如果声明类时没有明确指定父类，那么该类会默认继承 Object 类。

由于在 Java 中每个类都是由 Object 类扩展而来的，所以，熟悉这个类提供的服务十分重要。

## Object 类型

可以声明 Object 类型的变量引用任何类型的对象。

```java
Object obj = "object";
```

当然，Object 类型的变量只能用于作为任意值的一个泛型容器。要想对其中的内容进行具体的操作，还需要清楚对象的原始类型，并进行相应的强制类型转换：

```java
String str = (String) obj;
System.out.println(str.length());
```

在 Java 中，只有基本类型（primitive type）不是对象，例如，数值、字符和布尔类型的值都不是对象。如果将基本类型赋值给 `Object` 类型的对象，Java 会自动将其包装为对应的包装类对象。例如，将一个 `int` 类型的变量赋值给 `Object` 类型的对象，Java 会自动将其包装为 `Integer` 类的对象。

```java
Object obj = 100;
// 等价于
Object obj = Integer.valueOf(100);
```

Object 类提供了一些通用的方法，例如，`equals` 方法、`hashCode` 方法、`toString` 方法等。这些方法在所有的 Java 类中都是可用的。

## equals

`equals` 方法用于判断两个对象是否相等。默认情况下，`equals` 方法判断的是对象的引用是否相等，即是否指向同一个内存地址。

### `==` 运算符

- 针对基本类型：`==` 运算符用于判断两个基本类型的值是否相等。
- 针对引用类型：`==` 运算符用于判断两个引用类型的变量是否指向同一个内存地址。

```java
int a = 10;
int b = 10;
System.out.println(a == b); // true

String str1 = new String("hello");
String str2 = new String("hello");
System.out.println(str1 == str2); // false
System.out.println(str1.equals(str2)); // true
```

### `Object` 类的 `equals` 方法

`Object` 类的 `equals` 方法默认实现与 `==` 运算符相同，即判断两个对象是否指向同一个内存地址。

```java
// Object 类源码
public boolean equals(Object obj) {
    return (this == obj);
}
```

如果要自定义对象的相等的判断逻辑，需要在类中重写 `equals` 方法。

### 重写 `equals` 方法

Java 语言规范要求 `equals` 方法具有如下的性质：

1. 自反性：对于任何非空引用 x，x.equals(x) 必须返回 true。
2. 对称性：对于任何非空引用 x 和 y，当且仅当 y.equals(x) 返回 true 时，x.equals(y) 必须返回 true。
3. 传递性：对于任何非空引用 x、y 和 z，如果 x.equals(y) 返回 true，且 y.equals(z) 返回 true，那么 x.equals(z) 必须返回 true。
4. 一致性：对于任何非空引用 x 和 y，多次调用 x.equals(y) 必须一致地返回 true 或 false，前提是对象上 equals 比较中使用的信息没有被修改。
5. 对空引用的比较：对于任何非空引用 x，x.equals(null) 必须返回 false。

所以一个比较完善的 `equals` 方法的实现应该包含下面的判断：

```java
@Override
public boolean equals(Object obj) {
    // 1. 判断是否是同一个对象，如果是，直接返回 true。
    if (this == obj) {
        return true;
    }
    // 2. 判断是否为空引用，如果是，直接返回 false。这个检测是必要的
    if (obj == null) {
        return false;
    }
    // 3. 判断是否是同一个类的实例，如果不是，直接返回 false。
    if (getClass() != obj.getClass()) {
        return false;
    }
    // 4. 现在根据相等性概念的要求来比较字段，使用 == 比较基本类型的字段，使用 Objects.equals 比较对象字段。如果所有的字段都匹配，就返回 true；否则，返回 false。
    MyClass other = (MyClass) obj;
    return field1 == other.field1 && Objects.equals(field2, other.field2);

    // 5. 如果是在子类中重新定义 equals 方法，需要在上述条件中调用 super.equals 方法来比较父类的字段。
    // 例如：
    // return super.equals(obj) && field3 == other.field3;
}
```

对于数组类型的字段，可以使用静态的 Arrays.equals 方法检查相应的数组元素是否相等。
例如：

```java
return Arrays.equals(field3, other.field3);
```

对于多维数组，可以使用 Arrays.deepEquals 方法检查相应的数组元素是否相等。
例如：

```java
return Arrays.deepEquals(field4, other.field4);
```

Java 绝大多数对象都重写了 `equals` 方法，以判断对象的内容是否相等。比如 String 类的 `equals` 方法源代码。

```java
// String 类源码
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```

从源码中可以看出，String 类的 equals 方法逐字符比较两个字符串的内容，只有当所有字符都相同时才返回 true。

## hashCode

`hashCode` 方法用于返回对象的哈希码值。哈希码值通常是一个 32 位整数，它是根据对象的内部特征计算出来的。

### 为什么需要 hashCode 方法？

主要是为了在哈希表等数据结构中进行高效的存储和检索。

比如 HashMap、HashSet 等集合中，通过对象的 hashCode 值能优化以下场景的性能：

- 快速查找：当需要查找一个对象时，哈希表可以根据对象的 hashCode 值直接定位到可能包含该对象的桶（bucket），而不需要遍历整个集合。
- 减少 equals 方法的调用次数：在哈希表中，当需要判断两个对象是否相等时，先通过 hashCode 方法比较哈希码值，如果哈希码值不同，那么这两个对象一定不相等；只有当哈希码值相同时，才需要调用 equals 方法进行比较。

哈希码有以下特点：

- 相同对象多次调用 hashCode() 方法应该返回相同的哈希码值。
- 不同对象的哈希码值应尽可能地不同，以提高哈希表等数据结构的性能。但是因为存在哈希碰撞（hash collision）的情况，即不同对象的哈希码值可能相同，此时需要通过 equals 方法进行比较。

### hashCode 方法的实现

#### 默认实现

- Object 类：基于对象内存地址计算，并且是一个 `native` 方法实现（调用 C++ 实现的 hashCode 方法），不同实例即使内容相同也会返回不同 HashCode。
- String 类：重写后的 HashCode 基于字符值计算，如`"OK".hashCode()`与`new String("OK").hashCode()`结果相同。

```java
// String 类重写的 hashCode 方法
@Override
public int hashCode() {
    int hash = 0;
    if (hash == 0 && value.length > 0) {
        for (int i = 0; i < value.length; i++) {
            // 31 是一个奇质数，它可以确保哈希码值的分布更均匀，减少哈希碰撞的概率。并且 31 可以被 JVM 优化为 (i*31)<<5-i，即 i*31+i。
            hash = 31 * hash + charAt(i);
        }
        hash = h;
    }
    return hash;
}
```

代码示例：

```java
/**
 * 测试 hashCode 方法
 * 字符串 s 与 t 有相同的散列码，这是因为字符串的散列码是由字符值导出的。
 * 而字符串构建器 sb 与 tb 却有着不同的散列码. 因为在 StringBuilder 类中没有定义 hashCode 方法，继承了 Object
 * 类的默认 hashCode 方法，它会从对象的存储地址得出散列码。
 */
public static void testHashCode() {
    String s = "OK";
    StringBuilder sb = new StringBuilder(s);
    System.out.println("s.hashCode() = " + s.hashCode() + " sb.hashCode() = " + sb.hashCode());

    var t = new String("OK");
    var tb = new StringBuilder(t);
    System.out.println("t.hashCode() = " + t.hashCode() + " tb.hashCode() = " + tb.hashCode());
}
```

#### 自定义实现

比如为一个简单的 Person 类，根据姓名、年龄、性别计算 hashCode。

```java
// Person 类
public class Person {
    private String name;
    private int age;
    private String gender;
}
```

为了实现自定义的 hashCode 方法，需要根据类的字段计算出一个哈希码值。

```java
/**
 * 实现1
 */
@Override
public int hashCode() {
    return 7 * name.hashCode() + 11 * Integer.valueOf(age).hashCode() + 13 * gender.hashCode();
}
```

上述中，7、11、13 是任意选择的质数，用它们来作乘积是为了优化散列值的分布，让不同对象的散列值尽量分散开，从而减少哈希碰撞的概率。

但是，上述实现中，没有考虑到 `name` 字段为 `null` 的情况。如果 `name` 为 `null`，则会抛出 `NullPointerException` 异常。为了避免这种情况，在计算哈希码值时可以使用更案例的 `Objects.hashCode` 方法，它会在参数为 `null` 时返回 0，有值时返回该值的 hashCode。另外，对 `age` 字段也可以使用静态方法 `Integer.hashCode` 来避免创建 Integer 对象。

```java
/**
 * 实现2
 */
@Override
public int hashCode() {
    return 7 * Objects.hashCode(name) + 11 * Integer.hashCode(age) + 13 * Objects.hashCode(gender);
}
```

对于需要组合多个字段的情况，更好的方式是使用 `Objects.hash` 方法来计算哈希码值。它会根据传入的参数，对各个参数调用 `Objects.hashCode` 方法计算出一个哈希码值，并将这些哈希码值组合起来。这样可以避免手动计算需要分配权重值等问题，同时也能更好地处理 `null` 值。

> 如果有数组类型的字段，那么可以使用静态的 `Arrays.hashCode` 方法计算一个散列码，这个散列码由数组元素的散列码组成。

```java
/**
 * 实现3
 */
@Override
public int hashCode() {
    return Objects.hash(name, age, gender);
}
```

### hashCode 与 equals 方法相容性

- 规则 1：若 `a.equals(b) == true`，则 `a.hashCode() == b.hashCode()` 必须成立。
- 规则 2：`HashCode` 相等不代表对象相等，需进一步通过`equals()` 方法验证。
- 规则 3：任何时间重写 `equals` 方法，都应该重写 `hashCode` 方法，以保持一致性。并且不参与 `equals` 方法的比较的字段，也不应该参与 `hashCode` 方法的计算。

错误示例 1：仅重写 equals 未重写 hashCode。此时 HashSet 可能无法正确识别重复对象，导致集合逻辑异常。

```java
class User {
 int id;
 public boolean equals(Object o) {
    /* 根据id判断相等 */
    return o instanceof User && ((User) o).id == id;
    }
 // 未重写hashCode
}
Set<User> set = new HashSet<>();
set.add(new User(1));
set.contains(new User(1)); // 可能返回false，此时HashSet可能无法正确识别重复对象，导致集合逻辑异常。
```

错误示例 2：`HashCode` 相等不代表对象相等，需进一步通过`equals()` 方法验证。

```java
var s1 = "Aa";
var s2 = "BB";
System.out.println("s1.hashCode() = " + s1.hashCode()); // s1.hashCode() = 2112
System.out.println("s2.hashCode() = " + s2.hashCode()); // s2.hashCode() = 2112
System.out.println("s1.equals(s2) = " + s1.equals(s2)); // s1.equals(s2) = false
```

### 调试技巧

判断对象是否重写了 hashCode 方法：

```java
// 打印对象HashCode和内存地址，如果两者不一致，说明对象重写了 hashCode 方法。
var t = new String("OK");
var tb = new StringBuilder(t);
System.out.println("HashCode: " + t.hashCode() + " IdentityHashCode: " + System.identityHashCode(t)); // HashCode: 2524 IdentityHashCode: 41359092
System.out.println("HashCode: " + tb.hashCode() + " IdentityHashCode: " + System.identityHashCode(tb)); // HashCode: 1826771953 IdentityHashCode: 1826771953
```

## toString

Object 类的 toString 方法返回一个字符串，该字符串默认由类名和对象的哈希码值组成，而 Object 类的 hashCode 方法默认返回的是对象的内存地址。所以说，默认情况下，toString 方法返回的字符串是由类名和对象的内存地址组成。

```java
// 打印对象的 toString 方法返回的字符串
System.out.println(tb.toString()); // java.lang.StringBuilder@6d06d69c
```

但是在大多数 Java 类中，都重写了 toString 方法，以返回更有意义的字符串表示。例如，String 类的 toString 方法返回字符串本身，基本类型的包装类（如 Integer、Boolean 等）的 toString 方法返回对应值的字符串表示。

```java
int i = 100;
Integer iObj = i;
System.out.println(i); // Error: Cannot invoke toString() on the primitive type int
System.out.println(iObj.toString()); // 100

String s = "OK";
System.out.println(s.toString()); // OK
```

对于 `System.out.println` 方法，它会自动调用对象的 `toString` 方法，并且对于基本类型，也会自动进行装箱操作，所以在打印对象时，不需要显式调用 `toString` 方法。

```java
int i = 100;
Integer iObj = i;
System.out.println(i); // 100
String s = "OK";
System.out.println(s); // OK
```

对于数组，因为继承了 Object 类的 toString 方法，但是数组类型对前面的类名部分会采用一种古老的格式打印，例如：

```java
int[] luckyNumbers = { 2, 3, 5, 7, 11, 13 };
String s = "" + luckyNumbers;
System.out.println(s); // [I@6d06d69c
```

会生成字符串 "I@6d06d69c" (前缀 `[I` 表示这是一个整型数组)。

更好的做法是调用静态方法 `Arrays.toString` 来打印数组元素。例如：

> `import java.util.Arrays;`

```java
int[] luckyNumbers = { 2, 3, 5, 7, 11, 13 };
String s = Arrays.toString(luckyNumbers);
System.out.println(s); // [2, 3, 5, 7, 11, 13]
```

通常情况下，建议为每个自定义类都重写 `toString` 方法，以返回更有意义的字符串表示。比如 `Person` 类，通常会返回该对象的姓名、年龄等信息。

```java
@Override
public String toString() {
    return "Person{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", gender='" + gender + '\'' +
            '}';
}
```
