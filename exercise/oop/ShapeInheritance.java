public class ShapeInheritance {
  // #region execute
  public static void main(String[] args) {
    // Point center = new Point(2, 3);
    // Circle circle = new Circle(center, 2);
    // circle.draw();
    // System.out.println(circle.area());

    ShapeManager manager = new ShapeManager();
    manager.addShape(new Circle(new Point(4, 4), 2));
    manager.addShape(new Line(new Point(2, 3), new Point(3, 4), "green"));
    manager.addShape(new ArrowLine(new Point(1, 2), new Point(5, 5), "red", false, true));

    manager.draw();
  }
  // #endregion execute
}

// #region ShapeManager

/**
 * Point 类
 * 表示二维平面上的一个点
 */
class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public double distance(Point p) {
    int dx = this.x - p.getX();
    int dy = this.y - p.getY();
    return Math.sqrt(dx * dx + dy * dy);
  }

  @Override
  public String toString() {
    return "Point(" + x + ", " + y + ")";
  }
}

/**
 * 形状类
 * 表示二维平面上的一个形状
 * 包含颜色属性 color
 * 包含绘制方法 draw()
 */
class Shape {
  private static final String DEFAULT_COLOR = "black";
  private String color;

  public Shape() {
    this(DEFAULT_COLOR);
  }

  public Shape(String color) {
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void draw() {
    System.out.println("Drawing a shape");
  }

  @Override
  public String toString() {
    return "Shape{" +
        "color='" + color + '\'' +
        '}';
  }
}

/**
 * 圆形
 * 1. 继承自形状类，但子类不能直接访问父类私有的属性，直接通过父类公开的接口 getColor() 获取
 * 2. 自定义属性：中心点 center 和半径属性 radius
 * 3. 自定义方法：计算面积的方法 area()
 * 4. 重写继承的 draw() 方法
 */
class Circle extends Shape {
  // 中心点
  private Point center;
  // 半径
  private int radius;

  public Circle(Point center, int radius) {
    this.center = center;
    this.radius = radius;
  }

  public double area() {
    return Math.PI * radius * radius;
  }

  @Override
  public void draw() {
    System.out
        .println("Drawing a circle at " + center.toString() + "with radius " + radius + ", using color: " + getColor());
  }
}

/**
 * 直线
 * 1. 继承自形状类
 * 2. 自定义属性：起点 startPoint 和终点 endPoint
 * 3. 自定义方法：计算长度的方法 length()
 * 4. 重写继承的 draw() 方法
 */
class Line extends Shape {
  private Point start;
  private Point end;

  public Line(Point start, Point end, String color) {
    // super用于指代父类，可用于调用父类构造方法，访问父类方法和变量。必须放在子类构造方法的第一行
    super(color);
    this.start = start;
    this.end = start;
  }

  public Point getStart() {
    return start;
  }

  public Point getEnd() {
    return end;
  }

  public double length() {
    return start.distance(end);
  }

  @Override
  public void draw() {
    System.out
        .println("Drawing a line from " + start.toString() + " to " + end.toString() + ", using color: " + getColor());
  }
}

class ArrowLine extends Line {
  private boolean startArrow;
  private boolean endArrow;

  public ArrowLine(Point start, Point end, String color, boolean startArrow, boolean endArrow) {
    super(start, end, color);
    this.startArrow = startArrow;
    this.endArrow = endArrow;
  }

  @Override
  public void draw() {
    // 注意这里
    // super.draw()表示调用父类的draw()方法，这时候不带super．是不行的，因为当前的方法也叫draw()，如果不带super，就会调用当前类的draw()方法，这时候就会无限递归调用，栈溢出。
    super.draw();

    if (startArrow) {
      System.out.println("Drawing a start arrow");
    }
    if (endArrow) {
      System.out.println("Drawing a end arrow");
    }
  }
}

class ShapeManager {
  private static final int MAX_NUM = 100;
  private Shape[] shapes = new Shape[MAX_NUM];
  private int shapeNum = 0;

  public void addShape(Shape shape) {
    if (shapeNum < MAX_NUM) {
      shapes[shapeNum++] = shape;
    }
  }

  public void draw() {
    for (Shape s : shapes) {
      if (s != null) {
        // ShapeManager并不知道每个shape具体的类型，也不关心，但可以调用到子类的draw方法。这就是多态
        s.draw();
      }
    }
  }
}
// #endregion ShapeManager
