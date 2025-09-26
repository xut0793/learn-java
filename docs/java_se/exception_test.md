# 测试

## What 什么是测试

应用程序测试是指检查应用程序运行过程是否正常。

测试的方法主要有两种:手动测试和自动化测试。

- 手动测试是通过测试人员或者开发人员通过调试工具操作来检查其是否正常运行的方法。
- 自动化测试是编写测试代码，利用计算机来检查应用程序是否正常运行的方法。

简单说，自动化测试就是利用额外的代码来检查程序的功能代码是否符合预期。

自动化测试常分为:单元测试和集成测试

- 单元测试: 是对应用程序最小单元运行测试的过程。通常，测试的最小单元是**函数**。
  - 优点:
    - 测试运行速度快
    - 有相当于程序文档的功能,帮助新手快速了解功能需求
  - 缺点:
    - 重构代码困难，将一个已经具备完整单元测试的复杂功能拆分为两个单独的功能，需要在更改代码的同时更改相应的单元测试。
    - 只确保单元代码自身行为符合预期，但无法保证各单元之间交互是否正常，这就是为什么需要用到端到端测试的原因。
- 集成测试：是对应用程序多个组件或模块之间或整体程序功能进行测试的过程。
  - 优点:
    - 确保不同组件之间的交互正常运行
  - 缺点:
    - 测试运行速度慢
    - 调试困难

> 在前端领域，自动化测试除了单元测试和集成测试，还有端到端测试。
>
> - 端到端测试：也称功能测试或者 UI 测试，是从用户的角度模拟真实场景,测试应用程序的多个组件是否正常工作。

## Why 为什么需要测试

- 提高代码质量：通过测试可以发现代码中的逻辑错误、边界条件问题等，从而提高代码的可靠性和稳定性。
- 减少维护成本：在开发早期发现并修复缺陷，比在后期发现和修复要容易得多，能够显著降低维护成本。另外，自动化测试能减少人工操作，提高测试效率。
- 促进代码重构：有了完善的测试用例，开发者可以更放心地对代码进行重构，而不用担心引入新的缺陷。

## How 如何进行测试

在 Java 应用开发中，单元测试常用测试工具是 JUnit。它提供了一系列的注解和断言方法，方便开发者编写和执行测试用例。

## JUnit 基本使用

### 第一步：引入 JUnit 依赖

在 Maven 项目中，引入 JUnit 依赖的方式如下：

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
```

### 第二步：编写测试用例

在 `src/test/java` 目录下创建一个类，用于编写测试用例。测试用例类的命名规则是在被测试类的类名后面添加 Test。

JUnit 中五个最常用的也是最基础的注解：

- @Test：标记测试方法
- @BeforeEach：每个测试方法执行前执行
- @AfterEach：每个测试方法执行后执行
- @BeforeAll：测试类执行前执行一次
- @AfterAll：测试类执行后执行一次

一个完整的测试用例，示例代码如下：

```java
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {
    private static DatabaseConnection dbConnection;
    private BankAccount account;

    @BeforeAll
    public static void connectDB() {
        System.out.println("1. 建立数据库连接 - 整个测试类只执行一次");
        dbConnection = DatabaseConnection.getInstance();
    }

    @AfterAll
    public static void closeDB() {
        System.out.println("5. 关闭数据库连接 - 整个测试类只执行一次");
        dbConnection.close();
    }

    @BeforeEach
    public void initAccount() {
        System.out.println("2. 初始化账户 - 每个测试方法前执行");
        account = new BankAccount("张三", 1000.0);
    }

    @AfterEach
    public void resetAccount() {
        System.out.println("4. 重置账户状态 - 每个测试方法后执行");
        account = null;
    }

    @Test
    public void testDeposit() {
        System.out.println("3. 测试存款功能");
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWithdraw() {
        account.withdraw(-100.0);
    }

    @Test(timeout = 1000)
    public void testLongOperation() {
        account.calculateInterest();
    }
}
```

### 第三步：运行测试用例

可以使用 IDE 中的测试运行器来运行测试用例，也可以使用 Maven 命令 `mvn test` 来运行测试用例。

上面只是一个基本的测试过程，JUnit 使用还涉及到更多概念，比如断言、插桩 stub、mock 等，可以参考 [JUnit 5 用户指南](https://junit.java.net.cn/junit5/docs/current/user-guide/#extensions)。

> [JUnit5](https://junit.java.net.cn/junit5/)

## 测试覆盖率

代码覆盖率是衡量测试用例对代码的覆盖程度的指标，常见的测试覆盖率指标有：

- 语句覆盖率：测试用例是否覆盖到了所有的语句。
- 分支覆盖率：测试用例是否覆盖到了所有的分支（如 if 语句的 true 和 false 分支）。
- 函数覆盖率：测试用例是否覆盖到了所有的函数。

可以使用工具如 JaCoCo 来生成代码覆盖率报告，帮助开发者发现未被测试覆盖的代码。

## 持续集成

持续集成是指在开发过程中，频繁地将代码集成到主干分支（如 main 或 release 分支）中，并自动运行测试用例。这样可以及时发现集成错误，确保代码的质量。

常用的持续集成工具包括 Jenkins、GitLab CI/CD 、github action、或者独立的 devops 平台等。

## 测试驱动开 TDD

测试驱动开发（Test-Driven Development，TDD）是一种开发方法论，它强调在编写功能代码之前先编写测试用例。TDD 的流程如下：

1. 编写一个失败的测试用例，用于描述预期的功能行为。
2. 运行测试用例，确认它失败了。
3. 编写足够的代码，使测试用例通过。
4. 重复以上步骤，直到完成所有功能的开发。
