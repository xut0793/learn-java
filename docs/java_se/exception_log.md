# 日志

## What 什么是日志

日志是用来记录应用程序运行信息的，包括但不限于异常报错信息，性能统计信息等。帮助开发人员和运维人员了解应用程序的运行状态，及时发现和解决问题。

> 日志就是程序的脉象，通过日志能及时了解程序的健康程度，对程序的各项指标能进行有效的量化诊断，优化和改进。

## Why 为什么需要日志

假设没有日志记录，会导致：

- 对系统的运行状态一知半解，甚至一无所知
- 系统出现问题无法定位，或者需要花费巨大的时间和精力
- 无法发现系统瓶颈，不知优化从何做起
- 无法基于日志对系统运行过程中的错误和潜在风险进行监控和报警
- 对挖掘用户行为和提升产品价值毫无帮助
- ......

相对应的，如果有规范的日志记录，那么：

- 了解线上系统的运行状态
- 快速准确定位线上问题
- 及时发现系统瓶颈
- 预警系统潜在风险
- 挖掘产品最大价值
- ……

## How 如何记录日志

> 打日志是一门艺术，良好规范的日志是系统监控（数据分析、问题排查、异常告警、性能优化）的基础。

### 记录什么

要记录什么就要看我们需要什么

- 日志首要的需求就是问题的诊断，能够快速定位线上问题，这类一般称为诊断日志，通常需要记录
  - 应用的配置参数
  - 请求上下文信息、请求参数和响应参数
  - 异常堆栈信息
  - ......
- 需要统计分析，及时发现性能瓶颈，异常告警等，称为统计日志，通常需要记录
  - 用户行为分析：访问行为、操作、请求耗时等
  - 资源消耗情况，比如磁盘占用，或网络流量消耗等
  - .......

记录原则：**不多不少**

- 所谓不多，是指不要在日志中记录无用的信息。实践中常见到的无用的日志有：
  - 能够放在一条日志里的东西，放在多条日志中输出；
  - 预期会发生且能够被正常处理的异常，打印出一堆无用的堆栈；
  - 开发人员在开发过程中为了调试方便而加入的“临时”日志
  - ......
- 所谓不少，是指对于日志的使用者（开发人员、运维人员、日志分析系统等），能够从日志中得到所有需要的信息。实践中经常发生日志不够的情况：
  - 请求出错时不能通过日志直接来定位问题，而需要开发人员再临时增加日志并要求请求的发送者重新发送同样的请求才能定位问题；
  - 无法确定服务中的后台任务是否按照期望执行；
  - 无法确定服务的内存数据结构的状态；
  - 无法确定服务的异常处理逻辑（如重试）是否正确执行；
  - 无法确定服务启动时配置是否正确加载；
  - ......

### 日志级别

日志级别，根据日志的重要性和紧急程度，通常可以分为以下几个级别，级别标识不固定，可能不能日志框架有自己的日志级别，这里以 Java 内置的日志框架来说明：

- SEVERE：表示需要立即被处理的系统级错误。
  当该错误发生时，表示服务已经出现了某种程度的不可用，系统管理员需要立即介入。这属于最严重的日志级别，因此该日志级别必须慎用，如果这种级别的日志经常出现，则该日志也失去了意义。通常情况下，一个进程的生命周期中应该只记录一次这个级别的日志，即该进程遇到无法恢复的错误而退出时。当然，如果某个系统的子系统遇到了不可恢复的错误，那该子系统的调用方也可以记入级别日志，以便通过日志报警提醒系统管理员修复；

- WARNING：该日志表示系统可能出现问题，也可能没有，这种情况如网络的波动等。
  对于那些目前还不是错误，然而不及时处理也会变为错误的情况，也可以记为 WARN 日志，例如一个存储系统的磁盘使用量超过阀值，或者系统中某个用户的存储配额快用完等等。对于 WARN 级别的日志，虽然不需要系统管理员马上处理，也是需要及时查看并处理的。因此此种级别的日志也不应太多，能不打 WARN 级别的日志，就尽量不要打；

