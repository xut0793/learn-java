# 调试

## What: 调试的相关概念

我们利用不同的语言描述业务逻辑，然后运行它观察结果，当代码的逻辑比较复杂的时候，难免会出错，所以程序运行的**错误**叫做 `bug`。

为查找解决这个问题(BUG)，我们希望能够一步步运行或是运行到某个点停下来，这个点叫做**断点`breakpoint`**，通常情况下，它的表现真的是个点，比如 idea 中的断点就是一个红色的点，能让程序运行到此处停下来。此时可以查看断点所在的上下文环境中的作用域变量、函数参数、函数调用堆栈等信息，能够完成这个功能的程序叫**调试器`debugger`**。

> 1947 年 9 月 9 日，哈佛大学在测试马克 II 型艾肯中继器计算机的时候，一只飞蛾粘在一个继电器上，导致计算机无法正常工作，操作员把飞蛾移除之后，计算机又恢复了正常运转。于是他们将这只飞蛾贴在了他们当时记录的日志上，并在日志最后写了这样一句话：First actual case of bug being found。这是他们发现的第一个真正意义上的 bug，这也是人类计算机软件历史上发现的第一个 bug。他们也提出了一个词，“debug（调试）”了机器，由此引出了计算机调试技术的发展。

## Why: 为什么需要调试

在纷繁复杂的代码世界中，出错是难免的，调试代码是你最快找到问题原因的便捷途径。使用断点调试的主要好处就是可以观察程序运行的实际情况，而不用做假设。另一方面，在调试器中可以手动控制代码执行的逻辑，比如暂定执行，或者逐行运行，甚至修改内存中的值，让它走到另一个分支里。

## How: 如何调试

从简单到复杂的调试方法。

## print 打印日志

对于简单的问题调试，可以使用 `System.out.println` 或 `logger` API 来打日志。

```java
System.out.println("x = " + x);
// 或
logger.info("x = " + x);
```

这样 Java 程序会调用这个对象的 `toString` 方法，将对象转换为字符串，然后打印到控制台或者日志文件中。Java 类库中绝大多数类都覆盖了 `toString` 方法，从而能够提供有用的类信息。

但是这种方式只适用于排除简单问题，当问题比较复杂时，这种方式就有很多缺点：

- 不能完全展现代码逻辑当前的上下文信息，
- 每次添加调试语句都需要重新编译运行。

## printStackTrace 打印异常栈轨迹

当程序抛出异常时，我们可以调用异常对象的 `printStackTrace` 方法来打印异常栈轨迹，从而可以知道异常是从哪里抛出的，以及调用栈的情况。

```java
try {
    // 一些可能会抛出异常的代码
} catch (Exception e) {
    e.printStackTrace();
    throw e;
}
```

甚至不需要捕获异常来生成栈轨迹。只要在代码的某个位置插入下面这条语句就可以获得栈轨迹：

```java
Thread.dumpStack();
```

## assert 断言

不管是使用 `print` 还是 `logger` 打印日志，代码都一直保留在程序中，如果程序中含有大量这种检查语句，会导致代码冗长，并且拖慢程序执行。

断言 ( assertion) 机制允许你在测试期间在代码中捕人一些检查，而在生产代码中自动删除这些检查。

断言是一种调试工具，用于检查程序中的假设是否为真。它有两种语法形式：

```java
assert condition;
```

和

```java
assert condition : expression;
```

这两个语句都会计算条件( condition), 如果结果为 false, 则抛出一个 `AssertionError` 异常。在第二个语句中，表达式 (expression) 结果将传人 `AssertionError` 对象的构造器，并转换成一个消息字符串。如果条件为 true, 则不抛出异常。

默认情况下，断言是禁用的，需要在运行 Java 程序时显式地启用它。可以使用 `-enableassertions` 或 `-ea` 标志来启用断言。

```sh
java -ea MyProgram
```

也可以指定哪些类编译时启用断言：

```sh
javac -ea:com.example.MyClass MyClass.java
```

或者使用 `-disableassertions` 或 `-da` 编译时哪些类禁用断言：

```sh
javac -da:com.example.MyClass MyClass.java
```

## main 方法执行单个类

