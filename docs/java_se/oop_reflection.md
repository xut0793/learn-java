# 反射

> 本章节内容摘自《Java 编程的逻辑》 21.1

在常规的编码当中，我们一般操作数据的时候都是知道并且依赖于已知的数据类型（基本类型、类、接口、数组等），比如根据类型使用 new 创建对象，根据类型声明变量，根据类型访问对象的属性，调用对象的方法等。而且 Java 编译器也是根据类型进行代码检查编译的。这些我们都可以称为静态的编译时的行为。

在 Java 中，反射是指在运行时动态地获取类型信息，比如类的信息（属性、方法和构造函数）、接口信息等，根据这些动态获取到的信息进行操作（创建对象、访问属性、调用方法等）。

反射的入口是 `java.lang.Class`，它提供了获取类的信息和操作类的方法。

## Class 类

在前面[对象引用](./oop_reference.md)章节，提到内存区域有栈、堆、方法区等，其中栈内存用来存储局部变量和方法调用，堆内存用来存储对象实例，方法区用来存储类的信息、常量池、静态变量等。每个已经加载的类在方法区中都有一份类信息，每个实例对象都有指向它所属类信息的引用。

在 Java 语言实现中，保存类信息对应的类是 `java.lang.Class`。每个已经加载的类的信息都保存在 `Class` 类的一个实例中。

> 注意不是小写的 `class`，小写的`class` 是关键字，这个类是大写的 `Class`。

## 获取 Class 对象

获取一个类信息的 `Class` 实例对象有以下几种方式：

- 使用 `Class` 类的静态方法 `forName()` 来获取类的 `Class` 对象。
- 使用对象的 `getClass()` 方法来获取对象所属类的 `Class` 对象。
- 使用类字面常量来获取类的 `Class` 对象，例如 `MyClass.class`。

### `Class.forName()` 方法

`Class.forName()` 方法是 `Class` 类的一个静态方法，它的作用是根据**类的全限定名称**来获取对应的 `Class` 对象。

```java
try {
  Class<?> builtinCls = Class.forName("java.lang.String"); // 内置类名
  Class<?> customCls = Class.forName("com.example.MyClass"); // 自定义类名
} catch (ClassNotFoundException e) {
  // 注意：`forName()` 方法会抛出 `ClassNotFoundException` 异常，所以在使用时需要进行异常处理。
  e.printStackTrace();
}
```

### `obj.getClass()` 方法

`getClass()` 方法是根类 `Object` 类的一个方法，它的作用是获取对象所属类的 `Class` 对象。

```java
// getClass 的方法签名
public final native Class<?> getClass();
```

Class 是一个泛型类，有一个类型参数，但是并不知道具体的类型，所以使用 `Class<?>` 来表示。

```java
MyClass obj = new MyClass();
Class<MyClass> cls = obj.getClass();
```

特殊的数组，每个数组实例对象有对应的数组的类型的 `Class` 对象。多维数组，每个维度都有一个，比如一维数组有一个，二维数组另一个不同的类型。

```java
// 一维数组
int[] arr = new int[10];
Class<int[]> arrCls = arr.getClass();

// 二维数组
int[][] arr2d = new int[10][10];
Class<int[][]> arr2dCls = arr2d.getClass();
```

### `类名.class` 类字面量

如果类是一个具体的类或接口，我们可以使用 `class` 属性来获取类的 `Class` 对象。

```java
Class<MyClass> cls = MyClass.class; // 自定义类名
Class<Date> cls = Date.class; // 内置类
Class<Comparable> cls = Comparable.class; // 接口
```

基本类型对象没有 `getClass` 方法，但是也有对应的 `Class` 对象，我们可以使用 `class` 属性来获取，返回对应的包装类信息。

```java
Class<Integer> intCls = int.class;
Class<Byte> byteCls = byte.class;
Class<Character> charCls = char.class;
Class<Double> doubleCls = double.class;
Class<Boolean> booleanCls = boolean.class;
```

特别的 void 作为特殊的返回类型，也有对应的 `Class` 对象，我们可以使用 `class` 属性来获取，返回 `void.class`。

```java
Class<Void> voidCls = void.class;
```

检举类型也有对应的 `Class` 对象。