- INFO：该种日志记录系统的正常运行状态。
  例如某个子系统的初始化，某个请求的成功执行等等。通过查看 INFO 级别的日志，可以很快地对系统中出现的 WARN,ERROR,FATAL 错误进行定位。INFO 日志不宜过多，通常情况下，INFO 级别的日志应该不大于 TRACE 日志的 10%；

- CONFIG：该日志记录系统的配置信息。
  比如程序启动时加载的配置文件、初始化的参数等。这类日志有助于调试和验证程序的配置是否正确。

- FINE、FINER、FINEST：这三种日志级别分别对应了不同的详细程度，FINEST 级别是最详细的，而 FINE 级别是最不详细的。这三种日志级别通常用于调试的目的。
  - FINE：用来记录更详细的信息，比如程序内部的执行细节、变量的值等。这类日志对于开发者在调试程序时了解程序的内部状态非常有用。
  - FINER：比 FINE 级别更细的日志，记录更深入的执行细节。通常用于深入分析程序的运行情况。
  - FINEST：这是最低级别的日志，记录最详细的信息，包括程序的每一步执行细节。这类日志可能会产生大量的输出，通常只在需要非常详细的调试信息时使用。

### print

`System.out` 提供了一些打印消息的静态方法，常用的有：

- `System.out.println()`：打印一条消息并换行
- `System.out.print()`：打印一条消息不换行
- `System.out.printf()`：格式化打印消息
- `System.out.flush()`：刷新输出流
- `System.out.close()`：关闭输出流

> 程序的标准输入和输入流包括三个 IO 流：标准输入流 stdin、标准输出流 stdout、标准错误输出流 stderr。
>
> - 标准输入流 stdin：默认从键盘输入，也可以从文件或其他输入流中读取数据。
> - 标准输出流 stdout：默认输出到控制台，也可以输出到文件或其他输出流中。
> - 标准错误输出流 stderr：用于输出错误信息，默认也输出到控制台，也可以输出到文件或其他输出流中。

## JUL 日志框架

Java 自带的日志框架 ，功能相对基础，性能一般，但对于简单的日志需求来说足够用了。

它的主要组件类有：

- `java.util.logging.Logger`：日志记录器，用于记录日志消息，是日志系统的核心。
- `java.util.logging.Handler`：日志处理程序，将日志信息输出到不同的目的地，比如控制台、文件、数据库等。可以为每个 Logger 配置一个或多个 Handler
- `java.util.logging.Formatter`：日志格式化程序，用于格式化日志消息，比如显示时间戳、日志级别、线程名等。
- `java.util.logging.Level`：日志级别，用于指定日志消息的重要性和紧急程度。可以设置为 SEVERE、WARNING、INFO、CONFIG、FINE、FINER、FINEST 等级别。
- `java.util.logging.Filter`：日志过滤器，用于过滤日志消息，可以设置一些规则，只有满足这些规则的日志才会被记录，比如根据日志级别、日志来源、日志内容等条件进行过滤。
- `java.util.logging.LogRecord`：日志记录对象本身，用于表示一条日志消息，包含了日志的级别、消息内容、时间戳、线程名等信息。

使用步骤：

1. 获取 Logger 实例。
2. 添加 Handler
3. 为上一步添加的 Handler 设置日志级别（Level）和格式输出（Formatter）
4. 创建 Filter 过滤器
5. 为 Logger 实例添加日志处理器（Handler）和日志过滤器（Filter）
6. 记录日志。

这里我们按照上面的步骤，配置通过硬编码的方式，创建一个日志记录器，将日志文件分别输出到控制台和文件中。

