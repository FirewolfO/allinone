[toc]

# JAVA创建对象方式

1. new
2. 反射：newInstance
3. 反序列化
4. clone
5. String = "xxx"（特殊）

# HashMap 

## 底层实现

元素用Node维护：`static class Node<K,V> implements Map.Entry<K,V>`

- JDK7：数组（hash桶） + 链表
- JDK8：数组 +  链表 + 红黑树

## put操作

1. 判断当前桶是否为空，空的就需要初始化（resize 中会判断是否进行初始化）。
2. 根据当前 key 的 hashcode 定位到具体的桶中并判断是否为空，为空表明没有 Hash 冲突就直接在当前位置创建一个新桶即可。
3. 如果当前桶有值（ Hash 冲突），那么就要比较当前桶中的 `key、key 的 hashcode` 与写入的 key 是否相等，相等就赋值给 `e`,在第 8 步的时候会统一进行赋值及返回。
4. 如果当前桶为红黑树，那就要按照红黑树的方式写入数据。
5. 如果是个链表，就需要将当前的 key、value 封装成一个新节点插入链表（java7是插在头部，java8插在尾部）
6. 接着判断当前链表的大小是否大于预设的阈值，大于时就要转换为红黑树（java8）。
7. 如果在遍历过程中找到 key 相同时直接退出遍历。
8. 如果 `e != null` 就相当于存在相同的 key,那就需要将值覆盖。
9. 最后判断是否需要进行扩容。

## get操作

### 操作步骤

- 首先将 key hash 之后取得所定位的桶。
- 如果桶为空则直接返回 null 。
- 否则判断桶的第一个位置(有可能是链表、红黑树)的 key 是否为查询的 key，是就直接返回 value。
- 如果第一个不匹配，则判断它的下一个是红黑树还是链表。
- 红黑树就按照树的查找方式返回值。
- 不然就按照链表的方式遍历匹配返回值

### 时间复杂度

- 在理想状态下，及未发生任何hash碰撞，数组中的每一个链表都只有一个节点，那么get方法可以通过hash直接定位到目标元素在数组中的位置，时间复杂度为O(1)。
- 若发生hash碰撞，则可能需要进行遍历寻找，n个元素的情况下，链表时间复杂度为O(n)、红黑树为O(logn)

## Hash并发问题

