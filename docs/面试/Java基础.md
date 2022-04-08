# HashMap get操作时间复杂度

- 在理想状态下，及未发生任何hash碰撞，数组中的每一个链表都只有一个节点，那么get方法可以通过hash直接定位到目标元素在数组中的位置，时间复杂度为O(1)。
- 若发生hash碰撞，则可能需要进行遍历寻找，n个元素的情况下，链表时间复杂度为O(n)、红黑树为O(logn)



# 四种引用特点及是场景

- 强引用（StrongReference）：永远不会被回收，即便OOM；通常我们都是使用的强引用；
- 软引用（SoftReference）：内存不足的时候会被回收；缓存
- 弱引用（WeekReference）：每次GC都会回收；缓存
- 虚引用（PhantomReference）：无法使用虚引用来关联对象；用于垃圾回收跟踪；



# ThreadLocal

## 怎么保证线程安全的

多个线程访问同一个共享变量时，如果不做同步控制，往往会出现「数据不一致」的问题，通常会使用synchronized关键字加锁来解决，ThreadLocal则换了一个思路。

ThreadLocal本身并不存储值，它依赖于Thread类中的ThreadLocalMap，当调用set(T value)时，ThreadLocal将自身作为Key，值作为Value存储到Thread类中的ThreadLocalMap中，这就相当于所有线程读写的都是自身的一个私有副本，线程之间的数据是隔离的，互不影响，也就不存在线程安全问题了。

![image-20220326150916828](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326150916828.png)



## 内存泄漏问题

ThreadLocal的原理是操作Thread内部的一个ThreadLocalMap，这个Map的Entry继承了WeakReference,设值完成后map(WeakReference,value)这样的数据结构。java中的弱引用在内存不足的时候会被回收掉，回收之后变成(null,value)的形式，key被收回掉了。

如果线程执行完之后销毁，value也会被回收,这样也没问题。**<font color=red>但如果是在线程池中，线程执行完后不被回收，而是返回线程池中</font>**，此时ThreadLocal对象已经被销毁，一旦没有引用指向了ThreadLocal，那么Entry只持有弱引用的key就会自动在下一次YGC时被回收，而此时持有强引用的Entry对象并不会被回收。Thread有个强引用指向ThreadLocalMap,ThreadLocalMap有强引用指向Entry,导致value无法被回收，一直存在内存中。

在执行了ThreadLocal.set()方法之后一定要**<font color=red>记得使用ThreadLocal.remove(),将不要的数据移除掉</font>**，避免内存泄漏。

> ThreadLocal在以下几种情况会自发的清理掉key为null的Entry
>
> - 调用set()方法时，采样清理、全量清理，扩容时还会继续检查。
> - 调用get()方法，没有直接命中，向后环形查找时。
> - 调用remove()时，除了清理当前Entry，还会向后继续清理





# JDK代理和Cglib代理

## JDK代理

#### 要求

**<font color=red>jdk代理要求被代理的对象必须实现了某个接口</font>**

#### 核心API

- `InvocationHandler`：代理类需要实现
- `Proxy.newProxyIntance`：构建代理对象

#### 实现

1. 创建代理：代理类需要实现`InvocationHandler`接口，然后通过method.invoke来调用原始方法

   ```java
   public class ProxyClass implements InvocationHandler{
       private Object target;// 被代理对象
       public ProxyClass(Object target) {
           this.target = target;
       }
       //proxy：被代理的对象
       //method：要调用的方法
       //args：参数
       @Override
       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           // 方法调用前增强
           Object invoke = method.invoke(target, args); // 调用代理方法
           // 方法调用后增强
           return invoke; //也可以对原始方法调用的结果进行修改
       }
   }
   ```

2. 构建代理：这里可以通过构建代理工厂来达到方便调用的目的

   ```java
   public class ProxyFactory {
       public static TargetClass getInstance(Object target) {
           InvocationHandler handler = new ProxyClass(target);
           TargetClass proxy = null;
           proxy = (TargetClass) Proxy.newProxyInstance(
                   target.getClass().getClassLoader(),
                   target.getClass().getInterfaces(),
                   handler);
           return proxy;
       }
   }
   ```

3. 使用

   ```java
   ProxyFactory.getIntance(targetObject).xxxMethod();
   ```

## cglib代理

#### 要求

- cglib通过动态修改字节码达到代理的目的，通过集成需要代理的类，然后调用方法的时候，通过super.xxx调用原始方法。所以，<font color=red><b>被代理的类，不能使用final修饰</b></font>

#### 核心API

- `MethodInterceptor`：代理接口，在这里实现代理逻辑
- `Enhancer`：主要的增强类，用来生成代理。

#### 实现

1. 创建代理类

   ```java
   public class ProxyClass implements MethodInterceptor{
   	//methodProxy：方法的代理类
   	@Override
   	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
   		 // 增强
   		Object result =  methodProxy.invokeSuper(obj, args);
           //增强
           return result;
   	}
   
   }
   ```

2. 创建代理工厂

   ```java
   public class ProxyFactory {
   	public static Object getInstance(Class<?> clazz){
   		Enhancer enhancer = new Enhancer();
   		enhancer.setSuperclass(clazz);
           enhancer.setCallback(new ProxyClass());
           return enhancer.create();
   	}
   }
   ```

3. 使用

   ```java
   ProxyFactory.getIntance(TargetClass.class).xxxMethod();
   ```

   

# 设计模式原则

- 单一职责：一个类或者一个方法只负责一项职责，尽量做到类的只有一个行为原因引起变化；
- 里氏替换：子类可以扩展父类的功能，但不能改变原有父类的功能
- 依赖倒置：不应该依赖具体的类，而应该依赖抽象，强调面向接口编程
- 接口隔离：接口的职责尽量小
- 迪米特原则：强调尽量少依赖不需要的类，减少耦合
- 开闭原则：对修改关闭，对扩展开房



# Arthas原理

https://zhuanlan.zhihu.com/p/328974405