```java
import java.util.logging.*;

public class LoggingExampleTest {

    public void testLogging() {
        // 获取日志记录器
        Logger logger = Logger.getLogger("LoggingExample");

        // 设置日志级别为INFO，这意味着INFO级别及以上的日志会被记录
        logger.setLevel(Level.INFO);

        // 创建控制台Handler 将日志输出到控制台
        // 并设置其日志级别和Formatter
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.WARNING); // 控制台只输出WARNING及以上级别的日志
        consoleHandler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                // 自定义日志格式
                return String.format("%1$tF %1$tT [%2$s] %3$s %n", record.getMillis(), record.getLevel(), record.getMessage());
            }
        });
        logger.addHandler(consoleHandler);

        // 创建文件Handler 将日志输出到文件
        // 并设置其日志级别和Formatter
        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.ALL); // 文件将记录所有级别的日志
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    // 自定义日志格式
                    return String.format("%1$tF %1$tT [%2$s] %3$s %n", record.getMillis(), record.getLevel(), record.getMessage());
                }
            });
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建并设置Filter
        Filter filter = new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                // 这里可以添加过滤逻辑，例如只记录包含特定字符串的日志
                return record.getMessage().contains("important");
            }
        };

        // 将Filter应用到Logger
        //logger.setFilter(filter);

        // 记录不同级别的日志
        logger.severe("严重错误信息 - 应记录到控制台和文件");
        logger.warning("警告信息 - 应记录到控制台和文件");
        logger.info("常规信息 - 只记录到文件");
        logger.config("配置信息 - 只记录到文件");
        logger.fine("详细日志 - 只记录到文件");


        // 这条日志将被Filter过滤掉，不会记录
        logger.info("这条信息不重要，将被过滤");

        // 这条日志将被记录，因为消息中包含"important"
        logger.info("这条信息很重要，将被记录到控制台和文件");
    }
}
```

## 日志配置文件

日志配置信息通过硬编码的方式配置，不利于后期维护和修改。为了方便配置和管理日志，Java 提供了日志配置文件 `logging.properties`。

这个文件可以放在类路径下（如 `src/main/resources` 目录），也可以放在任意位置，然后通过 `-D java.util.logging.config.file=path/to/logging.properties` 系统属性指定配置文件的位置。

如果不指定配置文件，Java 会使用默认的配置，即记录所有级别的日志到控制台。

下面是一个简单的 `logging.properties` 配置文件示例：

```properties
# 指定日志处理器为：ConsoleHandler,FileHandler 表示同时使用控制台和文件处理器
handlers= java.util.logging.ConsoleHandler,java.util.logging.FileHandler

#设置默认的日志级别为：ALL
.level= ALL

# 配置自定义 Logger
com.xiezhr.handlers = com.xiezhr.DefConsoleHandler
com.xiezhr.level = CONFIG

# 如果想要使用自定义配置，需要关闭默认配置
com.xiezhr.useParentHanlders =true

# 向日志文件输出的 handler 对象
# 指定日志文件路径 当文件数为1时 日志为/logs/java0.log
java.util.logging.FileHandler.pattern = /logs/java%u.log
# 指定日志文件内容大小，下面配置表示日志文件达到 50000 字节时，自动创建新的日志文件
java.util.logging.FileHandler.limit = 50000
# 指定日志文件数量，下面配置表示只保留 1 个日志文件
java.util.logging.FileHandler.count = 1
# 指定 handler 对象日志消息格式对象
java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
# 指定 handler 对象的字符集为 UTF-8 ，防止出现乱码
java.util.logging.FileHandler.encoding = UTF-8
# 指定向文件中写入日志消息时，是否追加到文件末尾，true 表示追加，false 表示覆盖
java.util.logging.FileHandler.append = true


# 向控制台输出的 handler 对象
# 指定 handler 对象的日志级别
java.util.logging.ConsoleHandler.level =WARNING
# 指定 handler 对象的日志消息格式对象
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
# 指定 handler 对象的字符集
java.util.logging.ConsoleHandler.encoding = UTF-8

# 指定日志消息格式
java.util.logging.SimpleFormatter.format = [%1$tF %1$tT] %4$s: %5$s %n
```

编码读取日志配置文件

```java
import java.util.logging.*;

public class LoggingExampleTest {

    public void testLogProperties() throws Exception{
        // 1、读取配置文件，通过类加载器
        InputStream ins = LoggingExampleTest.class.getClassLoader().getResourceAsStream("logconfig.properties");
        // 2、创建LogManager
        LogManager logManager = LogManager.getLogManager();
        // 3、通过LogManager加载配置文件
        logManager.readConfiguration(ins);
        // 4、创建日志记录器
        Logger logger = Logger.getLogger("LoggingExample");
        // 5、记录不同级别的日志
        logger.severe("这是一条severe级别信息");
        logger.warning("这是一条warning级别信息");
    }

    @Test
    public void testLogProperties() throws Exception{
        testLogProperties();
    }
}
```