```java
enum Size {
  SMALL,
  MEDIUM,
  LARGE
}
Class<Size> sizeCls = Size.class;
```

获取到 `Class` 对象后，我们可以根据 `Class` 对象来获取类的信息，比如属性、方法、构造函数等，以及进行一些操作，比如创建对象等。

通常情况下，类字面量（class literal）更简单也更案例，效率也更高，更建议使用。

## 类的信息

通常一个类的信息包括类的名称、类的类型、类的修饰符、类的父类、类的接口、类的构造方法、类的字段、类的方法等。

## 类的名称

Class 有几个方法可以获取类的名称：

- `getName()`：返回类的全限定名称，包括包名。
- `getSimpleName()`：返回类的简单名称，不包括包名。
- `getCanonicalName()`：返回类的规范名称，与 `getName()` 相同，但是如果类是一个数组类型，返回的是数组的元素类型的名称，而不是数组的类型名称。
- `getPackage()`：返回类所属的包信息。

不同方法的返回值对比：

| Class 对象        | getName                 | getSimpleName | getCanonicalName        | getPackage  |
| ----------------- | ----------------------- | ------------- | ----------------------- | ----------- |
| `int.class`       | `"int"`                 | `"int"`       | `"int"`                 | `null`      |
| `int[].class`     | `"[I"`                  | `"int[]"`     | `"int[]"`               | `null`      |
| `int[][].class`   | `"[[I"`                 | `"int[][]"`   | `"int[][]"`             | `null`      |
| `String.class`    | `"java.lang.String"`    | `"String"`    | `"java.lang.String"`    | `java.lang` |
| `String[].class`  | `"[Ljava.lang.String;"` | `"String[]"`  | `"java.lang.String[]"`  | `null`      |
| `HashMap.class`   | `"java.util.HashMap"`   | `"HashMap"`   | `"java.util.HashMap"`   | `java.util` |
| `Map.Entry.class` | `"java.util.Map$Entry"` | `"Entry"`     | `"java.util.Map.Entry"` | `java.util` |

需要特别注意的是数组的类型的返回值，使用前缀 `[` 来表示数组，有几个`[` 表示是几维数组，然后数组的类型是一个特定的大写字符表示，`I`表示 `int`，其它类型的表示字符包括`boolean(Z) / byte(B) / char(C) / double(D) / float(F) / long(J) / short(S)`。其中 `long` 区别下，因为 `L`被用于表示当前类型是类类型或接口类型，不是基本类型。

使用示例：

```java
Class<MyClass> cls = MyClass.class;
String name = cls.getName(); // "com.example.MyClass"
String simpleName = cls.getSimpleName(); // "MyClass"
String canonicalName = cls.getCanonicalName(); // "com.example.MyClass"
```

## 类的类型

Class 代表的类型即可以是普通的类，也可以是内部类，还可以是基本类型、数组、接口等。如果获取到一个给定的 Class 对象，它到底是什么类型？

可以通过 `Class ` 对象的下列方法来判断类的类型：

- `isPrimitive()`：判断类是否为基本类型。
- `isArray()`：判断类是否为数组。
- `isInterface()`：判断类是否为接口。
- `isAnonymousClass()`：判断类是否为匿名类。
- `isMemberClass()`：判断类是否为成员类。
- `isLocalClass()`：判断类是否为本地类。
- `isEnum()`：判断类是否为枚举。
- `isAnnotation()`：判断类是否为注解。
- `isRecord()`：判断类是否为记录。
- `isSealed()`：判断类是否为密封类。

代码示例

```java
Class<MyClass> cls = MyClass.class;
boolean isPrimitive = cls.isPrimitive(); // false
boolean isArray = cls.isArray(); // false
boolean isInterface = cls.isInterface(); // false
boolean isEnum = cls.isEnum(); // false
boolean isAnnotation = cls.isAnnotation(); // false
boolean isRecord = cls.isRecord(); // false
boolean isSealed = cls.isSealed(); // false
```

## 类的修饰符 `getModifiers()`

通过 `getModifiers()` 方法可以获取类的修饰符，返回一个 `int` 值。

我们可以通过 `java.lang.reflect.Modifier` 类的静态方法来判断修饰符的类型。

