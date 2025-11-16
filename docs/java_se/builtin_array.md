# 数组

数组是计算机程序中常见的基本数据结构。

在 Java 语言中，数组是存储多个**相同类型**数据的**有序集合**。并且数组一旦创建，其大小就固定不变，是一种固定大小的数据结构。

### Java 数组的特点

- **同质性**：数组中的所有元素必须是相同的数据类型
- **有序性**：元素按照索引顺序存储，索引从 0 开始
- **固定大小**：数组创建后长度不可改变
- **连续内存**：数组元素在内存中连续存储
- **随机访问**：可以通过索引直接访问任意元素，时间复杂度为 O(1)

## 数组声明和初始化

```java
// 方式一：静态初始化，在声明时直接赋值
int[] numbers = {1, 2, 3, 4, 5};
String[] names = new String[]{"张三", "李四", "王五"};

// 方式二：动态初始化，先指定长度，然后赋值
int[] data = new int[3];
data[0] = 10;
data[1] = 20;
data[2] = 30;
```

## 数组基本操作

### 数组元素访问

```java
int[] numbers = {10, 20, 30, 40, 50};

// 获取数组长度
int length = numbers.length; // length = 5

// 访问元素（索引从0开始）
int first = numbers[0];    // 第一个元素：10
int last = numbers[4];     // 最后一个元素：50

// 修改元素
numbers[2] = 300; // 修改第三个元素为300
```

### 数组遍历

```java
int[] numbers = {1, 2, 3, 4, 5};

// 方式一：普通for循环
for (int i = 0; i < numbers.length; i++) {
    System.out.println("numbers[" + i + "] = " + numbers[i]);
}

// 方式二：增强for循环（foreach）
for (int num : numbers) {
    System.out.println("num = " + num);
}

// 方式三：Java 8 Stream API
Arrays.stream(numbers).forEach(System.out::println);
```

## 数组工具类（Arrays）

Java 提供了 `java.util.Arrays` 工具类，包含很多实用的数组操作方法：

## 排序

```java
// Arrays.sort(array); // 升序排序
// Arrays.sort(array, Collections.reverseOrder()); // 降序排序（Java 8+）
// Arrays.sort(array, fromIndex, toIndex); // 对指定范围排序
// Arrays.sort(array, (a, b) -> b - a); // 自定义排序规则
int[] numbers = { 5, 2, 8, 1, 9 };
int[] copiedNumbers = Arrays.copyOf(numbers, numbers.length);
Arrays.sort(numbers);
System.out.println("numbers: " + Arrays.toString(numbers)); // {1, 2, 5, 8, 9}

Arrays.sort(copiedNumbers, 1, 4); // 对索引1到3的元素排序
System.out.println("copiedNumbers: " + Arrays.toString(copiedNumbers)); // {9, 8, 5, 2, 1}

// Arrays.sort 中如果传入比较器时，不能是基本类型，必须包装类型，否则会报错
Integer[] integers = { 5, 2, 8, 1, 9 };
Arrays.sort(integers, (a, b) -> a - b); // 升序排序
System.out.println("integers: " + Arrays.toString(integers)); // {1, 2, 5, 8, 9}
Arrays.sort(integers, Collections.reverseOrder());
System.out.println("numbers: " + Arrays.toString(numbers)); // {9, 8, 5, 2, 1}
```

## 搜索

```java
int[] numbers = {1, 2, 3, 4, 5};

// 二分搜索（数组必须已排序）
int index = Arrays.binarySearch(numbers, 3); // index = 2
int notFound = Arrays.binarySearch(numbers, 6); // negative value
```

### 填充

```java
int[] arr = new int[5];

// 填充整个数组
Arrays.fill(arr, 100); // arr = {100, 100, 100, 100, 100}
// 填充指定范围
Arrays.fill(arr, 1, 3, 200); // arr = {100, 200, 200, 100, 100}
```

### 复制

```java
int[] original = {1, 2, 3, 4, 5};

// 复制数组
int[] copy = Arrays.copyOf(original, 3); // {1, 2, 3}
int[] copyFull = Arrays.copyOf(original, 7); // {1, 2, 3, 4, 5, 0, 0}

// 复制指定范围
int[] rangeCopy = Arrays.copyOfRange(original, 1, 4); // {2, 3, 4}

// System.arraycopy()（更高效）
int[] newArr = new int[7];
System.arraycopy(original, 0, newArr, 0, 5); // 复制5个元素
```

