# 路径 Path 和文件 Files

`Path` 负责文件路径，`Files` 负责文件操作

## 路径 Path

- 绝对路径：从盘符或根目录开始的完整路径（如 `D:/data/test.txt` 或 `/usr/local/bin`）。
- 相对路径：相对于当前项目工作目录的路径（如 `config/app.yml` 或 `../temp/data.log`）。
- 路径分隔符：Windows 使用反斜杠 `\`，Linux/macOS 使用正斜杠`/`。可以使用 `File.separator` 动态获取当前系统的分隔符。

`java.nio.file.Path` 接口，它是文件路径的抽象表示，描述路径的结构，不直接操作文件系统（文件操作由 `java.nio.file.Files` 接口定义）。Path 对象是不可变的，线程安全。

Path 接口提供了大量用于解析、转换和操作路径的方法。以下是日常开发中最常用的方法归纳：

| 分类           | 方法名                              | 功能说明                                                           |
| :------------- | :---------------------------------- | :----------------------------------------------------------------- |
| 基础信息       | `getFileName()`                     | 返回路径最末端的文件或目录名称（不包含上级路径）。                 |
|                | `getParent()`                       | 返回当前路径的父级路径；如果已经是根路径则返回 `null`。            |
|                | `getRoot()`                         | 返回路径的根组件（如 Windows 的 `C:\` 或 Linux 的 `/`）。          |
|                | `getNameCount()`                    | 返回路径中名称元素的数量（不包含根目录）。                         |
|                | `getName(int index)`                | 根据索引（0 到 `getNameCount()-1`）返回路径中指定位置的名称元素。  |
| 路径转换       | `toAbsolutePath()`                  | 将当前路径转换为绝对路径并返回一个新的 `Path` 对象。               |
|                | `normalize()`                       | 安全核心：返回一个消除了冗余元素（如 `.` 和 `..`）的规范化路径。   |
|                | `toFile()`                          | 将 `Path` 对象转换回传统的 `java.io.File` 对象（用于兼容旧 API）。 |
| 路径拼接与计算 | `resolve(Path/String other)`        | 路径拼接：在当前路径下拼接一个新的路径，返回拼接后的完整路径。     |
|                | `resolveSibling(Path/String other)` | 同级替换：将当前路径的末端文件名替换为指定的路径，返回新路径。     |
|                | `relativize(Path other)`            | 计算相对路径：构造从当前路径到目标路径的相对导航路径。             |
|                | `subpath(int begin, int end)`       | 返回路径中指定索引范围内的子序列（不包含根目录）。                 |
| 路径判断       | `isAbsolute()`                      | 判断当前路径是否为绝对路径。                                       |
|                | `startsWith(Path/String other)`     | 判断当前路径是否以指定的路径或字符串开头。                         |
|                | `endsWith(Path/String other)`       | 判断当前路径是否以指定的路径或字符串结尾。                         |

```java
import java.nio.file.Path;

public class PathDemo {
    public static void main(String[] args) {
        Path path = Path.of("src", "main", "java", "Demo.java");
        System.out.println("\n=== Path 接口路径信息 ===");
        System.out.println("原始路径: " + path);
        // 输出: src/main/java/Demo.java
        System.out.println("文件名: " + path.getFileName());
        // 输出: Demo.java
        System.out.println("父路径: " + path.getParent());
        // 输出: src/main/java

        // 路径拼接 (resolve)
        Path basePath = Path.of("/home/user");
        Path resolvedPath = basePath.resolve("downloads/music/song.mp3");
        System.out.println("拼接后的绝对路径: " + resolvedPath.toAbsolutePath());
        // 输出: /home/user/downloads/music/song.mp3

        // 路径规范化 (normalize) - 去除冗余的 . 和 ..
        Path messyPath = Path.of("/home/user/./docs/../downloads/");
        System.out.println("规范化后的路径: " + messyPath.normalize());
        // 输出: /home/user/downloads
    }
}
```

> java.nio.file.Paths 类本身非常简单，它目前仅包一个用于创建 Path 对象的静态工厂方法 static Path get(String first, String... more)
>
> 在 Java 11 及以上版本中，推荐使用 Path.of() 来替代 Paths.get()，两者的功能完全一致，但前者语义更直观。

延伸内容，在 nodejs 中有提供全局变量 `__filename__`，用于获取当前文件名，`__dirname__` 用于获取当前文件所在目录，以及 `process.cwd()` 用于获取当前工作目录。

在 Java 中，没有提供类似的便捷方法，但可以使用多个api获取到对应路径。

```java
import java.nio.file.Path;

// 获取当前类对应的 .class 文件的绝对路径（类似 __filename）
Path currentFilePath = Path.of(YourClassName.class.getProtectionDomain().getCodeSource().getLocation().toURI());
System.out.println("当前文件路径: " + currentFilePath);

