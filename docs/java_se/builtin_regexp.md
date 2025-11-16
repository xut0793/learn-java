# 正则表达式 RegExp

## 什么是正则表达式？

正则表达式（Regular Expression，简称 RegExp 或 Regex）是一种用于匹配字符串中字符组合的模式。

在 Java 中，正则表达式是一个用特殊语法编写的字符串，是用于处理字符串的有力工具。

以下是正则表达式处理字符串的常用场景：

- **字符串匹配**：检查一个字符串是否符合特定的模式（如邮箱、电话号码、URL 等）
- **字符串查找**：在文本中查找符合特定模式的子字符串
- **字符串替换**：将文本中符合特定模式的部分替换为其他内容
- **字符串分割**：根据特定模式将字符串分割成多个部分
- **数据验证**：验证用户输入是否符合预期格式

## Java 中的正则表达式支持

Java 提供了 `java.util.regex` 包来支持正则表达式操作，其中包含两个核心类：

- `Pattern`：表示编译后的正则表达式
- `Matcher`：用于执行匹配操作的引擎

此外，`String` 类也提供了多个便捷方法来直接使用正则表达式。

## 正则表达式基本语法

Java 的 `java.util.regex` 包提供了两个核心类：`Pattern` 和 `Matcher`，用于更灵活和强大的正则表达式操作。

### Pattern 类

`Pattern` 类表示编译后的正则表达式模式。由于正则表达式编译是一个耗时的过程，使用 `Pattern` 类可以避免重复编译，提高性能，特别是在多次使用同一正则表达式时。

```java
String regex = "\\d+";
Pattern pattern = Pattern.compile(regex);
```

#### 主要方法

| 方法                                   | 描述                                     | 返回值     |
| -------------------------------------- | ---------------------------------------- | ---------- |
| `compile(String regex)`                | 将正则表达式编译成 Pattern 对象          | `Pattern`  |
| `compile(String regex, int flags)`     | 使用指定的标志编译正则表达式             | `Pattern`  |
| `matcher(CharSequence input)`          | 创建一个 Matcher 对象来匹配输入序列      | `Matcher`  |
| `split(CharSequence input)`            | 根据正则表达式分割输入序列               | `String[]` |
| `split(CharSequence input, int limit)` | 根据正则表达式分割输入序列，限制分割次数 | `String[]` |
| `pattern()`                            | 返回编译此模式的正则表达式               | `String`   |

#### 常用标志

| 标志               | 描述                                         |
| ------------------ | -------------------------------------------- |
| `CASE_INSENSITIVE` | 忽略大小写                                   |
| `MULTILINE`        | 多行模式，使 `^` 和 `$` 匹配每行的开始和结束 |
| `DOTALL`           | 使 `.` 匹配任何字符，包括换行符              |
| `UNICODE_CASE`     | 忽略大小写，考虑 Unicode 字符                |
| `CANON_EQ`         | 启用规范等价                                 |

### Matcher 类

`Matcher` 类提供了对正则表达式匹配操作的支持，它可以在字符序列中执行各种匹配操作。

```java
String input = "12345";
Matcher matcher = pattern.matcher(input);
```

#### 主要方法

| 方法                               | 描述                                       | 返回值    |
| ---------------------------------- | ------------------------------------------ | --------- |
| `matches()`                        | 尝试将整个输入序列与模式匹配               | `boolean` |
| `lookingAt()`                      | 尝试从输入序列的开头匹配模式               | `boolean` |
| `find()`                           | 尝试查找输入序列中与模式匹配的下一个子序列 | `boolean` |
| `find(int start)`                  | 从指定位置开始查找                         | `boolean` |
| `group()`                          | 返回最后一次匹配的子序列                   | `String`  |
| `group(int group)`                 | 返回指定捕获组匹配的子序列                 | `String`  |
| `groupCount()`                     | 返回此匹配器模式中的捕获组数量             | `int`     |
| `start()`                          | 返回最后一次匹配的起始索引                 | `int`     |
| `start(int group)`                 | 返回指定捕获组匹配的起始索引               | `int`     |
| `end()`                            | 返回最后一次匹配的结束索引（不包含）       | `int`     |
| `end(int group)`                   | 返回指定捕获组匹配的结束索引（不包含）     | `int`     |
| `replaceAll(String replacement)`   | 替换所有匹配的子序列                       | `String`  |
| `replaceFirst(String replacement)` | 替换第一个匹配的子序列                     | `String`  |
| `reset()`                          | 重置匹配器，放弃当前的匹配状态             | `Matcher` |
| `reset(CharSequence input)`        | 使用新的输入序列重置匹配器                 | `Matcher` |

