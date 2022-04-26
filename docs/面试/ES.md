[toc]

# ES集群架构

主从架构，一个Index包含多个有分片（数据切分）和副本（备份防止丢失）

- 节点类型
  - Master：主节点
    - `node.data ＝ true`
    - 索引创建和删除
    - 跟踪节点状态
    - 决定分片如何分配
  - Voting ：投票节点
    - `node.voting_only = true` 仅仅投票，也可以作为数据节点
  - Coordinating：协调节点
    - 如果同时设置了data.master = false和data.data=false，那么此节点将成为仅协调节点。
    - 处理路由和搜索请求，智能的负载均衡
  - Data：数据节点
    - 存储索引数据
    - 对文档进行增删改查，聚合操作

# ES近实时搜索原理（存储、刷盘）

参考文献：

- https://www.jianshu.com/p/cd608a5cc8f2
- https://www.cnblogs.com/ipoke/p/13744741.html

## ES存储组成结构

 ![image-20220331184029381](https://gitee.com/firewolf/allinone/raw/master/images/image-20220331184029381.png)



- Segment（段） file ：Lucene里面的一个数据集概念，最核心的概念就是Segment(段)，每个段本身就是一个倒排索引。
- Commit point file：有一个列表存放着所有已知的所有段
- In-memory buffer：内存缓冲区
- Translog：为了避免在两次commit操作间隔时间发生异常导致Doc丢失，ES中采用了一个事务日志记录每次对ES的操作，没有flush到磁盘里面的数据，会被写入到translog中，避免数据丢失，系统重启的时候，会从translog中恢复。作用类似于mysql 的 redo log；

## 刷盘机制

### 写入

数据持久化步骤为：write ---> refresh ----> flush ----> merge

具体步骤如下：

#### write

- 一个新文档过来，会存储在in-memory buffer内存缓存区中，顺便记录translog，此时数据没有到segment，无法被检索到；

 ![image-20220401180641949](https://gitee.com/firewolf/allinone/raw/master/images/image-20220401180641949.png)

#### refresh

> 默认 1 秒钟，ES 是支持修改这个值的，通过 index.refresh_interval 设置 refresh （冲刷）间隔时间；
>
> 文档经过 refresh 后， segment 暂时写到文件系统缓存，这样避免了性能 IO 操作，又可以使文档搜索到

- 把in-memory buffer的数据写入到新的segment中（没有被commit point管理），但是segment还是存在文件系统的缓存中，此时文档可以被搜素到
- 清空in-memory buffer，而 translog没有被清空；

<font color=red><b>refresh机制也是es近实时的原因</b></font>

![image-20220401180546627](https://gitee.com/firewolf/allinone/raw/master/images/image-20220401180546627.png)

#### flush

- 每隔一段时间（默认每个分片30分钟）或translog变得太大，index会被flush到磁盘，会发生以下事情：
  - 所有内存中的in-mermory buffer会被写入新段
  - in-memory buffer被清空
  - 一个提交点被写入磁盘
  - 文件系统缓存通过fsync flush 到磁盘上
  - 之前的旧translog被删除，生成新的translog

 ![image-20220401180505743](https://gitee.com/firewolf/allinone/raw/master/images/image-20220401180505743.png)

#### merge

当生成的segment越来越多的时候，搜索会越来越慢，会通过merge操作，将多个小的segment文件合并成一个大的segment，同时也会生成新的commit point 文件，之后，会删除就的segment文件和commit point文件

​        <img src="https://gitee.com/firewolf/allinone/raw/master/images/image-20220401180906592.png" style="zoom:70%;" />



### 删除

- es在删除一个文档的时候，并不会立马从磁盘移除，只是标记为已删除

- 段是不可变的，所以既无法删除段中的文档，也无法更改；
- es在每个提交点文件中包含一个.del文件，包含了段上已经标记为删除状态的文档。在检索的时候，返回数据前，根据.del文件把已经删除了的文档从结果中移除；
- 在merge的过程中，删除了的数据不再合并到新段中，从而，数据被从磁盘屋里删除



### 更新

- 文档的更新操作和删除是类似的：当一个文档被更新，旧版本的文档被标记为删除，新版本的文档在新的段中索引。



# ES为什么查询快

- 存储采用倒排索引
-  https://www.jianshu.com/p/cd608a5cc8f2





# 倒排索引

参考文献：https://zhuanlan.zhihu.com/p/33671444

## 基本描述

比如：有如下三个文档

 ![image-20220402114213500](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402114213500.png)

那么，给这三个doc建立的倒排索引为：

 ![image-20220402114306641](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402114306641.png)

这里可以看到，<font color=red><b>倒排索引是每一个字段有一个自己的倒排索引</b></font>



## 整体结构

#### 相关概念

- Term：具体的数据项，如上面的18、20等

- Posting list：文档id数组，是一个int数组

- Term dictionary：把同一个字段你的所有term进行了排序，可以通过二分查找完成<font color=red><b>查询加速</b></font>，可以通过logN次磁盘检索找到目标

- Term index：为了减少磁盘IO，我们希望把一些数据缓存到内存进行加速。然而，由于Term dictionary是所有数据，无法全部缓存。term index 是一个trie树（前缀树、自然排序），把Term dictionary的前缀形成一棵树存在内存里，这样，可以通过内存查找进行<font color=red><b>查询加速</b></font>。

   ![image-20220402115407124](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402115407124.png )

> Term index 在内存中是以FST的形式保存的，非常节省内存。
>
> Term dictionary 在磁盘上是以分block的方式保存的，一个block内部利用公共前缀压缩，比如都是Ab开头的单词就可以把Ab省去。这样term dictionary可以比b-tree更节约磁盘空间。

#### 结构图

 ![image-20220402115523089](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402115523089.png)

#### es比mysql快的原因

Mysql只有term dictionary这一层，是以b-tree排序的方式存储在磁盘上的。检索一个term需要若干次的random access的磁盘操作。而Lucene在term dictionary的基础上添加了term index来加速检索，term index以树的形式缓存在内存中。从term index查到对应的term dictionary的block位置之后，再去磁盘上找term，大大减少了磁盘的random access次数。



## 检索实现

### 单term检索

比如：查找name=“zhangsan”的信息

根据上面的结构，查询的时候，从term index开始，通过trie树查找到相应的term，然后找到posting list（文档id），从而找到文档存储在磁盘的地址，加载数据即可

### 多term联合检索

比如：查找 name="zhangsan" AND age=18 的信息

先分别找到各自的posting list ，然后做一个“与”的合并，合并方式有两种：

#### skip list

对要合并的多个字段的posting list同时进行遍历，通过跳跃表的方式，可以一次跳过多个元素，可以高效获取交集

> 具体skip list的结构，和redis 跳跃表是一致的；

同时，考虑到频繁出现的term，如：gender字段中的男或女，使用 Frame of Reference编码进行压缩来减少磁盘占用，减少索引大小；

 ![image-20220402135428313](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402135428313.png)

#### bitset

> 这种结构比较直观，是一个byte数组，如果相应位置的数据出现，值就是1，否则就是0，比如：
>
> [1,3,4,7,10] ，使用 bitset表示为： [1,0,1,1,0,0,1,0,0,1] 

bitset自身就有压缩的特点，用一个byte就可以代表8个文档，所以如果是100万个文档，则只需要12.5万个byte，

然而，由于es支持海量数据，所以如果每一个filter都要消耗一个bitset，那么就比较奢侈。

<font color=red><b>ES底层的Lucence使用 Roaring Bitmap来完成</b></font>

- 可以很压缩地保存上亿个bit代表对应的文档是否匹配filter
- 这个压缩的bitset仍然可以很快地进行AND和 OR的逻辑操作

 ![image-20220402142221824](https://gitee.com/firewolf/allinone/raw/master/images/image-20220402142221824.png)





## 相关知识

### 前缀树

参考文献：https://www.cnblogs.com/bonelee/p/8830825.html



### FST

参考文献：https://blog.csdn.net/lsx2017/article/details/113917796



### Roaring Bitmap

参考文献：https://zhuanlan.zhihu.com/p/351365841





# 常用的查询API



# 选举策略

https://blog.csdn.net/ailiandeziwei/article/details/87856210

## 使用的算法

- 因为可能有多个候选节点，所以可能出现脑裂
- ES采用了常见的分布式系统思路，保证选举出的master被多数派(quorum)的master-eligible node认可，以此来保证只有一个master

## 谁发起的？

master选举是由master-eligible节点发起，当一个master-eligible节点发现满足以下条件时发起选举：

1. 该master-eligible节点的当前状态不是master。
2. 该master-eligible节点通过ZenDiscovery模块的ping操作询问其已知的集群其他节点，没有任何节点连接到master。
3. 包括本节点在内，当前已有超过minimum_master_nodes个节点没有连接到master。



https://blog.csdn.net/lsx2017/article/details/1139177



# 节点注册

ZenDiscovery

在本节点到每个hosts中的节点建立一条边，当整个集群所有的node形成一个联通图时，所有节点都可以知道集群中有哪些节点，不会形成孤岛





# 主从同步机制

参考文献：https://blog.csdn.net/u014618114/article/details/115417017

## 写入步骤

1. 客户端向 Node 1 发送新建、索引或者删除请求。coodinate（协调）节点通过hash算法可以计算出是在哪个主分片上，然后路由到对应的节点shard = hash(document_id) % (num_of_primary_shards)
2. 节点使用文档的 _id 确定文档属于分片 0 。请求会被转发到 Node 3，因为分片 0 的主分片目前被分配在 Node 3 上。
3. Node 3 在主分片上面执行请求。如果成功了，它将请求并行转发到 Node 1 和 Node 2 的副本分片上。一旦所有的副本分片都报告成功, Node 3 将向协调节点报告成功，协调节点向客户端报告成功。
4. 在客户端收到成功响应时，文档变更已经在主分片和所有副本分片执行完成，变更是安全的

## 写一致性保障

对于增删改：es API为我们提供了一个可自定的参数consistency。该参数可以让我们自定义处理一次增删改请求，是不是必须要求所有分片都是active的才会执行

有三个取值：

- one：要求我们这个写操作，只要有一个primary shard是active活跃可用的，就可以执行。
- all：要求我们这个写操作，必须所有的primary shard和replica shard都是活跃的，才可以执行这个写操作
- quorum：默认的值，要求所有的shard中，必须是大部分的shard都是活跃的，可用的，才可以执行这个写操作
  

# 相关度原理

① TF(词频term frequency): 关键词在每个doc中出现的次数
② IDF(反文档词频inversed document frequency):关键词在整个索引中出现的次数
③ norm:字段长度越长，值越小。

公式计算

