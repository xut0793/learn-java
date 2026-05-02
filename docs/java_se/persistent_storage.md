# 数据持久化存储

“**程序=数据结构+算法**”，由瑞士计算机科学家 尼克劳斯·沃斯（Niklaus Wirth）在其 1976 年出版的经典著作《算法 + 数据结构 = 程序》中提出。

在进行日常软件开发的编码工作中，应该要形成本能地思考：“这块功能要处理什么数据？如何组织这些数据？需要哪些操作？哪种算法最有效？”。 引导自己从 算法 和 数据结构 两个维度切入进行软件设计。

作为最关键的数据以及数据持久化的问题，从计算机科学的演变历程来看，数据存储的本质就是如何将**内存中的易失性数据（0和1）映射到磁盘上的非易失性介质中**。

这个过程经历了从“人读为主为文本类型文件”到“机读为主二进制类型文件”，数据组织从简单 → 结构化 → 高效 → 分布式”进行演变。

在 Java 中，我们处理这些数据的方式也随着格式的演进而变得日益丰富和抽象。按上述演变历程进行梳理和总结。

## 文本数据 Text Data

演变逻辑：从纯字符到具有语义的结构化描述。文本文件的核心优势是通用性和可读性，但往往牺牲了存储空间和解析性能。

Java 处理这类文件核心是字符流（Reader/Writer）。

- 写入： 使用 FileWriter 或 OutputStreamWriter（可指定 UTF-8 等编码，防止跨平台乱码），配合其它功能的操作类，比如 BufferedWriter 提高写入效率，使用 `newLine()` 处理换行。
- 读取： 使用 FileReader 或 InputStreamReader，配合其它功能的操作类 BufferedReader 的 `readLine()` 方法逐行读取。

注意： 必须使用 try-with-resources 语法确保流资源被正确关闭。

### 简单文本文件

纯文本文件，比如 txt、log 这类文件，仅存储纯字符串，无任何结构，适合极简数据。需要注意的是文件编码格式（如 utf-8），写入编码和读取编码不一致会出现乱码。