// 获取当前文件所在目录的绝对路径（类似 __dirname）
Path currentDirPath = currentFilePath.getParent();
System.out.println("当前目录路径: " + currentDirPath);

// 获取当前工作目录的绝对路径（类似 process.cwd()）
Path cwd_1 = Path.of("").toAbsolutePath();
// 或者 System.getProperty("user.dir");
Path cwd_2 = Path.of(System.getProperty("user.dir"));
System.out.printf("当前工作目录路径: Path.of("").toAbsolutePath() -> %s, Path.of(System.getProperty("user.dir")) -> %s", cwd_1, cwd_2);
```

## 文件 Files

`java.nio.file.Files` 类提供了大量用于操作文件系统的方法，如创建、删除、移动、复制、读取和写入文件，以及检查文件属性。以下是日常开发中最常用的方法归纳：

| 操作分类  | 核心方法                                      | 功能描述                                                     |
| :-------- | :-------------------------------------------- | :----------------------------------------------------------- |
| 创建目录  | `Files.createDirectory(Path)`                 | 创建单级目录（父目录不存在会抛异常）                         |
|           | `Files.createDirectories(Path)`               | 创建多级目录（自动创建不存在的父目录，推荐）                 |
| 创建文件  | `Files.createFile(Path)`                      | 创建一个新的空文件                                           |
| 读取/查询 | `Files.exists(Path)`                          | 判断文件或目录是否存在                                       |
|           | `Files.isRegularFile(Path)`                   | 判断是否为普通文件                                           |
|           | `Files.isDirectory(Path)`                     | 判断是否为目录                                               |
|           | `Files.isSymbolicLink(Path)`                  | 判断是否为符号链接                                           |
|           | `Files.isHidden(Path)`                        | 判断文件是否为隐藏文件                                       |
|           | `Files.isReadable`                            | 判断文件是否可读                                             |
|           | `Files.isWritable`                            | 判断文件是否可写                                             |
|           | `Files.isExecutable`                          | 判断文件是否可执行                                           |
|           | `Files.readAllBytes(Path)`                    | 读取文件的所有字节（适合小文件）                             |
|           | `Files.readAllLines(Path)`                    | 读取文件的所有行，返回 `List<String>`                        |
|           | `Files.readString(path)`                      | 读取文件全部内容为字符串 (Java 11+) 极简读取小文件           |
|           | `Files.size(Path)`                            | 获取文件的大小（字节数）                                     |
|           | `Files.list(Path)`                            | 遍历当前目录下的直接子文件或子目录（非递归）                 |
|           | `Files.walk(Path)`                            | 递归遍历目录树，返回 `Stream<Path>`                          |
| 写入/修改 | `Files.write(Path, byte[])`                   | 将字节数组写入文件（可配合 StandardOpenOption 控制写入方式） |
|           | `Files.writeString(path, str)`                | 将字符串写入文件 (Java 11+) 极简写入小文件                   |
|           | `Files.copy(src, dest, opts)`                 | 复制文件（可通过 StandardCopyOption.REPLACE_EXISTING 覆盖）  |
|           | `Files.move(src, dest, opts)`                 | 移动或重命名文件（支持原子移动）                             |
| 删除      | `Files.delete(Path)`                          | 删除文件或目录（目录非空或文件不存在时会抛异常）             |
|           | `Files.deleteIfExists(Path)`                  | 如果文件存在则删除，不存在时不抛异常（更安全）               |
| 流相关    | `Files.newInputStream(path)`                  |                                                              |
|           | `Files.newOutputStream(path`                  |                                                              |
|           | `Files.newBufferedReader(path, charset)`      |                                                              |
|           | `Files.newBufferedWriter(path, charset)`      |                                                              |
| 文件属性  | `java.nio.file.attribute.BasicFileAttributes` | `Files.readAttributes(path, BasicFileAttributes.class)`      |
|           | `FileTime createTime()`                       | 文件创建时间                                                 |
|           | `FileTime lastAccessTime()`                   | 文件最近访问时间                                             |
|           | `FileTime lastModifiedTime()`                 | 文件最近修改时间                                             |

> 在进行目录遍历时，Files.list() 和 Files.walk() 返回的是 Stream，在使用完毕后底层的目录句柄会自动关闭（推荐使用 try-with-resources 语法）。

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class NioFileDemo {
    public static void main(String[] args) throws IOException {
        // 1. 准备路径对象
        Path dirPath = Paths.get("nio_test_dir");
        Path filePath = Paths.get("nio_test_dir", "example.txt");
        Path targetPath = Paths.get("nio_test_dir", "example_copy.txt");

        // --- 增 (Create) ---
        // 创建多级目录（如果父目录不存在会自动创建，不会抛异常）
        Files.createDirectories(dirPath);
        // 创建新文件
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        // 向文件中写入内容（CREATE表示不存在则创建，APPEND表示追加写入）
        String content = "Hello, Java NIO!";
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("文件创建并写入成功！");

        // --- 查 (Read) ---
        // 检查文件或目录是否存在、是否为目录
        System.out.println("文件是否存在: " + Files.exists(filePath));
        System.out.println("是否是目录: " + Files.isDirectory(dirPath));

        // 读取文件的所有字节或所有行
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        System.out.println("读取到的文件内容: " + lines);

        // 获取文件属性（如大小、最后修改时间）
        System.out.println("文件大小: " + Files.size(filePath) + " 字节");

        // --- 改 (Update) ---
        // 修改（覆盖）文件内容
        Files.write(filePath, "Updated Content".getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.TRUNCATE_EXISTING);

        // 移动或重命名文件（ATOMIC_MOVE 保证移动的原子性）
        Files.move(filePath, targetPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("文件已重命名并移动");

        // --- 删 (Delete) ---
        // 复制文件（演示删除前的准备，REPLACE_EXISTING表示覆盖已存在的文件）
        Files.copy(targetPath, filePath, StandardCopyOption.REPLACE_EXISTING);

        // 删除文件（如果文件不存在会抛出 NoSuchFileException）
        Files.delete(filePath);
        // 安全删除：如果文件存在则删除，不存在也不抛异常
        Files.deleteIfExists(targetPath);

        // 删除目录（注意：使用 delete 删除目录时，目录必须为空）
        Files.delete(dirPath);
        System.out.println("文件与目录清理完毕！");
    }
}
```

## StandardOpenOption

在 Java NIO 中，StandardOpenOption 是一个枚举类，它定义了打开文件时的各种标准选项。你可以把它理解为“打开文件的操作指令”，用来精确控制 Files 或 FileChannel 等类在读写文件时的具体行为（比如是覆盖还是追加、文件不存在时是否自动创建等）。

| 选项名称          | 核心作用                           | 适用场景                                 |
| :---------------- | :--------------------------------- | :--------------------------------------- |
| READ              | 以只读方式打开文件                 | 读取文件内容，不能写入                   |
| WRITE             | 以写入方式打开文件                 | 需要修改或写入文件内容                   |
| APPEND            | 以追加方式写入                     | 在文件末尾添加新内容，不覆盖原有数据     |
| CREATE            | 文件不存在则创建，存在则打开       | 通用的写入或追加操作（防报错）           |
| CREATE_NEW        | 必须创建新文件，存在则报错         | 防止意外覆盖已有文件，确保数据唯一性     |
| TRUNCATE_EXISTING | 若文件存在，清空内容（长度截为0）  | 覆盖写入，每次写入都从文件开头开始       |
| DELETE_ON_CLOSE   | 流或通道关闭时，自动删除文件       | 处理临时文件，防止磁盘垃圾堆积           |
| DSYNC             | 强制同步文件内容到物理存储设备     | 对数据安全性要求极高（如金融日志）       |
| SYNC              | 强制同步文件内容与元数据到物理设备 | 对数据及属性（如修改时间）安全性要求极高 |

在调用 `Files.write()`、`Files.newOutputStream()` 或 `FileChannel.open()` 时，通常会根据业务需求组合这些选项来控制文件的打开方式。

```java
// 安全追加写入
Path path1 = Path.of("log.txt");
// 文件不存在则创建，存在则在末尾追加，不会覆盖原内容
Files.write(path1, "新日志信息".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

// 强制覆盖写入，重置文件
Path path2 = Path.of("config.json");
// 文件不存在则创建，存在则先清空内容，再从头写入
Files.write(path2, "{\"setting\": 1}".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

// 防止误覆盖（强制新建）
Path path3 = Path.of("report.txt");
try {
    // 必须是一个全新的文件，如果 report.txt 已存在会直接抛出 FileAlreadyExistsException
    Files.write(path3, "重要报告内容".getBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
} catch (FileAlreadyExistsException e) {
    System.out.println("文件已存在，请更换文件名！");
}


// 处理临时文件（用完即焚）
Path tempPath = Path.of("temp_data.tmp");
// 打开文件进行写入，当 try-with-resources 执行完毕关闭流时，该文件会被自动删除
try (BufferedWriter writer = Files.newBufferedWriter(tempPath, StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE)) {
    writer.write("这是一些临时数据");
} // 离开这个作用域后，temp_data.tmp 会自动从磁盘消失


// 高安全级别的日志写入
Path path4 = Path.of("transaction.log");
// 每次写入都会强制刷新到物理硬盘，即使系统突然断电，这条日志也不会丢失
Files.write(path4, "转账成功".getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.DSYNC);
```

如果不指定任何 StandardOpenOption，`Files.newInputStream` 默认是 `READ`，而 `Files.newOutputStream` 默认是 `CREATE + TRUNCATE_EXISTING`（即默认覆盖）。
