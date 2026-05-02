# 多线程

## 历史演变

Java 多线程编程的演进史，本质上是一部不断降低并发开发门槛、提升运行效率的进化史。

### JDK 1.0 - 1.4：原生与基础

- JDK 1.0 引入了 `Thread` 类和 `Runnable` 接口，确立了 Java 最基础的线程模型。开发者需要手动创建、启动线程，并自行处理线程的生命周期。在同步与通信方向，早期的线程协作依赖于简单粗暴的 `stop/resume/suspend` 方法（后被废弃），转而采用 `wait/notify` 机制。同时，`synchronized` 关键字提供了基础的互斥锁，但早期的实现相对粗粒度，性能较低。
- JDK 1.2 引入了 `ThreadLocal`（线程局部变量）和 `Collections` 工具类，为多线程环境下的数据隔离和集合操作提供了初步支持。

这一时期的痛点在于：没有线程池，频繁创建和销毁线程开销巨大；手动同步极易引发死锁、竞态条件等难以排查的问题。

### JDK 5 - 18：工具化与标准化

JDK 5 的发布是 Java 并发编程的里程碑，开发者可以更安全、舒适地驾驭多线程。

- JDK 5 引入了 `java.util.concurrent` (J.U.C) 包，彻底改变了并发编程的面貌。
  - 执行器框架（Executor Framework）：标准化了线程池管理（如 `ThreadPoolExecutor`），将任务的提交与执行机制分离，开发者无需再手动造轮子。
  - 高级同步工具：提供了 `ReentrantLock`、`CountDownLatch`、`CyclicBarrier`、`Semaphore` 等精细化的同步器，以及 `BlockingQueue` 简化了生产者-消费者模式。
  - 原子操作与并发集合：引入了 `AtomicXXX` 类提供无锁的原子操作，以及 `ConcurrentHashMap` 等高并发集合。
- JDK 6 对 `synchronized` 进行了大量优化（如锁升级机制），大幅提升了内置锁的性能。
- JDK 7 引入了 `Fork/Join` 框架，利用工作窃取（Work-Stealing）算法，极大提升了多核 CPU 在处理分治任务（如递归计算）时的利用率。
- JDK 8 带来了 `CompletableFuture`，极大地简化了异步编程和任务编排，同时 `Stream` 并行流也让数据并行处理变得极其简单。

### JDK 19 及以后：虚拟线程的革命

从 JDK 19 开始，Java 并发编程的核心突破是 **虚拟线程（Virtual Threads，Project Loom）**。

- 突破操作系统束缚：传统的平台线程（Platform Thread）是一对一映射到操作系统线程的，创建和切换成本高昂。而虚拟线程是由 JVM 自身调度的轻量级线程，数百万个虚拟线程可以运行在少量的操作系统线程之上。
- 简化高并发模型：虚拟线程让“一个请求一个线程”的简单编程模型重新回归。在执行 I/O 阻塞操作时，虚拟线程会自动从载体线程上卸载，不会占用宝贵的操作系统线程资源。这意味着开发者可以用同步的代码写法，轻松实现极高的 I/O 密集型应用吞吐量，告别复杂的异步回调地狱。

Java 21 为了配合虚拟线程，配套新特性：

- **结构化并发（Structured Concurrency）**：将并发任务组织为树状结构，自动处理取消和错误传播；
- **作用域值（Scoped Values）**：作为 `ThreadLocal` 的高效、安全替代方案，避免了海量虚拟线程下的内存泄漏风险。

Java 多线程从最初的手动管理 `Thread`，进化到使用 J.U.C 包的高级并发工具，再到如今利用虚拟线程彻底打破操作系统的性能瓶颈。这一演进过程，让 Java 开发者能够以更低的成本、更简单的代码，构建出性能极其强大的高并发系统。

## 第一阶段：手动管理 Thread（基础篇）

在 Java 中创建线程主要有三种基础方式。日常开发中，强烈推荐使用 `Runnable` 接口或 `Callable` 接口，因为 Java 是单继承的，实现接口可以避免继承 `Thread` 类带来的局限性。

### 创建线程的三种方式

方式一：继承 Thread 类，要求实现 `run()` 方法

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread创建的线程：" + Thread.currentThread().getName());
    }
}
// 启动方式
new MyThread().start();
```

方式二：实现 Runnable 接口（推荐）

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("实现Runnable创建的线程：" + Thread.currentThread().getName());
    }
}
// 启动方式
new Thread(new MyRunnable()).start();
```

