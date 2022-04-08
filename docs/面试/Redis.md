[toc]

# 缓存一致性方案

  [参考文献](https://blog.csdn.net/qq_39408435/article/details/119846429)

- 依赖redis的过期机制，定时刷新-----脏数据
- 先更新数据库，再删除缓存 ----- 会有脏数据
- 先删缓存，再更新数据库----- 会有脏数据
- 延迟双删：删除缓存、更新数据库、删除缓存 ----- 主从复制情况查询从库会产生脏数据
- 消息中间件：把删除缓存的消息加入到队列中，如果消息投递失败，就再次加入到队列执行，直到成功为止。我们就能够有效保证数据库和缓存数据不一致了，不管是读写分离还是其他情况，只要消息队列能够保证安全，那么缓存就一定会被刷新。（在发生删除消息前，Master需要等Slave节点发生ACK，否则在消息消费了，单主从并未完成同步，还是去从库读取旧值，可以参考下MySQL半同步复制）。
- 通过订阅binlog来更新redis，把我们搭建的消费服务，作为mysql的一个slave，订阅binlog，解析出更新内容，再更新到redis。
  - 监听binlog的技术
    - **Canal**：伪装自己是slave，向MySQL master发送 dump协议，MySQL master收到dump请求，开始推送binary log给slave（也就是canal）



# 哨兵模式和集群模式对比

## 架构

- 哨兵模式

  ![img](https://gitee.com/firewolf/allinone/raw/master/images/1350922-20191006122611921-809764078-20220326142520517.png)

- 集群模式

  ![img](https://gitee.com/firewolf/allinone/raw/master/images/1350922-20191006124637992-2055348918-20220326142520760.png)

## spring连接方式

- 单机模式
  - spring.redis.host   会自动生成RedisConnectionFactory，然后利用template.setConnectionFactory，生成RedisTemplate
- 集群模式
  - spring.redis.cluster.nodes=ip1:port1,ip2:port2...        获取后自行切割解析成Set<HostAnPort> nodes
  - RedisClusterConfiguration.setClusterNodes(nodes)
  - 生成RedisTemplate
  - 请求过程为：
    - 从集群节点列表中随机选择一个节点
    - 从该节点获取一个客户端连接（如果配置了连接池，从连接池中获取），执行命令
    - 如果抛出ClusterRedirectException异常，则跳转到返回的目标节点上执行
    - 如果跳转次数大于配置的值 max-redirects， 则抛出TooManyClusterRedirectionsException异常
- 哨兵模式
  - spring.redis.sentinel.master  和 spring.redis.sentinel.nodes 分别用来配置集群名和哨兵节点，
  - 内部会生成一个JedisSentinelPool，该类的初始化过程中，会对每一个Node尝试构建Jedis对象，通过jedis.sentinelGetMasterAddrByName方法来获取master节点信息
  - 对每一个哨兵节点通过一个 MasterListener 进行监听（Redis的发布订阅功能），订阅哨兵节点`+switch-master`频道，当发生故障转移时，客户端能收到哨兵的通知，通过重新初始化连接池，完成主节点的切换

## 节点失败检测

- 集群模式
  - 各个节点之间有互相的检测，如果超过半数的节点检测失效时，认为该节点失效
- 哨兵模式
  - 一个哨兵发现节点离线，标识为主观下线，如果超过半数哨兵发现节点下线后，会发现主节点切换过程，完成之后，主节点会客观下线

## 数据存储

- 哨兵模式

  - 集群中每台redsi服务器的数据相同，浪费内存，数据同步采用主从赋值

- 集群模式

  - 实现了redis的分布式存储，每天redis节点上存储不同的内容



# 项目使用redis的地方

## 缓存

基础信息的查询加速：人员、设备、时间计划、区域等

## 分布式锁

Redission，缓存过期时间，30s ~ 1d 不等，识别ppl中判断陌生人次数为3



# Redis CPU 负载高肯能的原因

- 客户的业务负载过重，qps过高，导致CPU被用满，排查方法请参考[排查QPS是否过高](https://support.huaweicloud.com/trouble-dcs/dcs-trouble-0715002.html#dcs-trouble-0715002__section8519162751218)。
- 使用了keys等消耗资源的命令，排查及处理措施请参考[查找并禁用高消耗命令](https://support.huaweicloud.com/trouble-dcs/dcs-trouble-0715002.html#dcs-trouble-0715002__section6300145853215)。
- 发生Redis的持久化重写操作，排查及处理措施请参考[是否存在Redis的持久化重写操作](https://support.huaweicloud.com/trouble-dcs/dcs-trouble-0715002.html#dcs-trouble-0715002__section6314242121514)。



跳跃表的

# 分布式锁加锁解锁过程、到时间了没有执行完怎么办？

- 基于Redis命令
  - 加锁：执行setNX，如果成功，执行expire添加过期时间
  - 解锁：执行delete命令
  - 缺点：expire执行失败，就可能出现死锁；不支持阻塞等待，不支持重入
- RedissionLock
  - Redisson 支持单点模式、主从模式、哨兵模式、集群模式
  - 加锁解锁过程都使用lua脚本进行交互，保证了原子性
  - 唯一标识：获取锁时set的唯一值，实现上为redisson客户端**ID(UUID)+线程ID**
  - 加锁：通过exists判断是否存在，存在则通过hset设置为1，然后执行pexpire设置过期时间；通过hexists判断锁存在并且唯一标识存在，如果锁存在，表明是重入，则通过hincrby给锁重入计数加1，并设置过期时间；如果所存在，则执行pttl命令返回锁的过期时间
  - 解锁：通过exists判断如果锁不存在，则通过publish发布消息；通过hexists判断如果所存在但是唯一标识不是当前线程，则不允许解锁；如果锁存在并且和当前唯一标识匹配，则通过hincrby给重入计数-1；如果计数仍大于0，则通过pexpire重设过期时间，否则通过del删除锁，然后通过publish发布消息。
  - 解锁过程中的广播，是为了统治阻塞线程去抢锁；
  - **RedissonLock 同样没有解决 节点挂掉的时候，存在丢失锁的风险的问题**
- RedissionRedLock
  - 需要额外为RedissionRedLock 搭建Redis环境
  - 加锁：依次尝试从多个redis实例节点上获取锁（需要设置获取超时时间），只有超过半数节点上面获取锁成功（在超时时间内获取到锁）才算成功；如果获取锁失败，则在所有的redis实例上进行解锁操作；
  - 其他的和RedissionLock一致
- lua脚本
  - lua脚本在执行的时候具有排他性，不允许其他命令或者脚本执行，类似于通过 MULTI/EXEC 指令保障一批操作具有原子性。但是由于redis事务不会回滚，所以一定要保证lua脚本的正确性
  - 使用上:`eval "if redis.call('get', KEYS[1]) == false then redis.call('set', KEYS[1], ARGV[1])`
    - eval：redis执行lua脚本
    - call/pcall：返回的错误不同
- 怎么做到一定能解锁？
  - 通常会通过过期时间保障一定能解锁；
- 怎么样保证过期时间一定设置成功？ 
  - 通过 `SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]` 这个原子命令实现。  
    - EX：设置超时时间，单位秒
    - PX：设置超时时间，单位毫秒
    - NX：当key不存在才进行设置
    - XX：当key存在的时候设置
  - 通过lua脚本完成

- 时间到了，没有执行完怎么办？
  - 采用<font color=red>**缓存续命（watchdo(看门狗)）组件）**</font>：一个后台线程每隔10秒（需要小于锁的过期时间，通常是过期时间的1/3）检查是否还持有锁（Redission实例还存在），如果持有则延长锁的时间为过期时间。



# Redis事务

不保障原子性，某个命令是错误的，则整体失败；正确的命令执行失败，则不会影响其他命令



# Redis的连接池有哪些？

- JedisSentinelPool ： 用于哨兵模式
- JedisPool：通常使用的
- SharedJedisPool：集群使用的，通过一致性hash算法，将不同的key分配到不同的redis server上，达到横向扩展的目的

> 一致性Hash算法：

# 缓存过期策略



# 线程池的工作原理



# 跳跃表和B+树区别