## 转换

```java
int[] numbers = {1, 2, 3};

// 转换为字符串
String str = Arrays.toString(numbers); // "[1, 2, 3]"

// 多维数组转字符串
int[][] matrix = {{1, 2}, {3, 4}};
String matrixStr = Arrays.deepToString(matrix); // "[[1, 2], [3, 4]]"

// 转换为List（固定大小）
List<Integer> list = Arrays.asList(1, 2, 3);
List<int[]> listOfArrays = Arrays.asList(original); // 注意：这是List<int[]>
```

## 二维数组

```java
// 声明二维数组
int[][] matrix = new int[3][4]; // 3行4列
int[][] matrix2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

// 访问元素
int element = matrix[1][2]; // 第二行第三列

// 双重循环遍历二维数组
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println(); // 换行
}
```

## 不规则数组

```java
// 创建不规则的二维数组
int[][] jaggedArray = new int[3][];
jaggedArray[0] = new int[] {1, 2};
jaggedArray[1] = new int[] {3, 4, 5, 6};
jaggedArray[2] = new int[] {7, 8, 9};

// 不规则数组遍历
for (int[] row : jaggedArray) {
    for (int element : row) {
        System.out.print(element + " ");
    }
    System.out.println();
}
```

## 数组与泛型

```java
// 使用泛型数组的注意事项
public class GenericArray<T> {
    private T[] array;

    public GenericArray(int size) {
        // 错误方式：new T[size] 会编译错误
        // this.array = new T[size];

        // 正确方式：使用反射
        this.array = (T[]) new Object[size];
    }
}
```

## 数组性能优化

```java
// 预分配数组大小，避免频繁扩容
List<Integer> list = new ArrayList<>();
list.ensureCapacity(1000); // 预分配容量

// 使用基本类型数组而不是包装类型
int[] primitiveArray = new int[1000]; // 更高效
Integer[] objectArray = new Integer[1000]; // 包含额外开销
```

## 常见错误

```java
// 错误1：数组越界
int[] arr = {1, 2, 3};
System.out.println(arr[3]); // ArrayIndexOutOfBoundsException

// 错误2：空指针异常
int[] arr = null;
System.out.println(arr.length); // NullPointerException

// 错误3：数组大小不能改变
int[] arr = new int[5];
arr = new int[10]; // 这是创建新数组，不是修改原数组
```

## 最佳实践

```java
// 1. 优先使用泛型集合而不是数组
List<Integer> list = new ArrayList<>(); // 比 Integer[] 更灵活

// 2. 检查数组边界
if (index >= 0 && index < array.length) {
    // 安全访问
}

// 3. 使用 Arrays.fill() 初始化数组
int[] arr = new int[1000];
Arrays.fill(arr, -1); // 初始化为-1

// 4. 考虑使用更高效的数据结构
// 对于频繁插入删除操作，考虑使用 ArrayList
// 对于需要快速查找，考虑使用 HashSet 或 HashMap

// 5. 避免在循环中创建数组
// 错误做法：
for (int i = 0; i < 1000; i++) {
    int[] temp = new int[1000]; // 每次循环都创建新数组
}

// 正确做法：
int[] temp = new int[1000];
for (int i = 0; i < 1000; i++) {
    // 使用 temp 数组
}
```

## 数组 vs 集合

```java
// 数组的特点
- 固定大小，性能更好
- 基本类型支持
- 可以存储原始数据类型

// 集合的特点
- 动态大小，更灵活
- 只支持引用类型
- 丰富的API支持
- 类型安全（泛型）

// 选择建议
- 需要固定大小且性能要求高：使用数组
- 需要动态大小和丰富功能：使用 ArrayList
- 需要去重功能：使用 HashSet
- 需要键值对：使用 HashMap
```

## 数组总结

数组是 Java 编程中的基础数据结构，具有以下优势：

- 快速随机访问（O(1) 时间复杂度）
- 内存连续，缓存友好
- 基本类型支持，避免装箱拆箱开销

但在以下场景下应考虑使用集合：

- 需要动态大小
- 需要丰富的操作方法
- 需要类型安全保证
- 需要线程安全

掌握数组的使用和特性，是 Java 编程的重要基础技能。