## 使用示例

1. 基本匹配操作

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#baseRegexp

1. 使用捕获组

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#captureGroup

1. 使用标志

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#patternFlags

1. 高级替换操作

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#advancedReplace

## 正则表达式基本规则

### 字符类

字符类用于匹配指定集合中的任意一个字符：

| 语法     | 描述                                          | 示例                           |
| -------- | --------------------------------------------- | ------------------------------ |
| `[abc]`  | 匹配方括号中的任意一个字符                    | `[aeiou]` 匹配任意一个元音字母 |
| `[^abc]` | 匹配除方括号中字符外的任意一个字符            | `[^0-9]` 匹配任意非数字字符    |
| `[a-z]`  | 匹配指定范围内的任意一个字符                  | `[a-zA-Z]` 匹配任意字母        |
| `.`      | 匹配除换行符外的任意一个字符                  | `a.c` 匹配 abc、adc 等         |
| `\d`     | 匹配任意数字字符，等价于 `[0-9]`              | `\d+` 匹配一个或多个数字       |
| `\D`     | 匹配任意非数字字符，等价于 `[^0-9]`           | `\D+` 匹配一个或多个非数字     |
| `\w`     | 匹配字母、数字、下划线，等价于 `[a-zA-Z_0-9]` | `\w+` 匹配一个或多个单词字符   |
| `\W`     | 匹配非单词字符，等价于 `[^a-zA-Z_0-9]`        | `\W+` 匹配一个或多个非单词字符 |
| `\s`     | 匹配任意空白字符（空格、制表符、换行符等）    | `\s+` 匹配一个或多个空白字符   |
| `\S`     | 匹配任意非空白字符                            | `\S+` 匹配一个或多个非空白字符 |

### 量词

量词用于指定模式出现的次数：

| 语法     | 描述                        | 示例                            |
| -------- | --------------------------- | ------------------------------- |
| `X?`     | 匹配 X 零次或一次           | `colou?r` 匹配 color 或 colour  |
| `X*`     | 匹配 X 零次或多次           | `a*b` 匹配 b、ab、aab 等        |
| `X+`     | 匹配 X 一次或多次           | `a+b` 匹配 ab、aab 等，不匹配 b |
| `X{n}`   | 匹配 X 恰好 n 次            | `a{3}` 匹配 aaa                 |
| `X{n,}`  | 匹配 X 至少 n 次            | `a{2,}` 匹配 aa、aaa、aaaa 等   |
| `X{n,m}` | 匹配 X 至少 n 次，最多 m 次 | `a{2,4}` 匹配 aa、aaa、aaaa     |

默认情况下，量词是贪婪的（尽可能匹配多的字符）。要使量词非贪婪，可以在量词后添加 `?`，如 `X*?`、`X+?`。

### 边界匹配器

边界匹配器用于匹配字符串的边界：

| 语法 | 描述             | 示例                                    |
| ---- | ---------------- | --------------------------------------- |
| `^`  | 匹配字符串的开始 | `^Hello` 匹配以 Hello 开头的字符串      |
| `$`  | 匹配字符串的结束 | `world$` 匹配以 world 结尾的字符串      |
| `\b` | 匹配单词边界     | `\bcat\b` 匹配单词 cat，不匹配 category |
| `\B` | 匹配非单词边界   | `\Bcat\B` 匹配字符串中但不是单词的 cat  |

### 转义字符

