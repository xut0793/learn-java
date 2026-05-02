# I/O（Input/Output）

输入和输出都是从内存角度说的，从数据源读取数据到内存中是输入Input，将数据从内存输出到目的地是输出Output。

而这个数据的来源地和目的地可以是文件、数据库、网络、甚至是在不同的内存块中转数据也是。

计算机底层的所有数据（无论是文本、图片还是视频）最终都是以二进制字节的形式存储的。直接用二进制字节序列进行数据流转具有更高的性能和效率，比如图片、视频等，但是对文本数据，需要从人类可读的字符输入或者输出可读的字符时又更方便。所以Java 实现了两组常用的 API 来处理不同数据源类型的输入和输出。

- 字节流：`java.io.InputStream` 和 `java.io.OutputStream`
- 字符流：`java.io.Reader` 和 `java.io.Writer`

```txt
                         +-------------+
                         |             |
            InputStream  |             |  OutputStream
source   +--------------->     内存     +---------------> target
            Reader       |             |  Writer
                         |             |
                         +-------------+
```

Java I/O 体系按数据类型分为字节流和字符流：

- 字节流 (`InputStream / OutputStream`)：以 8 位字节为单位传输二进制数据。它是处理所有数据（尤其是非文本的二进制数据，如图片、视频等）的基类。
- 字符流 (`Reader / Writer`)：以 16 位字符为单位传输文本数据。它在内部封装了字节流，并自动处理字符编码的编码与解码。

然后基于上述基类，采用装饰器模式（Decorator Pattern）。根据“数据源”与“处理功能”分离，通过层层包装来实现复杂的功能。

- 顶层抽象类：`InputStream/OutputStream`（字节流）和 `Reader/Writer`（字符流）定义了最基本的读写契约。
- 基础层节点实现类：直接与数据源（如文件、内存数组）交互，例如 `FileInputStream`、`FileReader`。
- 中间层过滤/包装功能的实现类：不直接连接数据源或者目标数据，而是包装其他流以增强功能。例如 `BufferedInputStream` 提供缓冲，`InputStreamReader` 提供字节到字符的转换。

有的类专门负责对接数据源（如文件、内存、网络），有的类专门负责提升性能（如缓冲区、编码转换、序列化、加解密、压缩等），有的类专门负责数据转换（如字节转字符），这样设计允许开发者在不改变原有类结构的前提下，通过包装（Wrapper）的方式，动态地为对象添加新功能（如缓冲、编码转换、数据格式化等）。这种分层避免了“类爆炸”（即为每一种功能组合都创建一个新类），实现了职责分离与灵活组合。

## 字节流体系 (InputStream / OutputStream)

它只负责把数据按8位的字节，一个字节一个字节地搬运。如果你用它读一张图片，它完美胜任；但如果你用它读一段中文文本，它只会给你一堆零散的字节，因为它不知道这些字节应该按什么规则（GBK还是UTF-8）拼成汉字。

字符流是所有数据的底层基础，适用于图片、音视频、网络报文等任意二进制数据。

| 功能分类                  | 核心子类 (后缀规律)                               | 核心作用                                                                               |
| :------------------------ | :------------------------------------------------ | :------------------------------------------------------------------------------------- |
| 对接数据源<br>(节点流)    | `FileInputStream`<br>`FileOutputStream`           | 直接对接磁盘文件，进行最基础的文件字节读写。                                           |
|                           | `ByteArrayInputStream`<br>`ByteArrayOutputStream` | 对接内存中的字节数组，在内存中读写二进制数据，减少磁盘 IO。                            |
|                           | `PipedInputStream`<br>`PipedOutputStream`         | 对接管道，主要用于多线程之间的字节数据传输。                                           |
| 增强功能<br>(处理/装饰流) | `BufferedInputStream`<br>`BufferedOutputStream`   | 缓冲流，提供缓冲区。大幅减少实际的物理 IO 次数，显著提升读写性能。                     |
|                           | `DataInputStream`<br>`DataOutputStream`           | 数据流，读写基本数据类型。能直接读写 `int`, `double`, `boolean` 等，无需手动转换字节。 |
|                           | `PrintStream`                                     | 打印流。最典型的是 System.out，提供 print/println 方法，且不会抛出 IOException。       |
|                           | `ObjectInputStream`<br>`ObjectOutputStream`       | 对象流，对象序列化与反序列化。直接将 Java 对象以二进制形式写入或从流中恢复成对象。     |

## 字符流体系 (Reader / Writer)

处理 16 位 Unicode 字符，专为文本设计，内部自动处理字符编码，避免乱码。它的底层依然拿着字节流在读取数据，但在数据交给你之前，它会通过一个“翻译器”（即 InputStreamReader / OutputStreamWriter），按照你指定的编码（比如 UTF-8）把这些零散的字节解码成人类能看懂的字符。

