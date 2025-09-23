# 注解

## 什么是注解

注解（Annotation）从字面意思理解，标注解释信息，就是为某些对象添加一些元数据，然后可以在编译器、运行时环境或者自身业务逻辑中获取这些标注信息做一些操作，用来增强或者修改程序功能。

从实现上看，注解是一种特殊的接口，它通过 `@interface` 关键字来定义，注解只是用来标注信息，真正的逻辑处理是由其它工具（比如注解处理器、ASM 框架或者反射）实现具体的逻辑。

> 元数据，用来描述数据的数据，也可以理解为数据的解释信息。

## 使用注解

Java 程序中注解可以应用于类、方法、构造方法、字段、参数、局部变量等程序元素上。

Java 程序中自身提供了一些注解，也可以自定义注解。注解的类型，大概分类如下：

- 标准注解：也称为内置注解，是 Java 程序中自身提供的注解，比如 `@Override`、`@Deprecated`、`@SuppressWarnings`、`@SafeVarargs`、`@FunctionalInterface` 等。
- 元注解：元注解是用于注解其他注解的注解，通常用于创建注解，Java 程序中自身提供了一些元注解，比如 `@Retention`、`@Target`、`@Documented`、`@Inherited`、`@Repeatable`、`@Native` 等。
- 构架和库提供的注解：一些框架和库也提供了自己的注解，比如 Spring 框架中的 `@Component`、`@Service`、`@Repository`、`@Controller` 等。
- 自定义注解：自定义注解是用户根据需要定义的注解，用于标注程序元素。

## 标准注解

### @Override

`@Override` 注解用于标注方法，用于表示该方法是重写父类的方法。如果一个方法被标注了 `@Override`，那么编译器会检查该方法是否真的重写了父类的方法（即方法签名是否与父类的方法签名一致，或者该方法是否是父类的方法），如果没有重写，编译器会报错。

```java
@Override
public String toString() {
    return "MyClass{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
}
```

### @Deprecated

`@Deprecated` 注解用于标注方法、类、字段等程序元素，表示该元素已经被废弃，不建议使用，通常编辑器会显示一条删除线以示警告。如果一个元素被标注了 `@Deprecated`，那么在使用该元素的地方，编译器会发出警告。

从 Java 9 开始，`@Deprecated` 注解多了两个属性：`since` 和 `forRemoval`。

- `since` 属性用于指定该元素废弃的版本号。如果一个元素被标注了 `@Deprecated(since = "1.8")`，那么在使用该元素的地方，编译器会发出警告，并且警告信息中会包含该元素废弃的版本号。
- `forRemoval` 属性用于指定该元素是否会在未来的版本中被移除。如果一个元素被标注了 `@Deprecated(forRemoval = true)`，那么在使用该元素的地方，编译器会发出警告，并且警告信息中会包含该元素将在未来的版本中被移除的信息。

```java
@Deprecated(since = "1.8", forRemoval = true)
public void deprecatedMethod() {
    // 该方法已经被废弃，不建议使用
}
```

### @SuppressWarnings

`@SuppressWarnings` 注解用于标注方法、类、字段等程序元素，表示压制 Java 的编译警告，它有一个必填参数，表示压制哪种类型的警告。

- `@SuppressWarnings("deprecation")`：压制被废弃的元素的警告。
- `@SuppressWarnings("unchecked")`：压制未经检查的类型转换警告。
- `@SuppressWarnings("rawtypes")`：压制使用原始类型的警告。
- `@SuppressWarnings("varargs")`：压制可变参数的警告。
- `@SuppressWarnings("serial")`：压制序列化警告。
- `@SuppressWarnings("fallthrough")`：压制 switch 语句中缺少 break 语句的警告。
- `@SuppressWarnings("path")`：压制文件路径警告。
- `@SuppressWarnings("deprecation")`：压制被废弃的元素的警告。

