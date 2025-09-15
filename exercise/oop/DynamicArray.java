import java.util.Arrays;
import java.util.Random;

/**
 * 简单实现的动态数组容器。
 * 所谓动态数组，就是长度可变的数组。模拟 ArrayList 的简化版
 * 
 * @param <E>
 */
public class DynamicArray<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elementData;

    public DynamicArray() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    public static void main(String[] args) {
        DynamicArray<Double> arr = new DynamicArray<Double>();
        Random rnd = new Random();
        int size = 1 + rnd.nextInt(100);

        for (int i = 0; i < size; i++) {
            arr.add(Math.random());
        }

        Double d = arr.get(rnd.nextInt(size));
        System.out.println(d);

        DynamicArray<Number> numbers = new DynamicArray<Number>();
        DynamicArray<Integer> ints = new DynamicArray<Integer>();
        ints.add(100);
        ints.add(34);
        // numbers是一个Number类型的容器，ints是一个Integer类型的容器，我们希望将ints添加到numbers中，因为Integer是Number的子类，应该说，这是一个合理的需求和操作
        // addAll需要的参数类型为DynamicArray<Number>，而传递过来的参数类型为DynamicArray<Integer>，
        // 虽然Integer是Number的子类，但DynamicArray<Integer>并不是DynamicArray<Number>的子类，DynamicArray<Integer>的对象也不能赋值给DynamicArray<Number>的变量，这一点初看上去是违反直觉的，但这是事实，必须要理解这一点。
        // 要想 DynamicArray<Integer> 可以赋值给 DynamicArray<Number>，需要在 addAll 函数声明时明确
        // Integer 是 Number 的子类型：
        // 解决1： public <T extends E> void addAll(DynamicArray<T>) {}
        // 解决2：<? extends E> 上界通配符
        numbers.addAll(ints);
    }

    /**
     * 通过 ensureCapacity 方法根据需求扩展数组
     * 
     * @param minCapacity
     */
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;

        if (oldCapacity >= minCapacity)
            return;

        int newCapacity = oldCapacity * 2;

        if (newCapacity < minCapacity)
            newCapacity = minCapacity;

        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    public void add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
    }

    public E get(int index) {
        return (E) elementData[index];
    }

    public int size() {
        return size;
    }

    public E set(int index, E element) {
        E oldValue = get(index);
        elementData[index] = element;
        return oldValue;
    }

    // 解决1：<T extends E>
    // public <T extends E> void addAll(DynamicArray<T> c) {
    // for (int i = 0; i < c.size; i++) {
    // add(c.get(i));
    // }
    // }

    // 解决2：<? extends E> 上界通配符，其中 ? 表示未知类型，<? extends E> 表示未知类型是 E 的子类
    public void addAll(DynamicArray<? extends E> c) {
        for (int i = 0; i < c.size; i++) {
            add(c.get(i));
        }
    }

    /*
     * 同样是extends关键字，同样应用于泛型，<T extends E>和<? extends E>到底有什么区别呢？
     * 
     * 主要是声明的位置不一样
     * 1. <T extends E> 是在方法签名中声明的，用于定义类型参数，它声明了一个类型参数 T，可以放在泛型类定义中类名后面，泛型方法方法返回值前面
     * 2. <? extends E> 是在方法参数中声明的，用于实例化类型参数 c ，表示 c 是一个未知类型的动态数组，未知类型是 E 的子类。
     * 
     * 虽然它们不一样，但两种写法经常可以达成相同目标。
     * 通配符形式，只需要在参数前面写一次；类型参数，除了在参数前面写，还需要在返回值前面进行声明。
     * 
     */
}