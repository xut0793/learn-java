import java.util.Arrays;

public class MyFirstJavaProgram {
    public static void main(String[] args) {
//        System.out.println("Hello World!");
        int a, b;
        final int c = 11;
        a = 5;
        b = a;
        a -= 3;
        a += 1;
        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }
}
/*
    Java 基础语法
    一、java 源文件
    - 一个源文件中只能有一个 public 类，可以有多个非 public 类
    - 源文件名必须和类名相同，否则会导致编译错误。并且文件名后缀为 .java，如 MyFirstJavaProgram.java
    - 主方法入口，所有 java 程序由 public static void main(String[] args) 方法开始执行
    - 类名的首字母应该大写，采用大驼峰命名法，如 MyFirstJavaProgram
    - 方法名首字母小写，采用小驼峰命名法，如 myFunction

    二、标识符
    类名、变量名、方法名都被称为标识符
    - 所有标识符应该以字母（A-Za-z）、美元符（$）、下划线开头
    - 除首字母外，其它字母可以是字母、美元符、下划线、以及数字组成
    - 标识符大小写敏感
    - java 程序的保留字不能用作标识符

    三、修饰符
    修饰类、方法、属性的关键字
    - 访问控制修饰符：default public protected private
    - 非访问控制修饰符：final abstract static synchronized volatile

    四、变量
    根据变量定义的位置不同，分为：
    - 局部变量，定义在方法或者块语句中，方法结束，变量就会自动销毁。
    - 成员变量：定义在类中，但在方法外，没有使用 static 修饰的变量
    - 静态变量：使用 static 修饰的变量

    五、常量
    常量就是一个固定值，声明的同时就初始化，并且不能再改变它的值。在 java 中用 final 修饰符标识
    final double PI = 3.1415927

    六、数据类型
    变量就是申请内存来存储值的位置标识，内存管理系统根据变量的类型为变量分配存储空间，分配的空间只能用来存储该类型的数据。
    - 基本数据类型（6种数字类型、1种字符类型、1种布尔类型）：byte short int long float double char boolean
    - 引用数据类型：对象 数组、字符串

    七、运算符
    - 算术运算符：+ - * / % ++ --
    - 关系运算符：== != > < >= <=
    - 位运算符：& | ^ ~ << >> >>>
    - 逻辑运算符: && || !
    - 赋值运算符: = += -= /= <<= >>= &= ^= |=
    - 其它运算符: 条件运算符（三元运算符）?:   instanceof

    八、控制流
    - 条件： if...else    switch
    - 循环：for while do...while
 */