```java
// 同时压制多种类型的警告
@SuppressWarnings({"deprecation", "unused"})
public static void main(String[] args) {
    Date date = new Date(2017, 4, 12);
    int year = date.getYear();
}
```

### @SafeVarargs

`@SafeVarargs` 注解用于标注方法、构造方法，表示该方法是安全的，不会在运行时抛出 `unchecked` 警告。如果一个方法被标注了 `@SafeVarargs`，那么在编译时，编译器会忽略该方法的 `unchecked` 警告。

```java
@SafeVarargs
public static <T> List<T> asList(T... a) {
    return new ArrayList<>(Arrays.asList(a));
}
```

### @FunctionalInterface

`@FunctionalInterface` 注解用于标注函数式接口，函数式接口是指只包含一个抽象方法的接口。如果一个接口被标注了 `@FunctionalInterface`，那么编译器会检查该接口是否符合函数式接口的定义，如果不符合，编译器会报错。

```java
@FunctionalInterface
public interface MyFunctionalInterface {
    void myMethod();
}
```

Java 提供的内置注解比较少，我们日常开发中使用的注解基本都是自定义的。不过，一般也不是我们定义的，而是由各种框架和库定义的，我们主要还是根据它们的文档直接使用。

## 定义注解

元注解是用于注解其他注解的注解，通常用于创建注解，Java 程序中提供了一些元注解，用来方便的自定义注解。

- `@Target`：用于指定注解可以应用于哪些程序元素，比如 `@Target(ElementType.METHOD)` 表示该注解只能应用于方法上。
- `@Retention`：用于指定注解的保留策略，比如 `@Retention(RetentionPolicy.RUNTIME)` 表示该注解在运行时保留，并且可以通过反射获取到。
- `@Documented`：用于指定注解是否会被包含在 JavaDoc 中。
- `@Inherited`：用于指定注解是否可以被继承。
- `@Repeatable`：用于指定注解是否可以重复应用于同一个程序元素。
- `@Native`：用于指定注解是否可以被本地代码使用。

其中

- ElementType 是一个枚举类型，用于指定注解可以应用于哪些程序元素。
  - METHOD：方法
  - TYPE：类、接口、枚举
  - FIELD：字段
  - PARAMETER：参数
  - CONSTRUCTOR：构造方法
  - LOCAL_VARIABLE：局部变量
  - ANNOTATION_TYPE：注解类型
  - PACKAGE：包
- RetentionPolicy 是一个枚举类型，用于指定注解的保留策略，比如 SOURCE、CLASS、RUNTIME 等。
  - SOURCE：只在源代码中保留，编译器将代码编译为字节码文件后就会丢掉。
  - CLASS：保留到字节码文件中，但 Java 虚拟机将 class 文件加载到内存时不一定会在内存中保留。
  - RUNTIME：一直保留到运行时，所以一般需要通过反射获取注解信息的都需要使用 RUNTIME 策略。

下面是 `@Override` 注解的定义：

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Override {
}
```

定义注解与定义接口有点类似，都用了 `interface`，不过注解的 `interface`前多了`@`符号，这是 Java 中定义注解的语法。另外，它还有两个元注解`@Target`和`@Retention`，这两个注解专门用于定义注解本身。

- `@Target`表示注解的目标是应用于方法（`ElementType.METHOD`）。
- `@Retention`表示注解的保留策略，表示仅在源文件中保留 SOURCE，所以仅应于编译器使用。

同接口一样，注解内部可以声明一些方法，这些方法称为注解元素。注解元素的定义与接口的方法定义类似，但是注解元素不能有方法体，只能有方法的声明。

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value();
    int age() default 18;
}
```

注解内的方法可以有默认值，通过声明 `default` 关键字来指定默认值。

在使用注解时，我们可以为注解元素指定值，也可以使用默认值。如果没有为注解元素指定值，那么就会使用默认值。

```java
@MyAnnotation(value = "hello", age = 20)
public void myMethod() {
    // 方法体
}
```

