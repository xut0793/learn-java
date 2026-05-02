# 标准输入输出和错误

在操作系统（如Linux/Unix）的底层视角中，程序启动时会默认分配三个整数编号（文件描述符）的通道：

- 0 代表标准输入
- 1 代表标准输出
- 2 代表标准错误。

而在 Java 语言层面，JVM 为每个程序预设了三个对应的流对象，它们都封装在 System 类中。

- `System.in` 代表标准输入，接收外部数据（默认连接键盘），比如等待并接收用户从键盘敲入的数据
- `System.out` 代表标准输出，输出正常信息（默认连接控制台/屏幕），比如我们向控制台打印正常的业务数据、日志或提示语时，使用 `System.out.println()`。
- `System.err` 代表标准错误，输出错误信息（默认也连接控制台），虽然`System.err`和`System.out`默认都是向控制台（屏幕）上打印文字，但在底层是两条独立的通道。这样做的好处是，在服务器运维或脚本执行时，可以将正常的日志（out）和报错的日志（err）分流，比如把正常日志存入文件A，把错误日志存入文件B，方便快速排查问题。当我们调用异常的 `e.printStackTrace()` 时，默认就是打印到 `System.err` 中。

```java
package com.learnjava.inputoutput;

import java.util.Scanner;

public class InOutErrDemo {
    public static void main(String[] args) {
        // 创建 Scanner 对象，监听键盘的标准输入 System.in
        Scanner sc = new Scanner(System.in);

        System.out.println("==== 欢迎使用 Java 控制台演示标准输入输出 ====");
        System.out.print("请输入您的年龄："); // 不换行

        // 读取输入，此时阻塞一直到用户输入
        int age = sc.nextInt();

        if (age <= 0) {
            // 标准错误输出
            System.err.println("年龄不能是负数");
        } else {
            System.out.printf("您输入的年龄是： %d", age);
        }

        // 程序结束，需要关闭监听资源
        sc.close();
    }
}
```

## Scanner

上述例子中，对标准输入的读取，需要使用 `Scanner sc = new Scanner(System.in);` 创建一个 Scanner 对象，并监听标准输入 `System.in`。为什么有了 `System.in` 还需要 `Scanner`？

`System.in` 是 JVM 启动时自动创建的全局标准输入流，它的基类型是 `InputStream`（字节流）。这意味着它只能读取最基础的“字节”数据。如果你想用 `System.in` 读取用户输入的一行中文字符串或一个整数，你需要自己写很多代码：先把字节流转为字符流（`InputStreamReader`），再处理字符转成为数字（`Integer.parseInt()`）。这么长处理路径在日常开发中非常繁琐。

比如上面年龄解析的代码，可以这样改写：

```java
// InputStreamReader 将字节流 System.in 转换为字符流
InputStreamReader input = new InputStreamReader(System.in);
// 为字符输入创建缓存区
BufferedReader reader = new BufferedReader(input);
try {
    // 读取输入，此时阻塞一直到用户输入
    String str = reader.readLine();
    // 将字符串转换为整数
    int age = Integer.parseInt(str);
    System.out.printf("您输入的年龄是： %d", age);
} catch (IOException e) {
    // 使用 BufferedReader 读取输入时，必须处理可能发生的 IO 异常。
    System.err.println("读取输入时发生错误： " + e.getMessage());
} catch (NumberFormatException e) {
    // 因为 Integer.parseInt() 在遇到非数字字符串时会抛出异常
    System.err.println("输入格式错误，请输入有效的整数！");
} finally {
    try {
        reader.close();
    } catch (IOException e) {
        System.err.println("关闭资源时发生错误：" + e.getMessage());
    }
}
```

Scanner 是 java.util 包下的一个工具类。它是一个功能强大的文本解析工具（加工工具），它通常会包装在 System.in 的外面，帮我们更方便地处理输入数据。自动完成以下工作：

- 封装字节转换：它内部会处理了从字节到字符的转换，内部有缓冲区，让你直接操作文本。
- 灵活的处理分隔符：默认以空格、回车作为分隔符，可以按行、按单词等读取。
- 自动类型转换：直接调用 `nextInt()`、`nextDouble()` 就能拿到数字，不用自己把字符串转成数字。

`System.in` 和 `Scanner` 像是“原材料”与“加工工具”的协作关系。

> 补充：
> System.in：没有被包装过的原始 InputStream
> System.out、System.err：被 PrintStream 包装过的 OutputStream
> Scanner 中通过InputStreamReader 将 System.in 转化为了 Reader
> PrintStream 中通过 outputStream 将 System.out / System.err 转化流转化为了Writer

## API

