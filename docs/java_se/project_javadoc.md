# 文档注释

Java 程序代码有三种注释：

- 单行注释：以 `//` 开头，注释内容直到行尾结束。
- 多行注释：以 `/*` 开头，以 `*/` 结尾，注释内容可以跨越多行。
- 文档注释：以 `/**` 开头，以 `*/` 结尾，用于生成 API 文档。

其中特殊的文档注释，能够通过 `javadoc` 工具生成一个 HTML 文档。

文档注释主体包含两部分：

- 标记：以 `@` 开头，用于指定文档注释的类型，比如方法参数的标记 `@param`。
- 自由格式文本：文档注释的具体内容。可以使用 HTML 修饰符，例如，用于强调的 `<em>…</em>`、用于着重强调的 `<strong>…</strong>`、用于项目符号列表的以及用于包含图像的 `<img src="…" alt="…" />`
  等。但是比较特殊的是，如果要键人代码，需要使用`{@code ...}`而不是 `<code>…</code>`，这样就不用操心代码文本中出现的转义字符了，比如 `< > /`。

> 如果注释中包含文件，比如图片文件，应该将这些文件放到 `src/main/resource/doc-files` 目录下，然后在注释文档中使用 `<img src="doc-files/filename.png" alt="description" />` 包含图片。

## javadoc 工具

`javadoc` 工具可以从 Java 源文件中提取文档注释，并生成一个 HTML 文档。要使用 `javadoc` 工具，需要在命令行中执行以下命令：

```bash
javadoc -d doc nameOfPackage1 nameOfPackage2 ...
```

其中，`-d doc` 表示将生成的 HTML 文档放到 `doc` 目录下，`nameOfPackage1 nameOfPackage2 ...` 表示要处理的包名。

执行完以上命令后，就会在 `doc` 目录下生成一个 HTML 文档，其中包含了所有 Java 源文件的文档注释。

javadoc 实用工具从下面几项中抽取信息：

- 模块；
- 包；
- 公共类与接口；
- 公共的和受保护的字段；
- 公共的和受保护的构造器及方法。

## 模块注释

模块注释是指在 `module-info.java` 文件头部添加一个 Javadoc 注释`/**...*/`，这个文件用于描述模块的名称、依赖关系、导出的包、服务等信息。

```java
/**
 * This is example module.
 * @module com.example
 * @requires java.base
 * @exports com.example.domain
 * @exports com.example.service
 */
module com.example {
    requires java.base;
    exports com.example.domain;
    exports com.example.service;
}
```

## 包注释

要想产生包注释，就需要在每一个包目录中添加一个单独的文件口 可以有如下两个选择：

1. 提供一个名为 `package-info.java` 的 Java 文件口 这个文件头部包含一个初始的 Javadoc 注释`/**...*/`，紧接着声明 `package` 语句。这个文件通常还包括其它更多内容，比如可以声明包级别的常量、注解等。
2. 提供一个名为 `package.html` 的 HTML 文件，javadoc 会抽取标记 `<body>...</body>` 之间的所有文本内容。

更推荐使用第一种。

```java
/**
* This is domain package, the core business logic is implemented here
*/
package com.example.domain;
```

## 类注释

类注释是指在类定义之前添加一个 Javadoc 注释`/**...*/`，这个注释用于描述类的功能、使用方法、注意事项等。

```java
/**
 * A {@code ExampleClass} is a class that implements the core business logic.
 * <p>
 * This class provides methods to perform the following operations:
 * <ul>
 *     <li>Operation 1</li>
 *     <li>Operation 2</li>
 * </ul>
 * </p>
 */
public class ExampleClass {
    // ...
}
```

## 方法注释

每个方法注释必须放在所描述的方法之前，方法注释的格式除了通用的注释标记外，还有几个专用的的标记表示方法的参数 `@param`、返回值 `@return`、异常 `@throws` 等信息。

```java
/**
 * Performs operation 1 on the given input.
 *
 * @param input the input to perform operation 1 on
 * @return the result of operation 1
 * @throws IllegalArgumentException if the input is invalid
 */
public int operation1(int input) throws IllegalArgumentException {
    // ...
}
```

## 字段注释

只需要对公共的字段添加注释，因为私有字段通常只在类内部使用，外部不需要知道其实现细节。

```java
/**
 * The "HEARTS" constant represents the value of the Hearts suit in a standard deck of playing cards.
 */
public static final int HEARTS = 1;
```

## 通用注释标记

- `@author` 作者，可以有多个作者，每个作者用一个 `@author` 标记表示。
- `@version` 版本
- `@since` 引用该特性的版本描述，比如 `@since 1.0` 表示从 1.0 版本开始引入该特性。
- `@see` 参考 可以使用超链接到其他类、方法、字段，或者外部文档等，比如 `@see {@link com.example.ExampleClass#operation1(int)}`。
- `@link` 链接 可以链接到其他类、方法、字段等，比如 `@link com.example.ExampleClass#operation1(int)`。
- `@code` 代码 用于在注释中包含代码片段，比如方法的实现代码。
- `@inheritDoc` 继承 用于继承父类或实现接口的方法的注释。
- `@serial` 序列化 用于描述类的序列化机制。
- `@serialData` 序列化数据 用于描述类的序列化数据。
- `@serialField` 序列化字段 用于描述类的序列化字段。
- `@deprecated` 已废弃 用于标记已废弃的类、方法、字段等。
- `@hidden` 隐藏 用于标记隐藏的类、方法、字段等。