参考文献：[HashMap并发问题](https://mbd.baidu.com/ug_share/mbox/4a81af9963/share?tk=7adc8ca58db343ec780f9cbcb2ead558&share_url=https%3A%2F%2Fkfs479.smartapps.cn%2Fpages%2Fblogdetail%2Fblogdetail%3F_swebfr%3D1%26id%3D2592161%26_swebFromHost%3Dbaiduboxapp)

- JDK1.7 HashMap线程不安全体现在：死循环（头插法导致，会将链表的顺序反转）、数据丢失

- JDK1.8 HashMap线程不安全体现在：数据覆盖

  

# ConcurrentHashMap

元素用Node维护：`static class Node<K,V> implements Map.Entry<K,V>`

参考文献：

- https://blog.csdn.net/qq_39625353/article/details/107019472
- https://blog.csdn.net/xuxinyuande/article/details/105738873
- size：https://blog.csdn.net/weixin_41563161/article/details/106382887

## 底层结构

### JDK7

 **Segment 数组 + HashEntry 数组 + 链表，同步使用Lock实现，锁粒度为Segment**

![image-20220416103005463](https://gitee.com/firewolf/allinone/raw/master/images/image-20220416103005463.png)

#### put操作

1. 第一次计算key的hash，找到Segment元素的位置
2. 判断当前Segment元素是否初始化，若没有初始化，则通过CAS进行初始化
3. 第二次计算key的hash，找到HashEntry数组的位置；
4. 由于Segment继承了ReentrantLock锁，所以TryLock() 尝试获取锁，如果锁获取成功，将数据插入到HashEntry位置，如果遇到Hash冲突，则插入到链表的末端；如果锁被其他线程获取，那么就会以自旋的方式重新获取锁，超过指定的次数之后还获取不到的话，就会挂起，等待唤醒；

#### get操作

1. 第一次计算key的hash，找到Segment元素的位置；
2. 第二次计算key的hash，找到HashEntry数组的位置；
3. 遍历链表，匹配就返回，否则返回null；

#### size操作

1. 采用不加锁的方式，多次计算count，（最多三次），比较结果，如果相同的话，就准确，返回结果；否则的话认为计算的结果不准确，转2；
2. 如果1不成功，则采用加锁的方式，给所有Segment元素加锁，计算出count值，这个是准确的；



### JDK8

**Node 数组 + 链表 / 红黑树，同步使用CAS + Synchronized，锁粒度为HashEntry**

![image-20220416101717080](https://gitee.com/firewolf/allinone/raw/master/images/image-20220416101717080.png)

#### put操作

1. 计算hashCode
2. 判断是否需要初始化，如果没有初始化就先调用initTable（）方法来进行初始化过程
   - 这里会判断一个变量sizeCtl
     - -1 说明正在初始化
     - -N 说明有N-1个线程正在进行扩容
     - 表示 table 初始化大小，如果 table 没有初始化
     - 表示 table 容量，如果 table　已经初始化。
3. 如果没有hash冲突就直接CAS插入，失败则自旋保证成功
4. 如果当前位置的 `hashcode == MOVED == -1表明还在进行扩容操作就先进行扩容；
5. 如果以上都不满足，就通过synchronized加锁来保证线程安全，这里有两种情况，一种是链表形式就直接遍历到尾端插入，一种是红黑树就按照红黑树结构插入；
6. 最后一个如果该链表的数量大于阈值TREEIFY_THRESHOLD（默认是8），就要先转换成黑红树的结构，break再一次进入循环；
7. 如果添加成功就调用addCount（）方法统计size，并且检查是否需要扩容；

#### get操作

1. 计算hash值，定位到该table索引位置，如果是首节点符合就返回；
2. 如果遇到扩容的时候，会调用标志正在扩容节点ForwardingNode的find方法，查找该节点，匹配就返回
3. 以上都不符合的话，就往下遍历节点，匹配就返回，否则最后就返回null；

#### size操作

在使用上除了有size()方法（返回int类型，如果实际数量超过Integer上限，会返回这个值，可能是错误的size），还提供了mappingCount()方法（返回long类型，推荐），实际都是调用了sumCount方法

底层实现：

有两个属性一起决定： 

- voliatile long baseCount

- CounterCells[]  counterCells

  ```java
  @sun.misc.Contended static final class CounterCell {
      volatile long value;
      CounterCell(long x) { value = x; }
  }
  ```

put操作的最后一步，会调用addCount方法，该方法逻辑如下：

- 使用CAS 对 baseCount 做加法
- CAS失败的话，使用counterCells
- 使用CAS在counterCells中添加数量；
- 如果上面失败，就继续死循环使用CAS操作，直到成功为止

获取size的时候，当 counterCells 不是 null，就遍历元素，并和 baseCount 累加；返回结果；





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



# 设计模式

## 原则

- 单一职责：一个类或者一个方法只负责一项职责，尽量做到类的只有一个行为原因引起变化；
- 里氏替换：子类可以扩展父类的功能，但不能改变原有父类的功能
- 依赖倒置：不应该依赖具体的类，而应该依赖抽象，强调面向接口编程
- 接口隔离：接口的职责尽量小
- 迪米特原则：强调尽量少依赖不需要的类，减少耦合
- 开闭原则：对修改关闭，对扩展开放

## 23种设计模式

### 创建型

- <font color=red>**单例模式**</font>：某个类只能有一个实例，提供一个全局的访问点。
- <font color=red>**简单工厂**</font>：一个工厂类根据传入的参量决定创建出那一种产品类的实例。
- <font color=red>**建造者模式**</font>：封装一个复杂对象的构建过程，并可以按步骤构造。
- 工厂方法：定义一个创建对象的接口，让子类决定实例化那个类。
- 抽象工厂：创建相关或依赖对象的家族，而无需明确指定具体类。
- 原型模式：通过复制现有的实例来创建新的实例。

### 结构性

- <font color=red>**组合模式**</font>：将对象组合成树形结构以表示“”部分-整体“”的层次结构。
- <font color=red>**适配器模式**</font>：将一个类的方法接口转换成客户希望的另外一个接口。
- <font color=red>**外观模式**</font>：对外提供一个统一的方法，来访问子系统中的一群接口。
- <font color=red>**代理模式**</font>：为其他对象提供一个代理以便控制这个对象的访问。
- 装饰模式：动态的给对象添加新的功能。
- 亨元（蝇量）模式：通过共享技术来有效的支持大量细粒度的对象。
- 桥接模式：将抽象部分和它的实现部分分离，使它们都可以独立的变化。

### 行为性

- <font color=red>**模板模式**</font>：定义一个算法结构，而将一些步骤延迟到子类实现。
- <font color=red>**责任链模式**</font>：将请求的发送者和接收者解耦，使的多个对象都有处理这个请求的机会。
- <font color=red>**观察者模式**</font>：对象间的一对多的依赖关系。
- <font color=red>**策略模式**</font>：定义一系列算法，把他们封装起来，并且使它们可以相互替换。
- 解释器模式：给定一个语言，定义它的文法的一种表示，并定义一个解释器。
- 状态模式：允许一个对象在其对象内部状态改变时改变它的行为。
- 备忘录模式：在不破坏封装的前提下，保持对象的内部状态。
- 中介者模式：用一个中介对象来封装一系列的对象交互。
- 命令模式：将命令请求封装为一个对象，使得可以用不同的请求来进行参数化。
- 访问者模式：在不改变数据结构的前提下，增加作用于一组对象元素的新功能。
- 迭代器模式：一种遍历访问聚合对象中各个元素的方法，不暴露该对象的内部结构。



# Arthas原理

参考文献：https://zhuanlan.zhihu.com/p/328974405

https://blog.csdn.net/fenglllle/article/details/119737716

简化：探针 agent（类加载）  + AOP（功能实现）





# Java Agent

参考文献：

- https://www.jianshu.com/p/d573456401eb
- https://blog.csdn.net/chuixue24/article/details/103829931

## 支持方式

1.在 JVM 启动的时加载 JDK5开始支持

       使用javaagent VM参数 java -javaagent:xxxagent.jar xxx，这种方式在 main 方法之前执行 agent 中的 premain 方法
       public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception


 2.在 JVM 启动后 Attach JDK6开始支持

    通过 Attach API 进行加载，在进程存在的时候，动态attach，这种方式会在 agent 加载以后执行 agentmain 方法
       public static void agentmain(String agentArgument, Instrumentation instrumentation) throws Exception
## 实现原理

### 启动时修改

![image-20220417122419080](https://gitee.com/firewolf/allinone/raw/master/images/image-20220417122419080.png)

启动时修改主要是在jvm启动时，执行native函数的Agent_OnLoad方法，在方法执行时，执行如下步骤：

- 创建InstrumentationImpl对象
- 监听ClassFileLoadHook事件
- 调用InstrumentationImpl的loadClassAndCallPremain方法，在这个方法里会去调用javaagent里MANIFEST.MF里指定的Premain-Class类的premain方法

### 运行时修改

![image-20220417122509863](https://gitee.com/firewolf/allinone/raw/master/images/image-20220417122509863.png)

运行时修改主要是通过jvm的attach机制来请求目标jvm加载对应的agent，执行native函数的Agent_OnAttach方法，在方法执行时，执行如下步骤：

- 创建InstrumentationImpl对象
- 监听ClassFileLoadHook事件
- 调用InstrumentationImpl的loadClassAndCallAgentmain方法，在这个方法里会去调用javaagent里MANIFEST.MF里指定的Agentmain-Class类的agentmain方法

### 核心API

- Instrument

  利用 java.lang.instrument 做动态 Instrumentation 是 Java SE 5 的新特性，**它把 Java 的 instrument 功能从本地代码中解放出来，使之可以用 Java 代码的方式解决问题**。使用 Instrumentation，**开发者可以构建一个独立于应用程序的代理程序（Agent），用来监测和协助运行在 JVM 上的程序，甚至能够替换和修改某些类的定义**。有了这样的功能，开发者就可以实现更为灵活的运行时虚拟机监控和 Java 类操作了，这样的特性实际上提供了 **一种虚拟机级别支持的 AOP 实现方式**，使得开发者无需对 JDK 做任何升级和改动，就可以实现某些 AOP 的功能了。

- ClassFileLoadHook

  ClassFileLoadHook是一个jvmti（jvm tool interface）事件，该事件是instrument agent的一个核心事件，主要是在读取字节码文件回调时调用，内部调用了TransFormClassFile函数。

- TranFormClassFile

  TransFormClassFile的主要作用是调用java.lang.instrument.ClassFileTransformer的tranform方法，该方法由开发者实现，通过instrument的addTransformer方法进行注册

整体流程：

在字节码文件加载的时候，会触发ClassFileLoadHook事件，该事件调用TransFormClassFile，通过经由instrument的addTransformer注册的方法完成整体的字节码修改。

对于已加载的类，需要调用retransformClass函数，然后经由redefineClasses函数，在读取已加载的字节码文件后，若该字节码文件对应的类关注了ClassFileLoadHook事件，则调用ClassFileLoadHook事件。后续流程与类加载时字节码替换一致。
