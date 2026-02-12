# 集合 Collection

Java 集合 Collection 简单说就是“持有对象”的容器。

Java 集合框架是 Java 开发中最常用的 API 之一，它提供了一套统一的接口和实现类，用于存储和操作对象。

整个 Java 集合框架的整体架构设计，采用 接口 `Interface` 和 实现 `implementation` 的分层设计，具体表现为 **"接口 - 抽象类 - 实现类"**。

- 接口 Interface 是抽象的，约定了一套访问容器的方法。
- 实现 Implementation 包括抽象类和实现类，可以通过不同的方式来实现接口的方法。

这样的架构设计有以下优势：

- 接口标准化：通过统一接口（如`List`）定义行为，实现类（如`ArrayList`）可灵活替换，符合 "面向接口编程" 思想。
- 功能分层：抽象类（如`AbstractList`）实现接口的通用方法，减少实现类的代码冗余（如`addAll()`、`contains()`等）。
- 工具类支持：`Collections`和`Arrays` 类提供排序、同步化等工具方法（如`Collections.sort(list)`、`Collections.synchronizedMap(map)`）。

> 接口-实现这样的面向接口编程思想，在 JavaEE 中体现的更为明显，比如 jakarta EE 规范中都是接口规范，而 Tomcat 等都是该规范的实现。

![java-collection](/images/java-collection.jpg)

简化分析：
![java-collection-simple](/images/java-collection-simple.webp)

## 接口体系

集合有两个基本的接口类型 `Collection` 和 `Map`。

- `Collection` 接口：存储**单值**集合，定义了添加、删除、遍历等基础操作，主要子接口有：
  - `List`：有序可重复集合，实现的类有`ArrayList`、`LinkedList`
  - `Set`：无序不可重复集合，实现的类有`HashSet`、`TreeSet`
  - `Queue`：队列（先进先出，实现的类有`LinkedList`、`PriorityQueue`
- `Map` 接口：存储键值对（key-value）集合，key 不可重复，主要实现类有：
  - `HashMap`：哈希表实现，无序
  - `TreeMap`：红黑树实现，按键排序
  - `LinkedHashMap`：哈希表 + 双向链表，保持插入顺序

另外，还有两类接口支撑着集合方法的实现：

- `Iterator`：约定了集合元素迭代遍历的方法，`Collection` 接口继承至`Iterator` 接口，所以该`Collection` 的实现类都有迭代遍历的方法或者被 `forEach` 迭代遍历。
- `Comparator`: 约定了集合元素间比较的方法。