比较特殊的是 `value` 它是一个特殊的注解元素，当注解只有一个元素，并且这个元素名称为 `value` 时，在使用时可以省略 `value` 元素的名称，直接为 `value` 元素指定值。

```java
// @MyAnnotation("hello") 等价于 @MyAnnotation(value = "hello")
@MyAnnotation("hello")
public void myMethod() {
    // 方法体
}
```

另一个比较特殊的元注解是 `@Repeatable`，它用于指定注解是否可以重复应用于同一个程序元素。如果一个注解被标注了 `@Repeatable`，那么在使用该注解时，就可以在同一个程序元素上重复应用该注解。

```java
@Repeatable(MyAnnotations.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value();
    int age() default 18;
}
```

使用时可以多次应用该注解：

```java
@MyAnnotation("hello")
@MyAnnotation("world")
public void myMethod() {
    // 方法体
}
```

另一个比较特殊的元注解是 `@Inherited`，它用于指定注解是否可以被继承。如果一个注解被标注了 `@Inherited`，那么在使用该注解时，子类会自动继承父类的注解。

```java
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
    String value();
    int age() default 18;
}
```

在父类中使用该注解：

```java
@MyAnnotation("hello")
public class MyClass {
    // 类体
}
```

在子类中使用该注解：

```java
public class MySubClass extends MyClass {
    // 类体
    public static void main(String[] args) {
        // 在子类中，`@MyAnnotation("hello")` 注解会被自动继承。
        System.out.println(MySubClass.class.isAnnotationPresent(MyAnnotation.class)); // true
    }
}
```

## 提取注解信息

定义注解时可以通过 `@Retention` 元注解指定注解的保留策略，常用的有 `SOURCE`、`CLASS`、`RUNTIME` 三种。

- `SOURCE`：只在源代码中保留，编译器中处理，最后在将代码编译为字节码文件后就会丢掉。
- `CLASS`：保留到字节码文件中，类加载器处理，在 JVM 虚拟机将 class 文件加载到内存时不一定会在内存中保留。
- `RUNTIME`：一直保留到运行时，可以通过反射 Reflection 获取注解信息进行处理。

所以下面从这三种情形中看下，如何使用注解信息

## 注解处理器处理 SOURCE 时的注解信息

在注解中指定 `@Retention(RetentionPolicy.SOURCE)` 保留策略时，编译器在编译时将通过注解处理器 `AbstractProcessor` 处理注解信息，但是在编译为字节码文件后，注解信息就会被丢掉。所以这部分注解信息只能在编译时使用，不能在运行时使用。

APT（Annotation Processing Tool）它是 Java 编译器使用的注解处理器接口集合，它可以让开发人员在编译期通过 APT 获取到注解和被注解对象的相关信息，并根据这些信息在编译期按我们的需求生成 java 代码模板或者配置文件。APT 获取注解及生成代码都是在代码编译时候完成的，相比反射在运行时处理注解大大提高了程序性能。

> 注意：注解处理器只能产生新的源文件，它无法修改已有源文件中的代码。

```
java源码 ++--> 解析和填充符号   +----> 注解处理器  +--+-----> 分析与字节码生成器   +----->class文件
          ^                                        |
          |                                        |
          |                                        |
          |                                        |
          +-------+ 生成新的 Java源文件 <-----------+

```

Java 官方文档给出的注解处理过程的定义：注解处理过程是一个有序的循环过程。在每次循环中，一个处理器可能被要求去处理那些在上一次循环中产生的源文件和类文件中的注解。

自定义一个注解处理器的核心是 `javax.annotation.processing.AbstractProcessor`，要求实现 `AbstractProcessor` 抽象类，并要求重写 `process` 方法。

抽象类 `AbstractProcessor` 定义如下：

