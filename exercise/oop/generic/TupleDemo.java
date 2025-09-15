package generic;

public class TupleDemo {

  static Tuple2<String, Integer> f() {
    return new Tuple2<>("a", 1);
  }

  static Tuple3<Car, String, Integer> g() {
    return new Tuple3<>(new Car(), "a", 1);
  }

  static Tuple4<ElectricCar, Car, String, Integer> h() {
    return new Tuple4<>(new ElectricCar(), new Car(), "a", 1);
  }

  static Tuple5<ElectricCar, Car, String, Integer, Double> k() {
    return new Tuple5<>(new ElectricCar(), new Car(), "a", 1, 1.0);
  }

  public static void main(String[] args) {
    Tuple2<String, Integer> ttsi = f();
    System.out.println(ttsi);
    // ttsi.a1 = "there";
    System.out.println(g());
    System.out.println(h());
    System.out.println(k());
  }
}

class ElectricCar extends Car {
}

class Tuple2<A, B> {
  public final A a1;
  public final B a2;

  public Tuple2(A a, B b) {
    this.a1 = a;
    this.a2 = b;
  }

  public String rep() {
    return a1 + ", " + a2;
  }

  @Override
  public String toString() {
    return "(" + rep() + ")";
  }
}

class Tuple3<A, B, C> extends Tuple2<A, B> {
  public final C a3;

  public Tuple3(A a, B b, C c) {
    super(a, b);
    this.a3 = c;
  }

  public String rep() {
    return super.rep() + ", " + a3;
  }
}

class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
  public final D a4;

  public Tuple4(A a, B b, C c, D d) {
    super(a, b, c);
    this.a4 = d;
  }

  public String rep() {
    return super.rep() + ", " + a4;
  }
}

class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {
  public final E a5;

  public Tuple5(A a, B b, C c, D d, E e) {
    super(a, b, c, d);
    this.a5 = e;
  }

  public String rep() {
    return super.rep() + ", " + a5;
  }
}