# 记录 Record

Record 是 Java 14 引入的预览特性，并在 Java 16 中正式成为标准特性。它是一种新的类类型，用于表示不可变的数据载体。

Record 主要用于简化数据类的创建，提供了一种更简洁的方式来创建不可变的数据传输对象。

> 有时，数据就只是数据，而面向对象程序设计提供的数据封装有些碍事。

## 定义 Record

第一种：简洁的声明方式

```java
public record Person(String name, int age, String email) {
    // 可以添加方法，但不能添加实例字段
}
```

第二种：使用构造器形式，比如需要对参数进行验证的情形

```java
public record Person(String name, int age, String email) {
    // 紧凑构造器，注意没有显式的构造器声明（没有返回值和参数列表）
    public Person {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
    }
}
```

在 Java 语言规范中，一个记录的实例字段称为组件 (component)。比如上例中的 `name`、`age`、`email` 都是组件。

## 基本使用

```java
Person person = new Person("张三", 25, "zhangsan@example.com");
System.out.println(person.name());    // 张三
System.out.println(person.age());     // 25
System.out.println(person.email());   // zhangsan@example.com
System.out.println(person);          // Person[name=张三, age=25, email=zhangsan@example.com]
```

## 内部实现原理

Record 在编译后会被转换为普通的类：

```java
// 源代码
public record Person(String name, int age) {}

// 编译器生成的等效代码（简化版）
public final class Person extends java.lang.Record {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String name() { return this.name; }
    public int age() { return this.age; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;
        Person other = (Person) o;
        return Objects.equals(this.name, other.name) &&
               this.age == other.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.age);
    }

    @Override
    public String toString() {
        return "Person[name=" + this.name + ", age=" + this.age + "]";
    }
}
```

可以看到：

1. **隐式 final**：Record 类和字段都是隐式 final 的
2. **继承限制**：Record 不能继承其他类，但可以实现接口
3. **无继承性**：Record 不能被继承，也不能继承其他类（除了 Object）

并且编译器会为 Record 自动生成以下成员：

- **私有 final 字段**：传入的每个参数都对应一个私有 final 字段
- **公共访问器方法**：每个参数对应一个同名的公共方法
- **equals() 方法**：基于所有参数的相等性比较
- **hashCode() 方法**：基于所有参数生成哈希码
- **toString() 方法**：返回所有参数的字符串表示
- **构造器**：默认构造器和紧凑构造器

## 扩展：添加自定义方法

```java
public record Person(String name, int age, String email) {

    // 添加自定义方法
    public String getDisplayName() {
        return name + " (" + age + "岁)";
    }

    public boolean isAdult() {
        return age >= 18;
    }
}
```

## 扩展：添加静态方法和静态字段

```java
public record Person(String name, int age, String email) {

    // 静态字段
    public static final int MIN_AGE = 0;

    // 静态方法
    public static Person createChild(String name, String email) {
        return new Person(name, 10, email);
    }

    public static Person createAdult(String name, String email) {
        return new Person(name, 25, email);
    }
}
```

## 局部 Record

```java
public class RecordExample {
    public void processData() {
        // 方法内定义的 Record
        record Point(int x, int y) {}

        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 40);

        System.out.println(p1);
    }
}
```

## 使用场景

### 数据传输对象（DTO）

```java
// API 响应
public record ApiResponse<T>(boolean success, T data, String message) {}

// 请求参数
public record CreateUserRequest(String username, String email, int age) {}
```

### 方法返回值

```java
public record Result<T>(T value, boolean success, String error) {

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, true, null);
    }

    public static <T> Result<T> error(String error) {
        return new Result<>(null, false, error);
    }
}
```

### 复合键

```java
public record CompositeKey(String tableName, String columnName) implements Comparable<CompositeKey> {

    @Override
    public int compareTo(CompositeKey other) {
        int cmp = this.tableName.compareTo(other.tableName);
        return cmp != 0 ? cmp : this.columnName.compareTo(other.columnName);
    }
}
```

## 与其他类的区别

### Record vs Class

| 特性            | Record       | 普通 Class |
| --------------- | ------------ | ---------- |
| 可变性          | 不可变       | 可变       |
| 字段声明        | 组件自动生成 | 手动声明   |
| 访问器          | 自动生成     | 手动创建   |
| equals/hashCode | 自动生成     | 手动实现   |
| 继承            | 不能继承类   | 可以继承   |

### Record vs Enum

| 特性     | Record     | Enum       |
| -------- | ---------- | ---------- |
| 用途     | 数据载体   | 常量集合   |
| 实例数量 | 无限       | 有限且固定 |
| 继承     | 不能继承类 | 继承 Enum  |
| 序列化   | 支持       | 内置支持   |

### Record vs Lombok

Lombok 的 `@Value` 注解也能生成类似的不可变类，但 Record 是 Java 原生支持：

```java
// Lombok 方式
@Value
public class PersonLombok {
    String name;
    int age;
}

// Record 方式
public record PersonRecord(String name, int age) {}
```

## 最佳实践

### 1. 命名约定

- 组件名使用小写字母开头（符合 JavaBean 规范）
- Record 类名使用名词或名词短语
- 避免使用动词或动词短语

### 2. 验证逻辑

在 Record 的紧凑构造器中添加验证逻辑，确保数据的有效性：

```java
public record EmailAddress(String value) {
    public EmailAddress {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        if (!value.contains("@")) {
            throw new IllegalArgumentException("邮箱地址格式不正确");
        }
        value = value.trim();
    }
}
```

### 3. 避免过度使用

Record 适用于：

- 数据传输对象（DTO）
- 方法返回值
- 不可变数据结构

不适合用于：

- 需要修改状态的场景
- 需要继承的场景
- 复杂业务逻辑的封装

## 注意事项

### 1. 不可变性

Record 创建后状态不可改变：

```java
Person person = new Person("张三", 25);
// person.age() = 26; // 编译错误！
```

### 2. 性能考虑

由于 Record 的不可变性，在高并发场景下性能较好，因为不需要同步机制。

### 3. 序列化兼容性

Record 支持序列化，但在使用 JSON 序列化时需要注意：

- 确保序列化框架支持 Record
- 可能需要自定义序列化策略

## 总结

Record 是 Java 语言的一个重要特性，它提供了一种简洁、安全的方式来定义不可变的数据载体。通过自动生成常用方法，Record 大大简化了数据类的编写，同时保持了代码的可读性和类型安全。在合适的场景下使用 Record 可以提高代码质量和开发效率。