方式三：实现 Callable 接口（带返回值）

```java
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Callable线程的返回结果";
    }
}
// 启动方式（需要 FutureTask 包装）
FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
new Thread(futureTask).start();
System.out.println(futureTask.get()); // 获取返回值
```

### Thread 核心属性与方法速查表

| 属性/方法             | 说明                                             | 使用示例                                         |
| :-------------------- | :----------------------------------------------- | :----------------------------------------------- |
| 线程名称 (Name)       | 线程的标识，默认为 `Thread-编号`                 | `thread.setName("MyThread");``thread.getName();` |
| 线程优先级 (Priority) | 范围 1(最低) 到 10(最高)，默认为 5               | `thread.setPriority(Thread.MAX_PRIORITY);`       |
| 守护线程 (Daemon)     | 后台线程，所有用户线程结束时 JVM 会自动退出      | `thread.setDaemon(true);` (需在 start 前调用)    |
| 线程状态 (State)      | 获取当前生命周期状态 (NEW, RUNNABLE, BLOCKED 等) | `thread.getState();`                             |
| 存活状态 (Alive)      | 判断线程是否已启动且未终止                       | `thread.isAlive();`                              |
| 线程休眠 (sleep)      | 让当前线程暂停执行指定的毫秒数                   | `Thread.sleep(1000);`                            |

## 第二阶段：JUC 并发工具包使用（进阶篇）

JDK 5 引入的 `java.util.concurrent` (JUC) 包是 Java 并发编程的核心。它主要包含三大核心板块：**线程池框架**、**原子操作类**、以及**并发集合与同步工具**。

### 线程池框架 (Executor Framework)

手动创建和销毁线程开销巨大，线程池通过复用线程来大幅提升性能。生产环境中**强烈建议手动创建 `ThreadPoolExecutor`**，以便明确线程池的运行规则，规避资源耗尽的风险。

```java
import java.util.concurrent.*;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 核心参数：核心线程数、最大线程数、空闲存活时间、时间单位、任务队列、拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,                      // 核心线程数
                5,                      // 最大线程数
                60L, TimeUnit.SECONDS,  // 非核心线程空闲60秒后销毁
                new ArrayBlockingQueue<>(10), // 任务等待队列
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者运行
        );

        // 提交任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 由线程 " + Thread.currentThread().getName() + " 执行");
            });
        }
        // 关闭线程池
        executor.shutdown();
    }
}
```

`ThreadPoolExecutor` 核心参数解析：

| 参数名                   | 说明                                                   |
| :----------------------- | :----------------------------------------------------- |
| corePoolSize             | 核心线程数，线程池长期保持的线程数量，即使空闲也不销毁 |
| maximumPoolSize          | 最大线程数，线程池允许创建的最大线程数量               |
| keepAliveTime            | 非核心线程空闲超过该时间会被销毁                       |
| workQueue                | 任务等待队列，核心线程都在忙时，新任务会进入此队列     |
| RejectedExecutionHandler | 拒绝策略，当队列满且达到最大线程数时如何处理新任务     |

## 第三阶段：虚拟线程

ava 21 中的虚拟线程（Virtual Threads）与其他语言（如 Kotlin、Python、Go）中的协程（Coroutines）在本质目标上是一致的，但在实现原理、编程体验和底层机制上存在明显的区别。

简单来说，它们都是为了解决传统操作系统线程（平台线程）昂贵且笨重的问题，通过用户态的轻量级调度来实现高并发。但 Java 走了一条“向下兼容、零学习成本”的独特路线。

### 本质上的相同点

无论是 Java 的虚拟线程，还是 Kotlin 的协程、Go 的 Goroutine，它们的核心特征都高度相似：

1. **轻量级**：创建和销毁的开销极低，单机可以轻松支撑百万级的并发任务。
2. **用户态调度**：不再由操作系统内核直接调度，而是由语言运行时（JVM、Go Runtime 等）在用户态进行 M:N 调度（将大量轻量级任务映射到少量的操作系统线程上）。
3. **阻塞挂起**：当任务遇到 I/O 阻塞时，会自动让出底层的操作系统线程（载体线程）去执行其他任务，从而极大提升系统吞吐量。

### 实现与体验上的核心差异

