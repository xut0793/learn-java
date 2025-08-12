# Java 开发环境

## 什么是 JDK

JDK（Java Development Kit）是 Java 开发工具包，它是开发 Java 程序的基础环境。

JDK 是整个 Java 的核心，包括了 Java 运行环境（Java Runtime Environment）​、一堆 Java 工具（如 javac、java、javadoc 等）和 Java 基础类库（rt.jar）​。无论什么 Java 应用服务器实质上都内置了某个版本的 JDK，因此了解和掌握 JDK 是学好 Java 的第一步。

当前主流的 JDK 是原 Sun 公司提供的 JDK，但是除了原 Sun 公司之外，还有很多公司和组织都开发了自己的 JDK，其中包括 BEA 公司开发的 Jrocket、IBM 公司开发的 JDK 及 GNU 组织开发的 JDK 等。虽然 IBM 的 JDK 和专门运行在 x86 平台的 Jrocket 包含的 JVM 效率高于 SunJDK，但由于原 Sun 公司开发的 JDK 是基础，所以建议大家先把 Sun JDK 掌握好。

## 什么是 JRE

JRE（Java Runtime Environment）是 Java 运行时环境，它是运行 Java 程序所必需的环境。JRE 包含了 Java 虚拟机（JVM）和 Java 核心类库。

## 安装和环境配置

1. **访问官网**：打开 [Oracle 官方 JDK 下载页面](https://www.oracle.com/java/technologies/javase-downloads.html) 或 [OpenJDK 下载页面](https://jdk.java.net/)。如果是商业用途，建议从 Oracle 官网下载并遵循其许可协议；如果是开源项目，OpenJDK 是不错的选择。
2. **选择版本**：根据自己的需求选择合适的 JDK 版本，目前常用的有 JDK 8、JDK 11 和 JDK 17 等长期支持（LTS）版本。
3. **下载安装包安装**：根据自己的操作系统（Windows、Mac、Linux 等）选择对应的安装包（如 `.exe`、`.dmg` 或 `.tar.gz` 文件），比如 Windows 系统双击下载的 `.exe` 安装包，按照安装向导提示进行操作。
4. **配置环境变量**：在系统环境变量中添加 JAVA_HOME 变量，指向 JDK 的安装目录，并将 `%JAVA_HOME%\bin` 添加到 `Path` 变量中。
5. **验证安装**：打开命令行工具（如 Windows 下的 cmd 或 PowerShell，Mac 下的终端），输入 `java -version` 和 `javac -version` 命令，若能显示版本信息，则说明安装成功。