```java
public abstract class AbstractProcessor implements Processor {
    // 构造方法
    public AbstractProcessor() {
    }

    // 初始化方法，会在注解处理开始前调用
    // 通过 ProcessingEnvironment 来获取一些帮助我们来处理注解的工具类
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }


    // 获取通过 @SupportedAnnotationTypes 元注解支持的注解类型，会在编译时被调用，返回的集合元素是注解的全限定名（包名+类名）
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    // 获取通过 @SupportedSourceVersion 元注解支持的 Java 版本，会在编译时被调用，返回当前正在使用的 Java 版本。一般通过 SourceVersion.latestSupported() 方法来获取最新的 Java 版本。
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    // 处理注解的核心方法，会在每次循环中调用
    @Override
    public abstract boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);
}
```

在编译器执行注解处理器的循环过程中，每次循环都会调用 `process` 方法，`process` 方法提供了两个参数：

- `Set<? extends TypeElement> annotations`： 是我们请求处理注解类型的集合（也就是我们通过重写 `@SupportedAnnotationTypes` 方法所指定的所有注解集合
- `RoundEnvironment roundEnv`： 是有关当前和上一次循环的信息的环境。返回值表示这些注解是否由当前 Processor 声明，如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们；如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们。

在第一个参数的 `TypeElement` 表示当前正在处理的注解类型，编译器对源码编译时会产生一颗类似 AST（Abstract Syntax Tree）的语法树，每个节点都是一个 `Element` 元素，它是 `javax.lang.model.element.Element` 接口及其子接口, 包括：

- `PackageElement`: 表示包元素，用于表示一个包。
- `TypeElement`: 表示类型元素，用于表示一个类、接口、枚举或注解类型。
- `ExecutableElement`: 表示可执行元素，用于表示一个方法、构造方法或初始化块。
- `VariableElement`: 表示变量元素，用于表示一个字段、枚举常量或方法参数。
- `AnnotationMirror`: 表示注解镜像，用于表示一个注解的实例。
- `TypeMirror`: 表示类型镜像，用于表示一个类型的实例。

通过一个简单的代码示例，看下这些表示的部分

```java
package com.learnjava.oop_annotation; // PackageElement

public class user { // TypeElement
    private String name; // VariableElement
    public void setUserName( // ExecutableElement
                String name) {  // VariableElement
        this.name = name;
    }
}
```

`Element` 提供了常用的方法来获取元素的信息：

```
getKind(): 返回元素的类型，比如 `PackageElement`、`TypeElement`、`ExecutableElement`、`VariableElement` 等。
getSimpleName(): 返回元素的简单名称，比如类名、方法名、字段名等。
getEnclosingElement(): 返回当前元素的父元素，比如类的父元素是包，方法的父元素是类，字段的父元素是类等。
getModifiers(): 返回当前元素的修饰符，比如 `public`、`private`、`protected`、`static`、`final` 等。
getQualifiedName(): 返回当前元素的限定名，比如 `com.learnjava.oop_annotation.user`。
getParameters(): 返回当前元素的参数列表，比如方法的参数、构造方法的参数等。
getReturnType(): 返回当前元素的返回类型，比如方法的返回类型、构造方法的返回类型等。
getConstantValue(): 返回当前元素的常量值，比如字段的常量值、枚举常量的常量值等。a
```

第二个参数 `RoundEnvironment roundEnv` 是一个 `RoundEnvironment` 对象，可以通过它的 `Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> annotationType)` 方法来获取被当前指定的注解标注过的所有元素构成的集合。

示例代码：

```java
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 遍历所有使用的注解
        for (TypeElement annotation : annotations) {
            // 获取所有被当前注解标注过的元素
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : elements) {
                MyAnnotation myAnnotation = element.getAnnotation(MyAnnotation.class);
                String value = myAnnotation.value();
                System.out.println("Processing annotation on " + element.getSimpleName() + " with value: " + value);
            }
        }
        return true;
    }
}
```

通过以下步骤来创建和使用一个注解处理器：