Java 虚拟线程最大的特色在于“零染色”和“完美兼容”，这使得它在迁移和开发体验上与其他语言的协程有显著不同：

> 关于函数着色问题，以及各语言实现异步编程的发展，可以看下这个篇文章 [What Async Promised and What it Delivered](https://causality.blog/essays/what-async-promised/)

1. 语法“染色”问题（Type Coloring）
   - Kotlin / Python（有染色）：
     - Kotlin 协程需要使用 `suspend` 关键字，任何调用挂起函数的函数自己也必须是 `suspend` 的，这种特性会像病毒一样传染，导致代码库中出现两套 API（普通函数和挂起函数）。
     - Python 的 `asyncio` 需要大量使用 `async` 和 `await` 关键字，同样存在“传染”问题，且必须使用专门的异步库，传统的阻塞库无法直接生效。
   - Java 虚拟线程（无染色）：虚拟线程完全复用了 Java 原有的 `Thread` API。你不需要修改任何方法的签名，普通的 `void run()` 方法可以直接跑在虚拟线程上。对于开发者而言，写法和传统的平台线程**完全一致**，没有任何学习成本和代码侵入性。
1. 底层调度与并行能力
   - Python（单核事件循环）：Python 的协程受限于全局解释器锁（GIL），本质上是单线程的事件循环模型，无法真正利用多核 CPU 进行并行计算。
   - Java 虚拟线程（多核并行）：虚拟线程采用 M:N 调度模型，底层的载体线程（Carrier Threads）是一个 `ForkJoinPool`，可以充分利用服务器的多核 CPU，实现真正的并行执行。
1. 对现有生态的兼容性
   - Kotlin / Python：如果想在协程中使用传统的阻塞式 I/O 库（如传统的 JDBC 驱动、同步的 HTTP 客户端），会阻塞底层的操作系统线程，从而抵消协程带来的性能优势，通常需要使用专门的异步/非阻塞库进行适配。
   - Java 虚拟线程：由于虚拟线程在遇到阻塞时会自动从载体线程上卸载（Yield），**几乎所有现有的阻塞式 API（包括传统的 JDBC、文件读写等）都可以直接无缝运行在虚拟线程上**，老代码无需任何改造就能直接享受到高并发的红利。

| 维度           | Java 虚拟线程                     | Kotlin 协程                    | Python asyncio               |
| :------------- | :-------------------------------- | :----------------------------- | :--------------------------- |
| **实现方式**   | JVM 内置，M:N 调度                | 编译器转换 + 库                | 单线程事件循环               |
| **语法“染色”** | **无**（API 不变，零学习成本）    | **有**（`suspend` 关键字传染） | **有**（`async/await` 传染） |
| **多核并行**   | **是**（真正利用多核 CPU）        | 是（依赖调度器）               | **否**（受 GIL 限制，单核）  |
| **旧代码兼容** | **完美兼容**（阻塞 API 直接受益） | 阻塞 API 需适配器包装          | 必须使用专门的 async 库      |

Java 21 的虚拟线程在概念上属于“协程”的范畴（即用户态轻量级线程），但它在设计上极其务实。它没有像其他语言那样引入全新的编程范式或关键字，而是通过 JVM 底层的深度改造，让开发者能够用最传统、最自然的同步阻塞代码风格，写出具备顶级异步非阻塞性能的高并发程序。

### 创建虚拟线程

虚拟线程的 API 设计得非常直观，几乎与原生 Thread 保持一致。以下是几种主流的创建方式：

| 创建方式      | 代码示例                                                                                              | 说明                                                                                     |
| :------------ | :---------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------- |
| 快速启动      | `Thread.startVirtualThread(() -> { ... });`                                                           | 最简洁的方式，直接创建并启动一个虚拟线程。                                               |
| Builder 构建  | `Thread.ofVirtual().name("vt-1").start(() -> { ... });`                                               | 链式调用，适合需要自定义线程名、异常处理器等属性的场景。                                 |
| 线程工厂      | `ThreadFactory factory = Thread.ofVirtual().factory();`<br>`Thread vt = factory.newThread(runnable);` | 适合需要与旧版 API 或第三方库集成的场景。                                                |
| 线程池 (推荐) | `ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();`                             | 企业开发中最常用的方式。为每个提交的任务自动分配一个虚拟线程，用完即销毁，无需手动池化。 |

在实际业务中，最推荐结合 ExecutorService 使用，代码风格与传统的线程池完全一致，迁移成本极低：

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadDemo {
    public static void main(String[] args) {
        // 创建一个为每个任务自动启动虚拟线程的执行器
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100_000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // 模拟 I/O 密集型任务（如调用远程接口）
                    System.out.println("虚拟线程 " + Thread.currentThread().getName() + " 正在处理任务: " + taskId);
                    Thread.sleep(1000); // 阻塞操作不会占用底层操作系统线程
                });
            }
        } // try-with-resources 会自动等待所有任务执行完毕并关闭执行器
        System.out.println("所有虚拟线程任务执行完毕！");
    }
}
```

### 注意事项

- 不需要池化：虚拟线程的创建和销毁开销极低，绝对不要对虚拟线程使用传统的固定大小线程池（如 newFixedThreadPool），直接“一任务一线程”即可。
- 小心 synchronized 和 ThreadLocal：
  - 在虚拟线程中使用 synchronized 代码块如果发生阻塞，会导致底层的载体线程被“固定（Pinned）”，从而降低并发性能。建议在虚拟线程中优先使用 ReentrantLock。
  - ThreadLocal 在虚拟线程中依然可用，但由于虚拟线程数量巨大，滥用 ThreadLocal 可能会导致内存占用失控。Java 21 引入了更高效的替代方案 Scoped Values（作用域值）。
- 始终是守护线程：虚拟线程默认且强制是守护线程（Daemon Thread），无法通过 setDaemon(false) 修改。这意味着如果主线程（非守护线程）执行完毕，JVM 会直接退出，不会等待虚拟线程执行结束。

## 线程间交换数据

并行应用常常需要在线程之间交换数据，即线程间传递信息。或者需要协调执行顺序时，通常有如下方式

- 简单的共享变量修改，优先用 Atomic 类或 synchronized；
- 任务分发与处理，首选 BlockingQueue
- 两个线程互相倒手数据，用 Exchanger
- 复杂条件下协调执行顺序，需要根据条件进行等待和唤醒，可以考虑使用底层的 wait/notify 或更高级的 Condition（配合 ReentrantLock 使用）。

### 原子操作类 (Atomic Classes)

在多线程环境下，`i++` 这种操作是不安全的（非原子性）。JUC 提供了 `java.util.concurrent.atomic` 包，利用 CAS (Compare And Swap) 机制实现无锁的线程安全操作。

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {
    // 创建一个初始值为 0 的原子整数
    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) count.incrementAndGet(); // 原子自增
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) count.incrementAndGet();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("最终计数结果：" + count.get()); // 结果一定是 2000
    }
}
```

