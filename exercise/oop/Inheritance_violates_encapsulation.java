/**
 * 继承破坏封装例子
 * 出处：《Java 编程的逻辑》4.4中代码清单 4-10
 */
public class Inheritance_violates_encapsulation {
  public static void main(String[] args) {
    Child c = new Child();
    c.addAll(new int[] { 1, 2, 3 });
    System.out.println(c.getSum()); // 期望输出是 6（1+2+3)，但实际输出12
  }
}

class Base {
  private static final int MAX_NUM = 1000;
  private int[] arr = new int[MAX_NUM];
  private int count;

  public void add(int number) {
    if (count < MAX_NUM) {
      arr[count++] = number;
    }
  }

  public void addAll(int[] numbers) {
    for (int number : numbers) {
      add(number);
    }
  }

  public void clear() {
    for (int i = 0; i < count; i++) {
      arr[i] = 0;
    }
    count = 0;
  }
}

class Child extends Base {
  private long sum;

  @Override
  public void add(int number) {
    super.add(number);
    sum += number;
  }

  @Override
  public void addAll(int[] numbers) {
    super.addAll(numbers);
    for (int number : numbers) {
      sum += number;
    }
  }

  public long getSum() {
    return sum;
  }

  @Override
  public void clear() {
    super.clear();
    sum = 0;
  }
}
