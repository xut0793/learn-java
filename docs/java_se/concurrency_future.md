# 异步编程

同步编程是“一条路走到黑”，遇到耗时操作（如读写数据库、网络请求）时，当前线程会被阻塞等待，期间无法处理其他任务。

而异步编程则是“发起任务后不等结果，继续做别的事，等任务完成后通过回调通知你”。

## Java异步编程的发展演化与核心架构

Java异步编程的演进史，就是一部从“手动挡”到“自动挡”，再到“智能驾驶”的进化史：

第一阶段：传统多线程与阻塞式 Future（Java 1.0 - Java 4/5）

- 核心类：Thread、Runnable、ExecutorService、Future。
- 特点：早期需要手动创建线程，开销极大。Java 5 引入了线程池和 Future，虽然解决了线程管理问题，但获取结果时 future.get() 依然是阻塞的，且缺乏任务编排能力。

第二阶段：非阻塞异步与 CompletableFuture（Java 8）

- 核心类：CompletableFuture。
- 特点：这是Java异步编程的里程碑。它引入了函数式编程思想，支持链式调用、任务组合（如 allOf、anyOf）以及完善的异常处理，彻底解决了“回调地狱”的问题，让异步代码变得可读且优雅。

第三阶段：底层异步I/O（Java 7+）

- 核心类：AsynchronousFileChannel、AsynchronousSocketChannel（NIO.2）。
- 特点：在操作系统层面实现了真正的异步I/O，通过 CompletionHandler 回调处理结果，完全不占用线程等待I/O完成。

第四阶段：虚拟线程（Java 21+）

- 核心类：Executors.newVirtualThreadPerTaskExecutor()。
- 特点：由JVM管理的轻量级线程。它打破了“一个请求一个OS线程”的限制，允许开发者用写同步代码的方式，轻松获得异步编程的高并发性能。

## Why 为什么需求它

既然有多线程和虚拟线程，为什么还需要异步编程？

- 多线程的瓶颈：传统的平台线程（Platform Thread）直接映射到操作系统线程，内存开销大（通常1MB起步），且上下文切换成本极高。在高并发I/O场景下，如果为每个请求分配一个线程，系统资源会迅速耗尽。
- 异步的核心价值：异步解决的是“伸缩性（Scalability）”和“怎么等（Non-blocking）”的问题。它允许用极少的线程（如单线程事件循环）支撑成千上万的并发连接，极大提升了系统的吞吐量和资源利用率。
- 虚拟线程的定位：Java 21推出的虚拟线程（Virtual Thread）其实融合了多线程和异步的优势。它用同步的代码写法实现了异步的高性能（遇到I/O阻塞时自动挂起并释放底层载体线程）。

在虚拟线程普及之前，以及在某些需要极致I/O多路复用的底层架构中，传统的异步编程模型依然至关重要。

## How 它如何使用

在 Java 中，异步编程的底层实现主要依赖于 `java.util.concurrent` 包下的几个核心类和接口。它们随着 Java 版本的迭代不断进化，形成了从基础到高级的完整体系。

### 基础阶段：`Runnable` 与 `ExecutorService`（线程池）

这是最基础的异步实现方式，核心是将“任务”与“执行任务的线程”解耦。

核心类/接口：

- `Runnable`：定义没有返回值的异步任务。
- `ExecutorService`：线程池接口，负责管理和复用线程，避免频繁创建和销毁线程带来的开销。

实现方式：

- 将 `Runnable` 任务提交给线程池，线程池会在后台分配线程去执行，主线程继续向下运行，实现“发后不管（Fire-and-Forget）”的异步效果。

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableDemo {
    public static void main(String[] args) {
        // 创建一个固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交一个没有返回值的异步任务
        executor.submit(() -> {
            System.out.println("异步任务正在执行，当前线程：" + Thread.currentThread().getName());
            // 模拟耗时操作
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            System.out.println("异步任务执行完毕！");
        });

        System.out.println("主线程继续执行其他逻辑，不需要等待异步任务完成。");

        // 记得在程序退出前关闭线程池
        executor.shutdown();
    }
}
```

### 进阶阶段：`Callable` 与 `Future`（带结果的异步）

`Runnable` 的缺点是拿不到执行结果，也无法感知任务执行中抛出的异常。Java 5 引入了 `Callable` 和 `Future` 来解决这个问题。

核心类/接口：

- `Callable<V>`：类似 `Runnable`，但有返回值，并且可以抛出受检异常。
- `Future<V>`：代表异步计算的结果。它像一个“提货单”，主线程可以通过它来查询任务是否完成、取消任务，或者阻塞等待并获取最终结果。

实现方式：

- 提交 `Callable` 任务后，会立即返回一个 `Future` 对象。主线程可以在未来的某个时刻调用 `future.get()` 来获取结果（此时如果任务没完成会阻塞主线程）。

```java
import java.util.concurrent.*;

