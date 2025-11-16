# 随机数 Random

`java.util.Random` 类提供了生成伪随机数的功能，可以产生不同类型的随机数，包括 boolean、int、long、float 和 double 等。

## 创建 Random 对象

```java
// 使用默认种子（基于当前时间）
Random random = new Random();

// 指定种子
Random randomWithSeed = new Random(12345L);

// 使用SecureRandom（更安全，用于密码学）
SecureRandom secureRandom = new SecureRandom();
SecureRandom secureRandomWithSeed = new SecureRandom(new byte[]{1, 2, 3, 4});
```

生成基本类型的随机数

## 生成 boolean 值

```java
Random random = new Random();
boolean randomBool = random.nextBoolean();  // true 或 false
```

## 生成 int 值

```java
// 生成任意int值（包括负数）
int randomInt = random.nextInt();           // [-2^31, 2^31-1]

// 生成0到指定值的int值（不包括指定值）
int randomIntInRange = random.nextInt(10);  // [0, 9]

// 生成指定范围的int值
int randomIntBetween(int min, int max) {
    return random.nextInt(max - min + 1) + min;
}
```

## 生成 long 值

```java
// 生成任意long值
long randomLong = random.nextLong();         // [-2^63, 2^63-1]

// 生成指定范围的long值
long randomLongBetween(long min, long max) {
    return min + (long) (random.nextDouble() * (max - min));
}
```

## 生成 float 值

```java
// 生成0.0到1.0之间的float值
float randomFloat = random.nextFloat();      // [0.0, 1.0)

// 生成指定范围的float值
float randomFloatBetween(float min, float max) {
    return min + random.nextFloat() * (max - min);
}
```

## 生成 double 值

```java
// 生成0.0到1.0之间的double值
double randomDouble = random.nextDouble();   // [0.0, 1.0)

// 生成指定范围的double值
double randomDoubleBetween(double min, double max) {
    return min + random.nextDouble() * (max - min);
}
```

## 生成 byte 数组

```java
// 生成指定长度的随机字节数组
byte[] randomBytes = new byte[10];
random.nextBytes(randomBytes);               // 填充数组
```

## 使用 ThreadLocalRandom（推荐）

从 Java 7 开始，推荐使用`ThreadLocalRandom`而不是`Random`，因为它在多线程环境中性能更好。

```java
import java.util.concurrent.ThreadLocalRandom;

// 生成int值
int randomInt = ThreadLocalRandom.current().nextInt();           // 任意int值
int randomIntInRange = ThreadLocalRandom.current().nextInt(10);  // [0, 9]

// 生成long值
long randomLong = ThreadLocalRandom.current().nextLong();
long randomLongInRange = ThreadLocalRandom.current().nextLong(1, 100);

// 生成boolean值
boolean randomBool = ThreadLocalRandom.current().nextBoolean();

// 生成float和double值
float randomFloat = ThreadLocalRandom.current().nextFloat();
double randomDouble = ThreadLocalRandom.current().nextDouble();

// 生成指定范围的double值
double randomDoubleInRange = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
```

常用随机数生成方法

## 随机索引

```java
// 从数组中随机选择一个元素
public static <T> T randomChoice(T[] array) {
    return array[ThreadLocalRandom.current().nextInt(array.length)];
}

// 从列表中随机选择一个元素
public static <T> T randomChoice(List<T> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
}
```

## 随机字符串

```java
// 生成指定长度的随机字符串
public static String randomString(int length) {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
        int index = ThreadLocalRandom.current().nextInt(chars.length());
        sb.append(chars.charAt(index));
    }
    return sb.toString();
}
```

## 随机验证码

```java
// 生成随机验证码
public static String generateVerificationCode() {
    return String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
}
```

## 随机密码

```java
// 生成随机密码
public static String generatePassword(int length) {
    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lower = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";
    String symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    String allChars = upper + lower + digits + symbols;

    StringBuilder password = new StringBuilder();

    // 确保每种类型至少有一个字符
    password.append(upper.charAt(ThreadLocalRandom.current().nextInt(upper.length())));
    password.append(lower.charAt(ThreadLocalRandom.current().nextInt(lower.length())));
    password.append(digits.charAt(ThreadLocalRandom.current().nextInt(digits.length())));
    password.append(symbols.charAt(ThreadLocalRandom.current().nextInt(symbols.length())));

    // 填充剩余位置
    for (int i = 4; i < length; i++) {
        password.append(allChars.charAt(ThreadLocalRandom.current().nextInt(allChars.length())));
    }

    // 打乱顺序
    char[] chars = password.toString().toCharArray();
    for (int i = chars.length - 1; i > 0; i--) {
        int j = ThreadLocalRandom.current().nextInt(i + 1);
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    return new String(chars);
}
```

