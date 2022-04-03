[toc]

# 一、什么是MVCC

MVCC （Multiversion Concurrency Control），多版本并发控制。顾名思义，MVCC 是通过数据行的多个版本管理来实现数据库的 并发控制 。这项技术使得在InnoDB的事务隔离级别下执行 一致性读 操作有了保证。换言之，就是为了查询一些正在被另一个事务更新的行，并且可以看到它们被更新之前的值，这样在做查询的时候就不用等待另一个事务释放锁。



# 二、快照度与当前读

MVCC在MySQL InnoDB中的实现主要是为了提高数据库并发性能，用更好的方式去处理 读-写冲突 ，做到即使有读写冲突时，也能做到 不加锁 ， 非阻塞并发读 ，而这个读指的就是 快照读 , 而非 当前读 。当前读实际上是一种加锁的操作，是悲观锁的实现。而MVCC本质是采用乐观锁思想的一种方式

## 快照度

快照读又叫一致性读，读取的是快照数据。**<font color=red>不加锁的简单的 SELECT 都属于快照读</font>**，即不加锁的非阻塞读

快照读的实现是基于MVCC，它在很多情况下，避免了加锁操作，降低了开销

## 当前读

当前读读取的是记录的最新版本（最新数据，而不是历史版本的数据），读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。<font color=red><b>加锁的 SELECT，或者对数据进行增删改都会进行当前读</b></font>



# 三、MVCC实现原理

MVCC的实现依赖了：隐藏字段、Undo Log、Read View

## 记录隐藏字段、Undo Log版本链

对于InnoDB存储引擎来说，它的聚餐索引中都包含了两个必要的隐藏列

- trx_id：改动该记录的事务id

- roll_pointer：每次对某条聚簇索引记录进行改动时，都会把旧的版本写入到 undo日志 中，然后这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息。

  例如：如下是事务20和事务10对id为1的name字段进行修改后的版本链描述

   ![image-20220403224935921](https://gitee.com/firewolf/allinone/raw/master/images/image-20220403224935921.png)

## ReadView

在READ_COMMITED 和 REPEATABLE_READ 隔离级别下，InnoDB通过在版本链中判断哪一个版本是当前事务可见的，来达到相应的效果，这是由ReadView进行支持的

### 组成部分

- creator_trx_id：创建read view 的事务id
- trx_ids：在生成ReadView时当前系统中活跃的读写事务的 事务id列表
- up_limit_id：活跃的事务中最小的事务id
- low_limit_id：表示生成ReadView时系统中应该分配给下一个事务的 id 值。low_limit_id 是系统最大的事务id值，这里要注意是系统中的事务id，需要区别于正在活跃的事务ID

### 规则

有了这个ReadView，这样在访问某条记录时，只需要按照下边的步骤判断记录的某个版本是否可见

- 如果被访问版本的trx_id属性值与ReadView中的 creator_trx_id 值相同，意味着当前事务在访问它自己修改过的记录，所以该版本可以被当前事务访问。
- 如果被访问版本的trx_id属性值小于ReadView中的 up_limit_id 值，表明生成该版本的事务在当前事务生成ReadView前已经提交，所以该版本可以被当前事务访问
- 如果被访问版本的trx_id属性值大于或等于ReadView中的 low_limit_id 值，表明生成该版本的事务在当前事务生成ReadView后才开启，所以该版本不可以被当前事务访问
- 如果被访问版本的trx_id属性值在ReadView的 up_limit_id 和 low_limit_id 之间，那就需要判断一下trx_id属性值是不是在 trx_ids 列表中。
  - 如果在，说明创建ReadView时生成该版本的事务还是活跃的，该版本不可以被访问。
  - 如果不在，说明创建ReadView时生成该版本的事务已经被提交，该版本可以被访问。

### 隔离级别与ReadView

- Read Commited：同一个事务中，在每次语句执行的过程中，都关闭read_view, 重新在查询的时候创建当前的一份Read View这样就会产生不可重复读现象发生。

- Repeatable Read：创建当前事务的时候，就户其创建read view，一直维持到事务结束，在这段时间每一次查询都不会重新重建Read View，从而实现了可重复度



## MVCC整体流程

1. 首先获取事务自己的版本号，也就是事务 ID； 
2. 获取 ReadView
3. 查询得到的数据，然后与 ReadView 中的事务版本号进行比较
4. 如果不符合 ReadView 规则，就需要从 Undo Log 中获取历史快照；
5. 最后返回符合规则的数据



