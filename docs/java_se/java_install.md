# Java 开发环境

## 什么是 JDK

JDK（Java Development Kit）是 Java 开发工具包，它是开发 Java 程序的基础环境。

JDK 是整个 Java 的核心，包括了 Java 运行环境（Java Runtime Environment）​、一堆 Java 工具（如 javac、java、jdb、javap、javadoc 等）​。无论什么 Java 应用服务器实质上都内置了某个版本的 JDK，因此了解和掌握 JDK 是学好 Java 的第一步。

当前主流的 JDK 是原 Sun 公司提供的 JDK，但是除了原 Sun 公司之外，还有很多公司和组织都开发了自己的 JDK，其中包括 BEA 公司开发的 Jrocket、IBM 公司开发的 JDK 及 GNU 组织开发的 JDK 等。虽然 IBM 的 JDK 和专门运行在 x86 平台的 Jrocket 包含的 JVM 效率高于 SunJDK，但由于原 Sun 公司开发的 JDK 是基础，所以建议大家先把 Sun JDK 掌握好。

## 什么是 JRE

JRE（Java Runtime Environment）是 Java 运行时环境，它是运行 Java 程序所必需的环境。JRE 包含了 Java 虚拟机（JVM）和 Java 核心类库。

## 什么时 JVM

JVM（Java Virtual Machine）是 Java 虚拟机，它可以在不同的操作系统上运行 Java 程序。JVM 会加载字节码文件并解释执行。JVM 会逐行解释字节码指令，并将其转换为特定操作系统的机器码执行。

三者关系：JDK 包含 JRE，JRE 包含 JVM。开发 Java 程序需要安装 JDK，而运行 Java 程序只需安装 JRE。

## 环境变量

环境变量是操作系统中一个重要的概念，它用于存储一些系统或程序运行时需要的信息。

在 Java 环境搭建中，主要涉及到两个环境变量：`JAVA_HOME`和`PATH`。

- `JAVA_HOME`：用于指定 JDK 的安装路径，方便其他程序或脚本找到 JDK 的位置。
- `PATH`：用于指定可执行文件的搜索路径。将 JDK 的 bin 目录添加到 PATH 环境变量中，使得在命令行中可以直接执行 `javac`、`java`等命令。

## 安装 JDK

1. **访问官网**：打开 [Oracle 官方 JDK 下载页面](https://www.oracle.com/java/technologies/javase-downloads.html) 或 [OpenJDK 下载页面](https://jdk.java.net/)。如果是商业用途，建议从 Oracle 官网下载并遵循其许可协议；如果是开源项目，OpenJDK 是不错的选择。
2. **选择版本**：根据自己的需求选择合适的 JDK 版本，目前常用的有 JDK 8、JDK 11 和 JDK 17 等长期支持（LTS）版本。
3. **下载安装包安装**：根据自己的操作系统（Windows、Mac、Linux 等）选择对应的安装包（如 `.exe`、`.dmg` 或 `.tar.gz` 文件），比如 Windows 系统双击下载的 `.exe` 安装包，按照安装向导提示进行操作。
4. **配置环境变量**：右键点击”我的电脑“，选择”属性“，点击”高级系统设置“，在弹出的窗口中点击”环境变量“，在系统环境变量中添加 `JAVA_HOME` 变量，指向 JDK 的安装目录，并将 `%JAVA_HOME%\bin` 添加到 `Path` 变量中。
5. **验证安装**：打开命令行工具（如 Windows 下的 cmd 或 PowerShell，Mac 下的终端），输入 `java -version` 和 `javac -version` 命令，若能显示版本信息，则说明安装成功。

## 多版本 JDK 的管理

在实际开发中，可能需要在不同版本的 JDK 之间进行切换。为了方便管理，建议使用 JDK 版本管理工具，如 jenv（适用于 MacOS 和 Linux）、jswitch（适用于 Windows）、SDKMAN!（适用于所有操作系统）等。

### 使用 jEnv 管理 JDK

jEnv 是一个用于管理多个 Java 版本的工具，它可以在不同的项目中使用不同的 JDK 版本。

1. **安装 jEnv**：根据操作系统选择合适的安装方法，如在 Mac 下使用 Homebrew 安装：`brew install jenv`。
2. **添加 JDK 到 jEnv**：使用 `jenv add /path/to/jdk` 命令将安装的 JDK 添加到 jEnv 中。
3. **设置全局 JDK 版本**：使用 `jenv global <version>` 命令设置全局默认的 JDK 版本。
4. **设置项目 JDK 版本**：在项目目录下使用 `jenv local <version>` 命令设置项目本地的 JDK 版本。
5. **验证切换**：在项目目录下使用 `java -version` 命令验证当前项目使用的 JDK 版本是否正确。

### 使用 SDKMAN! 管理 JDK

SDKMAN! 是一个用于管理多个 SDK 版本的工具，它可以在不同的项目中使用不同的 SDK 版本。

1. **安装 SDKMAN!**：在命令行中输入 `curl -s "https://get.sdkman.io" | bash` 命令安装 SDKMAN!，然后设置环境变量 `source "$HOME/.sdkman/bin/sdkman-init.sh"`, 完成安装后，执行 sdk version 命令，验证是否安装成功。
2. **添加 JDK 到 SDKMAN!**：使用 `sdk install java <version>` 命令将安装的 JDK 添加到 SDKMAN! 中。
3. **设置全局 JDK 版本**：使用 `sdk use java <version>` 命令设置全局默认的 JDK 版本。
4. **设置项目 JDK 版本**：在项目目录下使用 `sdk use java <version>` 命令设置项目本地的 JDK 版本。
5. **验证切换**：在项目目录下使用 `java -version` 命令验证当前项目使用的 JDK 版本是否正确。

另外 SDKMAN! 还提供了其他功能，如管理 Maven、Gradle、Scala 等版本。可以使用 `sdk install <tool> <version>` 命令安装其他版本的工具，使用 `sdk use <tool> <version>` 命令切换工具版本。