在正则表达式中，某些字符具有特殊含义。要匹配这些字符本身，需要使用反斜杠 `\` 进行转义：

| 特殊字符 | 转义方式 | 示例                                 |
| -------- | -------- | ------------------------------------ |
| `.`      | `\.`     | `a\.b` 匹配 a.b，而不是 a 任意字符 b |
| `*`      | `\*`     | `a\*b` 匹配 `a*b`                    |
| `+`      | `\+`     | `a\+b` 匹配 `a+b`                    |
| `(`      | `\(`     | `\(abc\)` 匹配 `(abc)`               |
| `)`      | `\)`     | `\(abc\)` 匹配 `(abc)`               |
| `[`      | `\[`     | `\[abc\]` 匹配 `[abc]`               |
| `]`      | `\]`     | `\[abc\]` 匹配 `[abc]`               |
| `{`      | `\{`     | `\{1,3\}` 匹配 `{1,3}`               |
| `}`      | `\}`     | `\{1,3\}` 匹配 `{1,3}`               |
| `?`      | `\?`     | `a\?b` 匹配 `a?b`                    |
| `^`      | `\^`     | `\^abc` 匹配 `^abc`                  |
| `$`      | `\$`     | `abc\$` 匹配 `abc$`                  |

注意：在 Java 字符串中，反斜杠 `\` 本身需要转义，因此实际编写时需要使用两个反斜杠 `\\`，例如 `"a\\.b"` 表示匹配 `a.b`。

### 分组和捕获

分组用于将多个字符作为一个整体处理，并可以捕获匹配的内容：

| 语法    | 描述                              | 示例                                                        |
| ------- | --------------------------------- | ----------------------------------------------------------- |
| `(X)`   | 捕获组，捕获匹配 X 的内容         | `(ab)+` 匹配 ab、abab 等，并捕获 ab                         |
| `(?:X)` | 非捕获组，不捕获匹配 X 的内容     | `(?:ab)+` 匹配 ab、abab 等，不捕获内容                      |
| `(?>X)` | 独立捕获组，不允许回溯            | `(?>=\d+)(\d)` 如果前一个组匹配所有数字，第二个组将无法匹配 |
| `(?=X)` | 正向前瞻，匹配后面跟着 X 的位置   | `a(?=b)` 匹配后面跟着 b 的 a                                |
| `(?!X)` | 负向前瞻，匹配后面不跟着 X 的位置 | `a(?!b)` 匹配后面不跟着 b 的 a                              |

### 特殊构造

| 语法 | 描述   | 示例                   |
| ---- | ------ | ---------------------- |
| `\n` | 换行符 | `a\nb` 匹配 a 换行 b   |
| `\t` | 制表符 | `a\tb` 匹配 a 制表符 b |
| `\r` | 回车符 | `a\rb` 匹配 a 回车 b   |
| `\f` | 换页符 | `a\fb` 匹配 a 换页 b   |

## String 类中使用正则表达式

Java 的 `String` 类提供了多个便捷方法来直接使用正则表达式，无需显式创建 `Pattern` 和 `Matcher` 对象。

1. `matches()` 方法
2. `split()` 方法
3. `replaceAll()` 方法
4. `replaceFirst()` 方法

### 1. matches() 方法

`matches()` 方法用于检查整个字符串是否匹配给定的正则表达式。

**语法：**

```java
boolean matches(String regex)
```

**示例：**

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#stringMatches

**注意：** `matches()` 方法会尝试匹配整个字符串，而不是查找子字符串。如果需要查找子字符串，请使用 `Pattern` 和 `Matcher` 类。

### 2. split() 方法

`split()` 方法用于根据给定的正则表达式将字符串分割成字符串数组。

**语法：**

```java
String[] split(String regex)
String[] split(String regex, int limit)
```

**示例：**

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#stringSplit

### 3. replaceAll() 方法

`replaceAll()` 方法用于将匹配给定正则表达式的所有子字符串替换为指定的替换字符串。

**语法：**

```java
// `regex` - 要匹配的正则表达式
//  `replacement` - 替换字符串
String replaceAll(String regex, String replacement)
```

**示例：**

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#stringReplaceAll

**注意：** 在替换字符串中，可以使用 `$n` 引用第 n 个捕获组的内容（n 从 1 开始）。

### 4. replaceFirst() 方法

`replaceFirst()` 方法用于将匹配给定正则表达式的第一个子字符串替换为指定的替换字符串。

**语法：**

```java
// `regex` - 要匹配的正则表达式
// `replacement` - 替换字符串
String replaceFirst(String regex, String replacement)
```

**示例：**

<<< ../../learnjava/src/com/learnjava/builtin/regexp/RegexpExample.java#stringReplaceFirst

## Pattern 和 Matcher 与 String 方法的区别

虽然 `String` 类提供了便捷的正则表达式方法，但 `Pattern` 和 `Matcher` 类提供了更强大和灵活的功能：

1. **性能**：`Pattern` 对象可以重复使用，避免重复编译正则表达式
2. **更多操作**：`Matcher` 提供了查找子序列、获取捕获组、定位匹配位置等功能
3. **更灵活的替换**：可以使用 `appendReplacement()` 和 `appendTail()` 进行复杂的替换操作
4. **控制匹配过程**：可以从指定位置开始匹配，或者重置匹配器状态

在处理复杂的正则表达式操作或需要重复使用同一个正则表达式时，推荐使用 `Pattern` 和 `Matcher` 类。

## 常见正则表达式模式

### 1. 验证邮箱

```java
String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
```

### 2. 验证手机号码（中国大陆）

```java
String phoneRegex = "^1[3-9]\\d{9}$";
```

### 3. 验证 URL

```java
String urlRegex = "^(https?|ftp):\\/\\/[\\w\\.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$";
```

### 4. 验证身份证号（中国大陆）

```java
String idCardRegex = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";
```

### 5. 验证日期格式（YYYY-MM-DD）

```java
String dateRegex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
```

### 6. 提取 HTML 标签

```java
String htmlTagRegex = "<([a-zA-Z][a-zA-Z0-9]*)\\b[^>]*>(.*?)</\\1>";
```

## 正则表达式最佳实践

1. **预编译正则表达式**：对于重复使用的正则表达式，使用 `Pattern.compile()` 预编译以提高性能

   ```java
   // 推荐做法
   private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

   public boolean isValidEmail(String email) {
       return EMAIL_PATTERN.matcher(email).matches();
   }
   ```

2. **使用非捕获组**：当不需要捕获匹配内容时，使用非捕获组 `(?:X)` 提高性能

   ```java
   // 不推荐
   Pattern pattern1 = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

   // 如果只需要整体匹配而不需要捕获组
   Pattern pattern2 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

   // 如果部分不需要捕获
   Pattern pattern3 = Pattern.compile("(\\d{4})-(?:\\d{2})-(\\d{2})");
   ```

3. **小心贪婪匹配**：默认情况下量词是贪婪的，可能导致性能问题

   ```java
   // 贪婪匹配，可能会匹配过多内容
   String greedyRegex = ".*\\.jpg";

   // 非贪婪匹配，尽可能少地匹配
   String lazyRegex = ".*?\\.jpg";
   ```

4. **注意转义字符**：在 Java 字符串中，反斜杠需要双重转义

   ```java
   // 正确的转义
   String regex = "\\d+\\.\\d+";

   // 错误的转义
   // String regex = "\d+\.\d+"; // 编译错误
   ```

5. **避免过度使用正则表达式**：对于简单的字符串操作，使用 `String` 类的基本方法可能更高效

   ```java
   // 对于简单的前缀检查，使用 startsWith() 更高效
   boolean isPrefixed = str.startsWith("prefix");

   // 对于简单的包含检查，使用 contains() 更高效
   boolean contains = str.contains("substring");
   ```

6. **使用标志简化表达式**：适当使用 `Pattern` 类的标志可以使正则表达式更简洁

   ```java
   // 使用 CASE_INSENSITIVE 标志
   Pattern pattern = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);

   // 等同于但比下面的表达式更简洁
   // Pattern pattern = Pattern.compile("[hH][eE][lL][lL][oO]");
   ```

7. **拆分复杂正则表达式**：对于非常复杂的模式，考虑拆分或使用其他方法

   ```java
   // 对于复杂的验证，考虑分步验证
   public boolean isValidData(String data) {
       // 第一步：检查基本格式
       if (!basicPattern.matcher(data).matches()) {
           return false;
       }
       // 第二步：进一步验证逻辑
       // ...
       return true;
   }
   ```

8. **测试和调试**：使用在线工具（如 regex101.com）测试正则表达式，确保其按预期工作

9. **考虑性能影响**：复杂的正则表达式可能导致性能问题，特别是在处理大量文本时

10. **添加注释**：对于复杂的正则表达式，添加注释说明其用途和工作原理

    ```java
    // 匹配 YYYY-MM-DD 格式的日期
    private static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    ```

## 性能优化建议

1. **避免回溯**：某些正则表达式模式可能导致大量回溯，影响性能
2. **使用独立捕获组**：对于不需要回溯的部分，使用 `(?>X)` 独立捕获组
3. **限制量词范围**：尽可能使用明确的量词范围，如 `{n,m}` 而不是 `*` 或 `+`
4. **预编译和缓存**：预编译并缓存常用的正则表达式模式
5. **使用字符串方法代替**：对于简单的字符串操作，优先使用 `String` 类的方法

通过遵循这些最佳实践，可以编写更高效、更可维护的正则表达式代码。
