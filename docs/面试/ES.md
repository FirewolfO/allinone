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

    

# ES为什么效率高，查询快

- 存储采用倒排索引
-  https://www.jianshu.com/p/cd608a5cc8f2

# ES 刷盘策略





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



# PAX_OS 协议缺点





# 节点注册

ZenDiscovery

在本节点到每个hosts中的节点建立一条边，当整个集群所有的node形成一个联通图时，所有节点都可以知道集群中有哪些节点，不会形成孤岛





# 主从同步机制