# Java 内置日期时间 API

Java 提供了两套日期时间 API：

1. 传统方式**legacy API** (Java 8 之前)：主要基于`java.util.Date`、`java.util.Calendar`等类
2. 现代方式**modern API** (Java 8+)：基于`java.time`包的新 API，推荐使用

Legacy Date/Time API (Java 8 之前)

## java.util.Date 类

```java
// 创建当前日期时间的Date对象
Date date = new Date();

// 从毫秒值创建Date对象
long millis = System.currentTimeMillis();
Date dateFromMillis = new Date(millis);

// 比较日期
boolean after = date.after(anotherDate);
boolean before = date.before(anotherDate);
int compareResult = date.compareTo(anotherDate);
```

## java.util.Calendar 类

```java
// 获取Calendar实例
Calendar calendar = Calendar.getInstance();

// 设置日期时间
calendar.set(Calendar.YEAR, 2024);
calendar.set(Calendar.MONTH, Calendar.MARCH); // 月份从0开始
calendar.set(Calendar.DAY_OF_MONTH, 15);110116120
calendar.set(Calendar.HOUR, 10);
calendar.set(Calendar.MINUTE, 30);
calendar.set(Calendar.SECOND, 45);

// 获取Date对象
Date calendarDate = calendar.getTime();

// 日期运算
calendar.add(Calendar.DAY_OF_MONTH, 7); // 加7天
calendar.roll(Calendar.MONTH, true); // 滚动月份，不影响年份
```

## java.text.SimpleDateFormat 格式化

```java
// 格式化日期为字符串
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String formatted = sdf.format(new Date());

// 解析字符串为Date
Date parsedDate = sdf.parse("2024-03-15 10:30:45");

// 常用格式模式
// yyyy - 年份
// MM - 月份 (01-12)
// dd - 日期
// HH - 小时 (24小时制)
// mm - 分钟
// ss - 秒
// SSS - 毫秒
```

Modern Date/Time API (Java 8+) 核心包：java.time

## java.time.LocalDate - 仅表示日期

```java
// 获取当前日期
LocalDate today = LocalDate.now();

// 创建指定日期
LocalDate birthday = LocalDate.of(1990, 5, 15);
LocalDate birthday2 = LocalDate.of(1990, Month.MAY, 15);

// 从字符串解析
LocalDate fromString = LocalDate.parse("2024-03-15");

// 日期运算
LocalDate tomorrow = today.plusDays(1);
LocalDate nextMonth = today.plusMonths(1);
LocalDate nextYear = today.plusYears(1);
LocalDate previousWeek = today.minusWeeks(1);

// 日期比较
boolean isAfter = today.isAfter(birthday);
boolean isBefore = today.isBefore(birthday);
boolean isEqual = today.isEqual(birthday);

// 获取日期信息
int year = today.getYear();
int month = today.getMonthValue();
int dayOfMonth = today.getDayOfMonth();
DayOfWeek dayOfWeek = today.getDayOfWeek();
int dayOfYear = today.getDayOfYear();
```

## java.time.LocalTime - 仅表示时间

```java
// 获取当前时间
LocalTime now = LocalTime.now();

// 创建指定时间
LocalTime time = Local.of(14, 30, 45);
LocalTime time2 = LocalTime.of(14, 30);

// 从字符串解析
LocalTime fromString = LocalTime.parse("15:30:45");

// 时间运算
LocalTime later = now.plusHours(2);
LocalTime earlier = now.minusMinutes(30);

// 时间比较
boolean isBefore = now.isBefore(time);

// 获取时间信息
int hour = now.getHour();
int minute = now.getMinute();
int second = now.getSecond();
```

## java.time.LocalDateTime - 表示日期和时间

```java
// 获取当前日期时间
LocalDateTime now = LocalDateTime.now();

// 创建指定日期时间
LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 10, 30, 45);
LocalDateTime fromDateAndTime = LocalDateTime.of(today, now);

// 日期时间运算
LocalDateTime tomorrowSameTime = now.plusDays(1);
LocalDateTime nextHour = now.plusHours(1);

// 格式化为字符串
String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
```

## java.time.Instant - 表示时间戳

```java
// 获取当前时间戳
Instant now = Instant.now();

// 从Date转换
Instant fromDate = new Date().toInstant();

// 转换为Date
Date dateFromInstant = Date.from(now);

// 时间戳运算
Instant later = now.plusSeconds(3600); // 加1小时
Instant earlier = now.minusMillis(1000); // 减1秒

// 获取Unix时间戳
long epochMillis = now.toEpochMilli();
long epochSeconds = now.getEpochSecond();
```

## java.time.Period 和 java.time.Duration - 时间间隔

```java
// Period - 日期间隔（年月日）
Period period = Period.between(birthday, today);
int years = period.getYears();
int months = period.getMonths();
int days = period.getDays();

// Duration - 时间间隔（时分秒毫秒）
Duration duration = Duration.between(startTime, endTime);
long hours = duration.toHours();
long minutes = duration.toMinutes();
long seconds = duration.getSeconds();

// 创建时间间隔
Period oneWeek = Period.ofWeeks(1);
Period twoMonths = Period.ofMonths(2);
Duration thirtyMinutes = Duration.ofMinutes(30);
Duration twoHours = Duration.ofHours(2);
```

## java.time.ZonedDateTime - 带时区的日期时间

