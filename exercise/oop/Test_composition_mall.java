public class Test_composition_mall {
  public static void main(String[] args) {
    Product product = new Product(1, "商品1", "商品1的描述", 100);
    User user = new User(1, "用户1", "123456", "13800000000");
    Order order = new Order(1, user, product, 2, "收货人1", "13800000000", "收货人1的地址");
  }
}

class Product {
  private int id;
  private String name;
  private String description;
  private double price;

  public Product(int id, String name, String description, double price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public double getPrice() {
    return price;
  }
}

class User {
  private int id;
  private String name;
  private String password;
  private String phone;

  public User(int id, String name, String password, String phone) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.phone = phone;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getPhone() {
    return phone;
  }
}

class OrderItem {
  private Product product;
  private int quantity;

  public OrderItem(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public double computeTotalPrice() {
    return product.getPrice() * quantity;
  }
}

class Order {
  private int id;
  private User user; // 购买用户
  private OrderItem[] items; // 订单条目（购买的产品列表及数量）
  private String receiver; // 收货人
  private String receiverPhone; // 收货人手机号
  private String receiverAddress; // 收货人地址
  private String status; // 订单状态
  private double totalPrice; // 订单总金额

  public Order(int id, User user, Product product, int quantity, String receiver, String receiverPhone,
      String receiverAddress) {
    this.id = id;
    this.user = user;
    this.items = new OrderItem[] { new OrderItem(product, quantity) };
    this.receiver = receiver;
    this.receiverPhone = receiverPhone;
    this.receiverAddress = receiverAddress;
    this.status = "未支付";
    this.totalPrice = product.getPrice() * quantity;
  }

  public int getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public OrderItem[] getItems() {
    return items;
  }

  public String getReceiver() {
    return receiver;
  }

  public String getReceiverPhone() {
    return receiverPhone;
  }

  public String getReceiverAddress() {
    return receiverAddress;
  }

  public String getStatus() {
    return status;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public double computeTotalPrice() {
    double totalPrice = 0;
    for (OrderItem item : items) {
      totalPrice += item.computeTotalPrice();
    }
    return totalPrice;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}