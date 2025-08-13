# 数据转换

## 自动类型转换，也叫隐式类型转换

在 Java 中，基础类型的隐式转换（自动类型转换）是指编译器自动将一种数据类型转换为另一种数据类型，无需手动干预。这种转换的前提是：目标类型的取值范围大于源类型（或兼容），以保证数据不丢失。

- 自动类型转换：编译器会自动将一种数据类型转换为另一种数据类型，无需手动干预。
- 自动类型转换的规则：
  - 容量小的类型赋值给容量大的类型，会自动转换。
  - byte、short、char 类型在进行运算时，会先转换为 int 类型。java 中数值运算的最小类型是 int 类型。
  - 表达式运算时，参与运算的类型会自动转换为最大的类型，再进行计算。
  - 整型（byte、short、int、long）赋值给浮点型（float、double），会自动转换。
  - char 可以自动转为 int、long 等，但 byte/short 不能自动转为 char（因为 char 是无符号类型，范围不兼容）。
  - boolean 不能与任何其他基础类型进行隐式转换（包括强制转换），这是 Java 的语法规定。

隐式转换的核心原则是「安全转换」—— 确保转换后的数据不会丢失或溢出，编译器会自动完成这类转换。

```java
public class ImplicitCast {
    public static void main(String[] args) {
        //===============容量小的类型赋值给容量大的类型，会自动转换===============
        // 1. byte → int（小范围→大范围）
        byte b = 100;
        int i = b; // 自动转换，无需强制
        System.out.println(i); // 输出：100

        // 2. int → long
        int num = 200;
        long bigNum = num; // 自动转换
        System.out.println(bigNum); // 输出：200

        // 3. long → float（float范围比long大，可自动转换）
        long l = 123456L;
        float f = l; // 自动转换
        System.out.println(f); // 输出：123456.0

        // 4. float → double
        float pi = 3.14f;
        double precisePi = pi; // 自动转换
        System.out.println(precisePi); // 输出：3.14

        // 5. char → int（char的Unicode值自动转为int）
        char c = 'A'; // 'A'的Unicode值是65
        int code = c; // 自动转换
        System.out.println(code); // 输出：65

        //=======================表达式中自动类型转换===================
        // 1. byte + int → 结果自动提升为int
        byte a = 10;
        int b = 20;
        // byte sum = a + b; // 错误：a + b的结果是int类型，不能直接赋值给byte
        int sum = a + b; // 正确：自动提升为int
        System.out.println(sum); // 输出：30

        // 2. int + long → 结果自动提升为long
        int x = 100;
        long y = 200L;
        long result = x + y; // x自动转为long后计算
        System.out.println(result); // 输出：300

        // 3. float + double → 结果自动提升为double
        float f = 1.5f;
        double d = 2.5;
        double total = f + d; // f自动转为double后计算
        System.out.println(total); // 输出：4.0
    }
}
```

## 强制类型转换，也叫显式类型转换

- 强制类型转换：将一个数据类型强制转换为另一个数据类型，需要强制类型转换。
- 强制类型转换的语法：`(目标数据类型) 变量名`
- 注意事项：容量大的类型强制转换为容量小的类型，会丢失数据。

```java
public class PrimitiveCast {
    public static void main(String[] args) {
        // 1. 浮点型转整型（可能丢失小数部分）
        double num1 = 3.14;
        int num2 = (int) num1; // 强制转换为int
        System.out.println(num2); // 输出：3（小数部分被截断）

        // 2. 大整数类型转小整数类型（可能溢出）
        long bigNum = 12345678901L;
        int smallNum = (int) bigNum; // 强制转换为int
        System.out.println(smallNum); // 输出：-539222987（超出int范围导致溢出）

        // 3. char与int互转（char本质是Unicode编码）
        char c = 'A';
        int code = (int) c; // char转int（获取ASCII码）
        System.out.println(code); // 输出：65

        int newCode = 66;
        char newChar = (char) newCode; // int转char（根据编码获取字符）
        System.out.println(newChar); // 输出：B
    }
}
```

> 这里主要描述基础类型（隐式转换和显式转换），对于基本类型的装箱和拆箱，以及引用类型的查看、判断、向上转型、向下转型等概念在面向对象章节讲解。