| 功能分类                  | 核心子类 (后缀规律)                         | 核心作用                                                                     |
| :------------------------ | :------------------------------------------ | :--------------------------------------------------------------------------- |
| 对接数据源<br>(节点流)    | `FileReader`<br>`FileWriter`                | 直接对接文本文件，使用系统默认编码进行字符读写。                             |
|                           | `StringReader`<br>`StringWriter`            | 对接内存中的字符串，将字符串当作字符流来处理。                               |
|                           | `CharArrayReader`<br>`CharArrayWriter`      | 对接内存中的字符数组，在内存中读写字符数据。                                 |
| 增强功能<br>(处理/装饰流) | `BufferedReader`<br>`BufferedWriter`        | 提供缓冲区。`BufferedReader` 提供了极其常用的 `readLine()` 按行读取功能。    |
|                           | `PrintWriter`                               | 提供方便的格式化打印输出字符（如 `println()`），常用于日志或控制台输出。     |
| 桥梁转换<br>(特殊处理流)  | `InputStreamReader`<br>`OutputStreamWriter` | 字节流与字符流的桥梁。在读写时显式指定字符集（如 UTF-8），解决文本乱码问题。 |

选择原则：

- 字节流：只要不是纯文本（比如复制图片、上传视频、处理 PDF、网络传输二进制报文），无脑选字节流 (InputStream / OutputStream)。
- 字符流：只要是处理文本（比如读写配置文件、解析 JSON、读取日志、操作源代码），优先选字符流 (Reader / Writer)。

类的层级关系：

```txt
InputStream (抽象类 - 字节输入流)
      FileInputStream (节点流：从文件读取字节)
      ByteArrayInputStream (节点流：从字节数组读取)
      PipedInputStream (节点流：管道输入)
      FilterInputStream (装饰流基类)
            BufferedInputStream (缓冲功能：提升读取效率)
            DataInputStream (数据功能：读取 int, double 等基本类型)
            ObjectInputStream (对象功能：反序列化)

OutputStream (抽象类 - 字节输出流)
      FileOutputStream (节点流：向文件写入字节)
      ByteArrayOutputStream (节点流：向字节数组写入)
      PipedOutputStream (节点流：管道输出)
      FilterOutputStream (装饰流基类)
            BufferedOutputStream (缓冲功能：提升写入效率)
            DataOutputStream (数据功能：写入基本类型)
            ObjectOutputStream (对象功能：序列化)
            PrintStream (打印功能：如 System.out，方便打印各种数据)

Reader (抽象类 - 字符输入流)
      InputStreamReader (转换流：字节流转字符流，指定编码解码)
            FileReader (便捷类：继承自 InputStreamReader，但使用系统默认编码)
      BufferedReader (装饰流：提供缓冲区和 readLine() 按行读取)
      StringReader (节点流：从字符串读取字符)
      CharArrayReader (节点流：从字符数组读取)

Writer (抽象类 - 字符输出流)
      OutputStreamWriter (转换流：字符流转字节流，指定编码编码)
            FileWriter (便捷类：继承自 OutputStreamWriter，但使用系统默认编码)
      BufferedWriter (装饰流：提供缓冲区)
      PrintWriter (打印流：提供 println, printf 等格式化输出)
      StringWriter (节点流：向字符串写入字符)
      CharArrayWriter (节点流：向字符数组写入)
```

## 综合示例

```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOStreamExample {
    public static void main(String[] args) {
        // 场景：将一段包含中文的文本，以 UTF-8 编码写入文件，再读取出来
        String filePath = "example.txt";
        String textToWrite = "Hello, Java IO!\n你好，字节流与字符流。";

        // 1. 写入文件（字符流 -> 编码转换 -> 字节流 -> 文件）
        // 分层逻辑：BufferedWriter(缓冲) -> OutputStreamWriter(编码转换) -> FileOutputStream(基础字节流)
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            bw.write(textToWrite);
            System.out.println("写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 读取文件（文件 -> 基础字节流 -> 编码转换 -> 字符流 -> 缓冲）
        // 分层逻辑：BufferedReader(缓冲+按行读取) -> InputStreamReader(编码转换) -> FileInputStream(基础字节流)
        // 1. FileInputStream: 负责从磁盘读取原始字节 (节点流)
        // 2. InputStreamReader: 负责将读取的字节按 UTF-8 解码成字符 (转换桥梁)
        // 3. BufferedReader: 负责提供缓冲，并按行高效读取文本 (功能增强)
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            System.out.println("读取内容：");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
