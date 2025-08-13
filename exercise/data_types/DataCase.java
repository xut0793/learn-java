package exercise.data_types;

public class DataCase {
  // #region implicitCast
  public static void implicitCast() {
    // ===============容量小的类型赋值给容量大的类型，会自动转换===============
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

    // =======================表达式中自动类型转换===================
    // 1. byte + short → 结果自动提升为int
    byte bt = 10;
    short st = 20;
    // short sum = bt + st; // 错误：bt + it的结果是int类型，不能直接赋值给byte
    int sum = bt + st; // 正确：自动提升为int
    System.out.println(sum); // 输出：30

    // 2. int + long → 结果自动提升为long
    int x = 100;
    long y = 200L;
    long result = x + y; // x自动转为long后计算
    System.out.println(result); // 输出：300

    // 3. float + double → 结果自动提升为double
    float f1 = 1.5f;
    double d1 = 2.5;
    double total = f1 + d1; // f1自动转为double后计算
    System.out.println(total); // 输出：4.0

    // 4. char + int → 结果自动提升为int
    char ch = 'A';
    int num1 = 10;
    int result2 = ch + num1; // ch自动转为int后计算
    System.out.println(result2); // 输出：75（'A'的Unicode值是65）
  }
  // #endregion implicitCast

  // #region explicitCast
  public static void explicitCast() {
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
  // #endregion explicitCast
}
