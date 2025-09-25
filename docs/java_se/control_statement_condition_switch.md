# 条件语句 switch

如果 if 的条件分支比较多，那么可以使用 switch 语句。

switch 语句有三种使用方式：

1. switch 基础形式
2. switch 箭头->形式，java 12 开始支持
3. switch yield 形式，java 14 开始支持

## switch 基础形式

switch 基础形式的语法如下：

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchDefault

### switch 语句执行流程

switch 语句的执行流程如下：

1. 计算表 switch 表达式的值 `switch(expression)`。
2. 与每个 case 语句中指定的值进行比较。
3. 如果找到匹配的值，就执行对应的语句，直到遇到 break 语句，或 switch 语句结束。
4. 如果没有找到匹配的值，但是存在 default 语句，就执行 default 语句。如果不存在 default 语句，将不执行任何操作，结束 switch 语句。

### switch 语句的表达式

switch 表达式语句支持的参数类型：

1. jdk 1.0 开始支持 byte、short、int、char 类型
2. jdk 1.5 开始支持 enum 类型和包装类型（Byte、Short、Integer、Character）
3. jdk 1.7 开始支持 String 类型

对于 switch 表达式结果的数据类型要求，可以统一理解**要求为 int 整型**。

1. byte short char 类型可以在不损失精度的情况下向上转型成 int 类型，所以 float 和 double 不能作为 switch 表达式的参数。
2. 包装类型（Byte、Short、Integer、Character）类型支持是因为 java 编译器在底层会自动进行拆箱操作。
3. 对枚举类的支持是因为枚举类有一个 ordinal 方法,该方法实际上是一个 int 类型的数值。
4. 对 String 类型的支持是因为 java 编译器在底层会自动调用 String 类的 hashCode 方法，该方法返回一个 int 类型的数值。

### switch 语句的 case 子句

1. case 语句中指定的每个值，必须与表达式的结果类型兼容，否则会编译错误。
2. default 子句是可选的，如果存在 default 子句，当没有匹配的 case 时，执行 default 子句。
3. break 语句，当执行完当前 case 子句时，使程序跳出 switch 整个语句块；但它是可选的，如果某个 case 子句中不存在 break 语句，将发生穿透现象，继续执行下一个 case 语句，直到遇到 break 语句，或 switch 语句结束。

### switch 语句的穿透现象

如果某个 case 子句中不存在 break 语句，将发生穿透现象，继续执行下一个 case 语句，直到遇到 break 语句，或 switch 语句结束。

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchPenetrate

## switch 箭头 -> 形式

如果每个 case 语句只有一条语句，那么可以使用箭头 -> 形式。

> java 12 开始支持 switch 箭头 -> 形式。这是 java 函数式接口和 lambda 表达式在 switch 语句中的应用。

基础语法：

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchArrow

`->` 箭头相当于不用输入 break 语句，执行完当前 case 语句，自动跳出 switch 语句块。

### switch 箭头 -> 形式的多条件合并

另外，还可以多个 case 语句进行合并，用逗号分隔，产生类似 switch 基本类型中 穿透现象的效果。

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchArrowMerge

### switch 箭头 -> 形式的限制

1. 每个 case 语句中只能有一条语句，不能有多条语句。
2. 每个 case 语句中不能有 break 语句，否则会编译错误。
3. default 语句是可选的，如果存在 default 语句，必须放在最后。

## switch yield 形式

> java 14 开始支持 switch yield 形式。

如果希望将 switch 语句作为表达式语句，可以有返回值，用于赋值。可以采用 switch yield 形式。

`yield` 与默认 switch 形式结合使用，基本形式如下：

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchYieldDefault

`yield` 与箭头形式结合使用，基本形式如下：

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchYieldArrow

`yield` 与箭头形式结合使用时，case 可以使用语句块，但是语句块中必须有 yield 语句，用于返回值。

<<< ../../learnjava/src/com/learnjava/statement/ConditionStatement.java#switchYieldArrowBlock

注意这里 switch 作为表达式语句，未尾必须要加冒号。
