/**
* 这是一个经典的 Java 入门程序类。
* 该类的主要功能是在控制台输出 "Hello World!" 字符串，
* 常被用于演示 Java 程序的基本结构和运行方式。
*/
public class HelloWorld {
    /**
    * 程序的入口方法，JVM 在启动时会调用该方法来执行程序。
    * 此方法将在控制台输出 "Hello World!" 字符串。
    *
    * @param args 命令行参数，是一个字符串数组，在本程序中未被使用。
    */
    public static void main(String[] args) {
      /*
       * 声明变量 s 并赋值为 "World"
       */
      String s = "World";
      // 输出 "Hello World!" 字符串到控制台
      System.out.println("Hello " + s + "!");
    }
}