## 日志格式化

日志格式化是指在日志消息中添加额外的信息，例如时间、日志级别、线程名称等。

Java 日志框架提供了 `java.util.logging.Formatter` 类来实现日志格式化。

默认情况下，`java.util.logging.SimpleFormatter` 类会将日志消息格式化为 `[时间] 日志级别: 日志消息` 的格式。

如果需要自定义日志消息格式，可以继承 `java.util.logging.Formatter` 类，然后在 `format` 方法中实现自定义的格式化逻辑。

下面是一个简单的自定义日志格式化器示例：

```java
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("[%1$tF %1$tT] %4$s: %5$s %n",
                record.getMillis(), record.getLevel(), record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
    }
}
```

也可以在 `logging.properties` 配置文件中，指定自定义的格式化器：

```properties
java.util.logging.ConsoleHandler.formatter = com.xiezhr.CustomFormatter
```

不管是哪种方式设置日志格式，我们看源码最终都是通过 `String.format` 函数来实现的，所有我们有必要学一学 `String` 类提供的 `format` 这个方法的使用。

`String.format` 方法的使用

```java
String.format("Hello, %s! You are %d years old.", "Alice", 25);
```

`String.format` 方法的参数中常用的占位符：

以下是 `String.format` 方法中常用的占位符类型：

| 占位符 | 说明                                           | 示例                                                                            |
| ------ | ---------------------------------------------- | ------------------------------------------------------------------------------- |
| `%s`   | 用于字符串类型的格式化                         | `String.format("Hello, %s!", "Alice")` 输出 `Hello, Alice!`                     |
| `%d`   | 用于十进制整数类型的格式化                     | `String.format("You are %d years old.", 25)` 输出 `You are 25 years old.`       |
| `%f`   | 用于浮点数类型的格式化                         | `String.format("Price: %.2f", 19.99)` 输出 `Price: 19.99`                       |
| `%c`   | 用于字符类型的格式化                           | `String.format("First letter: %c", 'A')` 输出 `First letter: A`                 |
| `%b`   | 用于布尔类型的格式化                           | `String.format("Is valid: %b", true)` 输出 `Is valid: true`                     |
| `%x`   | 用于十六进制整数类型的格式化                   | `String.format("Hex: %x", 255)` 输出 `Hex: ff`                                  |
| `%o`   | 用于八进制整数类型的格式化                     | `String.format("Octal: %o", 8)` 输出 `Octal: 10`                                |
| `%e`   | 用于科学计数法浮点数类型的格式化               | `String.format("Scientific: %e", 10000.0)` 输出 `Scientific: 1.000000e+04`      |
| `%%`   | 用于输出百分号 `%`                             | `String.format("Discount: %d%%", 20)` 输出 `Discount: 20%`                      |
| `%t`   | 用于日期和时间类型的格式化，需要搭配子格式使用 | `String.format("Date: %tF", new Date())` 输出类似 `Date: 2024-01-01` 的日期格式 |

占位符中还可以搭配使用一些特别的格式说明符，例如：

- `%0d`：用于指定输出的宽度，不足的部分用 0 填充
- ` `: 空格，指定数学或字符前面补空格，用于结课
- `-`：用于指定输出的对齐方式，左对齐
- `.2f`：用于指定输出的精度，例如 `.2f` 表示保留两位小数
- `,`：用于指定数字的分组分隔符，例如 `,d` 表示以逗号分隔千位
- `数字$`：默认情况下，可变参数是按照顺序依次替换，但是我们可以通过`数字$`来重复利用可变参数,用于指定参数的索引，例如 `%1$s` 表示第一个参数，`%2$d` 表示第二个参数

另外，还有一些日期时间的占位符，以 `%t`开头，接对应的占位符字母，比如 `%tF`。

