package com.learnjava.builtin.regexp;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexpExample {
  public static void main(String[] args) {
    baseRegexp();
    getMatchedInfo();
    captureGroup();
    patternFlags();
    advancedReplace();
  }

  // #region baseRegexp
  /**
   * 基础正则表达式示例
   */
  public static void baseRegexp() {
    String regexp = "\\d+";
    String str = "12345";

    // 编译正则表达式
    Pattern pattern = Pattern.compile(regexp);
    // 创建 Matcher 对象
    Matcher matcher = pattern.matcher(str);

    // 检查整个字符串是否匹配
    boolean isMatched = matcher.matches();
    System.out.println("是否完全匹配: " + isMatched); // 输出: 完全匹配: true
  }
  // #endregion baseRegexp

  // #region getMatchedInfo
  /**
   * 获取匹配信息示例
   */
  public static void getMatchedInfo() {
    // 编译正则表达式
    Pattern pattern = Pattern.compile("\\d+");

    // 创建 Matcher 对象
    Matcher matcher = pattern.matcher("There are 123 apples and 456 oranges");

    // 查找并打印所有匹配项
    while (matcher.find()) {
      String matched = matcher.group();
      int startIndex = matcher.start();
      int endIndex = matcher.end();
      System.out.println("找到匹配: '" + matched + "'，位置: [" + startIndex + ", " + endIndex + ")");
    }
    // 输出:
    // 找到匹配: '123'，位置: [10, 13)
    // 找到匹配: '456'，位置: [25, 28)
  }
  // #endregion getMatchedInfo

  // #region captureGroup
  /**
   * 捕获组示例
   */
  public static void captureGroup() {
    // 匹配日期格式 YYYY-MM-DD
    String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
    String input = "今天是 2023-12-25，明天是 2023-12-26";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      System.out.println("完整匹配: " + matcher.group(0)); // 整个匹配
      System.out.println("年份: " + matcher.group(1)); // 第一个捕获组
      System.out.println("月份: " + matcher.group(2)); // 第二个捕获组
      System.out.println("日期: " + matcher.group(3)); // 第三个捕获组
      System.out.println();
    }
    // 输出:
    // 完整匹配: 2023-12-25
    // 年份: 2023
    // 月份: 12
    // 日期: 25
    //
    // 完整匹配: 2023-12-26
    // 年份: 2023
    // 月份: 12
    // 日期: 26
  }
  // #endregion captureGroup

  // #region patternFlags
  /**
   * 模式标志示例
   */
  public static void patternFlags() {
    // 使用不区分大小写标志
    Pattern pattern = Pattern.compile("hello", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher("Hello WORLD, hElLo Java");

    while (matcher.find()) {
      System.out.println("找到匹配: '" + matcher.group() + "'");
    }
    // 输出:
    // 找到匹配: 'Hello'
    // 找到匹配: 'hElLo'

    // 使用多行模式
    String text = "Line 1: start\nLine 2: middle\nLine 3: end";
    Pattern multiLinePattern = Pattern.compile("^Line", Pattern.MULTILINE);
    Matcher multiLineMatcher = multiLinePattern.matcher(text);

    while (multiLineMatcher.find()) {
      System.out.println("找到行开始: '" + multiLineMatcher.group() + "'，位置: " + multiLineMatcher.start());
    }
  }
  // #endregion patternFlags

  // #region advancedReplace
  /**
   * 高级替换操作
   */
  public static void advancedReplace() {
    String text = "John Doe: john.doe@example.com, Jane Smith: jane.smith@example.org";

    // 隐藏电子邮件地址
    Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
    Matcher emailMatcher = emailPattern.matcher(text);

    StringBuffer result = new StringBuffer();
    while (emailMatcher.find()) {
      String username = emailMatcher.group(1);
      String domain = emailMatcher.group(2);
      // 替换为部分隐藏的电子邮件
      String replacement = username.charAt(0) + "*****@" + domain;
      emailMatcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
    }
    emailMatcher.appendTail(result);

    System.out.println(result.toString());
    // 输出: John Doe: j*****@example.com, Jane Smith: j*****@example.org
  }
  // #endregion advancedReplace

  // #region stringMatches
  /**
   * 字符串匹配示例
   */
  public static void stringMatches() {
    // 检查是否为有效的邮箱格式
    String email = "user@example.com";
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    boolean isValidEmail = email.matches(emailRegex);
    System.out.println("邮箱是否有效: " + isValidEmail); // 输出: 邮箱是否有效: true

    // 检查是否为有效的手机号码（中国大陆）
    String phone = "13812345678";
    String phoneRegex = "^1[3-9]\\d{9}$";
    boolean isValidPhone = phone.matches(phoneRegex);
    System.out.println("手机号是否有效: " + isValidPhone); // 输出: 手机号是否有效: true

    // 检查是否为纯数字
    String number = "12345";
    String numberRegex = "\\d+";
    boolean isNumber = number.matches(numberRegex);
    System.out.println("是否为纯数字: " + isNumber); // 输出: 是否为纯数字: true
  }
  // #endregion stringMatches

  // #region stringSplit
  /**
   * 字符串分割示例
   */
  public static void stringSplit() {
    // 根据空格分割字符串
    String text = "Hello World Java Regex";
    String[] words = text.split("\\s+");
    System.out.println("按空格分割结果:");
    for (String word : words) {
      System.out.println(word);
    }
    // 输出:
    // Hello
    // World
    // Java
    // Regex

    // 根据逗号、空格或分号分割字符串
    String csvData = "apple, banana; orange pear";
    String[] fruits = csvData.split("[,;\\s]+");
    System.out.println("\n按多种分隔符分割结果:");
    for (String fruit : fruits) {
      System.out.println(fruit);
    }
    // 输出:
    // apple
    // banana
    // orange
    // pear

    // 使用limit参数
    String text2 = "a,b,c,d,e";
    String[] limitedSplit = text2.split(",", 3);
    System.out.println("\n限制分割次数为3:");
    for (String part : limitedSplit) {
      System.out.println(part);
    }
    // 输出:
    // a
    // b
    // c,d,e
  }
  // #endregion stringSplit

  // #region stringReplaceAll
  /**
   * 字符串替换示例
   */
  public static void stringReplaceAll() {
    // 替换所有数字为星号
    String text = "My phone number is 13812345678 and 15987654321";
    String replaced = text.replaceAll("\\d+", "***");
    System.out.println(replaced);
    // 输出: My phone number is *** and ***

    // 移除所有空白字符
    String textWithSpaces = "Hello   World  Java\tRegex";
    String noSpaces = textWithSpaces.replaceAll("\\s+", "");
    System.out.println(noSpaces);
    // 输出: HelloWorldJavaRegex

    // 格式化日期（从 YYYY-MM-DD 格式化为 DD/MM/YYYY）
    String date = "2023-12-25";
    String formattedDate = date.replaceAll("(\\d{4})-(\\d{2})-(\\d{2})", "$3/$2/$1");
    System.out.println(formattedDate);
    // 输出: 25/12/2023
  }
  // #endregion stringReplaceAll

  // #region stringReplaceFirst
  /**
   * 字符串替换示例
   */
  public static void stringReplaceFirst() {
    // 只替换第一个数字为星号
    String text = "Price: $100, Discount: $20";
    String replaced = text.replaceFirst("\\d+", "***");
    System.out.println(replaced);
    // 输出: Price: $***, Discount: $20

    // 替换第一个单词为 "REPLACED"
    String sentence = "This is a sample sentence.";
    String modified = sentence.replaceFirst("\\b\\w+\\b", "REPLACED");
    System.out.println(modified);
    // 输出: REPLACED is a sample sentence.
  }
  // #endregion stringReplaceFirst
}
