# 多进程

在现代 Java 开发中，使用**多进程**来实现常规的业务并发逻辑**是非常不推荐的**。

这并不是因为 Java 语言本身不能实现多进程，Java 提供了非常完善的 API 来启动和管理独立进程，不推荐的核心原因在于**性价比极低**。

我们可以从以下几个维度来理解为什么线程（尤其是现代的虚拟线程）是 Java 并发的首选，而多进程通常被排除在外：

## 为什么不推荐用多进程做并发？

1. 资源开销巨大（重量级）：
   - 进程：每个进程都有自己独立的 JVM 实例、独立的内存空间（堆、栈、方法区等）。启动一个 Java 进程，光是 JVM 的初始化和类加载就需要消耗大量的内存和 CPU 时间。
   - 线程：尤其是 JDK 21 引入的**虚拟线程**，其创建和销毁的开销微乎其微。单机可以轻松支撑数百万个虚拟线程，而进程通常只能开几十个。
2. 通信成本极高：
   - 进程间通信 (IPC)：由于进程间内存隔离，数据交换必须通过复杂的 IPC 机制（如 Socket、管道、共享内存、消息队列等），这涉及到数据的序列化、内核态与用户态的切换，效率很低。
   - 线程间通信：同一个 JVM 内的线程共享堆内存，可以通过共享变量、并发容器（如 `ConcurrentHashMap`）直接高效地交换数据。
3. 开发复杂度：多进程架构通常意味着你需要维护多个独立的应用程序，处理分布式环境下的各种故障（如网络抖动、进程崩溃），这本质上是在写一个分布式系统，而非单纯的并发程序。

## 那 Java 的多进程通常用在什么场景？

Java 的多进程能力通常用于**系统隔离**和**资源管控**，而不是为了提升单一业务的并发吞吐量。例如：

- **插件化架构**：防止某个第三方插件崩溃导致整个主程序挂掉（如 IDE 的插件进程）。
- **执行外部命令**：在 Java 程序中调用 Python 脚本、Shell 脚本或系统工具（如 FFmpeg 处理视频）。
- **资源限制**：需要严格限制某个任务的 CPU 和内存上限，防止其拖垮主服务。

如果你是为了实现高并发的业务逻辑（如处理成千上万的 Web 请求、并行计算），请毫不犹豫地选择 **线程池** 或 **虚拟线程**；只有当你需要绝对的**故障隔离**或**调用外部系统工具**时，才考虑使用多进程。

## ProcessBuilder

`ProcessBuilder` 是 Java 1.5 引入的用于创建和管理操作系统进程的核心类（位于 `java.lang` 包中）。相比于老旧的 `Runtime.exec()`，它提供了更细粒度的控制能力，允许开发者灵活地设置进程的环境变量、工作目录、输入/输出重定向等，是 Java 多进程编程的首选方案。

### 基础用法

`ProcessBuilder` 的使用非常直观，通过构造方法传入命令及参数，调用 `start()` 即可启动进程：

```java
// 执行 "ls -l /" 命令
ProcessBuilder pb = new ProcessBuilder("ls", "-l", "/");
Process process = pb.start();
```

在实际业务中，我们通常需要修改工作目录、合并错误流或重定向输出：

```java
ProcessBuilder pb = new ProcessBuilder("myCommand", "arg1");

pb.directory(new File("/path/to/working/dir")) // 设置工作目录
    .redirectErrorStream(true) // 将标准错误合并到标准输出
    .redirectOutput(new File("output.log")); // 将输出重定向到文件
Process process = pb.start();
```

### API

`ProcessBuilder` 对象的核心属性与常用方法的详细总结：

| 类别     | 名称                         | 作用与说明                                                          |
| :------- | :--------------------------- | :------------------------------------------------------------------ |
| 核心属性 | command (命令)               | 一个字符串列表，包含要调用的外部程序及其参数。                      |
|          | environment (环境)           | 变量到值的映射（Map），初始值为当前进程环境变量的副本。             |
|          | directory (工作目录)         | 子进程的工作目录，默认为当前 Java 进程的 `user.dir`。               |
|          | redirectErrorStream          | 布尔值。为 `true` 时，子进程的标准错误会合并到标准输出中。          |
| 常用方法 | start()                      | 利用当前配置的属性启动一个新的子进程，返回 `Process` 对象。         |
|          | command(...)                 | 设置或获取此进程生成器的操作系统程序和参数（支持可变参数或 List）。 |
|          | environment()                | 返回此进程生成器环境变量的 Map 视图，可直接增删改环境变量。         |
|          | directory(File dir)          | 设置子进程的工作目录。                                              |
|          | redirectErrorStream(boolean) | 设置是否将标准错误流合并到标准输出流（默认 false）。                |
|          | redirectOutput/Error(...)    | 将子进程的标准输出或标准错误重定向到指定文件或继承当前进程。        |