可以为每一个类中放置一个单独的 `main` 方法，在其中创建这个类的实例对象，这样允许你独立执行这个类来测试功能是否正常。

编译构建程序时，可以保留 `main` 方法，因为这些 main 方法不会被调用，在运行应用程序时，Java 虚拟机只会调用启动类的 `main` 方法作为入口。

## `>` 管道收集异常信息

在终端容器中显示全部异常信息会显得很冗长，而且在真正需要诊断错误原因时却又无法得到这些消息, 更好的方法是将这些消息记录到一个文件中。不过，错误会发送到 `System.err` 而不是 `System.out` 因此，不能通过运行下面的命令来获取错误；

```sh
java MyProgram > errors.txt
```

而应当如下所示，捕获错误流：

```sh
java MyProgram 2> errors.txt
```

要想在同一个文件中同时捕获 `System.err` 和 `System.out`, 需要使用以下命令：

```sh
java MyProgram 1> errors.txt 2>&1
```

这在 bash 和 Windows shell 中都有效。

另外，也可以用静态方法 `Thread.setDefaultUncaughtExceptionHandler` 全局改变未捕获异常的处理器，将异常信息记录到文件中。

```java
Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 记录异常信息到文件
        try (PrintWriter writer = new PrintWriter(new FileWriter("errors.txt", true))) {
            writer.println("Thread: " + t.getName());
            e.printStackTrace(writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
});
```

## `-verbose` 观察类的启动过程

要想观察类的加载过程，启动 Java 虚拟机时可以使用 `-verbose` 标志。这样就可以看到类加载的详细信息，包括类的加载路径、加载时间、加载的类名等。

类似所示的输出：

```
[0.012s] [info] [class, load] opened: /opt/jdk-17.0.1/lib/modules
[0.012s] [info] [class, load] java.lang.Object source: jrt:/java.base
...
```

有时候，这对诊断类路径问题会很有帮助。

## JDB 调试器

JDB 是 Java 开发工具包 (JDK) 中的一个调试器，它可以用来调试 Java 程序。使用 JDB 可以在命令行中设置断点、单步执行、查看变量值等。

要想使用 JDB 调试程序，需要先编译程序时添加 `-g` 标志来生成调试信息。然后，在命令行中使用 `jdb` 命令来启动调试器。

```sh
# 编译程序时添加 -g 标志
javac -g MyProgram.java

# 启动 JDB 调试器
jdb MyProgram
```

这将启动 JDB 调试器，为你提供一个交互式调试跟踪入口。可以通过在提示符下键入 `?` 来查看可用的 JDB 命令列表。

使用终端命令行调试程序总是不方便，它需要显式命令来查看变量的状态(locals、dump )，或在源码中列出执行点(list)，找出系统中的线程(threads )，设置断点(stop in、stop at)等等。

但在图形化调试器窗口中只需单击几下鼠标即可提供这些功能，并旦在不使用显式命令的情况下，还可以显示正在调试的程序的最新详细信息。因此，尽管你可能是通过体验 JDB 开始的.但你会发现，学习使用图形调试器来快
速跟踪错误会更有效率。像 JetBrains 公司的 IntelliJ IDEA 这样的 IDE.都包含了很好的 Java 图形调试器。

[IntelliJ IDEA 调试你的第一个 Java 应用程序](https://www.jetbrains.com/zh-cn/help/idea/debugging-your-first-java-application.html)

## jconsole 监控和管理程序

Java 虚拟机提供了对 Java 应用的监控 ( monitoring) 和管理 ( management) 提供了 API，允许在虚拟机中安装代理工具来跟踪内存消耗、线程使用、类加载等情况。这个特性对于规模很
大而且长时间运行的 Java 程序 (如应用服务器) 尤其重要。

JDK 提供了一个名为 jconsole 的图形工具，可以显示有关虚拟机性能的统计结果，启动你的程序，然后启动 jconsole, 从正在运行的 Java 程序列表中选择你的程序端口，就可以查看该程序的性能统计信息了。

或者也可以在命令行中使用 `jconsole` 命令来启动 jconsole 工具。

```sh
jconsole -port 12345
```

也可以第三方工具如 VisualVM、JMC 等来监控和管理 Java 应用。