| 所属对象/类                    | 常用方法             | 功能描述                                                                 |
| :----------------------------- | :------------------- | :----------------------------------------------------------------------- |
| System.in<br>(标准输入字节流)  | `read()`             | 从输入流中读取单个字节的数据，返回其 ASCII 码值。                        |
|                                | `read(byte[] b)`     | 将读取到的字节数据填充到指定的字节数组中，返回实际读取的字节数。         |
| System.out<br>(标准输出打印流) | `print(内容)`        | 将指定内容打印到控制台，打印后不换行。                                   |
|                                | `println(内容)`      | 将指定内容打印到控制台，打印后自动换行（最常用）。                       |
|                                | `printf(格式, 数据)` | 按照指定的格式（如 `%s`, `%d`, `%.2f`）格式化输出数据。                  |
| System.err<br>(标准错误打印流) | `print(内容)`        | 将错误信息打印到控制台（通常显示为红色），不换行。                       |
|                                | `println(内容)`      | 将错误信息打印到控制台（通常显示为红色），自动换行。                     |
| Scanner 类<br>(文本解析工具)   | `next()`             | 读取下一个字符串，遇到空格、Tab 或回车即停止。                           |
|                                | `nextLine()`         | 读取一整行文本（包含空格），直到遇到回车换行符才停止。                   |
|                                | `nextInt()`          | 读取并解析下一个整数（`int` 类型）。                                     |
|                                | `nextDouble()`       | 读取并解析下一个浮点数（`double` 类型）。                                |
|                                | `hasNextInt()`       | 判断输入的下一个数据是否为整数，常用于输入校验，返回 `true` 或 `false`。 |

## 格式化输出

上面年龄输出时使用了 `System.out.printf("您输入的年龄是： %d", age);`，其中`System.out.printf` 中的格式说明符（Format Specifier）有一套语法规则。这套规则源于 C 语言，Java 完美继承并扩展了它。

它的核心语法结构可以拆解为以下 5 个部分：

`%[参数索引$][标志][宽度][.精度]转换符`

其中，只有 `%` 和 `转换符` 是必须的，其他都是可选的修饰符。

### 核心：转换符

转换符决定了参数最终以什么数据类型呈现。它必须跟在 % 后面（或修饰符后面）

| 转换符      | 含义             | 适用类型             | 示例 (假设变量 `val`) |
| :---------- | :--------------- | :------------------- | :-------------------- |
| `%d`        | 十进制整数       | 整数 (int, long)     | `%d` -> `123`         |
| `%f`        | 浮点数（定点）   | 小数 (float, double) | `%.2f` -> `123.46`    |
| `%s`        | 字符串           | 任意对象             | `%s` -> `"Hello"`     |
| `%c`        | 字符             | 字符 (char)          | `%c` -> `'A'`         |
| `%b`        | 布尔值           | 任意类型             | `%b` -> `true`        |
| `%x` / `%X` | 十六进制整数     | 整数                 | `%x` -> `7b`          |
| `%o`        | 八进制整数       | 整数                 | `%o` -> `173`         |
| `%%`        | 输出百分号本身   | 无                   | `%%` -> `%`           |
| `%n`        | 换行符（跨平台） | 无                   | `%n` -> (换行)        |

### 参数索引`[index$]`

指定使用参数列表中的第几个参数（从 1 开始）。如果不写，默认按顺序依次取值。可以重复使用同一个参数，或者打乱输出顺序。

```java
System.out.printf("%2$d, %1$d, %2$d", 10, 20);
// 20, 10, 20（先取第2个参数，再取第1个，再取第2个）
```

### 标志`[flags]`

用来控制对齐方式、正负号显示等细节。

- `-`：左对齐。默认是右对齐，加上它后，填充的空格会跑到右边。
- `+`：显示正号。正数前面会强制加上 + 号（负数本身就有 - 号，不受影响）。
- `0`：零填充。当指定了宽度但内容不够长时，左边用 0 补齐，而不是用空格。
- `,`：千分位分隔符。常用于金额显示。
- `#`：进制前缀。用于八进制（加 0）或十六进制（加 0x）。

### 宽度 `[width]`

一个非负整数，表示输出内容最少占用的字符个数。

- 如果实际内容长度 < 宽度：默认在左边补空格或按标志指定的 `0` 填充。
- 如果实际内容长度 > 宽度：按实际长度完整输出（不会截断）。

示例：`%5d` 输出 `123 -> "  123"`（前面有两个空格）。

### 精度 `[.precision]`

以 `.` 开头，后面跟一个整数。它的含义取决于转换符：

- 对于浮点数 (`%f`)：表示保留几位小数。例如 `%.2f` 表示保留两位小数（会自动四舍五入）。
- 对于字符串 (`%s`)：表示最多输出多少个字符。例如 `%.3s` 输出 `"Hello" -> "Hel"`。

```java
double price = 1234.5;
int amount = 50;
String item = "机械键盘";

// 1. 基础格式化：保留两位小数
System.out.printf("单价：%.2f 元%n", price);
// 输出：单价：1234.50 元

// 2. 宽度与对齐：总宽10位，左对齐
System.out.printf("商品：|%-10s|%n", item);
// 输出：商品：|机械键盘    |  (后面补了空格)

// 3. 零填充与千分位：总宽10位，不足补0，带千分位逗号
System.out.printf("总价：%0,10.2f 元%n", price * amount);
// 输出：总价：0061,725.00 元

// 4. 强制显示正负号
System.out.printf("涨跌：%+d%n", 15);  // 输出：涨跌：+15
System.out.printf("涨跌：%+d%n", -8);  // 输出：涨跌：-8

// 5. 参数索引复用
System.out.printf("原价%1$.2f，现价%1$.2f，节省%2$.2f元%n", 100.0, 20.0);
// 输出：原价100.00，现价100.00，节省20.00元
```