调用 `ProcessBuilder.start()` 后会返回一个 `Process` 对象，该对象代表了被启动的子进程，其常用方法如下：

| 方法名称          | 作用与说明                                                         |
| :---------------- | :----------------------------------------------------------------- |
| getInputStream()  | 获取子进程的标准输出流（注意：在父进程中是作为输入流读取）。       |
| getErrorStream()  | 获取子进程的标准错误输出流。                                       |
| getOutputStream() | 获取子进程的标准输入流（向该流写入数据，相当于给子进程发送输入）。 |
| waitFor()         | 阻塞当前线程，直到子进程执行结束，返回进程的退出码。               |
| exitValue()       | 获取子进程的退出状态码（若进程未结束直接调用会抛出异常）。         |
| destroy()         | 杀掉（终止）子进程。                                               |
| destroyForcibly() | 强制终止子进程（通常在 `destroy()` 无效或设置超时后使用）。        |

### 最佳实践

- **防止进程阻塞**：子进程的标准输出和错误输出缓冲区是有限的。如果父进程（Java程序）不及时读取这些流，缓冲区写满后会导致子进程永久阻塞。因此，**必须**在调用 `process.waitFor()` 之前或同时，通过独立线程或异步方式读取 `getInputStream()` 和 `getErrorStream()`。
- **命令注入安全**：严禁直接将未经校验的用户输入拼接到命令字符串中。建议对参数进行白名单校验，或使用 `List<String>` 的形式传递命令和参数。
- **跨平台兼容**：Windows 和 Linux 的系统命令不同（如 `cmd.exe /c dir` 与 `ls -l`），编码时建议通过 `System.getProperty("os.name")` 进行系统判断与适配。

## 示例代码

模拟了一个真实的业务场景：配置并启动一个带有自定义环境变量、指定工作目录、合并了错误流并输出到日志文件的子进程，同时妥善处理了流读取和进程退出的逻辑。

```java
import java.io.*;
import java.util.Map;

public class ProcessBuilderComprehensiveDemo {

    public static void main(String[] args) {
        // 1. 创建 ProcessBuilder 实例并设置命令及参数
        // 这里以 Linux/macOS 的 "ls -l" 为例，Windows 可替换为 "cmd.exe", "/c", "dir"
        ProcessBuilder processBuilder = new ProcessBuilder("ls", "-l");

        // 2. 设置环境变量 (environment 属性)
        // 获取当前进程环境的副本，并进行增删改
        Map<String, String> env = processBuilder.environment();
        env.put("MY_CUSTOM_VAR", "Hello_ProcessBuilder"); // 新增或修改环境变量
        env.remove("PATH"); // 谨慎操作：删除某个环境变量（此处仅做演示）

        // 3. 设置工作目录 (directory 属性)
        // 指定子进程在哪个目录下运行
        processBuilder.directory(new File("/tmp"));

        // 4. 设置流的重定向 (redirectErrorStream 属性)
        // 将标准错误流合并到标准输出流中，方便统一读取和日志记录
        processBuilder.redirectErrorStream(true);

        // 5. 将输出重定向到文件 (redirectOutput 方法)
        // 将子进程的所有输出（因为合并了错误流）追加写入到指定的日志文件中
        File logFile = new File("process_output.log");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));

        try {
            // 6. 启动进程 (start 方法)
            System.out.println("正在启动子进程...");
            Process process = processBuilder.start();

            // 7. 处理进程的输入/输出流
            // 注意：因为我们把输出重定向到了文件，process.getInputStream() 在这里将读取不到数据（会直接返回 EOF）
            // 如果未重定向到文件，必须在这里通过独立线程读取 getInputStream() 和 getErrorStream() 防止缓冲区阻塞
            // try (BufferedReader reader = new BufferedReader(
            //         new InputStreamReader(process.getInputStream()))) {
            //     String line;
            //     while ((line = reader.readLine()) != null) {
            //         System.out.println("主进程收到子进程输出 -> " + line);
            //     }
            // }

            // 如果需要向子进程发送输入（例如模拟键盘输入），可以使用 process.getOutputStream()
            // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            // writer.write("some input");
            // writer.flush();

            // 8. 等待进程结束并获取退出码 (waitFor 方法)
            // waitFor 会阻塞当前线程，直到子进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("子进程已执行完毕，退出码为: " + exitCode);

            // 9. 根据退出码判断执行结果
            if (exitCode == 0) {
                System.out.println("子进程执行成功！日志已写入: " + logFile.getAbsolutePath());
            } else {
                System.err.println("子进程执行异常，退出码: " + exitCode);
                // 如果需要强制终止失控的子进程，可以调用 process.destroyForcibly();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // 如果当前线程在 waitFor 期间被中断，可以在此处进行销毁子进程等善后处理
            Thread.currentThread().interrupt();
        }
    }
}
```