| 类名          | 常用方法                        | 说明                                     |
| :------------ | :------------------------------ | :--------------------------------------- |
| AtomicInteger | `incrementAndGet()`             | 原子自增 1，并返回自增后的值             |
|               | `getAndIncrement()`             | 原子自增 1，并返回自增前的值             |
|               | `get()`                         | 获取当前值                               |
| AtomicBoolean | `compareAndSet(expect, update)` | 如果当前值等于预期值，则原子地更新为新值 |

### 数据队列 BlockingQueue

最经典的数据交换方式。使用队列作为缓冲区，生产者放入数据，消费者取出数据，队列满或空时会自动阻塞。适用异步处理、任务调度、流量削峰。

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueClassDemo {
    // 创建一个容量为 5 的阻塞队列，作为共享资源
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) throws InterruptedException {
        // 通过 new Thread() 传入独立的 Runnable 实现类
        Thread producer = new Thread(new ProducerTask(), "Producer-Thread");
        Thread consumer = new Thread(new ConsumerTask(), "Consumer-Thread");

        producer.start();
        consumer.start();

        // 主线程运行 5 秒后，优雅地关闭子线程
        Thread.sleep(5000);
        System.out.println("主线程准备关闭子线程...");
        producer.interrupt();
        consumer.interrupt();

        producer.join();
        consumer.join();
        System.out.println("所有线程已安全退出，程序结束。");
    }

    // 独立的生产者任务类
    static class ProducerTask implements Runnable {
        @Override
        public void run() {
            try {
                int i = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    String data = "Data-" + i++;
                    queue.put(data);
                    System.out.println(Thread.currentThread().getName() + " 生产了: " + data);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 收到中断信号，准备退出。");
                Thread.currentThread().interrupt();
            }
        }
    }

    // 独立的消费者任务类
    static class ConsumerTask implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String data = queue.take();
                    System.out.println(Thread.currentThread().getName() + " 消费了: " + data);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 收到中断信号，准备退出。");
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

