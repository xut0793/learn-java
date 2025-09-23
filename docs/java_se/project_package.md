# 包 package

使用任何语言进行编程都有一个类似的问题，那就是如何组织代码。

具体来说包含以下内容：

- 如何合理组织各种源文件，避免命名冲突？-- 包和模块
- 如何使用第三方的依赖库？-- jar 包和 classpath
- 各种代码和依赖库如何编译链接为一个完整的程序？ --jar

## What 什么是 package

在 Java 里，`package`（包）是一种组织类和接口的机制，它将相关的类和接口放在同一个命名空间（namespace）下。

完成可以把包类比计算机中的文件夹，不同的包就像不同的文件夹，用于分类存放 Java 类文件，避免命名冲突。事实上，包名和文件目录的层级结构也是要求一致的，在 IDE 中根据包名的层级结构，会自动创建对应的文件夹来存放类文件。

而且包的层级之间并没有像任何关系，只是一种组织代码的方式。就像同一层级文件夹中可以直接放文件，也可以再创建子文件夹一样。比如 `java.util` 包中有一些常用的类，其中嵌套的 `java.util.concurrent` 包中有一些并发相关的类，但 `java.util` 包和 `java.util.concurrent`包看起来是嵌套关系，但逻辑上没有任何关系，每个包都是独立的类或接口的集合。

## Why 为什么需要 package

使用任何语言进行编程都有一个相同的问题，就是命名冲突。

程序一般不全是一个人写的，会调用系统提供的代码、第三方库中的代码、项目中其他人写的代码等，不同的人就不同的目的可能定义同样的类名/接口名，即使代码都是一个人写的，将多个关系不太大的类和接口都放在一起也容易造成冲突，为了避免命名冲突，Java 引入了包的概念。

1. **避免命名冲突**：不同的开发者或项目可能会使用相同的类名，通过将类放在不同的包中，可以确保类名的唯一性。
2. **提高代码的可维护性**：将相关的类组织在同一个包中，方便查找和管理代码。
3. **访问控制**：包可以作为访问控制的边界，通过包的访问权限可以限制类的可见性。

## How 如何使用

使用 package 通常分为两步：

- 声明 package
- 导入 package

## 声明 package

在 Java 源文件的第一行（注释除外）使用 `package` 关键字来声明包，语法如下：

```java
package package_name;
```

其中包名的命名规范需要符合 Java 的标识符命名规则，即只能包含字母、数字、下划线和美元符号，并且不能以数字开头。

另外，包名称中以点号 `.` 分隔表示层级结构，包名和文件目录的层级结构要求一致，比如包名 `learnjava.oop.package` 对应的文件目录就是 `E:\\learnjava\\oop\\package`。

使用包的主要原因就是确保类名的唯一性，为了保证不同项目的包名绝对唯一，可以使用一个互联网域名（域名是全球唯一的）的反序形式作为包名，然后对于不同的项目使用不同的子包名。

比如：

- 项目 A 的包名可以是 `com.baidu.tieba.projecta`
- 项目 B 的包名可以是 `com.baidu.tieba.projectb`

这样就可以确保项目 A 中的类和项目 B 中的类在公司内部不会发生命名冲突，也在整个互联网上不会与其它项目发生命名冲突。

比如 Java API 中所有的类和接口都位于包`Java`或`javax`下，`Java`是标准包，`javax`是扩展包。

> javax 中的 x 是扩展包 extension 缩写。在编程中很普遍将 extension 缩写为 x，比如 reactive extension for java 简写称为 rxjava。

## 导入 import

在 Java 中，同一个包下的类之间互相引用是不需要包名的，可以直接使用。但如果类不在同一个包内，则必须要知道其所在的包。使用有两种方式：

- 一种是通过类的完全限定名；
- 另外一种是将用到的类引入当前类。

### 完全限定类名（fully qualified class name）。

**完全限定类名（fully qualified class name）**就是带有完整包名和类名称的类名。比如项目 A 中有一个类 `Employee`，它的完全限定类名就是 `com.baidu.tieba.projecta.Employee`。

然后如果项目 A 中的 `Employee` 类中需要使用 `java.util.time` 包中的 `LocalDate` 类的相关方法，可以这样编写代码：

```java
java.time.localDate today = java.time.LocalDate.now();
```

但这样写起来比较冗长，为了方便起见，Java 提供了 `import` 语句来导入其他包中的类。

比如在 `Employee` 类中需要使用 `java.util.time` 包中的 `LocalDate` 类的相关方法，就可以使用 `import` 语句导入该类：

```java
import java.util.time.LocalDate;
```

然后在 `Employee` 类中就可以直接使用 `LocalDate` 类的相关方法了：

```java
LocalDate today = LocalDate.now();
```

