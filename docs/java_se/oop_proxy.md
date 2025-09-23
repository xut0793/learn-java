# 代理

代理 Proxy 在软件编程中通常是指一种设计模式：代理模式，它的作用是为其他对象提供一种代理以控制对这个对象的访问。

## 静态代理

要实现代理模式，可以通过类的组合形式实现。

```java
interface IService {
  public void sayHello();
}

class RealService implements IService {
  @Override
  public void sayHello() {
      System.out.println("hello");
  }
}

class ProxyService implements IService {
  private IService realService;
  public ProxyService(IService realService) {
      this.realService = realService;
  }
  @Override
  public void sayHello() {
    System.out.println("entering sayHello");
    this.realService.sayHello();
    System.out.println("leaving sayHello");
  }
}

public class SimpleStaticProxyDemo {
  public static void main(String[] args) {
    IService realService = new RealService();
    IService proxyService = new ProxyService(realService);
    proxyService.sayHello();
  }
}

// 程序输出
// entering sayHello
// hello
// leaving sayHello
```

代理类和实际类一般都实现至同一个接口，在上述例子中，共同的接口是 `IService`，被代理的实际对象的类是 `RealService`，代理对象的类是 `ProxyService`。

在代理类 `ProxyService` 内部有一个 `IService` 类型的成员变量 `realService`，用于持有实际对象的引用。在构造方法中被初始化，然后对方法的调用，实际是委托给了 `realService` 来处理。

像这种在将代理类的逻辑在程序中直接编码的形式，我们称之为静态代理。

静态代理突出的问题的是，静态代理只能代理实现了某个接口的类，这会限制代理的范围。

- 比如接口中新增了方法或删减了方法，那么代理类就需要进行修改；
- 另外，如果对通用的需求，比如打日志、跟踪调试信息这类需求实现代理，不可能为每一个类都写一个代理类，这会导致代码的重复和维护成本的增加。

这里就需要动态代理了。

## 动态代理 Java SDK

在静态代理中，代理类是直接定义在代码中的。在动态代理中，代理类是动态生成的。

具体怎么动态生成呢？我们用动态代理实现前面同样的例子。

```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface IService {
  public void sayHello();
}

class RealService implements IService {
  @Override
  public void sayHello() {
      System.out.println("hello");
  }
}

class SimpleInvocationHandler implements InvocationHandler {
  private Object realObj;
  public SimpleInvocationHandler(Object realObj) {
      this.realObj = realObj;
  }
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("entering " + method.getName());
    Object result = method.invoke(realObj, args);
    System.out.println("leaving " + method.getName());
    return result;
  }
}

public class SimpleJDKDynamicProxyDemo {
  public static void main(String[] args) {
    IService realService = new RealService();
    IService proxyService = (IService) Proxy.newProxyInstance(
        IService.class.getClassLoader(),
        new Class<?>[] { IService.class },
        new SimpleInvocationHandler(realService));

    proxyService.sayHello();
  }
}
```

要创建一个动态代理对象，需要使用 `java.lang.reflect` 包导出的 `Proxy` 类的 `newProxyInstance` 方法。方法的签名如下：

```java
public static Object newProxyInstance(
  ClassLoader loader,
  Class<?>[] interfaces,
  InvocationHandler h
)
```

`newProxyInstance` 方法接受三个参数：

- `loader`：类加载器，用于加载代理类，可以从要代理实现的接口中获取类加载器，比如`IService.class.getClassLoader()`；
- `interfaces`：被代理类实现的接口列表，可以是一个或多个接口，比如上例中创建一个 `Class` 类型的数组，元素是 `IService.class`；
- `h`：一个实现 `InvocationHandler` 接口的实例，用于处理代理对象的方法调用，在这里实现代理的核心逻辑。

`InvocationHandler` 接口实现类，可以在构造方法中传入被代理的对象，然后要求实现 `invoke` 方法，对代理对象中所有实现的接口 `IService` 中方法的调用都会被转发到 `invoke` 方法中，它接受三个参数：

- `proxy`：代理对象本身，即 `newProxyInstance` 方法返回的对象；它不是被代理的对象，这个参数一般用处不大。
- `method`：被调用的方法；
- `args`：方法的参数列表。

在 `invoke` 方法中实现代理的核心逻辑，比如在调用实际对象的方法前后输出跟踪调试信息。

相比静态代理，使用动态代理，可以编写通用的代理逻辑，用于各种类型的被代理对象，而不需要为每个被代理的类都创建一个静态代理类。

