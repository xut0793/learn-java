# 数学 Math

`java.lang.Math` 类提供了用于执行基本数学运算的方法，如基本指数、对数、平方根和三角函数等。这是一个 final 类，所有方法都是静态的，无需实例化对象。

## 常用常量

```java
public static final double E = 2.7182818284590452354;      // 自然对数的底数
public static final double PI = 3.14159265358979323846;    // 圆周率
```

基本数学运算

## 绝对值

```java
Math.abs(-5);        // 5
Math.abs(-5.5);      // 5.5
Math.abs(Integer.MIN_VALUE);  // -2147483648 (注意整数的最小值)
```

## 取整运算

```java
// 向上取整
Math.ceil(4.2);      // 5.0
Math.ceil(-4.2);     // -4.0

// 向下取整
Math.floor(4.8);     // 4.0
Math.floor(-4.8);    // -5.0

// 四舍五入
Math.round(4.5);     // 5
Math.round(4.4);     // 4
Math.round(-4.5);    // -4 (注意负数的四舍五入规则)
```

## 最大值和最小值

```java
Math.max(10, 20);        // 20
Math.min(10, 20);        // 10
Math.max(Math.max(5, 8), 3);  // 8 (嵌套调用)
```

## 幂运算

```java
Math.pow(2, 3);      // 8.0 (2的3次方)
Math.pow(9, 0.5);    // 3.0 (9的0.5次方，即平方根)
Math.sqrt(16);       // 4.0 (平方根，更精确)
Math.cbrt(8);        // 2.0 (立方根)
```

## 开方运算

```java
Math.sqrt(16);       // 4.0
Math.cbrt(27);       // 3.0
```

## 三角函数

```java
// 角度转弧度
Math.toRadians(180);     // π (3.141592653589793)

// 弧度转角度
Math.toDegrees(Math.PI); // 180.0

// 三角函数
Math.sin(Math.PI/2);     // 1.0 (正弦)
Math.cos(0);             // 1.0 (余弦)
Math.tan(Math.PI/4);     // 1.0 (正切)

// 反三角函数
Math.asin(1.0);          // π/2 (反正弦)
Math.acos(1.0);          // 0.0 (反余弦)
Math.atan(1.0);          // π/4 (反正切)

// 双曲函数
Math.sinh(1.0);          // 双曲正弦
Math.cosh(1.0);          // 双曲余弦
Math.tanh(1.0);          // 双曲正切
```

## 对数和指数函数

```java
// 自然对数
Math.log(Math.E);        // 1.0
Math.log10(100);         // 2.0 (以10为底的对数)

// 指数函数
Math.exp(1);             // e^1 = 2.718281828459045
Math.expm1(1);           // e^1 - 1，更精确的小数值计算

// 指数和对数的精确计算
Math.log1p(Math.E - 1);  // log(1 + x)的精确计算
```

## 数值比较

```java
// 精确的浮点数比较
Math.equals(0.1 + 0.2, 0.3);           // false (二进制精度问题)

// 使用epsilon进行比较
boolean approximatelyEqual(double a, double b, double epsilon) {
    return Math.abs(a - b) <= epsilon;
}

approximatelyEqual(0.1 + 0.2, 0.3, 1e-10);  // true
```

## 其他实用方法

```java
// 符号函数
Math.signum(-5.5);      // -1.0 (符号函数)
Math.signum(5.5);       // 1.0
Math.signum(0);         // 0.0

// 精确的加法运算
Math.addExact(10, 20);      // 30
Math.addExact(Integer.MAX_VALUE, 1);  // 抛出ArithmeticException

// 精确的减法、乘法运算
Math.subtractExact(20, 10);     // 10
Math.multiplyExact(5, 6);       // 30

// 整数运算
Math.incrementExact(5);         // 6
Math.decrementExact(5);         // 4
Math.negateExact(5);            // -5

// 绝对值运算
Math.absExact(Integer.MIN_VALUE);  // 抛出ArithmeticException
```

## 随机数相关

```java
// 生成0.0到1.0之间的伪随机double数
Math.random();       // [0.0, 1.0)

// 生成指定范围的随机数
int randomInRange(int min, int max) {
    return (int) (Math.random() * (max - min + 1)) + min;
}
```

## 性能优化建议

1. **避免重复计算常量**：Math.PI 和 Math.E 已经做了优化，直接使用即可
2. **使用精确运算方法**：当涉及精确计算时，使用 addExact、multiplyExact 等方法
3. **避免不必要的类型转换**：选择合适的数据类型
4. **使用 Math.sqrt()而不是 Math.pow()进行平方根计算**

## 常见陷阱

1. **整除问题**：注意区分整数除法和浮点数除法
2. **精度问题**：浮点数的二进制表示可能导致精度损失
3. **溢出问题**：使用 exact 方法可以避免溢出但不声不响
4. **负数的四舍五入**：Math.round(-4.5)返回-4

## 实际应用示例

```java
// 计算两点之间的距离
public static double distance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
}

// 计算角度的三角函数值
public static double[] trigValues(double angleInDegrees) {
    double radians = Math.toRadians(angleInDegrees);
    return new double[] {
        Math.sin(radians),
        Math.cos(radians),
        Math.tan(radians)
    };
}

// 数值验证
public static boolean isValidTriangle(double a, double b, double c) {
    return a + b > c && a + c > b && b + c > a;
}
```
