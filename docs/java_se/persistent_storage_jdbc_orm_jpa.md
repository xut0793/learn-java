# 数据库

“All problems in computer science can be solved by another level of indirection（计算机科学中的所有问题都可以通过增加一个间接层来解决）” -- David Wheeler（剑桥大学计算机科学教授）

在 Java 语言中使用数据库进行数据持久化编程开发中，这条格言同样适用。

## JDBC：地基与标准

> Why：为了解决“各自为战”的混乱

在早期，如果你想连接 MySQL，你得学一套指令；想连接 Oracle，又得学另一套完全不同的指令。这就像每个国家的插座标准都不一样，你每去一个国家都得带个新转接头，非常麻烦。

JDBC（Java Database Connectivity）是 Java 官方提供的一套访问数据库的标准 API。JDBC 的出现为 Java 开发者屏蔽了底层不同数据库厂商（如 MySQL、Oracle）在通信协议上的差异，提供了一套统一的接口。

- 优点：执行效率高，开发者可以直接编写和优化 SQL，对数据库有完全的控制权。
- 缺点：代码极其繁琐（需要手动加载驱动、建立连接、释放资源等），存在大量重复的模板代码；同时需要手动将结果集（ResultSet）映射为 Java 对象，容易出错且开发效率低。

```java
// 传统的 JDBC 操作示例
String url = "jdbc:mysql://localhost:3306/testdb";
String user = "root";
String password = "password";

// 使用 try-with-resources 自动关闭资源，避免资源泄露
try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {

    pstmt.setInt(1, 1);
    try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
            // 手动将数据库字段映射到 Java 对象属性
            String name = rs.getString("username");
            System.out.println("User: " + name);
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

## ORM：思想与进化

> Why：为了解决“手写 SQL 的痛苦”与“对象-关系阻抗失配”）

有 JDBC 虽然统一了接口，我们虽然能连数据库了，但还是得像个“SQL 搬运工”一样，把数据库的 SQL 操作语句手动拼成字符串。

比如，你要存一个用户，你得写 `INSERT INTO users (name, age) VALUES ('Alice', 18)`。

这就产生了一个矛盾：Java 是面向对象的（操作的是类/对象），而数据库是关系型的（操作的是表/行）。 这两者之间存在“阻抗失配”。

另外，不同的数据库方言的 SQL 语法也不一样。比如 MySQL 的 INSERT 语句是 `INSERT INTO`，而 Oracle 的 INSERT 语句是 `INSERT ALL` 等等问题。

ORM (Object-Relational Mapping，对象关系映射) 是一种编程思想。它的核心目标是：让你像操作对象一样操作数据库，彻底告别手写 SQL，并且无需再编写大量的 SQL 和手动映射结果集。

作用：

- 映射： 它建立了一座桥梁，把“数据库的表”映射为“Java 的类”，把“表中的行”映射为“类的实例”。
- 自动化： 当你执行 `user.save()` 时，ORM 会在后台自动帮你生成并执行 `INSERT` 语句，并且抹平不同数据库方言的 SQL 差异，自动处理参数转义，杜绝 SQL 注入。

ORM 是一种编程理论，一个抽象的概念，你可以用 Java 实现 ORM，也可以用 Python 实现（如 Hibernate）。它不是特指某一个库。

早期的 Hibernate 是全自动 ORM 的代表，后来的 MyBatis 则提供了半自动的、更灵活的 SQL 控制能力。

```java
// 以 Hibernate 风格为例，假设 User 是一个已经被 @Entity 注解标记的实体类
User user = new User();
user.setName("张三");

// 开启事务
em.getTransaction().begin();
// 直接保存对象，无需写 INSERT 语句
em.persist(user);
// 提交事务
em.getTransaction().commit();
```

## JPA：ORM 升级

> Why：为了各个 orm 框架之间的语法差异

在 ORM 框架百花齐放（如 Hibernate, TopLink 等）的时期，各个框架的 API 互不兼容，导致项目一旦更换框架就需要重写大量代码。JPA（Java Persistence API）的出现就是为了解决这个问题。

JPA 本身不是框架，而是一套官方制定的 ORM 标准规范（定义在 javax.persistence 包中）。它提供了一系列标准接口和标准注解（如 @Entity, @Id, EntityManager）。你的业务代码面向这些标准接口编程，底层可以轻松切换不同的 ORM 实现框架。

```java
import javax.persistence.*;

@Entity // 标记为 JPA 实体
@Table(name = "users") // 映射到数据库的 users 表
public class User {
    @Id // 标记主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增策略
    private Long id;

    @Column(name = "username") // 映射到 username 字段
    private String name;

