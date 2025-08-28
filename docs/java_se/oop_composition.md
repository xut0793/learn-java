# 组合

在面向对象编程中，组合是建立一种对象间的关系。在代码实现上，就是一个对象包含另一个对象的实例引用。

## 组合的引用实现

比如，商系统中最基本的有产品、用户和订单。

- 产品：包括产品唯一 id、产品名称、产品描述、产品价格等。
- 用户：有用户名、用户密码、用户手机号等。
- 订单：有订单号、下单用户、选购的产品列表及数量、收货人、收货人手机号、收货人地址等。

```java
class Product {
  private int id;
  private String name;
  private String description;
  private double price;

  // 省略其他代码
}

class User {
  private int id;
  private String name;
  private String password;
  private String phone;

  // 省略其他代码
}

// 一个订单可能会有多个产品，每个产品可能有不同的数量，我们用订单条目OrderItem这个类来描述单个产品及选购的数量
class OrderItem {
  private Product product;
  private int quantity;

  // 省略其他代码
}

class Order {
  private int id;
  private User user; // 购买用户
  private OrderItem[] items; // 订单条目（购买的产品列表及数量）

  public Order(int id, User user, Product product, int quantity, ) {
    this.id = id;
    this.user = user;
    this.items = new OrderItem[] { new OrderItem(product, quantity) };
  }
  // 省略其他代码
}
```

在这个代码示例中，订单类在构造函数中接收用户和产品的实例对象的引用，保存在自身的 user 和 items 属性中。在逻辑关系中，这是两种明显不同的关系：

- 一种是包含：Order 与 OrderItem 的关系就是包含，OrderItem 总是从属于某一个 Order。
- 另一种是关联：Order 与 User 的关系就是关联，User 可以独立存在。

总结：**类之间的组合关系在 java 中实现的都是引用关系。**

组合的优点在于它提高了类的灵活性，能够在运行时动态地改变组合对象。

## 组合和继承的区别

组合和继承都是实现类的复用的方式，但它们有不同的适用场景和特点：

- 组合能够实现一种 has-a 关系，即一个类包含另一个类的实例。例如，Car 类包含 Engine 和 Wheel 类的实例。
- 继承能够实现一种 is-a 关系，即一个类是另一个类的子类。例如，Dog 类继承自 Animal 类。

## 何时使用组合或继承

选择使用组合还是继承，取决于具体的需求和关系：

- 当类之间存在 is-a 关系时，使用继承。例如，Dog 类和 Animal 类应该使用继承关系，因为狗是动物。
- 当类之间存在 has-a 的依赖关系时，使用参数传递。例如，Order 类和 Product 类应该使用参数传递关系，因为订单依赖于产品。
- 当类之间存在关联关系时，使用关联关系。例如，User 类和 Order 类应该使用关联关系，因为用户可以有多个订单。