```java
import java.lang.reflect.Modifier;

Class<MyClass> cls = MyClass.class;
int modifiers = cls.getModifiers();

boolean isPublic = Modifier.isPublic(modifiers); // true
boolean isPrivate = Modifier.isPrivate(modifiers); // false
boolean isProtected = Modifier.isProtected(modifiers); // false
boolean isAbstract = Modifier.isAbstract(modifiers); // false
boolean isFinal = Modifier.isFinal(modifiers); // false
boolean isSealed = Modifier.isSealed(modifiers); // false
```

## 类的父类 `getSuperclass()`

通过 `getSuperclass()` 方法可以获取类的父类，返回一个 `Class` 对象。

```java
Class<MyClass> cls = MyClass.class;
Class<? super MyClass> superclass = cls.getSuperclass(); // java.lang.Object
```

## 字段信息 Field

类中定义的静态变量和实例变量都被称为字段（field），用 `java.lang.reflect.Field` 类表示。

Class 类提供了以下方法来获取字段信息：

- `getFields()`：获取类的所有公共字段，包括从父类继承的字段。
- `getDeclaredFields()`：获取类的所有字段，包括私有字段、受保护字段、默认字段和公共字段。
- `getField(String name)` 方法，用于获取指定名称的公共字段，包括从父类继承的字段。如果指定名称的字段不存在，会抛出 `NoSuchFieldException` 异常。
- `getDeclaredField(String name)` 方法，用于获取指定名称的字段，包括私有字段、受保护字段、默认字段和公共字段。如果指定名称的字段不存在，会抛出 `NoSuchFieldException` 异常。

获取到指定字段的对象后，我们可以通过 `Field` 类提供的方法来获取字段的具体信息，例如：

- `getName()`：获取字段的名称。
- `getType()`：获取字段的类型，返回一个 `Class` 对象。
- `getModifiers()`：获取字段的修饰符，返回一个 `int` 值。
- `isAccessible()`：判断字段是否可以被访问，返回一个 `boolean` 值。
- `setAccessible(boolean flag)`：设置字段的访问修饰符，改变字段的可访问性。
- `get(Object obj)`：获取指定对象的字段值。
- `set(Object obj, Object value)`：设置指定对象的字段值，如果当前字段是静态字段，则传入的 obj 可以是 null，设置当前静态字段的值。对于私有字段 private，需要先改变它的可访问性 `setAccessible(true)`关闭 java 的检查机制，再调用 set 设置值，否则将抛出非法访问异常（IllegalAccessException）。

代码示例，设置一个私有字段的值：

```java
import java.lang.reflect.Field;

class MyClass {
    private String privateField = "default value";
}

Class<MyClass> cls = MyClass.class;
Field field = cls.getDeclaredField("privateField");
field.setAccessible(true);
MyClass obj = new MyClass();
field.set(obj, "new value");
field.setAccessible(false);
String value = (String) field.get(obj);
```

对于字段修饰符信息，同上述类的修饰符信息获取和使用方式一样。

比如对于 `public static final int MAX_NAME_LEN = 255;` 这句，我们可以通过 `getModifiers()` 方法获取到字段的修饰符，然后使用 `Modifier` 类的静态方法来判断修饰符的类型。

```java
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class MyClass {
    public static final int MAX_NAME_LEN = 255;
}

Class<MyClass> cls = MyClass.class;
Field field = cls.getDeclaredField("MAX_NAME_LEN");
int modifiers = field.getModifiers();
boolean isPublic = Modifier.isPublic(modifiers); // true
boolean isStatic = Modifier.isStatic(modifiers); // true
boolean isFinal = Modifier.isFinal(modifiers); // true
```

## 方法信息 Method

类中定义的静态方法和实例方法都被称为方法（method），用 `java.lang.reflect.Method` 类表示。

Class 类提供了以下方法来获取方法信息：

