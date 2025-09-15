# 内部接口

在前面 [内部类](oop_inner_class.md) 中，我们介绍了内部类的概念。内部类可以扮演静态成员、实例成员、局部变量的角色，同样，接口也可以扮演成员的角色，用于约束内部类的实现。

## 内部接口

内部接口是指定义在类内部的接口。它有以下几个特征：

- 内部接口无论是否使用 static 修饰，默认都会添加 static 修饰符，扮演静态成员的角色
- 内部接口也可以被成员的访问限制修饰符进行修饰，而普通接口则不行。
- 当使用 private 访问限制修饰符修饰内部接口时，意味着该接口只能由其外部类中的某个内部类来实现，其他类不能实现该接口。因为对于外界来说，private 修饰的成员都是不可见的。
- 内部接口不能扮演局部的角色，否则编译报错。这是因为接口的设计初衷是对外公布的，让很多类可以来进行实现，而局部的角色显然违背了接口的设计初衷。

```java
1   //代码实现
2   //定义外部类
3   class OuterClass
4   {
5        //定义非静态内部接口
6        public interface InnerInterface
7        {
8            public void show();
9        }
10       //定义实现自InnerInterface接口的内部类
11       public class InnerClass implements InnerInterface
12       {
13           //实现接口中的方法
14           public void show()
15           {
16               System.out.println("这里是内部类中的方法，该内部"
17               +"类实现了私有内部接口InnerInterface！！！");
18           }
19       }
20       //定义获得该内部类对象的方法
21       public InnerInterface getIn()
22       {
23           return new InnerClass();
24       }
25  }
26  //实现内部接口的普通类
27  class Common implements OuterClass.InnerInterface
28  {
29           //实现接口中的方法
30           public void show()
31           {
32               System.out.println("这里是普通类中的方法，该普通"
33               +"类实现了私有内部接口InnerInterface！！！");
34           }
35  }
36  //主类
37  public class Sample15_15
38  {
39       public static void main(String[] args)
40       {
41           //创建外部类对象
42           OuterClass oc=new OuterClass();
43           //声明内部接口的引用
44           OuterClass.InnerInterface ic=null;
45           //获得实现内部接口的内部类对象
46           ic=oc.getIn();
47           //访问内部接口中的方法
48           ic.show();
49           //创建实现内部接口的普通类对象
50           ic=new Common();
51           //访问内部接口中的方法
52           ic.show();
53       }
54  }
```

## 嵌套的内部接口

内部接口也可以定义在另外一个接口中。可以从两个角度去看待接口中的内部接口：

- 从内部接口里面来看，其就是一个接口，需要满足接口的所有规则，具有接口的所有能力。
- 从外部接口的角度来看，内部接口就相当于接口中的一个成员，需要满足接口中成员的所有规则，例如内部接口同样默认为 static，不能用 private 进行修饰，但是可以用 public、protected、default 进行修饰。

使用嵌套的内部接口语法： `class 类名 implements 外部接口名.内部接口名 {}`

```java
1   //代码实现
2   //定义外部接口
3   interface OuterInterface
4   {
5        //定义内部接口
6        interface InnerInterface
7        {
8            //定义内部接口中的方法
9            public void inShow();
10       }
11       //定义外部接口中的方法
12       public void outShow();
13  }
14  //定义实现自内部接口的类
15  class InnerInterfaceImpl implements OuterInterface.InnerInterface
16  {
17       //实现内部接口中的方法
18       public void inShow()
19       {
20           System.out.println("恭喜你，这个类成功地实现了内部接口！！！");
21       }
22  }
23  //定义实现外部接口的方法
24  class OutInterfaceImpl implements OuterInterface
25  {
26       //实现外部接口中的方法
27       public void outShow()
28       {
29           System.out.println("恭喜你，这个类成功地实现了外部接口！！！");
30       }
31  }
32  //主类
33  public class Sample15_16
34  {
35       public static void main(String[] args)
36       {
37           //创建实现自内部接口与外部接口类的对象
38           OuterInterface.InnerInterface iic=new InnerInterfaceImpl();
39           OuterInterface oic=new OutInterfaceImpl();
40           //调用两个对象中的方法
41           iic.inShow();
42           oic.outShow();
43       }
44  }
```