1. 创建注解类
2. 创建注解处理器类，继承 `AbstractProcessor` 类，并重写 `process` 方法。
3. 注册注解处理器，在 `META-INF/services` 目录下创建一个文件，文件名是 `javax.annotation.processing.Processor`，文件内容是注解处理器类的全限定名。
4. 编译并运行项目，注解处理器会在编译时被调用，处理注解信息。

下面以为标识了 `@Log` 注解的类生成日志类为示例：

```java
// 第一步：定义注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Log {
    String value() default "";
}


// 第二步：编写注解处理器
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.Log")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class LogProcessor extends AbstractProcessor {
   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
       for (Element element : roundEnv.getElementsAnnotatedWith(Log.class)) {
           String className = element.getSimpleName().toString();
           String logClassName = className + "Logger";
           // 生成日志类代码
           String logClassCode = "package com.example;\n" +
                   "public class " + logClassName + " {\n" +
                   "    public void log(String message) {\n" +
                   "        System.out.println(\"" + className + ": \" + message);\n" +
                   "    }\n" +
                   "}\n";
           // 写入日志类文件
           try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/java/com/example/" + logClassName + ".java"))) {
               writer.println(logClassCode);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return true;
   }
}

// 使用注解
@Log("com.example.User")
public class User {
    private String name;
    private int age;
}
```

这里注册注解使用了 `@AutoService(Processor.class)` 注解，它会自动将注解处理器注册到 `META-INF/services` 目录下的 `javax.annotation.processing.Processor` 文件中。

然后在命令行编译时需要添加 `-processor` 参数来指定注解处理器，例如：

```sh
javac -processor com.example.LogProcessor com/example/User.java
```

注解处理器核心的作用是用来生成类的模板代码或者配置文件等。目前生成 Java 代码通常有两种方式：原始方式和 JavaPoet。

- 原始方式：通过 `PrintWriter` 等 IO 流手动拼接字符串的方式生成 Java 代码，比较繁琐且容易出错。
- JavaPoet：是一个用于生成 Java 代码的库，它提供了一种更加方便、类型安全的方式来生成 Java 代码。使用 JavaPoet 可以避免手动拼接字符串的错误，并且可以利用 Java 代码的类型检查和自动补全等功能。

在 JavaPoet 中使用了面向对象的思想，万物皆对象，方法和类也变成了对象。在类中代码主要被分为了两块，一块是方法（MethodSpec），一块是类（TypeSpec）。

下面是一个使用 JavaPoet 生成 HelloWorld 类的示例：

预期的 HelloWorld 类的代码如下：

```java
package com.example;


public class HelloWorld { // 这个 {} 区域称为 TypeSpec
    public static void main(String[] args) { // 这个 {} 区域称为 MethodSpec
        System.out.println("Hello World!");
    }
}
```

使用 JavaPoet 生成 HelloWorld 类的代码如下：

```java
package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class HelloWorldGenerator {
    public static void main(String[] args) throws IOException {
        // 定义类名
        ClassName className = ClassName.get("com.example", "HelloWorld");
        // 定义方法
        MethodSpec mainMethod = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello World!")
                .build();
        // 定义类
        TypeSpec helloWorldClass = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(mainMethod)
                .build();
        // 生成 Java 文件
        JavaFile javaFile = JavaFile.builder(className.packageName(), helloWorldClass)
                .build();
        // 写入文件
        javaFile.writeTo(new File("src/main/java"));
    }
}
```

上述中几个奇怪的类型字符器表示：

```
$L：表示一个参数，会被替换为方法中传递的参数值。
$S：表示一个字符串，会被替换为方法中传递的字符串值。
$T：表示一个类型，会被替换为方法中传递的类型值。
$N：表示一个名称，会被替换为方法中传递的名称值。
```

## 提取 CLASS 字节码注解信息

`Class` 类的字节码文件格式是归过档的，这种格式相当复杂，并且在没有特殊类库支持的情况下，处理类 Class 文件具有很大的挑战性。