- `getMethods()`：获取类的所有公共方法，包括从父类继承的方法。
- `getDeclaredMethods()`：获取类的所有方法，包括私有方法、受保护方法、默认方法和公共方法。
- `getMethod(String name, Class<?>... parameterTypes)`：获取指定名称和参数类型的公共方法，包括从父类继承的方法。如果指定名称和参数类型的方法不存在，会抛出 `NoSuchMethodException` 异常。
- `getDeclaredMethod(String name, Class<?>... parameterTypes)`：获取指定名称和参数类型的方法，包括私有方法、受保护方法、默认方法和公共方法。如果指定名称和参数类型的方法不存在，会抛出 `NoSuchMethodException` 异常。

获取到指定方法的对象后，我们可以通过 `Method` 类提供的方法来获取方法的具体信息。一个方法的声明通常包括修饰符、返回值类型、方法名、参数列表，以及方法调用。

- `getName()`：获取方法的名称。
- `getParameterTypes()`：获取方法的参数类型，返回一个 `Class` 对象数组。
- `getReturnType()`：获取方法的返回类型，返回一个 `Class` 对象。
- `getModifiers()`：获取方法的修饰符，返回一个 `int` 值。
- `isAccessible()`：判断方法是否可以被访问，返回一个 `boolean` 值。
- `setAccessible(boolean flag)`：设置方法的访问修饰符，改变方法的可访问性。
- `invoke(Object obj, Object... args)`：调用指定对象的方法，如果当前方法是静态方法，则传入的 obj 可以是 null，调用当前静态方法。如果当前方法没有参数，则 args 可以为 null，或者为空数组。如果调用抛出异常，会被包装为 `InvocationTargetException` 异常抛出。

代码示例，调用一个方法：

```java
import java.lang.reflect.Method;

class MyClass {
    public static void myMethod(String arg) {
        System.out.println("myMethod: " + arg);
    }
}

Class<MyClass> cls = MyClass.class;
Method method = cls.getDeclaredMethod("myMethod", cls);

try {
    method.invoke(null, "hello");
} catch (NoSuchMethodException e) {
    e.printStackTrace();
} catch (InvocationTargetException e) {
    e.printStackTrace();
} catch (IllegalAccessException e) {
    e.printStackTrace();
}
```

## 创建对象 newInstance

使用 `Class` 类的 `newInstance()` 方法可以创建类的实例。

```java
Class<MyClass> cls = MyClass.class;
MyClass obj = cls.newInstance();
```

但是 `newInstance()` 方法只能调用无参构造方法，如果类中不存在无参构造方法，会抛出 `InstantiationException` 异常。

如果类中存在有参构造方法，我们需要获取到类的构造方法，然后传入构造参数进行实例化对象。

## 构造方法 Constructor

Class 类提供了以下方法来获取构造方法信息：

- `getConstructors()`：获取类的所有公共 public 构造方法。
- `getDeclaredConstructors()`：获取类的所有构造方法，包括私有构造方法、受保护构造方法、默认构造方法和公共构造方法。
- `getConstructor(Class<?>... parameterTypes)`：获取指定参数类型的公共构造方法。如果指定参数类型的构造方法不存在，会抛出 `NoSuchMethodException` 异常。
- `getDeclaredConstructor(Class<?>... parameterTypes)`：获取指定参数类型的构造方法，包括私有构造方法、受保护构造方法、默认构造方法和公共构造方法。如果指定参数类型的构造方法不存在，会抛出 `NoSuchMethodException` 异常。

这些方法调用返回一个 `Constructor` 对象，我们可以通过 `Constructor` 类提供的方法 `newInstance` 传入构造参数来实例化对象。

代码示例，调用有参构造方法创建对象：

```java
import java.lang.reflect.Constructor;

class MyClass {
    public MyClass(String arg) {
        System.out.println("MyClass: " + arg);
    }
}

Class<MyClass> cls = MyClass.class;
Constructor<MyClass> constructor = cls.getDeclaredConstructor(String.class);
MyClass obj = constructor.newInstance("hello");
```

构造方法的其它声明信息，同方法一样。

## 类型检查 `isInstance(Object obj)`

通常情况下，我们在代码中直接使用 `instanceof` 运算符来检查一个对象是否是某个类的实例。

反射中，我们可以使用 `Class` 类的 `isInstance(Object obj)` 方法来检查一个对象是否是某个类的实例。

代码示例：