### 直接交换数据 Exchange

Exchanger 允许两个线程在某个同步点互相交换数据对象。当两个线程都到达交换点时，它们会交换各自的数据。适用遗传算法、校对工作（两个线程互相交换并校对数据）。

```java
import java.util.concurrent.Exchanger;

public class ExchangerDemo {
    private static final Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
       Thread t1 = new Thread(() -> {
            try {
                String dataA = "线程A的数据";
                System.out.println("A 准备交换：" + dataA);
                String received = exchanger.exchange(dataA); // 等待并交换
                System.out.println("A 收到：" + received);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();

      Thread t2 = new Thread(() -> {
            try {
                String dataB = "线程B的数据";
                Thread.sleep(1000); // 模拟延迟，确保 A 先到达
                System.out.println("B 准备交换：" + dataB);
                String received = exchanger.exchange(dataB);
                System.out.println("B 收到：" + received);
            } catch (InterruptedException e) { e.printStackTrace(); }
        }).start();

      t1.start();
      t2.start();
      t1.join();
      t2.join();
    }
}
```

### 底层实现 wait 和 notify

这是 Java 最底层的线程通信机制。线程在条件不满足时调用 `wait()` 释放锁并等待，条件满足后由其他线程调用 `notify()` 或 `notifyAll()` 唤醒。但注意必须在 synchronized 同步块中使用，且判断条件时必须用 while 循环以防止虚假唤醒。

```java
public class WaitNotifyClassDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建共享的缓冲区对象
        Buffer buffer = new Buffer();

        // 通过 new Thread() 传入独立的 Runnable 实现类，并共享同一个 buffer 对象
        Thread producer = new Thread(new ProducerTask(buffer), "Producer-Thread");
        Thread consumer = new Thread(new ConsumerTask(buffer), "Consumer-Thread");

        producer.start();
        consumer.start();

        // 主线程运行 5 秒后，优雅地关闭子线程
        Thread.sleep(5000);
        System.out.println("主线程准备关闭子线程...");
        producer.interrupt();
        consumer.interrupt();

        producer.join();
        consumer.join();
        System.out.println("所有线程已安全退出，程序结束。");
    }

    // 共享的缓冲区类（负责数据的存取与线程通信）
    static class Buffer {
        private final Object lock = new Object();
        private String data;
        private boolean hasData = false;

        public void put(String item) throws InterruptedException {
            synchronized (lock) {
                while (hasData) {
                    lock.wait();
                }
                data = item;
                hasData = true;
                System.out.println(Thread.currentThread().getName() + " 生产了: " + data);
                lock.notifyAll();
            }
        }

        public String take() throws InterruptedException {
            synchronized (lock) {
                while (!hasData) {
                    lock.wait();
                }
                String item = data;
                hasData = false;
                System.out.println(Thread.currentThread().getName() + " 消费了: " + item);
                lock.notifyAll();
                return item;
            }
        }
    }

    // 独立的生产者任务类
    static class ProducerTask implements Runnable {
        private final Buffer buffer;
        // 通过构造函数注入共享资源
        public ProducerTask(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            int i = 0;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    buffer.put("Data-" + i++);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 收到中断信号，准备退出。");
                Thread.currentThread().interrupt();
            }
        }
    }

    // 独立的消费者任务类
    static class ConsumerTask implements Runnable {
        private final Buffer buffer;
        // 通过构造函数注入共享资源
        public ConsumerTask(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    buffer.take();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 收到中断信号，准备退出。");
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

## 线程间同步数据

数据同步，即保证数据一致性。当两个或以上对共享内存的操作发生在并发线程中，并且至少有一个可以改变数据，又没有同步机制的条件下，就会产生竞争条件，可能会导致执行无效代码、bug、或异常行为。

竞争条件最简单的解决方法是使用锁。锁的操作非常简单，当一个线程需要访问部分共享内存时，它必须先获得锁才能访问。此线程对这部分共享资源使用完成之后，该线程必须释放锁，然后其他线程就可以拿到这个锁并访问这部分资源了。很显然，避免竞争条件出现是非常重要的，所以我们要保证，在同一时刻只有一个线程允许访问共享内存。

共享数据协同的线程必须以适当的策略来读写数据，线程的同步原语如下：

- synchronized 内置锁，是 Java 最基础、最常用的同步机制。它通过给对象或方法加锁，确保同一时刻只有一个线程能执行被保护的代码块，从而保证了操作的原子性和可见性，类似其它语言的 Lock 锁。
- ReentrantLock 显式锁，是 java.util.concurrent.locks 包下的一个类，提供了比 synchronized 更灵活的锁操作。它需要手动调用 lock() 和 unlock() 方法。类似其它语言的 RLock 锁。
- ReentrantReadWriteLock 读写锁，是在读多写少的场景下，使用普通的互斥锁会导致读操作之间也互相阻塞，效率低下。ReentrantReadWriteLock 提供了读锁（共享锁）和写锁（排他锁）的分离。
- volatile 是一种轻量级的同步机制，它保证了变量的可见性（一个线程修改，其他线程立即可见）和有序性（禁止指令重排序），但不保证原子性。
- Atomic 无锁原子类，是java.util.concurrent.atomic 包下的类（如 AtomicInteger、AtomicBoolean）通过底层的 CAS（比较并交换）机制，在不使用锁的情况下实现线程安全的原子操作，性能极高。
- Semaphore 信号量，用于控制同时访问特定资源的线程数量（例如限制数据库连接池的最大连接数）。
- CountDownLatch（倒计时门闩）：允许一个或多个线程等待其他线程完成一组操作后再继续执行（例如主线程等待所有子线程加载完配置）。
- CyclicBarrier（循环屏障）：让一组线程到达一个同步点（屏障）后，再同时继续执行（例如多线程分块计算，最后汇总结果）。

> JUC 提供了大量高性能的线程安全集合和辅助类，解决了传统集合（如 HashMap, ArrayList）在多线程下的安全问题。

在 Java 多线程编程中，为了保证数据的一致性，我们需要使用各种同步原语来协调线程对共享资源的访问。Java 提供了丰富的同步机制，主要可以分为以下几大类：

### 内置锁：synchronized 关键字

`synchronized` 是 Java 最基础、最常用的同步机制。它通过给对象或方法加锁，确保同一时刻只有一个线程能执行被保护的代码块，从而保证了操作的**原子性**和**可见性**。

特点：使用简单，锁的获取和释放由 JVM 自动完成，且支持锁重入。

基本使用：

```java
public class SynchronizedDemo {
    private int count = 0;
    private final Object lock = new Object();