```java
// 获取系统默认时区的当前时间
ZonedDateTime now = ZonedDateTime.now();

// 指定时区
ZonedDateTime newYorkTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
ZonedDateTime tokyoTime = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));

// 创建带时区的日期时间
ZonedDateTime dateTime = ZonedDateTime.of(
    LocalDate.of(2024, 3, 15),
    LocalTime.of(10, 30),
    ZoneId.of("Asia/Shanghai")
);

// 时区转换
ZonedDateTime converted = dateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

// 格式化
String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
```

## DateTimeFormatter - 日期时间格式化

```java
// 预定义格式化器
String isoDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
String isoDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

// 自定义格式化
DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
String chineseFormat = LocalDateTime.now().format(customFormatter);

// 常用格式模式
// yyyy - 四位年份
// yy - 两位年份
// MMMM - 月份全称 (January)
// MMM - 月份缩写 (Jan)
// MM - 两位月份
// M - 一位月份
// dd - 两位日期
// d - 一位日期
// HH - 24小时制小时
// hh - 12小时制小时
// mm - 分钟
// ss - 秒
// SSS - 毫秒
// EEEE - 星期全称 (Monday)
// EEE - 星期缩写 (Mon)
// z - 时区名称
// Z - 时区偏移
```

## 常用场景和最佳实践

### 1. 计算两个日期之间的天数

```java
// 使用现代API
long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

// 使用Period
Period period = Period.between(startDate, endDate);
int totalDays = period.getDays() + period.getMonths() * 30 + period.getYears() * 365;
```

### 2. 计算两个时间之间的差值

```java
// 使用Duration
Duration duration = Duration.between(startTime, endTime);
long hours = duration.toHours();
long minutes = duration.toMinutes();
long seconds = duration.getSeconds();
```

### 3. 获取工作日（排除周末）

```java
public static List<LocalDate> getWeekdays(LocalDate start, LocalDate end) {
    List<LocalDate> weekdays = new ArrayList<>();
    LocalDate current = start;

    while (!current.isAfter(end)) {
        DayOfWeek dayOfWeek = current.getDayOfWeek();
        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            weekdays.add(current);
        }
        current = current.plusDays(1);
    }

    return weekdays;
}
```

### 4. 获取月份的第一天和最后一天

```java
public static void getMonthBounds(LocalDate date) {
    LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
    LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());

    LocalDate firstDayNextMonth = date.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    LocalDate lastDayThisMonth = firstDayNextMonth.minusDays(1);
}
```

### 5. 判断是否为闰年

```java
public static boolean isLeapYear(int year) {
    return Year.isLeap(year);
}
```

## Legacy API 到 Modern API 区别

- **不可变性**：Modern API 的类都是不可变的，避免了线程安全问题。
- **函数式风格**：Modern API 支持函数式编程，如 Stream API。
- **更好的错误处理**：Modern API 使用异常来处理错误，而不是返回特殊值。
- **更好的性能**：Modern API 在性能上有显著优势。

## Legacy API 到 Modern API 的转换

### Date 转换为 LocalDateTime

```java
// 方法1：使用Instant作为中介
Date date = new Date();
Instant instant = date.toInstant();
LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

// 方法2：直接转换
LocalDateTime directConversion = LocalDateTime.ofEpochSecond(date.getTime() / 1000,
    (int) (date.getTime() % 1000) * 1_000_000, ZoneOffset.UTC);
```

### Calendar 转换为 LocalDateTime

```java
Calendar calendar = Calendar.getInstance();
Instant instant = calendar.toInstant();
LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
```

### SimpleDateFormat 转换为 DateTimeFormatter

```java
// SimpleDateFormat格式
// yyyy-MM-dd HH:mm:ss
String pattern = "yyyy-MM-dd HH:mm:ss";
DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

// 使用
LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
String formatted = dateTime.format(formatter);
```

## 线程安全性对比

### Legacy API 线程不安全

```java
// 危险：不安全的写法
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
List<Thread> threads = new ArrayList<>();

for (int i = 0; i < 10; i++) {
    threads.add(new Thread(() -> {
        try {
            Date date = sdf.parse("2024-03-15");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }));
}

threads.forEach(Thread::start);
```

### Modern API 线程安全

```java
// 安全：线程安全的写法
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
List<Thread> threads = new ArrayList<>();

for (int i = 0; i < 10; i++) {
    threads.add(new Thread(() -> {
        LocalDate date = LocalDate.parse("2024-03-15", formatter);
        System.out.println(date);
    }));
}

threads.forEach(Thread::start);
```

## 性能对比

现代 API 在大多数场景下性能更优：

- 不可变对象避免了同步开销
- 更简洁的 API 减少了方法调用
- 更好的内存管理

## 总结

**推荐使用 Java 8+的现代日期时间 API（java.time 包）**，原因：

1. 线程安全
2. 不可变，避免副作用
3. API 设计更清晰
4. 功能更强大
5. 性能更好
6. 更好的国际化支持

**何时使用 Legacy API：**

- 需要与旧代码兼容
- 某些第三方库仍使用 legacy API
- 特定的企业环境限制

**最佳实践：**

- 始终使用`LocalDate`、`LocalTime`、`LocalDateTime`、`Instant`等现代 API 类
- 避免使用`java.util.Date`和`java.util.Calendar`
- 使用`DateTimeFormatter`而不是`SimpleDateFormat`
- 合理使用时区和格式化
- 利用`TemporalAdjusters`处理复杂日期运算