```java
MyClass obj = new MyClass("hello");


// instanceof 示例
if (obj instanceof MyClass) {
    System.out.println("obj is instance of MyClass");
}

// 动态类型使用 isInstance 方法
Class<MyClass> cls = MyClass.class;
boolean isInstance = cls.isInstance(obj); // true
```

## 类型转换 `cast(Object obj)`

除了类型判断，在程序中往往需要进行类型强制转换，反射中也提供了类型转换的方法。

`Class` 类的 `cast(Object obj)` 方法：将对象强制转换为指定的类类型。如果对象不是指定类的实例，会抛出 `ClassCastException` 异常。

代码示例：

```java
Class<Double> cls = Double.class;
Double castedObj = cls.cast(123); // 将整数 123 强制转为浮点数
```

更完案例的写法，通常是结合 `isInstance(Object obj)` 方法来判断对象是否是指定类的实例，然后再进行类型转换。

代码示例：

```java
Class<Double> cls = Double.class;
if (cls.isInstance(obj)) {
    Double castedObj = cls.cast(obj);
}
```

`isInstance` 和 `cast` 描述的都是类和实例对象之间的关系，`Class` 还有一个方法 `isAssignableFrom(Class<?> cls)`，用于判断类之间的关系，判断当前类是否是指定类的子类或实现类，或者说检查 cls 类型的变量能否赋值给当前类型的变量。

```java
Object.class.isAssignableFrom(String.class)
String.class.isAssignableFrom(String.class)
List.class.isAssignableFrom(ArrayList.class)
```

## 数组元素信息

除了获取数组的类信息，`Class`还提供了一个专门的方法，获取数组的元素类型。

```java
Class<int[]> cls = int[].class;
Class<?> componentType = cls.getComponentType(); // class java.lang.Integer
```

`java.lang.reflect` 包中也提供了 `Array` 类，用于操作数组类型。注意区别于 `java.util.Arrays` 提供的数组操作方法。

- `Array.newInstance(Class<?> componentType, int length)`：创建一个新的数组，组件类型为 `componentType`，长度为 `length`。
- `Array.getLength(Object array)`：返回数组的长度。
- `Array.get(Object array, int index)`：返回数组中指定索引位置的元素。
- `Array.set(Object array, int index, Object value)`：将数组中指定索引位置的元素设置为指定值。

## 枚举信息

枚举类型也有一个专门的方法，可以获取枚举的所有常量。

```java
Class<MyEnum> cls = MyEnum.class;
MyEnum[] constants = cls.getEnumConstants();
```

## 接口信息

Class 类的 `getInterfaces()` 方法可以获取类实现的所有接口。

```java
Class<MyClass> cls = MyClass.class;
Class<?>[] interfaces = cls.getInterfaces();
```

`java.lang.reflect.Interface` 接口表示类实现的接口。

- `getName()`：返回接口的名称。
- `getMethods()`：返回接口的所有公共 public 方法。
- `getDeclaredMethods()`：返回接口的所有方法，包括私有方法、受保护方法、默认方法和公共方法。

代码示例：

```java
class MyClass implements MyInterface {
    // 类的成员变量、方法等
}

interface MyInterface {
    // 接口的成员变量、方法等
}

Class<MyClass> cls = MyClass.class;
Class<?>[] interfaces = cls.getInterfaces();
for (Class<?> iface : interfaces) {
    System.out.println(iface.getName());
}
```

## 泛型信息

在前面介绍[泛型](./oop_generic.md)章节，提到泛型参数在运行时会被擦除，但是在类信息中依然有关于泛型的一些信息，可以通过反射获取到。

- `Class` 类的 `TypeVariable<Class<T>>[] getTypeParameters()` 方法可以获取类的泛型参数类型。
- `Constructor` 构造器有 `public Type[] getGenericParameterTypes()` 方法可以获取构造器的参数的泛型类型。
- `Field` 字段有 `public Type getGenericType()` 方法可以获取字段的泛型类型。
- `Method` 方法有如下方法获取相关泛型信息：
  - `public Type getGenericReturnType()` 方法可以获取方法的返回值的泛型类型。
  - `public Type[] getGenericParameterTypes()` 方法可以获取方法的参数的泛型类型。
  - `public Type[] getGenericExceptionTypes()` 方法可以获取方法声明抛出的异常的泛型类型。