```java
import java.io.*;

public class SimpleTextFileDemo {
    public static void main(String[] args) {
        String fileName = "demo.txt";

        // 写入简单文本
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            writer.write("你好，Java IO！");
            writer.newLine(); // 跨平台换行
            writer.write("这是第二行内容。");
            System.out.println("文本写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取简单文本
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("读取到: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 填充式文本文件

在这种格式中，记录中的每个字段都有固定的宽度，如果内容宽度不够，通常会使用空格字符进行填充，使得每行（记录）保持一样的宽度。此时数据已经有了简单结构，但因为填充的冗余的空格字符，导致数据存储空间和读取性能都比纯文本文件要高，但好处是数据操作方便。

在 Java 中可以通过 String.substring(start, end) 截取固定长度的字段。或者使用 RandomAccessFile 类的 `seek(long pos)` 直接跳转到指定位置读取。

下面是包含两个场景的综合示例：

- 场景一（按行解析）：模拟读取银行交易流水，每行数据长度固定，使用 String.substring() 截取字段。
- 场景二（随机定位）：模拟读取员工档案，每条记录长度完全一致，使用 RandomAccessFile 的 seek() 方法直接跳转到指定记录读取。

```java
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class FixedWidthFileExample {

    // 模拟的填充式文本内容（每行固定 30 个字符，不足用空格补齐）
    // 格式：[交易日期(8位)][交易码(6位)][金额(10位)][备注(6位)]
    private static final String FIXED_LINE_CONTENT =
            "20250301TRADE000005000      成功  \n" +
            "20250302WITHDR000012000      失败  \n" +
            "20250303DEPOSI000008500      成功  ";

    // 模拟的定长记录内容（每条记录固定 12 字节：姓名8字节 + 年龄4字节）
    // 姓名不足8位用空格补齐，年龄使用 int (4字节)
    private static final String RECORD_FILE = "employees.dat";

    public static void main(String[] args) {
        // 1. 演示按行读取并使用 substring 截取
        parseByLine();

        // 2. 演示使用 RandomAccessFile 随机定位读取
        randomAccessRecord();
    }

    /**
     * 场景一：按行读取，使用 String.substring 截取固定长度字段
     */
    public static void parseByLine() {
        System.out.println("---------- 场景一：按行 substring 截取 ----------");
        // 模拟按行读取（实际开发中可用 BufferedReader）
        String[] lines = FIXED_LINE_CONTENT.split("\n");

        for (String line : lines) {
            // 确保行长度足够，防止 StringIndexOutOfBoundsException
            if (line.length() >= 30) {
                // 根据预设的偏移量和长度截取字段（注意 substring 左闭右开）
                String date = line.substring(0, 8).trim();
                String code = line.substring(8, 14).trim();
                String amount = line.substring(14, 24).trim();
                String remark = line.substring(24, 30).trim();

                System.out.printf("日期: %s, 交易码: %s, 金额: %s, 备注: %s%n",
                        date, code, amount, remark);
            }
        }
    }

    /**
     * 场景二：使用 RandomAccessFile 直接跳转到指定位置读取定长记录
     */
    public static void randomAccessRecord() {
        System.out.println("\n---------- 场景二：RandomAccessFile 随机定位 ----------");

        // 先准备测试文件
        prepareRecordFile();

        // 每条记录的长度：姓名(8字节) + 年龄(4字节) = 12字节
        final int RECORD_LENGTH = 12;

        try (RandomAccessFile raf = new RandomAccessFile(RECORD_FILE, "r")) {
            // 需求：直接读取第 2 条记录（索引为 1，即 "lisi" 的数据）
            int targetIndex = 1;
            // 计算跳转的字节位置
            long seekPos = targetIndex * RECORD_LENGTH;
            raf.seek(seekPos); // 将文件指针直接移动到目标位置

            // 读取 8 字节的姓名
            byte[] nameBytes = new byte[8];
            raf.read(nameBytes);
            String name = new String(nameBytes, StandardCharsets.UTF_8).trim();

            // 读取 4 字节的年龄 (int)
            int age = raf.readInt();

            System.out.printf("成功跳转到第 %d 条记录 -> 姓名: %s, 年龄: %d%n",
                    targetIndex + 1, name, age);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 辅助方法：生成包含定长二进制记录的文件
    private static void prepareRecordFile() {
        try (RandomAccessFile raf = new RandomAccessFile(RECORD_FILE, "rw")) {
            // 写入第一条记录：zhangsan (8字节) + 30 (4字节)
            raf.write("zhangsan".getBytes(StandardCharsets.UTF_8));
            raf.writeInt(30);

            // 写入第二条记录：lisi (4字节) + 4个空格补齐(4字节) + 20 (4字节)
            raf.write("lisi    ".getBytes(StandardCharsets.UTF_8));
            raf.writeInt(20);

            // 写入第三条记录：wangwu (6字节) + 2个空格补齐(2字节) + 16 (4字节)
            raf.write("wangwu  ".getBytes(StandardCharsets.UTF_8));
            raf.writeInt(16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 结构化文本文件

对于简单或者填充式的文本文件，其唯一的组织层次就是行。有时候，你希望有更多的结构，表达数据的意义。常见的格式约束有：

- 分隔符（separator）或界定符（delimiter）​，比如制表符（'\t'）​、逗号（','）或竖线（'|'）​。典型文件格式就是逗号分隔值的CSV文件（comma-separated value）就是这种格式。
- 标签周围的'<'和'>'，比如XML和HTML。
- 标点符号，比如JSON。
- 缩进，比如YAML（​“YAML Ain't Markup Language”的递归缩写）​。
- 杂项，比如程序配置文件 .ini 文件。

#### CSV

CSV 格式是一种最常用的数据格式，它将数据存储为行和列，行和列之间用逗号分隔。也称为表格文件格式，excel处理软件和数据库可以直接导入导出 CSV 文件。

Java 中可使用 OpenCSV 库，或直接用 `String.split(",")` 进行简单解析。

```java
// 需引入依赖: com.opencsv:opencsv
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;

public class CsvDemo {
    public static void main(String[] args) {
        // 写入 CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter("users.csv"))) {
            String[] header = {"ID", "Name", "Role"};
            String[] data = {"1", "Alice", "Engineer"};
            writer.writeNext(header);
            writer.writeNext(data);
        } catch (IOException e) { e.printStackTrace(); }

        // 读取 CSV
        try (CSVReader reader = new CSVReader(new FileReader("users.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                System.out.println("CSV行数据: " + String.join(", ", nextLine));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
```

#### xml

XML (Extensive Markup Language 可扩展标记语言) 是早期互联网数据交换标准，结构严谨但冗余，常用于旧系统兼容或网页解析。

Java 提供了 DOM/SAX 原生解析器，或使用更便捷的 Jsoup 库（常用于爬虫和 HTML 清洗）。

XML 文件的格式如下：

```xml
<users>
  <user>
    <name>张三</name>
    <age>25</age>
    <city>北京</city>
  </user>
</users>
```

Java 中，处理 XML 最原生且通用的方式是使用 JAXP (Java API for XML Processing) 提供的 DOM 解析器。

```java
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlExample {
    public static void main(String[] args) {
        try {
            // --- 1. 写入 XML ---
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // 创建根元素
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);

            // 创建子元素
            Element user = doc.createElement("user");
            user.setAttribute("id", "1"); // 设置属性
            rootElement.appendChild(user);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode("张三"));
            user.appendChild(name);

            // 将 DOM 对象写入文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 格式化输出
            transformer.transform(new DOMSource(doc), new StreamResult(new File("users.xml")));
            System.out.println("XML 写入成功！");

            // --- 2. 读取 XML ---
            Document docRead = docBuilder.parse("users.xml");
            docRead.getDocumentElement().normalize();

            NodeList nList = docRead.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("读取到用户 ID: " + element.getAttribute("id"));
                    System.out.println("读取到用户名: " + element.getElementsByTagName("name").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### json

JSON (JavaScript Object Notation) 是一种数据格式规范，它基于 JavaScript 语言，但与 JavaScript 不同的是，JSON 是一种数据格式，而不是一种编程语言。

Java 中主流使用 Jackson 或 Gson 库，将 JSON 字符串直接映射为 Java 对象（反序列化）或反之。

JSON 文件的格式如下：

```json
{
  "name": "张三",
  "age": 25,
  "city": "北京"
}
```

```java
// 需引入依赖: com.fasterxml.jackson.core:jackson-databind
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

class User {
    public String name;
    public int age;
    // 省略构造器
}

public class JsonDemo {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("Bob", 25);

        // 写入 JSON
        try {
            mapper.writeValue(new File("user.json"), user);
        } catch (IOException e) { e.printStackTrace(); }

        // 读取 JSON
        try {
            User readUser = mapper.readValue(new File("user.json"), User.class);
            System.out.println("JSON解析对象: " + readUser.name);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
```

#### yaml

YAML（YAML Ain’t a Markup Language）是一种可读性极高的数据序列化格式，常用于配置文件和数据交换。它通过缩进表示层级关系，支持对象、数组和纯量三种数据结构，语法简洁且易于手工编辑，常用于配置文件。

Java 中通常使用 SnakeYAML 库进行解析。

YAML 文件的格式如下：

```yaml
server:
  host: 127.0.0.1
  port: 8080
users:
  - name: Tom
    role: admin
  - name: Jerry
    role: user
features:
  logging: true
  cache_size: 256
```

```java
// 需引入依赖: org.yaml:snakeyaml
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.Map;

public class YamlDemo {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();

        // 写入 YAML
        try (FileWriter writer = new FileWriter("config.yaml")) {
            Map<String, Object> data = Map.of("app", "MyApp", "port", 8080);
            yaml.dump(data, writer);
        } catch (IOException e) { e.printStackTrace(); }

        // 读取 YAML
        try (FileInputStream fis = new FileInputStream("config.yaml")) {
            Map<String, Object> loadedData = yaml.load(fis);
            System.out.println("YAML配置: " + loadedData);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
```

#### toml

TOML（Tom’s Obvious, Minimal Language）是一种简洁、可读性强的配置文件格式，由 GitHub 前 CEO Tom Preston-Werner 于 2013 年创建，设计目标是语义清晰且能无歧义映射为哈希表结构。它常被用于替代 JSON、YAML 和 INI，广泛应用于 Rust Cargo、Python Poetry、Hugo 等项目。

Toml 文件的格式如下：

```toml
# 应用配置
[app]
name = "我的应用"
version = "1.0.0"
debug = false
# 数据库配置
[database]
url = "postgresql://user:pass@localhost:5432/db"
pool_size = 10
# 嵌套表格
[servers.alpha]
ip = "10.0.0.1"
dc = "east"
# 表格数组
[[products]]
name = "Hammer"
price = 29.99
[[products]]
name = "Nail"
price = 2.99"
```

```java
// 需引入依赖: com.moandjiezana.toml:toml4j
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TomlExample {
    public static void main(String[] args) {
        try {
            // --- 1. 写入 TOML ---
            Map<String, Object> data = new HashMap<>();
            data.put("title", "My Application");

            Map<String, Object> database = new HashMap<>();
            database.put("server", "192.168.1.1");
            database.put("ports", new int[]{8001, 8002, 8003});
            data.put("database", database);

            TomlWriter writer = new TomlWriter();
            writer.write(data, new File("config.toml"));
            System.out.println("TOML 写入成功！");

            // --- 2. 读取 TOML ---
            Toml toml = new Toml().read(new File("config.toml"));
            String title = toml.getString("title");
            String dbServer = toml.getString("database.server");
            Long[] dbPorts = toml.getArray("database.ports", Long.class);

            System.out.println("应用标题: " + title);
            System.out.println("数据库服务器: " + dbServer);
            System.out.print("数据库端口: ");
            for (Long port : dbPorts) System.out.print(port + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## 二进制数据 Binary Data

演变逻辑：追求极致的性能、空间效率以及复杂对象的还原。二进制文件对人类不可读（乱码），但对机器极其高效。

### 简单的二进制文件

直接存储原始的字节数据，速度最快，适合图片、音频、原始数据。

Java 处理的核心是字节流（InputStream/OutputStream）。使用 FileInputStream 和 FileOutputStream 进行原始字节（byte[]）的读写。

```python
# 写入二进制
data = b"hello binary"  # bytes 类型
with open("simple.bin", "wb") as f:
    f.write(data)

# 读取二进制
with open("simple.bin", "rb") as f:
    data = f.read()
print(data)
```

### 填充式二进制文件

二进制版固定格式，用字节填充字段，比文本更快更小。按照 C 语言的结构体方式，将整数、浮点数紧密排列，没有分隔符。

Java 使用 RandomAccessFile 或 Java NIO 的 FileChannel 与 ByteBuffer。通过 ByteBuffer 的 putInt(), putLong() 等方法按固定字节长度写入，读取时再通过对应的 get 方法还原。

```java

```

### 结构化二进制文件

复杂的结构化数据，比较 Java 语言中的数据结构字段、对象等存储。通常涉及到数据序列化和反序列化：

- 序列化（Serialization）是将数据结构或对象状态转换为一个可以存储或传输的格式的过程。这意味着我们可以将复杂的数据结构转换为简单的字节流或字符串，以便于存储或传输。
- 反序列化（Deserialization）则是将这些数据恢复为其原始形式的过程。

Java 的类必须实现 Serializable 标记接口，然后使用 ObjectOutputStream 的 writeObject() 方法将对象转为字节流写入文件；使用 ObjectInputStream 的 readObject() 方法将字节流重构为 Java 对象。

```java
import java.io.*;

// 必须实现 Serializable 接口才能被序列化
class Person implements Serializable {
    private static final long serialVersionUID = 1L; // 建议显式声明版本号，保证兼容性
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

public class JavaSerializationExample {
    public static void main(String[] args) {
        String filePath = "person.dat";

        // --- 1. 使用 ObjectOutputStream 写入对象 (序列化) ---
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            Person person = new Person("李四", 25);
            oos.writeObject(person); // 将对象转为二进制写入文件
            System.out.println("对象序列化写入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- 2. 使用 ObjectInputStream 读取对象 (反序列化) ---
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            // 读取出的对象默认是 Object 类型，需要强制类型转换
            Person restoredPerson = (Person) ois.readObject();
            System.out.println("对象反序列化读取成功：" + restoredPerson);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

其它结构化二进制数据，比如 压缩文件（.zip）、 excel 文件（.xlsx）、word 文件（.docx）、pdf 文件（.pdf）等等，这类文件通常使用第三模块来处理。

- 压缩文件：内置标准工具库 zip
- excel 文件：POI
- word 文件：Apache POI
- pdf 文件：Apache PDFBox
- 图片文件：ImageIO
- 音频文件：FFmpeg
- 视频文件：FFmpeg

```java
import java.io.*;
import java.util.zip.*;

public class ZipExample {
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        // --- 1. 压缩文件 (生成 ZIP) ---
        try (FileOutputStream fos = new FileOutputStream("archive.zip");
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream("config.toml")) { // 假设压缩前面生成的 toml 文件

            // 在 ZIP 中创建一个条目（Entry）
            ZipEntry zipEntry = new ZipEntry("config.toml");
            zos.putNextEntry(zipEntry);

            // 将文件内容写入 ZIP 条目
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
            System.out.println("ZIP 压缩成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // --- 2. 解压缩文件 (读取 ZIP) ---
        try (FileInputStream fis = new FileInputStream("archive.zip");
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry zipEntry;
            // 遍历 ZIP 包中的所有条目
            while ((zipEntry = zis.getNextEntry()) != null) {
                System.out.println("正在解压文件: " + zipEntry.getName());
                File newFile = new File("unzipped_" + zipEntry.getName());

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] bytes = new byte[BUFFER_SIZE];
                    int length;
                    while ((length = zis.read(bytes)) >= 0) {
                        fos.write(bytes, 0, length);
                    }
                }
                zis.closeEntry();
            }
            System.out.println("ZIP 解压成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

ZIP 文件在二进制层面有固定的文件头标识，即前 4 个字节为 50 4B 03 04（对应的 ASCII 字符是 PK）。但是如果以此解析判断一个文件是 zip 文件会掉入“误判”的陷阱。很多基于 ZIP 格式封装的文件（例如 Office 的 .xlsx、.docx 文档，以及 .jar 包）本质上也是 ZIP 格式，它们同样以 PK 开头。如果你需要严格区分“普通的 ZIP 压缩包”和“其他 ZIP 衍生格式”，单靠魔术字是不够的。稳健的方法。直接利用 Java 标准库提供的 ZipFile 类尝试去打开并解析该文件。如果文件不是合法的 ZIP 格式或文件已损坏，Java 会抛出 ZipException 异常。

```java
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipValidator {
    public static boolean isValidZipFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        // 尝试用 ZipFile 打开，如果成功则说明是有效的 ZIP 文件
        try (ZipFile zipFile = new ZipFile(file)) {
            return true;
        } catch (ZipException e) {
            // 文件格式错误或损坏
            System.err.println("不是有效的 ZIP 文件: " + e.getMessage());
            return false;
        } catch (IOException e) {
            // 文件读取发生其他 IO 错误
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        File file = new File("test.zip");
        System.out.println("是否为有效ZIP文件: " + isValidZipFile(file));
    }
}
```

Excel 文件命名用 Apache POI 库操作

```java
// 需引入依赖: org.apache.poi:poi-ooxml
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

public class ExcelDemo {
    public static void main(String[] args) {
        String fileName = "report.xlsx";

        // 写入 Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("员工表");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("姓名");
            row.createCell(1).setCellValue("部门");

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                workbook.write(fos);
            }
            System.out.println("Excel 写入成功");
        } catch (IOException e) { e.printStackTrace(); }

        // 读取 Excel
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(fileName))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row firstRow = sheet.getRow(0);
            System.out.println("Excel首行第一列: " + firstRow.getCell(0).getStringCellValue());
        } catch (IOException e) { e.printStackTrace(); }
    }
}
```

PDF 文件 (.pdf) 使用 Apache PDFBox 库来提取或生成 PDF。

```java
// 需引入依赖: org.apache.pdfbox:pdfbox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;

public class PdfDemo {
    public static void main(String[] args) {
        String fileName = "document.pdf";

        // 创建并写入 PDF
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Hello, PDFBox!");
                contentStream.endText();
            }
            document.save(fileName);
            System.out.println("PDF 创建成功");
        } catch (IOException
```

## 数据库系统 Database System

当数据量超越单机文件处理能力，且需要并发访问、事务安全和复杂查询时，数据库成为必然选择。

### 关系型数据库 (RDBMS)

- 代表: SQLite, MySQL, PostgreSQL。
- 特点: 结构化查询语言 (SQL)，强一致性，表与表之间有关联。

Java 底层使用 JDBC (Java Database Connectivity) 标准接口，通过 DriverManager 获取数据库连接，使用 PreparedStatement 执行 SQL 语句。

实际开发中通常使用 ORM（对象关系映射）框架，如 MyBatis 或 Spring Data JPA (Hibernate)，将数据库表直接映射为 Java 实体类，极大简化了 CRUD 操作。

1. 使用原生 JDBC 连接 SQLite 数据库

> 使用 SQLite 时，我们需要先引入SQLite 的 JDBC 驱动。(注：Maven 依赖为 `<dependency><groupId>org.xerial</groupId><artifactId>sqlite-jdbc</artifactId><version>3.36.0.3</version></dependency>`)

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcSqliteExample {
    // SQLite 的 JDBC URL 格式，如果文件不存在会自动在当前目录创建
    private static final String DB_URL = "jdbc:sqlite:sample.db";

    public static void main(String[] args) {
        // 1. 建立连接与创建表
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            System.out.println("成功连接到 SQLite 数据库！");

            // 创建一张简单的用户表
            String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "name TEXT NOT NULL, " +
                                    "email TEXT)";
            stmt.execute(createTableSql);

            // 2. 插入数据（使用 PreparedStatement 防止 SQL 注入）
            String insertSql = "INSERT INTO users(name, email) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, "张三");
                pstmt.setString(2, "zhangsan@example.com");
                pstmt.executeUpdate();
                System.out.println("数据插入成功！");
            }

            // 3. 查询数据
            String selectSql = "SELECT id, name, email FROM users";
            try (ResultSet rs = stmt.executeQuery(selectSql)) {
                System.out.println("查询到的数据：");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", 姓名: " + rs.getString("name") +
                                       ", 邮箱: " + rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### 非关系型数据库 (NoSQL)

- 代表：MongoDB (文档型) 直接存储类似 JSON 的 BSON 文档; Redis (内存存储的键值对/缓存)。
- 特点：无固定表结构，数据结构灵活，适合需要高并发读写的大数据、分布式场景

各大 NoSQL 数据库通常提供专属的 Java 客户端 SDK。

- MongoDB 提供了官方的 Java 同步驱动，通过连接字符串（Connection String）和 MongoClient 即可轻松建立连接。
- Redis 在 Java 中最常用的客户端是 Jedis（同步阻塞）或 Lettuce（异步非阻塞）。

```java
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbExample {
    public static void main(String[] args) {
        // 1. 设置连接字符串 (本地默认端口，如果是远程需加上用户名密码)
        String connectionString = "mongodb://localhost:27017";

        // 2. 创建 MongoClient (官方建议作为单例在整个应用中共享，它是线程安全的)
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {

            // 3. 获取指定的数据库和集合（相当于关系型数据库的“表”）
            MongoDatabase database = mongoClient.getDatabase("myDatabase");
            MongoCollection<Document> collection = database.getCollection("users");

            // 4. 插入一个文档（相当于关系型数据库的“行”，但以 JSON/BSON 格式存储）
            Document userDoc = new Document("name", "王五")
                                    .append("email", "wangwu@example.com")
                                    .append("age", 28);
            collection.insertOne(userDoc);
            System.out.println("MongoDB 文档插入成功！");

            // 5. 查询文档
            Document filter = new Document("name", "王五");
            Document foundDoc = collection.find(filter).first();
            System.out.println("MongoDB 查询到的文档: " + foundDoc.toJson());
        }
    }
}
```

Redis 在 Java 中最常用的客户端是 Jedis（同步阻塞）或 Lettuce（异步非阻塞）。这里以最经典的 Jedis 为例。

```java
import redis.clients.jedis.Jedis;

public class RedisExample {
    public static void main(String[] args) {
        // 1. 建立连接 (默认端口 6379，如果有密码需调用 jedis.auth("password"))
        try (Jedis jedis = new Jedis("localhost", 6379)) {

            System.out.println("成功连接到 Redis 服务器！");

            // 2. 存储键值对 (String 类型)
            jedis.set("user:1001:name", "赵六");
            jedis.set("user:1001:email", "zhaoliu@example.com");
            System.out.println("Redis 键值写入成功！");

            // 3. 获取键值对
            String name = jedis.get("user:1001:name");
            System.out.println("Redis 查询到的 name: " + name);

            // 4. 操作 Hash 结构 (非常适合存储对象属性)
            jedis.hset("user:1002", "name", "孙七");
            jedis.hset("user:1002", "age", "30");
            System.out.println("Redis Hash 结构数据: " + jedis.hgetAll("user:1002"));
        }
    }
}
```