比如如下例子，有两个接口`IServiceA`和`IServiceB`，它们对应的实现类是`ServiceAImpl`和`ServiceBImpl`，虽然它们的接口和实现不同，但利用动态代理，它们可以调用同样的方法`getProxy`获取代理对象，共享同样的代理逻辑`SimpleInvocationHandler`，即在每个方法调用前后输出一条跟踪调试的语句。

```java
interface IServiceA {
  public void sayHello();
}

class ServiceAImpl implements IServiceA {
  @Override
  public void sayHello() {
    System.out.println("hello");
  }
}

interface IServiceB {
  public void fly();
}

class ServiceBImpl implements IServiceB {
  @Override
  public void fly() {
    System.out.println("flying");
  }
}

class SimpleInvocationHandler implements InvocationHandler {
  private Object realObj;
  public SimpleInvocationHandler(Object realObj) {
      this.realObj = realObj;
  }
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("entering " + realObj.getClass().getSimpleName() + "::" + method.getName());
    Object result = method.invoke(realObj, args);
    System.out.println("leaving " + realObj.getClass().getSimpleName() + "::" + method.getName());
    return result;
  }
}

public class GeneralProxyDemo {
  private static <T> T getProxy(Class<T> intf, T realObj) {
    return (T) Proxy.newProxyInstance(intf.getClassLoader(), new Class<?>[] { intf }, new SimpleInvocationHandler(realObj));
  }
  public static void main(String[] args) throws Exception {
    IServiceA a = new ServiceAImpl();
    IServiceA aProxy = getProxy(IServiceA.class, a);
    aProxy.sayHello();

    IServiceB b = new ServiceBImpl();
    IServiceB bProxy = getProxy(IServiceB.class, b);
    bProxy.fly();
  }
}
```

上述动态代理使用 Java SDK 内置语法实现，它只能为接口创建代理，返回的代理对象也只能转换到某个接口类型，如果一个类没有接口，或者希望代理非接口中定义的方法，那就没有办法了。

## 动态代理 cglib

cglib 是一个社区里的第三方类库，它可以在运行时动态生成新的类。cglib 可以为没有实现接口的类创建代理，在 Spring、Hibernate 等框架中被广泛使用。

```java
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


class RealService {
  public void sayHello() {
    System.out.println("hello");
  }
}

// 代理逻辑
class SimpleInterceptor implements MethodInterceptor {
  @Override
  public Object intercept(
            Object object,
            Method method,
            Object[] args,
            MethodProxy proxy
        ) throws Throwable {
      System.out.println("entering " + method.getName());
      Object result = proxy.invokeSuper(object, args);
      System.out.println("leaving " + method.getName());
      return result;
  }
}

public class SimpleCGLibDemo {
  private static <T> T getProxy(Class<T> cls) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(cls);
    enhancer.setCallback(new SimpleInterceptor());
    return (T) enhancer.create();
  }
  public static void main(String[] args) throws Exception {
    RealService proxyService = getProxy(RealService.class);
    proxyService.sayHello();
  }
}
```

`RealService` 表示被代理的类，它没有实现的接口。定义 `getProxy()`为一个类生成代理对象，这个代理对象可以安全地转换为被代理类的类型，它使用了 `cglib` 的 `Enhancer` 类。

`Enhancer` 类的`setSuperclass`设置被代理的类，`setCallback`设置被代理类的 `public` 非`final`方法被调用时的处理类。

`Enhancer` 支持多种类型，这里使用的类实现了 `MethodInterceptor` 接口，它与 JavaSDK 中的 `InvocationHandler` 有点类似，方法名称变成了 `intercept` ，多了一个`MethodProxy`类型的参数。

在 main 方法中，我们也没有创建被代理的对象，创建的对象直接就是代理对象，它的类型是 `RealService`。

## Java SDK 和 cglib 实现动态代理的区别

- Java SDK 实现的动态代理只能为接口创建代理，而 cglib 可以为没有实现接口的类创建代理。
- 实现机制不同，Java SDK 实现的动态代理是基于反射的，生成的代理对象和实际对象是兄弟关系，实现于相同的接口。而 cglib 实现的动态代理是基于继承的方式来实现的，代理对象是被代理对象的子类。
- Java SDK 实现的动态代理在调用代理对象的方法时，会通过反射调用被代理对象的方法，而 cglib 实现的动态代理则是通过生成子类的方式来实现的，因此 cglib 的性能要高一些。

## 应用场景

动态代理在很多场景下都有应用，比如实现 Spring 中的 AOP 功能，或者实现 Hibernate 中的延迟加载等。