public class FutureDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 提交一个有返回值的异步任务
        Future<Integer> future = executor.submit(() -> {
            System.out.println("开始计算 1+1...");
            Thread.sleep(2000); // 模拟耗时计算
            return 1 + 1;
        });

        System.out.println("主线程先去忙点别的...");

        // 在需要结果的时候获取（如果任务未完成，这里会阻塞等待）
        Integer result = future.get();
        System.out.println("获取到异步计算结果：" + result);

        executor.shutdown();
    }
}
```

### 现代阶段：`CompletableFuture`（强大的异步编排）

`Future` 的缺点是 `get()` 方法会阻塞线程，且不支持多个任务的链式组合（比如任务A完成后自动触发任务B）。Java 8 推出的 `CompletableFuture` 完美解决了这些问题，它是现代 Java 异步编程的绝对主力。

- 核心类：`CompletableFuture<T>`：实现了 `Future` 和 `CompletionStage` 接口。它不仅代表一个未来的结果，还提供了大量的回调、组合和异常处理方法。
- 实现方式：通过 `supplyAsync`（有返回值）或 `runAsync`（无返回值）开启异步任务，然后使用 `thenApply`、`thenAccept`、`thenCombine` 等方法进行非阻塞的链式编排。

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        // supplyAsync 开启一个有返回值的异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("第一步：获取用户信息...");
            return "用户A";
        })
        // 链式调用：当上一步完成后，自动执行这一步（非阻塞）
        .thenApply(user -> {
            System.out.println("第二步：根据 " + user + " 查询订单...");
            return "订单123";
        })
        // 最终消费结果
        .thenAccept(order -> System.out.println("最终结果：处理 " + order + " 完毕"))
        // 统一的异常处理
        .exceptionally(ex -> {
            System.err.println("异步任务发生异常：" + ex.getMessage());
            return null;
        });

        System.out.println("主线程完全不被阻塞，可以继续处理其他请求...");

        // 等待异步流程走完（实际生产中通常不需要 join，而是让回调自然触发）
        future.join();
    }
}
```

### 总结

- 如果只是简单的“发后不管”任务，使用 **`ExecutorService` + `Runnable`**。
- 如果需要获取异步任务的执行结果，且不需要复杂的任务编排，使用 **`Future` + `Callable`**。
- 如果是复杂的业务场景（如多个微服务并行调用、任务链式依赖、非阻塞回调），**`CompletableFuture`** 是最优雅、最强大的选择。

### 核心组件：Future / CompletableFuture

`Future` 是一个接口，而 `CompletableFuture` 是一个实现了 `Future` 接口的具体类。

`Future` 的设计初衷是作为一个“异步计算结果的凭证”，因此它的方法主要围绕**检查状态**、**获取结果**和**取消任务**展开。

| 方法名               | 返回值       | 作用与描述                                                                                   |
| :------------------- | :----------- | :------------------------------------------------------------------------------------------- |
| `get()`              | V (泛型结果) | **阻塞式**获取异步任务的执行结果。如果任务未完成，当前线程会一直等待。                       |
| get(long, TimeUnit)` | V (泛型结果) | 在指定的时间内**阻塞等待**结果。如果超时任务仍未完成，会抛出 `TimeoutException`。            |
| `cancel(boolean)`    | boolean      | 尝试取消任务的执行。如果任务已完成或已被取消则返回 `false`。参数决定是否中断正在执行的任务。 |
| `isCancelled()`      | boolean      | 判断任务是否在完成前被正常取消。                                                             |
| `isDone()`           | boolean      | 判断任务是否已经完成（包括正常结束、异常终止或被取消）。                                     |

`CompletableFuture` 不仅包含了 `Future` 的所有能力，还引入了大量的函数式编程方法，用于**任务编排**、**链式调用**和**异常处理**。

| 分类       | 常用方法                               | 作用与描述                                                                                                      |
| :--------- | :------------------------------------- | :-------------------------------------------------------------------------------------------------------------- |
| 创建任务   | `supplyAsync` / `runAsync`             | 异步执行有返回值(`Supplier`)或无返回值(`Runnable`)的任务。支持传入自定义线程池。                                |
| 串行编排   | `thenApply` / `thenAccept` / `thenRun` | 任务完成后，对结果进行转换(`Apply`)、消费(`Accept`)或执行收尾动作(`Run`)。带 `Async` 后缀的方法会切换线程执行。 |
| 扁平化串行 | `thenCompose `                         | 当一个异步任务的结果是另一个异步任务时（即返回 `CompletableFuture`），用于将多层嵌套“拍平”。                    |
| 并行组合   | `thenCombine`                          | 将两个独立的 `CompletableFuture` 的结果合并处理。                                                               |
| 多任务聚合 | `allOf / anyOf`                        | `allOf` 等待所有任务完成；`anyOf` 只要任意一个任务完成即返回。                                                  |
| 竞速选择   | `applyToEither` / `acceptEither`       | 两个任务中，哪个先执行完就使用哪个的结果。                                                                      |
| 异常处理   | `exceptionally` / `handle`             | `exceptionally` 在发生异常时提供降级返回值；`handle` 无论成功或异常都会触发，可统一处理。                       |
| 手动完成   | `complete` / `completeExceptionally`   | 手动设置任务的结果或异常，强行结束该 `CompletableFuture`。                                                      |
| 状态检查   | `join()`                               | 类似于 `get()`，用于获取结果，但抛出的是**非受检异常**（RuntimeException），更适合链式调用。                    |

如果你只需要一个简单的异步任务并阻塞等待结果，`Future` 就足够了；但在现代 Java 开发中，面对复杂的业务逻辑（如多服务并行调用、任务依赖编排），`CompletableFuture` 丰富的 API 能极大地提升代码的可读性和执行效率。

- `runAsync()`：执行一个没有返回值的异步任务。

```java
import java.util.concurrent.CompletableFuture;

