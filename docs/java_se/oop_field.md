# 成员变量

直接在类中声明的变量称为成员变量。

## 成员变量声明

通常在类的代码块中直接定义成员变量，格式为 `访问修饰符 数据类型 变量名;`

```java
class Student {
  // 成员变量
  public int id; // 学号
  public String name; // 姓名
  public int age; // 年龄
  public String gender; // 性别
  public int score; // 成绩
}
```

## 默认初始化

当类被加载时，成员变量会被默认初始化。默认初始化值如下：

| 数据类型 | 默认值   |
| -------- | -------- |
| byte     | 0        |
| short    | 0        |
| int      | 0        |
| long     | 0L       |
| float    | 0.0f     |
| double   | 0.0      |
| char     | '\u0000' |
| boolean  | false    |
| 引用类型 | null     |

## 显式初始化

通常默认初始化的值并不是业务逻辑需要的，所以通常需要能自定义成员变量的初始化值。

在 Java 类中，可以为变量进行初始化赋值的方式有：

- 直接赋值，在定义成员变量时，也可以进行显式初始化，格式为 `数据类型 变量名 = 值;`
- 构造方法赋值，在构造方法中为成员变量赋值，格式为 `变量名 = 值;`，如果构造方法的参数名与成员变量名相同，可以使用 `this` 关键字来区分 `this.变量名 = 值`。
- 代码块赋值，在代码块中为成员变量赋值，格式为 `变量名 = 值;`

### 直接赋值

```java
class Student {
  // 成员变量
  public int id = 1001; // 学号
  public String name = "张三"; // 姓名
  public int age = 18; // 年龄
  public String gender = "男"; // 性别
  public int score = 90; // 成绩
}
```

### 构造方法赋值

```java
class Student {
  // 成员变量
  public int id; // 学号
  public String name; // 姓名
  public int age; // 年龄
  public String gender; // 性别
  public int score; // 成绩

  // 构造方法
  public Student(int id, String name, int age, String gender, int score) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.score = score;
  }
}
```

### 代码块赋值

因为类的构造方法可以重载，可以同时定义多个构造方法，所以每个构造方法可以为成员变量赋值不同的值。

但如果有某些成员变量的值需要自定义初始化，但初始化过程都一样，又不想在每个构造方法中重复写初始化代码，就可以使用代码块赋值。

书写上，代码块是独立在构造器方式之外的，但实际编译过程中，会把代码块的语句插入到每个构造方法体的最前面，先进行执行。

```java
class Student {
  // 成员变量
  public int id; // 学号
  public String name; // 姓名
  public int age; // 年龄
  public String gender; // 性别
  public int score; // 成绩

  // 这段代码会在每个构造方法执行前执行
  {
    id = uuid();
  }
}
```

## 静态成员变量 Static

类的静态成员变量是指类中定义的静态变量，用于描述类的特征。静态变量为所有实例化对象所共享。

静态成员变量的声明语法如下：

```java
访问修饰符 static 数据类型 变量名;
```

示例代码：

```java
class Student {
  // 静态成员变量
  static int count = 0;
}
```

静态成员变量可以通过对象引用变量访问，也可以类名直接访问。为了区分，最佳实践是统一通过类名来访问静态成员变量。

示例代码：

```java
// 通过实例对象访问静态成员变量
Student stu = new Student();
stu.count = 100; // 不推荐，因为静态成员变量为所有实例化对象所共享

// 推荐通过类名访问静态成员变量
Student.count = 100;
```

## 常量 final

如果某个值在程序运行过程中不会改变，就可以将其定义为常量，添加 `final` 关键字修饰。

常量的定义格式为 `public final 数据类型 变量名 = 值;`

```java
class Student {
  // 通常将常量定义为静态的，因为静态变量为所有实例化对象所共享
  public final static int SCORE_MAX = 100;
  public final static int SCORE_MIN = 0;

  // 成员变量
  public int id; // 学号
  public String name; // 姓名
}
```

## 变量的使用

变量通常在类的方法中使用

- 在同类中方法使用，可以直接使用变量名访问。如果遇到跟方法参数名称同名，可以添加 `this.变量名` 加以区别。
- 在其它类中，通过对象实例来访问变量，格式为 `对象引用变量.变量名`。
- 静态变量可以通过类名直接访问，格式为 `类名.变量名`。

示例代码：

```java
class Student {
  // 成员变量
  int id; // 学号
  String name; // 姓名

  // 静态成员变量
  static int count = 0;

  // 构造方法中，与参数名称相同时，需要使用 this 关键字区分
  public Student(int id, String stuName) {
    this.id = id;
    name = stuName;
  }

  // 方法
  public void study() {
    System.out.println("我是" + name + "，我正在学习");
    // 静态变量可以通过类名直接访问
    System.out.println("我是第" + Student.count + "个学生");
  }
}


class TestField {
  public static void main(String[] args) {
    // 在其它类的方法中使用时，通过对象实例来访问变量
    Student stu = new Student();
    stu.id = 1001;
    stu.name = "张三";
    System.out.println(stu.id);
    System.out.println(stu.name);
  }
}
```

## 局部变量

局部变量是指定义在语句块中的临时变量，比如通常在方法中定义的临时变量。

```java
class LocalVariable {
  public static void main(String args[]) {
    // a 和 b 都是局部变量
    int a = 10;
    int b = 13;

    if (b > 13) {
      // a 和 result 都是局部变量
      int a = 15;
      int result = method(a);
      System.out.println(result);
    }
  }
  public int method(int a) {
    // a 和 result 都是局部变量
    int result = a + 10;
    return result;
  }
}
```

局部变量与成员变量不同，在声明之后如果不为其赋初始值，系统不会自动为其分配初始值。如果企图使用没有初始化的局部变量的值，则会编译报错。

示例代码：

```java
class LocalVariable {
  public static void main(String args[]) {
    // a 是局部变量，没有初始化，会编译报错
    int a;
    System.out.println(a);
  }
}
```

## 作用域

成员变量的作用域是整个类，在类的任何方法中都可以使用。另外还涉及到变量的修饰符，具体在[变量的修饰符](oop_access_modifier.md)中介绍。

局部变量的作用域是定义它的方法或代码块，出了作用域就不能使用。

示例代码：

```java
class LocalVariable {
  public static void main(String args[]) {
    // a 是局部变量，作用域是 main 方法
    int a = 10;
    if (a > 0) {
      // a 是局部变量，作用域是 if 语句块
      int b = 13;
      System.out.println(b);
    }
    // 编译报错，b 超出了作用域
    System.out.println(b);
  }
}
```