| 占位符 | 说明                                         | 示例                            |
| ------ | -------------------------------------------- | ------------------------------- |
| `c`    | 包含全部日期和时间信息                       | `周六 8月 03 17:16:37 CST 2024` |
| `F`    | ISO 8601 格式的日期（年-月-日）              | `2024-08-03`                    |
| `D`    | 美国格式的日期（月/日/年）                   | `08/03/24`                      |
| `tr`   | 12 小时制的时间（时:分:秒 上午/下午）        | `05:16:37 下午`                 |
| `T`    | 24 小时制的时间（时:分:秒）                  | `17:16:37`                      |
| `R`    | 24 小时制的时间（时:分）                     | `17:16`                         |
| `Y`    | 4 位数的年份                                 | `2024`                          |
| `y`    | 2 位数的年份                                 | `24`                            |
| `m`    | 2 位数的月份                                 | `08`                            |
| `d`    | 2 位数的日期                                 | `03`                            |
| `e`    | 不带前导零的日期                             | `3`                             |
| `H`    | 24 小时制的 2 位数小时                       | `17`                            |
| `I`    | 12 小时制的 2 位数小时                       | `05`                            |
| `k`    | 24 小时制的不带前导零的小时                  | `17`                            |
| `l`    | 12 小时制的不带前导零的小时                  | `5`                             |
| `M`    | 2 位数的分钟                                 | `16`                            |
| `S`    | 2 位数的秒                                   | `37`                            |
| `L`    | 3 位数的毫秒                                 | `123`                           |
| `N`    | 9 位数的纳秒                                 | `123456789`                     |
| `p`    | 上午/下午标记                                | `下午`                          |
| `Z`    | 时区缩写                                     | `CST`                           |
| `z`    | 时区偏移量                                   | `+0800`                         |
| `A`    | 星期几的全称                                 | `星期六`                        |
| `a`    | 星期几的简称                                 | `周六`                          |
| `B`    | 月份的全称                                   | `八月`                          |
| `b`    | 月份的简称                                   | `8月`                           |
| `C`    | 世纪数（年份的前两位）                       | `20`                            |
| `Q`    | 自 UTC 1970 年 1 月 1 日 00:00:00 起的毫秒数 | `1722666997123`                 |

## 日志框架

标准 Java 发行版日志包`java.util.logging`的设计被普遍认为很糟糕。大多数人会选择一个替代的日志包。

日志门面提供了一套标准的日志记录接口，而具体的日志记录工作则由不同的日志框架来完成。这样做的好处是，可以在不修改代码的情况下，通过配置来切换不同的日志框架。

主流的日志门面框架主要有：

- JCL：这是早期的一个日志门面。
- SLF4J：这是一个非常流行的日志门面，它提供了一套简单的日志记录接口，并且可以与多种日志框架（如 Log4j、Logback 等）配合使用。

现在基本上都使用 SLF4J 作为日志门面，而具体的日志实现框架则根据项目需求和团队偏好进行选择。

实现了 `SLF4J` 日志门面的常用日志框架有：

- `JUL: java.util.logging`
- `logback`
- `log4j`
- `log4j2`

## 日志分析

应用程序通常会记录海量的日志信息，这些日志信息可以帮助开发人员定位和修复问题，以及监控应用程序的运行状态。因此，日志分析成为了应用程序开发和维护中的重要任务。

日志分析通常包括以下几个方面：

- 日志过滤：根据日志级别、时间范围、日志内容等条件，过滤出感兴趣的日志记录。
- 日志分析：对过滤出的日志记录进行分析，提取有用的信息，例如异常栈轨迹、性能指标、业务数据等。
- 日志可视化：将分析出的日志信息以图表、表格等形式进行可视化展示，以便于开发人员快速理解和分析日志数据。

`ELK Stack` (Elasticsearch, Logstash, Kibana)：`ELK Stack` 是一个开源的日志管理解决方案，它由 Elasticsearch、Logstash 和 Kibana 这三个组件组成。

- Logstash：这是一个用于日志收集、处理和分析的工具，它可以从多个来源（如文件、数据库、消息队列等）收集日志数据，并将其发送到 Elasticsearch 等分析平台进行处理和可视化。
- Kibana：这是一个用于日志可视化的工具，它可以连接到 Elasticsearch 等分析平台，将日志数据以图表、表格等形式进行展示和分析。
- Elasticsearch：这是一个用于存储和搜索日志数据的分布式搜索引擎，它可以与 Logstash 和 Kibana 等工具配合使用，提供强大的日志分析功能。