public class RunAsyncDemo {
    public static void main(String[] args) {
        // 执行无返回值的异步任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("执行无返回值的异步任务，当前线程：" + Thread.currentThread().getName());
        });
        future.join(); // 等待任务完成
    }
}
```

- `supplyAsync()`：执行一个有返回值的异步任务。

```java
import java.util.concurrent.CompletableFuture;

public class SupplyAsyncDemo {
    public static void main(String[] args) {
        // 执行有返回值的异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行有返回值的异步任务...");
            return "异步任务的结果";
        });
        System.out.println("获取结果：" + future.join());
    }
}
```

获取任务结果

- `get()`：阻塞获取结果，需要处理受检异常（InterruptedException, ExecutionException）。
- `join()`：阻塞获取结果，抛出非受检异常（CompletionException），在链式调用中不需要繁琐的 try-catch，开发中更常用。

```java
import java.util.concurrent.CompletableFuture;

public class GetDemo {
    public static void main(String[] args) {

      try {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        // 阻塞等待结果，必须 try-catch 或抛出异常
        String result = future.get();
        System.out.println("get() 获取结果：" + result);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}


public class JoinDemo {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        // 阻塞等待结果，代码更简洁
        String result = future.join();
        System.out.println("join() 获取结果：" + result);
    }
}
```

### 异步任务的编排

#### 任务串行回调:

- `thenRun()`：不关心上一个任务的结果，上一个任务完成后执行收尾动作（无入参，无返回值）。
- `thenAccept()`：接收上一个任务的结果并进行消费（有入参，无返回值）。
- `thenApply()`：接收上一个任务的结果，进行转换并返回新结果（有入参，有返回值）。

```java
import java.util.concurrent.CompletableFuture;

public class ThenRunDemo {
    public static void main(String[] args) {
        // `thenRun()`：不关心上一个任务的结果，上一个任务完成后执行收尾动作（无入参，无返回值）。
        CompletableFuture.supplyAsync(() -> "执行完毕")
            .thenRun(() -> System.out.println("上一个任务执行完毕，触发通知！"));

        // `thenAccept()`：接收上一个任务结果，进行消费（有入参，无返回值）。
        CompletableFuture.supplyAsync(() -> "Hello World")
            .thenAccept(result -> System.out.println("拿到上一个任务的结果并消费：" + result));

        // `thenApply()`：接收上一个任务结果，进行转换并返回新结果（有入参，有返回值）。
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> "Hello")
            .thenApply(str -> str.length()); // 把字符串转换成长度
        System.out.println("转换后的结果：" + future.join()); // 输出: 5
    }
}
```

#### 多任务串行

`thenCompose()`：扁平化串行。当你的下一步操作本身又是一个异步任务（返回 CompletableFuture）时，用它来避免嵌套。

```java
// 假设 getUser() 返回 CompletableFuture<User>，getOrder(user) 返回 CompletableFuture<Order>
CompletableFuture<Order> orderFuture = getUser().thenCompose(user -> {
    return getOrder(user); // 将两个嵌套的异步任务拍平
});
```

#### 多任务并行

- `thenCombine()`：双任务合并。等待两个独立的异步任务都完成后，将两者的结果合并处理。
- `applyToEither()`：竞速（有返回值）。两个任务谁先执行完，就取谁的结果并进行转换。
- `acceptEither()`：竞速（无返回值）。两个任务谁先执行完，就消费谁的结果。

```java
// 假设 taskA 和 taskB 是两个并行的异步任务
CompletableFuture<Integer> sumFuture = taskA.thenCombine(taskB, (resultA, resultB) -> {
    return resultA + resultB; // 将两个任务的结果相加
});