### 随机颜色

```java
// 生成随机颜色
public static Color randomColor() {
    int r = ThreadLocalRandom.current().nextInt(256);
    int g = ThreadLocalRandom.current().nextInt(256);
    int b = ThreadLocalRandom.current().nextInt(256);
    return new Color(r, g, b);
}

// 生成随机RGB值
public static int[] randomRGB() {
    return new int[] {
        ThreadLocalRandom.current().nextInt(256),
        ThreadLocalRandom.current().nextInt(256),
        ThreadLocalRandom.current().nextInt(256)
    };
}
```

### 高斯分布（正态分布）

```java
// 生成高斯分布的随机数
double gaussianValue = random.nextGaussian();  // 均值为0，标准差为1

// 生成指定均值和标准差的高斯分布
public static double gaussian(double mean, double stdDev) {
    return mean + stdDev * random.nextGaussian();
}

// 生成指定范围内的随机数（使用高斯分布）
public static double gaussianInRange(double min, double max) {
    double mean = (min + max) / 2;
    double stdDev = (max - min) / 6;  // 99.7%的值在范围内
    double value = mean + stdDev * random.nextGaussian();
    return Math.max(min, Math.min(max, value));
}
```

### 随机选择和洗牌

```java
// 随机选择n个不重复的元素
public static <T> List<T> randomSelection(List<T> list, int n) {
    List<T> shuffled = new ArrayList<>(list);
    Collections.shuffle(shuffled, ThreadLocalRandom.current());
    return shuffled.subList(0, Math.min(n, shuffled.size()));
}

// 使用Fisher-Yates算法洗牌
public static void shuffleArray(int[] array) {
    for (int i = array.length - 1; i > 0; i--) {
        int j = ThreadLocalRandom.current().nextInt(i + 1);
        // 交换元素
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

// 使用Collections.shuffle
public static void shuffleList(List<?> list) {
    Collections.shuffle(list, ThreadLocalRandom.current());
}
```

## 随机 UUID 简化版本

```java
// 生成随机UUID的简化版本
public static String generateSimpleUUID() {
    return String.format("%08x%08x",
        ThreadLocalRandom.current().nextLong(),
        ThreadLocalRandom.current().nextLong());
}
```

## 随机数种子

```java
// 固定种子用于测试（生成可重复的随机序列）
long seed = 12345L;
Random fixedRandom = new Random(seed);

// 多次运行会得到相同的结果
for (int i = 0; i < 5; i++) {
    System.out.println(fixedRandom.nextInt(100));
}

// 使用时间戳作为种子
Random timeBasedRandom = new Random(System.currentTimeMillis());
```

## 性能优化建议

1. **使用 ThreadLocalRandom**：在多线程环境中性能更好
2. **避免创建太多 Random 对象**：可以重用实例
3. **预先生成随机数**：如果需要大量随机数，可以批量生成
4. **使用 SecureRandom 时注意性能**：它比较慢，仅在需要安全性时使用

## 常见陷阱

1. **随机数种子**：相同的种子产生相同的随机序列
2. **边界问题**：nextInt(n)的范围是[0, n)，不包括 n
3. **线程安全**：Random 不是线程安全的，ThreadLocalRandom 是
4. **性能问题**：在循环中创建新的 Random 对象会影响性能
5. **加密安全性**：普通 Random 不安全用于加密，使用 SecureRandom

## 与 Math.random()的比较

```java
// Math.random()内部使用Random，生成[0.0, 1.0)的double值
double mathRandom = Math.random();

// 使用ThreadLocalRandom更高效
double threadLocalRandom = ThreadLocalRandom.current().nextDouble();

// 生成[0, 10)的随机整数
int random1 = (int) (Math.random() * 10);  // 旧方式
int random2 = ThreadLocalRandom.current().nextInt(10);  // 推荐方式
```

随机数在游戏开发、数据模拟、密码学、机器学习等场景中有广泛应用。正确选择随机数生成器和了解其特性对于应用程序的性能和安全性都很重要。