    // 省略 Getter 和 Setter
}
```

JDBC 和 JPA 最核心的区别在于：JDBC 是 Java 访问数据库的底层标准 API，而 JPA 是一套基于 ORM（对象关系映射）思想的高级规范。

简单来说，JPA 在底层依然是通过 JDBC 来与数据库交互的，但它充当了一个强大的“翻译官”和“抽象层”，把繁琐的 JDBC 操作和 SQL 语句隐藏了起来。

| 特性         | JDBC (Java Database Connectivity)           | JPA (Java Persistence API)               |
| :----------- | :------------------------------------------ | :--------------------------------------- |
| 核心定位     | 数据库访问的底层 API（面向 SQL）            | 持久化层的 ORM 规范（面向对象）          |
| 开发效率     | 较低（代码繁琐，需手动映射结果集）          | 极高（直接操作对象，自动生成 SQL）       |
| 数据库兼容性 | 较差（强依赖特定数据库的 SQL 语法）         | 极好（数据库无关，轻松跨库迁移）         |
| 性能与控制力 | 极致性能，开发者对 SQL 有完全控制权         | 有缓存等开销，复杂查询优化受限           |
| 异常处理     | 抛出受检异常（如 `SQLException`），必须捕获 | 抛出不受检异常（运行时异常），代码更简洁 |

## orm 框架

在 Java 的数据库操作中，除了原生的 JDBC，目前主流的框架主要可以分为三大流派：

- Spring 官方轻量级封装（JdbcTemplate）
- 全自动 ORM 框架（Hibernate/JPA）
- 半自动/灵活 SQL 框架（MyBatis 及其增强版 MyBatis-Plus）

JdbcTemplate：简单粗暴的性能派，它是 Spring 框架自带的工具，本质上是对原生 JDBC 的一层薄薄封装。它帮你处理了繁琐的数据库连接获取、资源释放和异常转换，但依然要求你手写 SQL。

Hibernate / JPA：面向对象的标准派，Hibernate 是最老牌、功能最强大的全自动 ORM 框架，而 JPA 是它遵循的官方规范。它的核心思想是让开发者完全忘记 SQL，直接像操作内存对象一样操作数据库。

MyBatis：灵活可控的实战派, 在国内互联网行业拥有绝对的统治地位。它是一个“半自动”框架，把 SQL 的控制权完全交给了开发者。你可以通过 XML 或注解来编写任意复杂的 SQL，并手动配置数据库字段与 Java 对象的映射关系。

MyBatis-Plus：集大成者的效率派，它是 MyBatis 的“官方外挂”。在保留 MyBatis 所有灵活性的基础上，它内置了通用的 Mapper 和 Service，仅仅通过少量配置就能实现单表的大部分 CRUD 操作，同时还提供了强大的条件构造器、分页插件和代码生成器。完美解决了原生 MyBatis 编写基础 CRUD 繁琐的痛点，开发效率直逼 JPA，同时又能随时手写原生 SQL 应对复杂场景。

| 框架/技术    | 核心定位与特点                | 优点                                                    | 缺点                                                   |
| :----------- | :---------------------------- | :------------------------------------------------------ | :----------------------------------------------------- |
| JdbcTemplate | Spring 对 JDBC 的轻量级封装   | 性能极佳（接近原生 JDBC），简单可控，无黑盒魔法         | 仍需手写大量 SQL，对象映射需手动处理，开发效率一般     |
| Hibernate    | 全自动 ORM 框架（JPA 的实现） | 开发效率极高，面向对象操作，数据库迁移性好              | 学习成本高，复杂查询优化难，SQL 黑盒导致排查问题较麻烦 |
| MyBatis      | 半自动持久层框架              | SQL 灵活可控，易于性能优化，学习曲线平缓                | 需手动编写所有 SQL 和结果映射，基础 CRUD 代码量大      |
| MyBatis-Plus | MyBatis 的增强工具            | 兼具 MyBatis 的灵活与 JPA 的便捷，内置 CRUD、分页等神器 | 属于非标准的扩展，对原生 MyBatis 有一定封装依赖        |

### 为什么 MyBatis / MyBatis-Plus 没有实现 JPA 标准

MyBatis 和 MyBatis-Plus 都没有实现 JPA 标准。这背后的根本原因在于它们的设计理念和底层逻辑完全不同，属于两条截然不同的技术路线。

JPA (Java Persistence API)：它是一套官方的 ORM（对象关系映射）规范。它的核心思想是“面向对象”，通过注解（如 @Entity、@OneToMany）将 Java 对象与数据库表进行全自动的绑定。JPA 的用法：你定义好实体类，直接调用 userRepository.save(user)，框架自动生成 SQL。Hibernate 是这套规范最主流的实现者。

MyBatis / MyBatis-Plus：它们是 SQL 映射框架。MyBatis 的核心理念是“面向 SQL”，它不强制要求对象和表的完全映射，而是让你手动编写 SQL，框架只负责把 SQL 的结果集映射成 Java 对象。MyBatis 使用时需要在 XML 或注解中写好 `SELECT * FROM user WHERE id = #{id}`，然后调用对应的 Mapper 方法。

