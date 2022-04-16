[toc]

# JAVA创建对象方式

1. new
2. 反射：newInstance
3. 反序列化
4. clone
5. String = "xxx"（特殊）

# HashMap 

## 底层实现

### JDK7

数组 + 链表



### JDK8

数组 + 红黑树 + 链表

#### put操作

1. put(key, value)中直接调用了内部的putVal方法，并且先对key进行了hash操作；
2. putVal方法中，先检查HashMap数据结构中的索引数组表是否位空，如果是的话则进行一次resize操作；
3. 以HashMap索引数组表的长度减一与key的hash值进行与运算，得出在数组中的索引，如果索引指定的位置值为空，则新建一个k-v的新节点；
4. 如果不满足的3的条件，则说明索引指定的数组位置的已经存在内容，这个时候称之碰撞出现；
5. 在上面判断流程走完之后，计算HashMap全局的modCount值，以便对外部并发的迭代操作提供修改的Fail-fast判断提供依据，于此同时增加map中的记录数，并判断记录数是否触及容量扩充的阈值，触及则进行一轮resize操作；
6. 在步骤4中出现碰撞情况时，从步骤7开始展开新一轮逻辑判断和处理；
7. 判断key索引到的节点（暂且称作被碰撞节点）的hash、key是否和当前待插入节点（新节点）的一致，如果是一致的话，则先保存记录下该节点；如果新旧节点的内容不一致时，则再看被碰撞节点是否是树（TreeNode）类型，如果是树类型的话，则按照树的操作去追加新节点内容；如果被碰撞节点不是树类型，则说明当前发生的碰撞在链表中（此时链表尚未转为红黑树），此时进入一轮循环处理逻辑中；
8. 循环中，先判断被碰撞节点的后继节点是否为空，为空则将新节点作为后继节点，作为后继节点之后并判断当前链表长度是否超过最大允许链表长度8，如果大于的话，需要进行一轮是否转树的操作；如果在一开始后继节点不为空，则先判断后继节点是否与新节点相同，相同的话就记录并跳出循环；如果两个条件判断都满足则继续循环，直至进入某一个条件判断然后跳出循环；
9. 步骤8中转树的操作treeifyBin，如果map的索引表为空或者当前索引表长度还小于64（最大转红黑树的索引数组表长度），那么进行resize操作就行了；否则，如果被碰撞节点不为空，那么就顺着被碰撞节点这条树往后新增该新节点；
10. 最后，回到那个被记住的被碰撞节点，如果它不为空，默认情况下，新节点的值将会替换被碰撞节点的值，同时返回被碰撞节点的值（V）。



## Get操作时间复杂度

- 在理想状态下，及未发生任何hash碰撞，数组中的每一个链表都只有一个节点，那么get方法可以通过hash直接定位到目标元素在数组中的位置，时间复杂度为O(1)。
- 若发生hash碰撞，则可能需要进行遍历寻找，n个元素的情况下，链表时间复杂度为O(n)、红黑树为O(logn)



## 存在问题

- JDK7
  - 并发问题
  - 死循环
  - 数据丢失
- JDK8
  - 并发问题

# ConcurrentHashMap

参考文献：

- https://blog.csdn.net/qq_39625353/article/details/107019472
- https://blog.csdn.net/xuxinyuande/article/details/105738873

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

1. 采用不加锁的方式，多次计算count，（最多三次），比较结果，如果相同的话，就准确，否则的话计算的结果不准确；
2. 采用加锁的方式，给所有Segment元素加锁，计算出count值，这个是准确的；



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

直接返回count



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
- 开闭原则：对修改关闭，对扩展开放



# Arthas原理

https://zhuanlan.zhihu.com/p/328974405
