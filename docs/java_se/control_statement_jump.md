# 循环控制语句

在循环的时候，会以循环条件作为是否结束的依据，但有时可能会需要根据别的条件提前结束循环或跳过一些代码，这时可以使用`break`或`continue`关键字对循环进行控制。

## continue

在循环的过程中，有的代码可能不需要每次都把循环体的所有代码都执行，这时候，可以使用`continue`语句。

`continue`关键字可以用于跳过循环体中剩余的代码，直接进入下一次循环。

<<< ../../exercise/control_statement/JumpStatement.java#continue

## break

`break`关键字可以用于结束循环，跳出循环体。

<<< ../../exercise/control_statement/JumpStatement.java#break

单个 `break` 语句用于跳出当前层的循环，如果是嵌套循环，需要逐层跳出。

```java
// 跳出嵌套循环
for (int i = 0; i < 10; i++) {
    for (int j = 0; j < 10; j++) {
        if (i == 5 && j == 5) {
            break;
        }
        System.out.println(i + " " + j);
    }
    System.out.println("-----------------");
    if (i == 5) {
        break;
    }
    System.out.println("i = " + i);
}
```

## label

在 Java 中，每个循环语句都可以有一个标签，用于在嵌套循环中跳出指定的循环。

标签的格式为：`标识符:`，其中标识符可以是任意的合法标识符。

但是这种语法在实际开发中不推荐使用，因为它会使代码变得复杂和难以维护。

```java
public static void labelLoop(String[] args) {
    // 定义外层循环的标签（任意合法标识符，后接冒号）
    outerLoop:
    for (int i = 0; i < 3; i++) {
        System.out.println("外层循环 i = " + i);
        for (int j = 0; j < 3; j++) {
            System.out.println("  内层循环 j = " + j);
            if (j == 1) {
                // 跳出标签指定的外层循环（整个嵌套循环）
                break outerLoop;
            }
        }
    }
    System.out.println("循环结束");
}
```

另一种解决方式是使用 标志位，定义一个布尔变量作为标志，当需要跳出所有循环时，修改标志位，然后在每层循环中判断标志位并退出。

```java
public static void breakWithFlag() {
    boolean shouldBreak = false; // 标志位

    for (int i = 0; i < 3 && !shouldBreak; i++) {
        System.out.println("外层循环 i = " + i);
        for (int j = 0; j < 3; j++) {
            System.out.println("  内层循环 j = " + j);
            if (j == 1) {
                shouldBreak = true; // 修改标志位
                break; // 先跳出内层循环
            }
        }
    }
    System.out.println("循环结束");
}
```

## return

return 语句用于从方法中返回一个值，并结束方法的执行。

通常循环的逻辑是定义在方法中执行的，如果需要在循环中提前结束方法的执行，可以使用 return 语句。

<<< ../../exercise/control_statement/JumpStatement.java#return