MyBatis-Plus 只是在 MyBatis 的基础上做了增强（比如内置了通用的 CRUD 接口），但其底层依然是基于 SQL 映射的，并没有去实现 JPA 的那套规范。MyBatis-Plus 提供了 `BaseMapper<T>` 接口，让你的 Mapper 继承它之后，也能像 JPA 一样直接调用 `insert(), deleteById(), selectList()` 等通用 CRUD 方法，无需手写 SQL。但这仅仅是 MyBatis-Plus 提供的一套自己的便捷 API，在功能上模仿了 JPA 的便捷性，它的底层依然是通过动态拼接 SQL 来实现的，并不是因为它实现了 JPA 标准。

在设计哲学上，PA 试图让开发者“忘记 SQL”，通过操作对象来间接操作数据库。而 MyBatis 的诞生恰恰是为了让开发者“掌控 SQL”，把 SQL 的编写和优化权完全交给开发人员。如果 MyBatis 去实现 JPA，就等于背离了自己“灵活、可控”的初衷。

```
+---------------------------------+
|        应用代码        |
|  (操作 User 对象: user.name = "Bob") |
+------------------+--------------+
                   |
+------------------v--------------+
|    ORM 层 Hibernate / MyBatis   |
| (将 User 对象翻译成 SQL 表达式)    |
+------------------+--------------+
                   |
+------------------v--------------+
|      JPA / Mapper 层          |
| (构建最终的 SQL 语句: UPDATE ...) |
+------------------+--------------+
                   |
+------------------v--------------+
|        JDBC 驱动层             |
|                               |
+------------------+--------------+
                   |
+------------------v--------------+
|        关系型数据库              |
| (如: PostgreSQL, MySQL)          |
+---------------------------------+
```

## 示例

以 MySQL 为例，创建一个 User 类，并使用 HIbernate ORM 层操作数据库，实现 CURD 功能，通常包含以下几个核心步骤：

- 引入依赖
- 配置数据库连接
- 创建实体类
- 编写数据访问层（DAO）
- 测试运行。

1. 安装依赖

```xml
<dependencies>
    <!-- Hibernate 核心依赖 -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.4.0.Final</version>
    </dependency>
    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

然后确保 MySQL 已经启动，并创建一个数据库：

```sql
CREATE DATABASE IF NOT EXISTS test_db DEFAULT CHARSET utf8mb4;
```

2. 配置数据库连接

在 src/main/resources 目录下创建 hibernate.cfg.xml，用于配置数据库连接信息和 Hibernate 的基本属性：

```xml
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 数据库连接配置 -->
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_database_name</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">your_password</property>

        <!-- Hibernate 属性配置 -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hibernate.show_sql">true</property> <!-- 控制台打印 SQL -->
        <property name="hibernate.hbm2ddl.auto">update</property> <!-- 自动更新表结构 -->

        <!-- 注册实体类 -->
        <mapping class="com.example.model.User"/>
    </session-factory>
</hibernate-configuration>
```

3. 创建 User 实体类

使用 JPA 注解将 Java 对象与数据库表进行映射。

```java
package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // 必须提供无参构造方法
    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

```

4. 编写 Hibernate 工具类与 DAO 层

为了代码的优雅，我们先创建一个获取 SessionFactory 的工具类，然后在 DAO 层实现 CRUD 逻辑：

```java
package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        try {
            // 读取 hibernate.cfg.xml 并构建 SessionFactory
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("SessionFactory 初始化失败：" + ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
```

UserDAO.java 实现 CURD

```java
package com.example.dao;

import com.example.model.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class UserDAO {

    // Create (新增)
    public void saveUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user); // Hibernate 6 推荐使用 persist，早期版本用 save
            tx.commit();
            System.out.println("用户新增成功！");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Read (查询单个)
    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    // Read (查询所有)
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    // Update (修改)
    public void updateUser(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user); // merge 用于更新 detached 状态的对象
            tx.commit();
            System.out.println("用户修改成功！");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Delete (删除)
    public void deleteUser(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user); // Hibernate 6 推荐使用 remove，早期版本用 delete
                System.out.println("用户删除成功！");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
```

最后，编写一个 Main 方法来测试我们的 CRUD 功能是否正常。

```java
package com.example;

import com.example.dao.UserDAO;
import com.example.model.User;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 1. 测试新增
        User newUser = new User("张三", "zhangsan@example.com");
        userDAO.saveUser(newUser);

        // 2. 测试查询所有
        System.out.println("查询所有用户：");
        userDAO.getAllUsers().forEach(u -> System.out.println(u.getName() + " - " + u.getEmail()));

        // 3. 测试修改
        User user = userDAO.getUserById(1L);
        if (user != null) {
            user.setName("张三丰");
            userDAO.updateUser(user);
        }

        // 4. 测试删除
        userDAO.deleteUser(1L);
    }
}
```
