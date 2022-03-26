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

