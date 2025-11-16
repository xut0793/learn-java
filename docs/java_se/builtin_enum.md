# 枚举类型

枚举（Enum）是一种特殊的数据类型，它允许我们将一组相关的常量定义在一个类型中。从 JDK 5 开始引入，是 Java 中用来表示固定数量的常量值的最佳方式。

## 枚举的基本定义

定义一个简单的枚举

```java
public enum Size {
    Small, // 小号
    Medium, // 中号
    Large // 大号
}
```

使用枚举

```java
Size currentSize = Size.Medium;
System.out.println("当前尺寸是：" + currentSize);
```

## 枚举的实现原理

从编译器将枚举编译后的 class 文件看，它实际上是一个继承自 `java.lang.Enum` 的类。

```java
// 类似这样的类：
public final class Size extends Enum<Size> {
    public static final Size Small = new Size("Small", 0);
    public static final Size Medium = new Size("Medium", 1);
    public static final Size Large = new Size("Large", 2);

    private Size(String name, int ordinal) {
        super(name, ordinal);
    }
}
```

可以发现：

- 枚举类是一个 final 类，不能被继承。
- 枚举类的构造方法是私有的，表示枚举类的实例只能在枚举类内部创建，不能在外部实例化。
- 所有枚举的常量都被声明类的静态常量 `public static final`，这意味着它们是全局可访问的，并且只能被初始化一次。
- `Enum` 类实例化时需要传递两个参数，分别是枚举常量的名称和序号，然后在构造方法中需要调用 `super(name, ordinal)` 来初始化这两个实例变量。

父类 `java.lang.Enum` 类提供了以下重要方法：

- `String name()` - 返回枚举常量的名称
- `int ordinal()` - 返回枚举常量在定义中的位置（从 0 开始）
- `static T[] values()` - 返回所有枚举常量的数组
- `static T valueOf(String name)` - 根据名称获取枚举常量

<<< ../../learnjava/src/com/learnjava/builtin/enum_example/EnumExample.java#enumBasic

## 检举类的扩展

检举类型的本质也是类，所以可以对它进行一些扩展，比如：

- 定义检举类自己的实例变量和方法。
- 定义检举类的抽象方法，实现不同的操作。
- 检举类可以实现接口，定义一些通用的方法。

### 扩展枚举类的实例变量和方法

比如，`Size` 例子，每个枚举值可以有关联的缩写和中文名称，也可以定义静态方法根据缩写返回对应的枚举值。

<<< ../../learnjava/src/com/learnjava/builtin/enum_example/EnumExample.java#enumExtend

此时，扩展枚举类，类似这样：

```java
// 注意构造函数的入参
public final class Size extends Enum<Size> {
  public static final Size SMALL = new Size("SMALL",0, "S", "小号");
  public static final Size MEDIUM = new Size("MEDIUM",1, "M", "中号");
  public static final Size LARGE = new Size("LARGE",2, "L", "大号");
  private String abbr;
  private String title;
  private Size(String name, int ordinal, String abbr, String title){
      super(name, ordinal);
      this.abbr = abbr;
      this.title = title;
  }
  //其他代码
}
```

### 扩展枚举类的抽象方法

对检举类添加抽象方法，实现不同的操作。

<<< ../../learnjava/src/com/learnjava/builtin/enum_example/EnumExample.java#enumExtendAbstract

### 扩展枚举类的实现接口

检举类可以实现接口，定义一些通用的方法。

<<< ../../learnjava/src/com/learnjava/builtin/enum_example/EnumExample.java#enumExtendInterface

## 检举的应用场景

### switch 语句

枚举与 switch 语句配合使用非常方便：

<<< ../../learnjava/src/com/learnjava/builtin/enum_example/EnumExample.java#enumSwitch

### 状态机

```java
public enum OrderStatus {
    PENDING {
        @Override
        public boolean canTransitionTo(OrderStatus next) {
            return next == PROCESSING || next == CANCELLED;
        }
    },
    PROCESSING {
        @Override
        public boolean canTransitionTo(OrderStatus next) {
            return next == SHIPPED || next == CANCELLED;
        }
    },
    SHIPPED {
        @Override
        public boolean canTransitionTo(OrderStatus next) {
            return next == DELIVERED;
        }
    },
    DELIVERED, CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        return true;  // 默认允许任何转换
    }
}
```

### 配置管理

```java
public enum Environment {
    DEVELOPMENT("dev", "开发环境"),
    STAGING("staging", "测试环境"),
    PRODUCTION("prod", "生产环境");

    private final String code;
    private final String description;

    Environment(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isProduction() {
        return this == PRODUCTION;
    }
}
```

## 枚举的优缺点

### 优点

- 类型安全，编译时检查
- 代码可读性强
- 内置丰富的方法
- 可以包含属性和方法
- 支持枚举集合和映射

### 缺点

- 枚举常量是静态的，无法动态添加
- 相比普通常量，内存占用稍大
- 不能继承其他类（但可以实现接口）

## 枚举的最佳实践

### 枚举的何时使用

- 表示固定数量的常量
- 需要类型安全的常量组
- 枚举值在 switch 语句中使用
- 需要将相关常量组合在一起

### 枚举的注意事项

- 枚举常量必须定义在枚举类的顶部
- 枚举常量后如果有其他代码，必须用分号分隔
- 构造方法必须是私有的（默认也是私有的）
- 枚举不能被继承，但可以实现接口
- 枚举不能在外部被实例化

### 枚举的常见错误

```java
// 错误：尝试实例化枚举
Season s = new Season();  // 编译错误

// 错误：枚举常量后缺少逗号
public enum BadEnum {
    CONSTANT1
    CONSTANT2;  // 编译错误

// 错误：修改枚举常量
CONSTANT1 = CONSTANT2;  // 编译错误
```