- ASM： 是一个 JAVA 字节码分析、创建和修改的开源应用框架。它可以动态生成二进制格式的 stub 类或其他代理类，或者在类被 JAVA 虚拟机装入内存之前，动态修改类。在 ASM 中提供了诸多的 API 用于对类的内容进行字节码操作的方法。
- Javassist：是一个开源的分析、编辑和创建 Java 字节码的类库，可以直接编辑和生成 Java 生成的字节码。相对于 bcel, asm 等这些工具，开发者不需要了解虚拟机指令，就能动态改变类的结构，或者动态生成类。

> [Java ASM 总结](https://www.cnblogs.com/erosion2020/p/18561454)
>
> [Java 安全基础之字节码操作框架 ASM 学习](https://xz.aliyun.com/news/12777)

ASM 通常涉及 `ClassReader`读取字节码，`ClassWriter`生成修改后的字节码，中间通过`ClassVisitor`和`MethodVisitor`来访问和修改类结构。用户需要检查方法是否有@Log 注解，如果有的话，在方法前后插入日志代码。

下面是一个使用 ASM 库向已注解 `@Log` 的方法中添加日志信息的代码示例：

```java
// 定义注解
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Log {
    String value() default "";
}

// 应用注解
package com.example;
public class DemoClass {
    @Log
    public int calculate(int a, int b) {
        return a + b;
    }
}
```

然后使用 ASM 库来处理 `DemoClass.class` 文件，添加日志代码。

```java
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LogInjector {
    public static void main(String[] args) throws Exception {
        // 1. 读取原始类
        ClassReader cr = new ClassReader("com.example.DemoClass");
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

        // 2. 创建自定义 ClassVisitor
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                // 检查方法是否有 @Log 注解
                AnnotationNode logAnnotation = findLogAnnotation(cv, name, desc);
                if (logAnnotation != null) {
                    return new LoggingMethodVisitor(mv, name, desc);
                }
                return mv;
            }
        };

        cr.accept(cv, 0);
        byte[] modifiedClass = cw.toByteArray();

        // 3. 写入修改后的类
        try (FileOutputStream fos = new FileOutputStream("DemoClass.class")) {
            fos.write(modifiedClass);
        }
    }

    // 查找方法上的 @Log 注解
    private static AnnotationNode findLogAnnotation(ClassVisitor cv, String name, String desc) {
        for (AnnotationNode an : ((MethodNode) cv.visitMethod(0, name, desc, null, null)).invisibleAnnotations) {
            if ("Lcom/example/Log;".equals(an.desc)) {
                return an;
            }
        }
        return null;
    }

    // 自定义 MethodVisitor 实现日志插入
    static class LoggingMethodVisitor extends MethodNode {
        private final String methodName;
        private final String methodDesc;

        public LoggingMethodVisitor(MethodVisitor mv, String name, String desc) {
            super(Opcodes.ASM9);
            this.methodName = name;
            this.methodDesc = desc;
            mv.accept(this);
        }

        @Override
        public void visitCode() {
            // 插入方法入口日志
            mv.visitLdcInsn("Entering method: " + methodName);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            // 记录参数（示例仅处理前两个参数）
            for (int i = 0; i < 2; i++) {
                mv.visitVarInsn(ILOAD, i); // 假设参数为 int 类型
                mv.visitLdcInsn("Param" + i);
                mv.visitMethodInsn(INVOKESTATIC, "com/example/LogUtil", "logParam", "(ILjava/lang/String;)V", false);
            }
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                // 插入方法出口日志
                mv.visitLdcInsn("Exiting method: " + methodName);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
```

## 反射提取 RUNTIME 注解信息

在运行时，每个对象都有对应的 `Class` 对象，持有定义该对象类的相关信息，其中也包含了该类的注解信息。

通常注解可以应用于类、方法、字段、参数、构造器等，所以在 `Class / Field / Method / Constructor` 类中都有同样的的方法来获取注解信息。

- `public Annotation[] getAnnotations()` 方法可以获取当前元素上所有注解，包括继承的注解。
- `public Annotation[] getDeclaredAnnotations()` 方法可以获取在当前元素上直接声明的注解，忽略继承的注解。
- `public <A extends Annotation> A getAnnotation(Class<A> annotationClass)` 方法可以获取当前元素上的指定类型的注解。
- `public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)` 方法可以判断当前元素是否有指定类型的注解。

对于 `Method` 和 `Constructor` 类，还可以获取方法中参数的注解信息。

- `public Annotation[][] getParameterAnnotations()` 方法可以获取方法的参数的注解信息，返回一个二维数组，每个元素是一个参数的注解数组，因为一个参数可以同时被多个注解标注。

其中 `Annotation` 接口由 `java.lang.annotation` 包提供，定义如下

```java
public interface Annotation {
  boolean equals(Object obj);
  int hashCode();
  String toString();
  // 返回真正的注解类
  Class<? extends Annotation> annotationType();
}
```

代码示例，通过反射获取注解信息

```java
// 定义注解
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface QueryParam {
    String value();
}

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@interface DefaultValue {
    String value() default "";
}


public class GetMethodParameterAnnotationsDemo {

    // 使用注解，标识方法的参数
    public void hello(
        @QueryParam("action") String action,
        @QueryParam("sort") @DefaultValue("asc") String sort
    ){
      System.out.println("hello, " + action + ", sort: " + sort);
    }

    public static void main(String[] args) throws Exception {
        // 利用反射获取注解信息
        Class<? > cls = GetMethodParameterAnnotationsDemo.class;
        Method method = cls.getMethod("hello", new Class[]{String.class, String.class}); // hello 方法的两个参数类型是 String

        // 方法参数的注解信息返回一个二维数组
        Annotation[][] annts = method.getParameterAnnotations();

        for(int i=0; i<annts.length; i++){
            System.out.println("annotations for paramter " + (i+1));

            // 取出当个参数的注解信息，也是一个数组，因为一个参数可以被多个注解标注，比如上述的 sort 参数同时被 @QueryParam 和 @DefaultValue 标注
            Annotation[] anntArr = annts[i];

            // 遍历单个参数的每个注解信息
            for(Annotation annt : anntArr){
                if(annt instanceof QueryParam){
                    // 强制类型转换
                    QueryParam qp = (QueryParam)annt;

                    // 每个注解都隐式继承了 java.lang.annotation.Annotation 接口，该接口提供了 annotationType 方法获取定义的注解类
                    // qp.value 方法是定义注解时声明的元素 value 属性，返回 String 类型
                    System.out.println(qp.annotationType()
                              .getSimpleName()+":"+ qp.value());
                }else if(annt instanceof DefaultValue){
                    DefaultValue dv = (DefaultValue)annt;
                    System.out.println(dv.annotationType()
                          .getSimpleName()+":"+ dv.value());
                }
            }
        }
    }
}
```

## 注解的使用场景

注解的使用场景

- 编译时检查：通过注解可以在编译时检查代码是否符合规范，例如检查方法参数是否正确、是否存在重复的注解等，通常用于一些验证框架。
- 运行时处理：通过注解可以在运行时获取类、方法、字段等的注解信息，根据注解信息进行动态处理，例如根据注解生成文档、根据注解实现依赖注入等。
- 代码生成：通过注解可以在编译时根据注解信息生成额外的代码，例如根据注解生成数据库表、根据注解生成 RESTful API 等。

实际应用框架，比如 Spring、MyBatis 等，都使用了注解来简化配置和开发。

## 总结

注解提升了 Java 语言的表达能力，有效地实现了应用功能和底层功能的分离，框架/库的程序员可以专注于底层实现，借助反射实现通用功能，提供注解给应用程序员使用，应用程序员可以专注于应用功能，通过简单的声明式注解与框架/库进行协作。

Java 中还有一种更为动态灵活的机制：[动态代理](./oop_proxy.md)。
