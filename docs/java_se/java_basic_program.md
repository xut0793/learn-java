# Java 程序基本结构

下面我们通过一个经典的 "Hello World" 程序来了解 Java 的基本语法，以及如何编译和运行 Java 程序。

以下是一个简单的 Java 程序，用于输出 "Hello World!"：

1. 使用文本文件创建一个名为 `HelloWorld.java` 的文件。
2. 在 `HelloWorld.java` 文件中输入以下代码：

```java
/**
* 这是一个经典的 Java 入门程序类。
* 该类的主要功能是在控制台输出 "Hello World!" 字符串，
* 常被用于演示 Java 程序的基本结构和运行方式。
*/
public class HelloWorld {
    /**
    * 程序的入口方法，JVM 在启动时会调用该方法来执行程序。
    * 此方法将在控制台输出 "Hello World!" 字符串。
    *
    * @param args 命令行参数，是一个字符串数组，在本程序中未被使用。
    */
    public static void main(String[] args) {
      /*
       * 声明变量 s 并赋值为 "World"
       */
      String s = "World";
      // 输出 "Hello World!" 字符串到控制台
      System.out.println("Hello " + s + "!");
    }
}
```

3. 编译 Java 程序: 打开命令行工具（如 Windows 下的 cmd 或 PowerShell，Mac 下的终端），切换到 `HelloWorld.java` 文件所在的目录，输入以下命令编译程序：
   ```shell
   javac HelloWorld.java
   ```
   编译成功后，会在同一目录下生成一个名为 `HelloWorld.class` 的文件。
4. 运行 Java 程序，在命令行工具中输入以下命令运行程序：
   ```shell
   java HelloWorld
   ```
   程序将在控制台输出 "Hello World!" 字符串。

## 基本语法规则

类是构建所有 Java 应用程序的基本单位。Java 应用程序中的全部内容都必须放置在类中。

1. 源代码的文件名必须与公共类的类名相同，且以 `.java` 为扩展名。
2. 每个 Java 程序都必须包含一个公共类，以 `public` 修饰，类的声明以关键字 `class` 开头，后面跟着类的名称，该类的类名必须与文件名相同，类名使用大驼峰命名法 `HelloWorld`。
3. 类体由大括号 `{}` 括起来，类体中可以包含字段、方法、构造方法、初始化块等。
4. 这个公共类必须包含 `public static void main(String[] args)` 方法作为程序的入口，Java 虚拟机将从指定类中的 main 方法开始执行。
5. Java 中的所有函数都属于某个类的方法（标准术语将其称为方法，而不是成员函数），方法名使用小驼峰命名法 `main`。
6. 方法的入参列表可以为空，也可以包含一个或多个参数。每个参数由`数据类型 参数名` 组成，多个参数之间用逗号隔开。`String[] args`
7. 方法体由大括号 `{}` 括起来，方法体中可以包含语句、变量声明、控制结构等。
8. 变量的声明语句格式为 `数据类型 变量名;`，例如 `String s = "World";` 表示声明一个字符串类型变量 `s`，同时赋值。Java 使用双引号包裹字符串。
9. 每个 Java 语句必须以分号 `;` 结尾。
10. 注释可以用于解释代码的功能和实现细节，Java 支持三种注释方式：
    - 单行注释：以 `//` 开头，注释内容直到行尾结束。
    - 多行注释：以 `/*` 开头，以 `*/` 结尾，注释内容可以跨越多行。
    - 文档注释：以 `/**` 开头，以 `*/` 结尾，注释内容可以包含 Javadoc 标签，用于生成 API 文档。
