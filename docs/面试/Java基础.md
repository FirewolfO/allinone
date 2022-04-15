# JAVA创建对象方式

1. new
2. 反射：newInstance
3. 反序列化
4. clone
5. String = "xxx"（特殊）

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



# 设计模式原则

- 单一职责：一个类或者一个方法只负责一项职责，尽量做到类的只有一个行为原因引起变化；
- 里氏替换：子类可以扩展父类的功能，但不能改变原有父类的功能
- 依赖倒置：不应该依赖具体的类，而应该依赖抽象，强调面向接口编程
- 接口隔离：接口的职责尽量小
- 迪米特原则：强调尽量少依赖不需要的类，减少耦合
- 开闭原则：对修改关闭，对扩展开房



# Arthas原理

https://zhuanlan.zhihu.com/p/328974405



# HashMap问题



# HashMap、ConcurrentHashMap底层实现