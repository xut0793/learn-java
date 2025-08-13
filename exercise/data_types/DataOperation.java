package exercise.data_types;

public class DataOperation {
  // #region arithmeticOperators
  public static void arithmeticOperators() {
    int a = 10;
    int b = 3;

    // + 加法
    int sum = a + b;
    System.out.println("加法 a + b = " + sum); // 输出：13

    // - 减法
    int diff = a - b;
    System.out.println("减法 a - b = " + diff); // 输出：7

    // * 乘法
    int product = a * b;
    System.out.println("乘法 a * b = " + product); // 输出：30

    // / 除法
    int quotient = a / b;
    System.out.println("除法 a / b = " + quotient); // 输出：3

    // % 取余
    int remainder = a % b;
    System.out.println("取余 a % b = " + remainder); // 输出：1
  }
  // #endregion arithmeticOperators

  // #region comparisonOperators
  public static void comparisonOperators() {
    int a = 10;
    int b = 3;

    // == 等于
    boolean isEqual = a == b;
    System.out.println("a == b: " + isEqual); // 输出：false

    // != 不等于
    boolean isNotEqual = a != b;
    System.out.println("a != b: " + isNotEqual); // 输出：true

    // > 大于
    boolean isGreater = a > b;
    System.out.println("a > b: " + isGreater); // 输出：true

    // < 小于
    boolean isLess = a < b;
    System.out.println("a < b: " + isLess); // 输出：false

    // >= 大于等于
    boolean isGreaterEqual = a >= b;
    System.out.println("a >= b: " + isGreaterEqual); // 输出：true

    // <= 小于等于
    boolean isLessEqual = a <= b;
    System.out.println("a <= b: " + isLessEqual); // 输出：false
  }
  // #endregion comparisonOperators

  // #region logicalOperators
  public static void logicalOperators() {
    boolean a = true;
    boolean b = false;

    // && 与
    boolean and = a && b;
    System.out.println("a && b: " + and); // 输出：false

    // || 或
    boolean or = a || b;
    System.out.println("a || b: " + or); // 输出：true

    // ! 非
    boolean not = !a;
    System.out.println("!a: " + not); // 输出：false
  }
  // #endregion logicalOperators

  // #region ternaryOperators
  public static void ternaryOperators() {
    int a = 10;
    int b = 3;
    int c = 5;

    // 条件运算符
    int max = a > b ? a : b;
    System.out.println("a > b ? a : b: " + max); // 输出：10

    // 多个条件运算符
    int min = a < b ? a : (b < c ? b : c);
    System.out.println("a < b ? a : (b < c ? b : c): " + min); // 输出：3
  }
  // #endregion ternaryOperators

  // #region bitwiseOperators
  public static void bitwiseOperators() {
    int a = 5; // 二进制表示: 00000101
    int b = 3; // 二进制表示: 00000011

    // & 按位与
    int and = a & b; // 二进制结果: 00000001
    System.out.println("a & b: " + and); // 输出: 1

    // | 按位或
    int or = a | b; // 二进制结果: 00000111
    System.out.println("a | b: " + or); // 输出: 7

    // ^ 按位异或
    int xor = a ^ b; // 二进制结果: 00000110
    System.out.println("a ^ b: " + xor); // 输出: 6

    // ~ 按位非
    int not = ~a; // 二进制结果: 11111010
    System.out.println("~a: " + not); // 输出: -6

    // << 左移
    int leftShift = a << 2; // 二进制结果: 00010100
    System.out.println("a << 2: " + leftShift); // 输出: 20

    // >> 右移
    int rightShift = a >> 2; // 二进制结果: 00000001
    System.out.println("a >> 2: " + rightShift); // 输出: 1

    // >>> 无符号右移
    int unsignedRightShift = a >>> 2; // 二进制结果: 00000001
    System.out.println("a >>> 2: " + unsignedRightShift); // 输出: 1
    // 无符号右移会用0填充高位，而不是用符号位填充
    System.out.println("-1 >>> 2: " + (-1 >>> 2)); // 输出: 1073741823
    // 解释：-1的二进制表示为11111111111111111111111111111111
    // 无符号右移2位后，用0填充高位，得到00111111111111111111111111111111
    // 转换为十进制为1073741823
  }
  // #endregion bitwiseOperators

  // #region compoundOperators
  public static void compoundOperators() {
    int a = 10;
    int b = 3;

    // += 加等于
    a += b; // 相当于 a = a + b
    System.out.println("a += b: " + a); // 输出: 13

    // -= 减等于
    a -= b; // 相当于 a = a - b
    System.out.println("a -= b: " + a); // 输出: 10

    // *= 乘等于
    a *= b; // 相当于 a = a * b
    System.out.println("a *= b: " + a); // 输出: 30

    // /= 除等于
    a /= b; // 相当于 a = a / b
    System.out.println("a /= b: " + a); // 输出: 10

    // %= 取模等于
    a %= b; // 相当于 a = a % b
    System.out.println("a %= b: " + a); // 输出: 1

    // ++ 自增
    int c = 10;
    int c1 = c++; // 先赋值后自增
    System.out.println("c++: c = " + c + ", c1 = " + c1); // 输出: c = 11, c1 = 10

    // ++ 自增
    int d = 10;
    int d2 = ++d; // 先自增后赋值
    System.out.println("++d: d = " + d + ", d2 = " + d2); // 输出: d = 11, d2 = 11

    // -- 自减
    int f = 10;
    int f1 = f--; // 先赋值后自减
    System.out.println("f--: f = " + f + ", f1 = " + f1); // 输出: f = 9, f1 = 10

    // -- 自减
    int e = 10;
    int e1 = --e; // 先自减后赋值
    System.out.println("--e: e = " + e + ", e1 = " + e1); // 输出: e = 9, e1 = 9
  }
  // #endregion compoundOperators

  // #region operatorPriority
  public static void operatorPriority() {
    int a = 10;
    int b = 3;
    int c = 5;

    // 先计算 a + b，再计算 a + b 的结果与 c 的乘积
    int result = (a + b) * c;
    System.out.println("(a + b) * c: " + result); // 输出: 80

    // 先计算 a * c，再计算 a * c 的结果与 b 的乘积
    int result2 = a * c + b;
    System.out.println("a * c + b: " + result2); // 输出: 53

    // 三元运算符，多个运算表达式优先级
    int max = a > b + c ? a : (b + c);
    System.out.println("a > b + c ? a : (b + c): " + max); // 输出: 10
  }
  // #endregion operatorPriority
}