CompletableFuture<String> fastResult = task1.applyToEither(task2, result -> {
    return "最快返回的结果是：" + result;
});

task1.acceptEither(task2, result -> {
    System.out.println("最先完成的任务结果是：" + result);
});
```

#### 多任务协调

- allOf()：等待所有任务完成。常用于批量聚合多个异步接口的结果。
- anyOf()：等待任一任务完成。只要有一个任务完成就返回，结果类型是 Object，需要强转。

```java
CompletableFuture<Void> allFuture = CompletableFuture.allOf(task1, task2, task3);
allFuture.thenRun(() -> {
    // 此时 task1, task2, task3 必定都已完成，可以安全地获取结果
    String r1 = task1.join();
    String r2 = task2.join();
    String r3 = task3.join();
    System.out.println("聚合所有结果: " + r1 + r2 + r3);
});

CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(task1, task2);
anyFuture.thenAccept(result -> {
    String firstResult = (String) result; // 需要手动类型转换
    System.out.println("最先返回的结果: " + firstResult);
});
```

#### 异常处理

- exceptionally()：异常兜底。当任务抛出异常时，提供一个默认的降级返回值。
- handle()：最终处理。无论任务是成功还是失败，都会执行该方法，可以同时获取结果和异常。

```java
CompletableFuture<String> safeFuture = riskyTask.exceptionally(ex -> {
    System.err.println("发生异常：" + ex.getMessage());
    return "默认降级数据";
});

CompletableFuture<String> handledFuture = task.handle((result, ex) -> {
    if (ex != null) {
        return "任务失败，返回兜底值";
    }
    return "任务成功，结果为：" + result;
});
```

## Deep 它的实现

Java 的异步实现和 Python / JavaScript 一样，底层也离不开事件循环（Event Loop）机制。不过， Java 在设计思路上更偏向“多线程 + 异步框架”，所以它的事件循环机制在实现和表现形式上会比 Python 更灵活、更底层。

你可能会发现，平时在 Java 中写异步代码（比如用 CompletableFuture 或虚拟线程）时，似乎并没有像 Python 那样显式地去写 async def 和 await，也很少直接操作事件循环。这是因为 Java 的 CompletableFuture 等工具，更多是站在任务编排的角度。它们底层可能会用到线程池，或者依赖像 Netty 这样已经封装好事件循环的框架来处理 I/O。你作为业务开发者，只需要关注“任务完成后做什么”，底层的 I/O 等待和事件分发已经被框架屏蔽了。

Java 21 推出的虚拟线程（Virtual Thread）更是把这种底层复杂性彻底隐藏了。当虚拟线程遇到 I/O 阻塞时，JVM 会自动把它挂起，底层其实依然是通过类似事件循环的机制（基于少量操作系统线程）来监听 I/O 完成事件。一旦 I/O 完成，JVM 再唤醒虚拟线程继续执行。

这就让你可以用最直观的同步代码写法，享受到事件循环带来的异步高性能。

### 底层核心：Java NIO 与 Selector

在 Java 中，事件循环最经典的底层实现就是 Java NIO（New I/O） 中的 Selector（多路复用器）。它的工作模式与 Python 的 asyncio 事件循环非常相似：

- 单线程轮询：一个线程（或少数几个线程）持有一个 Selector，它像一个调度员，不断轮询注册在上面的多个网络通道（Channel）。
- 事件驱动：当某个通道有事件发生（比如有新连接接入、有数据可读、可以写入数据）时，Selector 会把这些就绪的通道筛选出来。
- 非阻塞处理：应用线程不需要死等某个 I/O 操作完成，而是通过事件循环去处理那些已经就绪的通道，处理完立刻返回继续轮询下一个。

这也是很多高性能 Java 网络框架（比如大名鼎鼎的 Netty）的核心原理。Netty 实现了一套非常成熟的 Reactor 模式（事件驱动架构），本质上就是一个高度优化的事件循环，用来处理海量的网络 I/O 操作。

### 框架与 GUI 中的事件循环

除了底层的 NIO，在很多 Java 框架和应用场景中，你也能看到事件循环的影子：

- Netty 等网络框架：它们内置了 EventLoop 线程，专门负责监听和分发 I/O 事件，避免为每个连接创建线程，从而支撑百万级的并发连接。
- GUI 框架：如果你接触过 Java 的桌面端开发（如 Swing 或 JavaFX），它们内部也有一个“事件调度线程（EDT）”，专门用来循环处理用户的点击、键盘输入等界面事件，这和前端 JavaScript 的事件循环逻辑几乎一模一样。
