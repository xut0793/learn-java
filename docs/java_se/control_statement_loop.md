# 循环语句 for

循环，就是多次重复执行某段代码。

在 java 中，循环语句有 while 语句、do-while 语句、for 语句、foreach 语句。

## while 语句

while 语句是一种基本循环语句，先判断循环条件，再执行循环体语句，只要条件为真就会被重复执行循环体。

### 基本语法

```java
while (循环条件) {
    // 循环体语句
}
```

while 语句和 if 语句的语法很像，但是 while 语句的循环体语句只要条件为真就会被重复执行，而 if 语句的循环体语句在条件为真时只会被执行一次。

while 循环中，代码块中必须要有能影响循中断或退出的条件，否则会导致循环无限执行（死循环），造成程序崩溃。

<<< ../../learnjava/src/com/learnjava/statement/LoopStatement.java#while

## do-while 语句

如果不管条件语句是什么，循环体的代码都要执行一次，那么就可以使用 do-while 语句。

### 基本语法

```java
do {
    // 循环体语句
} while (循环条件);
```

do-while 语句和 while 语句的区别是：

- while 语句先判断循环条件，再执行循环体语句，只要条件为真，就会被重复执行循环体。
- do-while 语句先执行循环体语句，再判断循环条件，只要条件为真，就会被重复执行循环体。
- do-while 语句至少会执行一次循环体语句，而 while 语句可能一次都不执行。

<<< ../../learnjava/src/com/learnjava/statement/LoopStatement.java#doWhile

## for 语句

如果循环次数已知，那么就可以使用 for 语句。

### 基本语法

```java
for (初始化语句; 循环条件; 循环变量更新语句) {
    // 循环体语句
}
```

for 语句执行流程：

1. 执行初始化语句
2. 判断循环条件
3. 如果循环条件为真，就执行循环体语句，如果条件为假，则退出整个 for 语句
4. 如果循环体语句中没有退出语句，那么再执行循环变量更新语句
5. 再回到第 2 步

<<< ../../learnjava/src/com/learnjava/statement/LoopStatement.java#for

另外，for 语句的初始化语句、循环条件、循环变量更新语句都可以省略，但是分号不能省略。但是如果 for 语句中每条语句都是空语句`for(;;){循环体代码}`，那么就会形成死循环。

某些场景中，可能会把初始化语句放在 for 语句的外部，例如：

```java
int i = 0;
for (; i < 10; i++) {
    System.out.println(i);
}
```

## foreach 语句

foreach 语句是 java1.5.0 引入的一种简洁的循环语句，主要用于遍历数组或集合中的元素。

### 基本语法

```java
for (元素类型 元素变量 : 数组或集合) {
    // 循环体语句
}
```

可以看到，foreach 语句的循环体语句中只能使用元素变量，不能使用元素的索引。

<<< ../../learnjava/src/com/learnjava/statement/LoopStatement.java#foreach