`import` 语句的关键是可以提供一种简写的方式来引用其它包中的各个类。一旦声明了 import 语句，在使用其它类时，就不需要写类的全名了。

## 全量导入

如果需要导入一个包中的所有类，可以使用 `import` 语句的通配符 `*` 来导入该包中的所有类。

```java
import java.util.time.*;
```

这样就可以在 `Employee` 类中直接使用 `LocalDate` 类的相关方法了，而不需要写 `java.util.time.LocalDate`。

全量导入有两点需要注意：

- 全量导入只能是包级别的，不能是包之前的前缀路径，比如 `java.util.time.*` 是可以的，但是 `java.util.*` 或 `java.*` 是不可以的。
- 另外，如果多个包使用全量导入，但其它又有命名冲突的类，就需要使用完全限定类名来引用该类。比如 `java.util` 和 `java.sql` 包中都有 `Date` 类，就需要使用 `java.util.Date` 或 `java.sql.Date` 明确来引用该类。

```java
import java.util.*;
import java.sql.*;
import java.util.Date;
```

## 静态导入

如果需要导入一个类中的所有静态成员（静态字段和静态方法），可以使用 `import static` 语句来导入。

```java
import static java.util.time.LocalDate.now;
```

这样就可以在 `Employee` 类中直接使用 `now` 方法了，而不需要写 `java.util.time.LocalDate.now()`。

## 完全限定类名

在源代码名中使用 `import` 语句后，在代码中使用类名时可以简写类名。在编译时，编译器会根据 `import` 语句中的类名来查找对应的类文件。如果没有找到，就会报错。最后在编译为字节码的 class 文件中，类名会被转换为完全限定类名。

也就是说，简写类名只存在于源代码中，在字节码中总是使用完全限定类名来引用其它类的。

## 包的访问性

包可以作为访问控制的边界，通过包的访问权限可以限制类的可见性。

Java 中的包访问权限有两种：

- 默认访问权限（default access）：如果一个类没有使用 `public`、`protected`、`private` 访问修饰符声明，那么它就属于默认访问 `default` 权限，只能在同属一个包中访问。
- 包访问权限（package access）：如果一个类使用 `public` 或 `protected` 访问修饰符声明，那么它就属于包访问权限，可以在同一个包中或不同包的子类中访问。

总结：

- 不同包中的类只能通过 `public` 或 `protected` 访问修饰符来访问，而 `default` 默认访问权限只能在同一个包中访问。

## 无名包（默认名）

如果一个类没有使用 `package` 语句声明包名，那么它就属于无名包（default package）。

所有的无包名的类都属于默认包（default package），这样跨文件目录的 `public`, `protected`, `default` 权限的类都可以被访问到。

## 包信息描述

在 Java 中，每个包都可以有一个 `package-info.java` 是一个特殊的 Java 文件，可以添加到任何 Java 源码包中。它的主要目的是提供包级别的文档说明、注解和变量。该文件编译时不会生成单独的.class 文件，但会编译到包声明中。

在 Java 5 之前，包级的文档是通过 package.html 文件生成的。而在 Java 5 及以上版本，包的描述和相关文档可以写入 package-info.java 文件，并用于生成 JavaDoc。

`package-info.java` 的作用：

- 提供包的整体注释说明。
- 提供包级别的注解。
- 声明友好包级别的常量。

## 提供包级别的文档说明

通过在 package-info.java 文件中添加注释，可以生成对应包的 JavaDoc 文档。

```java
/**
* This is domain module, the core business logic is implemented here
*/
package com.example.domain;
```

## 提供包级别的注解

package-info.java 文件还可以用于为包提供注解。例如，可以创建一个自定义注解并将其应用于包，然后，可以通过反射机制获取包的注解信息。

```java
// 先定义一个只能标注在包上的注解
@Target(ElementType.PACKAGE) // 注解标注的目标是包
@Retention(RetentionPolicy.RUNTIME)
public @interface TestPkg {
}

// 然后在 `package-info.java` 类上使用该注解
@TestPkg
package com.example.domain;


// 在代码中使用该注解
public class TestPkgD {
    public static void main(String[] args) {
        // 获取包的注解
        Package pkg = Package.getPackage("com.company");
        Annotation[] annotations = pkg.getAnnotations();

        for(Annotation an : annotations) {
            if(an instanceof PkgAnnotation) {
                System.out.println("我是一个PkgAnnotation注解");
            }
        }
    }
}
```

## 提供包级别的变量

声明 default 默认访问权限的类，然后将这些变量放在 package-info.java 文件中。这样在包内的任意类可以访问这些变量，而包外的类则无法访问。

```java
/**
* 包常量
*/
class PACKAGE_CONST {
   public static final String TEST = "Test Value";
}
```