    // 1. 同步实例方法：锁住当前实例对象 (this)
    public synchronized void incrementMethod() {
        count++;
    }

    // 2. 同步静态方法：锁住当前类的 Class 对象
    public static synchronized void staticIncrement() {
        // 操作静态变量
    }

    // 3. 同步代码块：锁住指定的对象，粒度更细，性能更好
    public void incrementBlock() {
        synchronized (lock) {
            count++;
        }
    }
}
```

下面示例代码，创建多个生产者和消费者线程，它们共享同一个 Buffer 对象。synchronized 保证同一时间只有一个线程能进入缓冲区，而 wait/notify 则负责线程间的通信（满了就等，空了就等）。

```java
public class SynchronizedInteractionDemo {

    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // 启动 2 个生产者线程和 2 个消费者线程，形成多线程交互
        new Thread(new Producer(buffer), "生产者-A").start();
        new Thread(new Producer(buffer), "生产者-B").start();
        new Thread(new Consumer(buffer), "消费者-X").start();
        new Thread(new Consumer(buffer), "消费者-Y").start();
    }

    // 共享的缓冲区资源
    static class Buffer {
        private final int MAX_SIZE = 5;
        private int count = 0;

        // 生产方法：使用 synchronized 保证原子性
        public synchronized void produce() throws InterruptedException {
            // 使用 while 防止虚假唤醒，缓冲区满时当前线程释放锁并等待
            while (count == MAX_SIZE) {
                System.out.println(Thread.currentThread().getName() + " 发现缓冲区已满，等待中...");
                this.wait();
            }
            count++;
            System.out.println(Thread.currentThread().getName() + " 生产了一个，当前库存: " + count);
            this.notifyAll(); // 唤醒所有在等待的线程（包括消费者和其他生产者）
        }

        // 消费方法
        public synchronized void consume() throws InterruptedException {
            // 缓冲区空时当前线程释放锁并等待
            while (count == 0) {
                System.out.println(Thread.currentThread().getName() + " 发现缓冲区已空，等待中...");
                this.wait();
            }
            count--;
            System.out.println(Thread.currentThread().getName() + " 消费了一个，当前库存: " + count);
            this.notifyAll(); // 唤醒所有在等待的线程
        }
    }

    static class Producer implements Runnable {
        private final Buffer buffer;
        public Producer(Buffer buffer) { this.buffer = buffer; }
        @Override
        public void run() {
            try {
                while (true) {
                    buffer.produce();
                    Thread.sleep((long) (Math.random() * 1000)); // 随机休眠模拟耗时
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    static class Consumer implements Runnable {
        private final Buffer buffer;
        public Consumer(Buffer buffer) { this.buffer = buffer; }
        @Override
        public void run() {
            try {
                while (true) {
                    buffer.consume();
                    Thread.sleep((long) (Math.random() * 1500));
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
}
```

### 显式锁：ReentrantLock

`ReentrantLock` 是 `java.util.concurrent.locks` 包下的一个类，提供了比 `synchronized` 更灵活的锁操作。它需要手动调用 `lock()` 和 `unlock()` 方法。

特点：支持公平锁、可中断获取锁、超时获取锁，以及配合 `Condition` 实现多条件等待，功能更强大。

基本使用：

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // 手动获取锁
        try {
            count++;
        } finally {
            lock.unlock(); // 必须在 finally 中释放锁，防止死锁
        }
    }
}
```

ReentrantLock 的强大之处在于它可以绑定多个 Condition（条件队列）。我们可以让生产者只唤醒消费者，消费者只唤醒生产者，交互更加精准。

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockInteractionDemo {

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        // 同样启动多个线程进行交互
        new Thread(new Producer(buffer), "生产者-A").start();
        new Thread(new Consumer(buffer), "消费者-X").start();
        new Thread(new Consumer(buffer), "消费者-Y").start();
    }

    static class Buffer {
        private final ReentrantLock lock = new ReentrantLock();
        // 创建两个条件：不满（给生产者用）、不空（给消费者用）
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        private int count = 0;

        public void produce() throws InterruptedException {
            lock.lock();
            try {
                while (count == 5) {
                    System.out.println(Thread.currentThread().getName() + " 缓冲区满，在 notFull 条件上等待");
                    notFull.await(); // 生产者在“不满”条件上等待
                }
                count++;
                System.out.println(Thread.currentThread().getName() + " 生产了一个，当前库存: " + count);
                notEmpty.signalAll(); // 生产完毕，精准唤醒在“不空”条件上等待的消费者
            } finally {
                lock.unlock();
            }
        }

        public void consume() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    System.out.println(Thread.currentThread().getName() + " 缓冲区空，在 notEmpty 条件上等待");
                    notEmpty.await(); // 消费者在“不空”条件上等待
                }
                count--;
                System.out.println(Thread.currentThread().getName() + " 消费了一个，当前库存: " + count);
                notFull.signalAll(); // 消费完毕，精准唤醒在“不满”条件上等待的生产者
            } finally {
                lock.unlock();
            }
        }
    }

    // Producer 和 Consumer 的 Runnable 实现类与上面类似，这里省略...
    static class Producer implements Runnable {
        private final Buffer buffer;
        public Producer(Buffer buffer) { this.buffer = buffer; }
        @Override
        public void run() {
            try { while (true) { buffer.produce(); Thread.sleep(500); } }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
    static class Consumer implements Runnable {
        private final Buffer buffer;
        public Consumer(Buffer buffer) { this.buffer = buffer; }
        @Override
        public void run() {
            try { while (true) { buffer.consume(); Thread.sleep(1000); } }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
}
```

### 读写锁：ReentrantReadWriteLock

在读多写少的场景下，使用普通的互斥锁会导致读操作之间也互相阻塞，效率低下。`ReentrantReadWriteLock` 提供了读锁（共享锁）和写锁（排他锁）的分离。

特点：大幅提升读多写少场景下的并发性能。

基本使用：

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private int data = 0;

    // 读操作：加读锁，允许多个线程同时读取
    public void read() {
        rwLock.readLock().lock();
        try {
            System.out.println("读取数据: " + data);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    // 写操作：加写锁，同一时间只允许一个线程写入
    public void write(int newData) {
        rwLock.writeLock().lock();
        try {
            this.data = newData;
            System.out.println("写入数据: " + newData);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
```

### 轻量级同步：volatile 关键字

`volatile` 是一种轻量级的同步机制，它保证了变量的**可见性**（一个线程修改，其他线程立即可见）和**有序性**（禁止指令重排序），但**不保证原子性**。

注意：`volatile` 不能用于 `count++` 这种复合操作，因为它无法保证原子性。

```java
public class VolatileDemo {
    // 适用于作为状态标志位，如停止信号
    private volatile boolean isRunning = true;

    public void stop() {
        isRunning = false; // 修改对其他线程立即可见
    }

    public void doWork() {
        while (isRunning) {
            // 执行具体工作
        }
    }
}
```

### 无锁原子类：Atomic 系列

`java.util.concurrent.atomic` 包下的类（如 `AtomicInteger`、`AtomicBoolean`）通过底层的 CAS（比较并交换）机制，在不使用锁的情况下实现线程安全的原子操作，性能极高。

特点：适用于计数器、序列号生成器等简单的原子操作场景。

基本使用：

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // 原子自增，线程安全
    }

    public int getCount() {
        return count.get();
    }
}
```

原子类没有阻塞和唤醒的概念，它是通过 CAS（比较并交换）不断尝试来保证数据一致性的。非常适合高并发下的计数器场景。

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicInteractionDemo {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        // 启动 5 个线程，每个线程对同一个计数器累加 1000 次
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(new IncrementTask(counter), "线程-" + i);
            threads[i].start();
        }

        // 主线程等待所有子线程执行完毕
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("最终计数结果: " + counter.getCount()); // 预期结果：5000
    }

    static class Counter {
        private final AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            count.incrementAndGet(); // 线程安全的原子自增
        }

        public int getCount() {
            return count.get();
        }
    }

    static class IncrementTask implements Runnable {
        private final Counter counter;
        public IncrementTask(Counter counter) { this.counter = counter; }
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
            System.out.println(Thread.currentThread().getName() + " 完成累加任务");
        }
    }
}
```

- synchronized 像是一个单通道的会议室，大家抢着进去，进去后发现条件不满足（比如满了）就睡觉（wait），走的时候把大家都叫醒（notifyAll）。
- ReentrantLock + Condition 像是给会议室分了不同的休息区，生产者睡在生产者区，消费者睡在消费者区，叫醒时可以精准叫醒对方。
- AtomicInteger 像是大家在一个公开的黑板上改数字，每个人改之前都会看一眼黑板上的数字是不是自己预期的那样，如果是就改掉，如果不是就重试，全程不需要“抢房间”。

### 高级并发工具类

JUC 包还提供了一些专门用于线程协作的同步工具类：

- **Semaphore（信号量）**：用于控制同时访问特定资源的线程数量（例如限制数据库连接池的最大连接数）。
  ```java
  Semaphore semaphore = new Semaphore(5); // 允许5个线程同时访问
  semaphore.acquire(); // 获取许可
  // ... 访问共享资源
  semaphore.release(); // 释放许可
  ```
- **CountDownLatch（倒计时门闩）**：允许一个或多个线程等待其他线程完成一组操作后再继续执行（例如主线程等待所有子线程加载完配置）。
  ```java
  CountDownLatch latch = new CountDownLatch(3); // 设置计数器为3
  // 子线程完成任务后调用 latch.countDown();
  latch.await(); // 主线程等待计数器归零
  ```
- **CyclicBarrier（循环屏障）**：让一组线程到达一个同步点（屏障）后，再同时继续执行（例如多线程分块计算，最后汇总结果）。
  ```java
  CyclicBarrier barrier = new CyclicBarrier(3);
  barrier.await(); // 线程到达屏障后等待，直到3个线程全部到达
  ```

### 最佳实践

- 简单的互斥同步，首选 **`synchronized`**。
- 需要高级锁功能（如超时、公平性），使用 **`ReentrantLock`**。
- 读多写少的场景，使用 **`ReentrantReadWriteLock`**。
- 单一变量的状态标志，使用 **`volatile`**。
- 计数器等简单原子操作，使用 **`Atomic` 类**。
- 复杂的线程协作与流程控制，使用 **`CountDownLatch`、`Semaphore` 等工具类**。