其中 `Type` 是 `java.lang.reflect.Type` 接口定义，`Class` 实现了 `Type` 接口。

`java.lang.reflect.Type` 接口还定义了一些子接口，用于表示不同类型的泛型信息。

- `TypeVariable<T>`：表示类型变量，可以有上界，例如 `T`、`T extends Number` 等。
- `ParameterizedType`：表示参数化类型，有原始类型和具体实参类型，例如 `List<String>`、`Map<String, Integer>` 等。
- `GenericArrayType`：表示泛型数组类型，例如 `T[]`、`List<String>[]` 等。
- `WildcardType`：表示通配符类型，例如 `?`、`? extends Number`、`? super Integer` 等。

代码示例，通过反射获取泛型信息

```java
public class GenericDemo {
    static class GenericTest<U extends Comparable<U>, V> {
        U u;
        V v;
        List<String> list;
        public U test(List<? extends Number> numbers) {
            return null;
        }
    }
    public static void main(String[] args) throws Exception {
        Class<? > cls = GenericTest.class;
        //类的类型参数
        for(TypeVariable t : cls.getTypeParameters()) {
            System.out.println(t.getName() + " extends " +
                Arrays.toString(t.getBounds()));
        }
        //字段：泛型类型
        Field fu = cls.getDeclaredField("u");
        System.out.println(fu.getGenericType());
        //字段：参数化的类型
        Field flist = cls.getDeclaredField("list");
        Type listType = flist.getGenericType();
        if(listType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) listType;
            System.out.println("raw type: " + pType.getRawType()
                + ", type arguments:"
                + Arrays.toString(pType.getActualTypeArguments()));
        }
        //方法的泛型参数
        Method m = cls.getMethod("test", new Class[] { List.class });
        for(Type t : m.getGenericParameterTypes()) {
            System.out.println(t);
        }
    }
}
```

程序输出为：

```
U extends [java.lang.Comparable<U>]
V extends [class java.lang.Object]
U
raw type: interface java.util.List, type arguments:[class java.lang.String]
java.util.List<? extends java.lang.Number>
```

## 注解信息

通常注解可以应用于类、方法、字段、参数、构造器等。所以在 `Class / Field / Method / Constructor` 类中都有同样的的方法来获取注解信息。

- `public Annotation[] getAnnotations()` 方法可以获取当前元素上所有注解，包括继承的注解。
- `public Annotation[] getDeclaredAnnotations()` 方法可以获取在当前元素上直接声明的注解，忽略继承的注解。
- `public <A extends Annotation> A getAnnotation(Class<A> annotationClass)` 方法可以获取当前元素上的指定类型的注解。
- `public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass)` 方法可以判断当前元素是否有指定类型的注解。

对于 `Method` 和 `Constructor` 类，还可以获取方法中参数的注解信息。

- `public Annotation[][] getParameterAnnotations()` 方法可以获取方法的参数的注解信息，返回一个二维数组，每个元素是一个参数的注解数组，因为一个参数可以同时被多个注解标注。

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

`Annotation` 接口由 `java.lang.annotation` 包提供，定义如下

```java
public interface Annotation {
  boolean equals(Object obj);
  int hashCode();
  String toString();
  // 返回真正的注解类
  Class<? extends Annotation> annotationType();
}
```

## 反射的应用场景

- 动态代理
- 框架的设计与实现，如 Spring、Hibernate 等。
- 测试框架的设计与实现，如 JUnit、TestNG 等。
- 数据库操作框架的设计与实现，如 MyBatis、Hibernate 等。
- 序列化框架的设计与实现，如 Jackson、Gson 等。
- 日志框架的设计与实现，如 Log4j、Logback 等。

## 总结

反射虽然灵活，但是一般情况下，并不是优先建议使用。

1. 反射更容易出现运行时错误，使用显式的类和接口，编译器能帮我们做类型检查，减少错误，但使用反射，类型是运行时才知道的，编译器无能为力。
2. 反射的性能要低一些，在访问字段、调用方法前，反射先要查找对应的 Field/Method，要慢一些。

总得来说，如果能用接口实现同样的功能，就不要用反射。
