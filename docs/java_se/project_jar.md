# Jar 包

通常情况下，程序编写完成后，需要交付给其他开发者或用户使用，通常不会直接交付源文件。

程序源代码需要经过编译打包，才能交付给其他用户使用。Java 程序编译打包后的结果是一个 `.jar` 压缩包文件，里面不是源代码，而是经过编译后的所有字节码文件（`.class` 文件）以及其他相关的资源文件和元数据。

Jar （**J**ava **Ar**chive）是 Java 平台的一种归档文件格式，用于将多个 Java 类文件、相关的资源文件和元数据打包成一个单独的压缩文件包。

> jar 包其实就是一个压缩文件包，可以使用解压缩工具（如 WinRAR、7-Zip 等）来查看和提取其中的内容。

```sh
# 编译，-d 指定编译输出目录
javac -d ./out src/com/example/HelloWorld.java

# 打包，-cvf 是 create verbose file 的 缩写，-f 后面跟着的是输出的文件名， -C 后面跟着的是要压缩的目录，
jar -cvf ./out/hello.jar -C ./out .
```

## jar 命令

以下是 Java 中 `jar` 命令常用的参数：

| 参数 | 简要说明                              | 示例                                                       |
| ---- | ------------------------------------- | ---------------------------------------------------------- |
| `-c` | 创建新的 JAR 文件                     | `jar -cf hello.jar com/example/*.class`                    |
| `-v` | 在标准输出中生成详细输出              | `jar -cvf hello.jar com/example/*.class`                   |
| `-f` | 指定 JAR 文件的名称                   | `jar -cf hello.jar com/example/*.class`                    |
| `-t` | 列出 JAR 文件的内容                   | `jar -tf hello.jar`                                        |
| `-x` | 从 JAR 文件中提取指定的文件或所有文件 | `jar -xf hello.jar`                                        |
| `-u` | 更新已存在的 JAR 文件                 | `jar -uf hello.jar newfile.class`                          |
| `-m` | 包含指定清单文件中的清单信息          | `jar -cvfm hello.jar MANIFEST.MF com/example/*.class`      |
| `-C` | 在打包时切换到指定目录                | `jar -cvf hello.jar -C ./out .`                            |
| `-e` | 指定应用程序入口点                    | `jar -cvfe hello.jar com.example.Main com/example/*.class` |

## jar 包的结构

一个完整的 Jar 包通常会包含以下内容：

- 编译后的 Java 类文件（`.class` 文件），一般放在 `lib` 目录下
- 资源文件（如配置文件、图片、音频等），一般放在 `resources` 目录下
- 元数据文件，一般在 `META-INF` 目录下（如 `MANIFEST.MF` 文件）

## META-INF 目录

`META-INF` 目录是 Jar 包中的一个特殊目录，用于存放元数据文件。通常情况下，`META-INF` 目录下会包含以下文件：

- `MANIFEST.MF`：清单文件，用于描述 Jar 包的元数据信息。
- `INDEX.LIST`：索引文件，用于加快 Jar 包的加载速度。
- `SIGNATURE.MF`：签名文件，用于验证 Jar 包的完整性和来源。

## 清单文件

清单文件（`MANIFEST.MF`）是一个文本文件，用于描述 Jar 包的元数据信息。使用键值对格式来说明相关信息，包含了关于 Jar 包的版本、入口点、依赖项等重要信息。

清单文件的格式如下：

```
Manifest-Version: 1.0
Created-By: 1.8.0_362 (Oracle Corporation)
Main-Class: com.example.Main
Class-Path: lib/example.jar lib/other.jar
```

其中：

- `Manifest-Version`：清单文件的版本，通常为 `1.0`。
- `Created-By`：指定创建程序工具和版本，通常是 `Java` 版本号。
- `Main-Class`：指定应用程序的入口点，即包含 `public static void main(String[] args)` 方法的类。
- `Class-Path`：指定 Jar 包运行时需要依赖的其他 Jar 包或类路径，多个路径使用空格分隔。

其它一些常见的清单文件字段：

```
Implementation-Title: 实现的标题。
Implementation-Version: 实现的版本。
Implementation-Vendor: 实现的供应商。
Specification-Title: 规范的标题。
Specification-Version: 规范的版本。
Specification-Vendor: 规范的供应商。
Sealed: 如果为 true，则包中的所有类都必须在同一个 JAR 文件中找到。
```

清单文件通常会被包含在 Jar 包的 `META-INF` 目录下，文件名必须为 `MANIFEST.MF`。

可以创建一个文本文件，命名为 `MANIFEST.MF`，并在其中编写清单文件的内容。如果要修改清单文件的内容，只需要编辑 `MANIFEST.MF` 文件即可。但是需要注意的是，修改清单文件后，需要重新打包 Jar 包才能生效。

使用 `jar` 命令创建 Jar 包时，可以使用 `-m` 参数指定清单文件的路径，例如：

```sh
# 使用 jar 命令创建 Jar 包时，-m 参数指定清单文件的路径，命令会将 com/example 目录下的所有类文件打包成 hello.jar，并使用 MANIFEST.MF 作为清单文件。
jar -cvfm hello.jar MANIFEST.MF com/example/*.class
```

在大型项目中，通常会直接通过构建工具（如 Maven 或 Gradle）来自动化 Manifest 文件的生成。这些工具可以根据项目的配置和依赖信息自动生成准确的 Manifest 文件，减少手动错误。

在源程序代码中如果需要获取清单文件中的信息，可以使用 `java.util.jar.Manifest` 类来读取。

```java
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.util.Map;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadManifest {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("hello.jar");
             Manifest manifest = new Manifest(fis)) {
            Attributes mainAttributes = manifest.getMainAttributes();
            for (Map.Entry<Object, Object> entry : mainAttributes.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

## jar 包的运行

要运行一个 Jar 包，需要使用 `java -jar` 命令，例如：

```sh
java -jar hello.jar
```

java 虚拟机会自动解压包文件，然后根据 `MANIFEST.MF` 中的 `Main-Class` 来找到入口类，执行 `main` 方法。并且会根据 `Class-Path` 路径寻找依赖项，自动加载所需的类文件。
