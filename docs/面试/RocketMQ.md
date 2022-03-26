

[toc]

# RocketMQ整体架构

## 架构图

![image-20220326092333452](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326092333452.png)

## 工作过程

- NameServer是轻量级无状态的，类似于zk，主要进行路由信息和Broker的管理，各 NameServer 之间无任何数据交互。Broker 启动之后会向所有 NameServer 定期（每 30s）发送心跳包，包括：IP、Port、TopicInfo，NameServer 会定期扫描 Broker 存活列表，如果超过 120s 没有心跳则移除此 Broker 相关信息，代表下线；
- Producer每30秒从 NameServer 获取 Topic 和 Broker 的映射关系存在本地内存中，如果发现新的broker，每 30s 会发送心跳至 Broker 维护连接。这样就可以得知它要发送的某 Topic 消息在哪个 Broker 上，和对应的 Broker （Master 角色的）建立长连接，发送消息。
  - 并且会轮询当前可以发送的 Broker 来发送消息，达到负载均衡的目的，在同步发送情况下如果发送失败会默认重投两次（retryTimesWhenSendFailed = 2），并且不会选择上次失败的 broker，会向其他 broker 投递。
- Consumer 上线也可以从 NameServer 得知它所要接收的 Topic 是哪个 Broker ，和对应的 Master、Slave 建立连接，接收消息。

## Producer启动流程

![image-20220326100233901](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326100233901.png)

因为 Producer 和 Consumer 都需要用 MQClientInstance，而同一个 clientId 是共用一个 MQClientInstance 的， clientId 是通过本机 IP 和 instanceName（默认值 default）拼起来的，所以多个 Producer 、Consumer 实际用的是一个MQClientInstance。

## Producer发送消息流程

![image-20220326100819634](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326100819634.png)

流程比较简单：找到要发送消息的 Topic 在哪个 Broker 上，然后发送消息。



## 发送故障延时

有一个参数是 sendLatencyFaultEnable，默认不开启。这个参数的作用是对于之前发送超时的 Broker 进行一段时间的退避。

发送消息会记录此时发送消息的时间，如果超过一定时间，那么此 Broker 就在一段时间内不允许发送。

发送超时大概率表明此 Broker 负载高，所以先避让一会儿，让它缓一缓，这也是实现消息发送高可用的关键。



# 手动、自动创建Topic？

通过`autoCreateTopicEnable`参数控制是否自动创建topic，线下可以开启，线上需要关闭

## 自动创建Topic的弊端

<font color=red>可能影响负载均衡</font>

- 从Producer发送消息流程可以看出，Rocketmq在发送消息时，会先去获取topic的路由信息，如果topic没有创建，就没有该topic的路由信息，所以会再次以“**TBW102**”这个默认topic获取路由信息；
- 如果broker都开启了**自动创建开关**，那么此时会获取**所有broker**的路由信息，消息的发送会根据**负载**算法选择**其中一台Broker**发送消息（只能往其中一台发送消息，否则重复发送）；
- 消息到达broker后，发现本地没有该topic，会在创建该topic的信息塞进本地缓存中，同时会将topic路由信息注册到nameserver中
- 如果在该路由消息更新到Nameserver之前，又发送了消息，让其他的broker也创建了这个topic，那么就没有问题；但是，如果在路由消息同步到Nameserver之前（同步需要通过心跳，默认30秒），一直没有消息，这样，其他的broker就不会创建这个topic；
- 这样就会造成一个后果：**以后所有该topic的消息，都将发送到这台broker上**，如果该topic消息量非常大，会造成某个broker上负载过大，这样的消息存储就**达不到负载均衡**的效果了，同样，如果该Broker故障挂掉了，发送消息会失败，Broker的单节点故障。

## 手动创建

需要通过控制台命令进行创建

>`./mqadmin updateTopic`
>
>- -b,--brokerAddr <arg>    create topic to which broker
>- -c,--clusterName <arg>   create topic to which cluster

## broker模式创建

-b 参数会在指定的broker上创建topic

## 集群模式

-c参数会通过nameserver拉取所有的broker信息，然后遍历发送命令到各个broker创建topic



# Broker

## 模块划分

![image-20220326102432415](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326102432415.png)

主要分为五大模块：

- Remoting 远程模块，处理客户请求；
- Client Manager 管理客户端，维护订阅的主题；
- Store Service 提供消息存储查询服务；
- HA Serivce，主从同步高可用；
- Index Serivce，通过指定key 建立索引，便于查询。

## 存储

RocketMQ 存储用的是本地文件存储系统，效率高也可靠。主要涉及三种类型的文件：分别是 CommitLog、ConsumeQueue、IndexFile

![image-20220326102830740](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326102830740.png)

消息到了先存储到 Commitlog，然后会有一个 ReputMessageService 线程接近实时地将消息转发给消息消费队列文件与索引文件，也就是说是异步生成的

## 消息刷盘机制

RocketMQ 提供消息同步刷盘和异步刷盘两个选择



# 推、拉消息的区别



# 事务消息





# 如何保障消息被正确消费

- 消息顺序性：需要保持一致性的消息，投递到同一个队列中，失败后重试。https://www.sohu.com/a/129521820_487514
- 消息一定被消费
  - 服务端：同步发送、失败重试、死信队列
  - 客户端：ACK、失败重试。
- 消息去重：需要自己实现客户端幂等性保证，消费消息表、消息标识进行唯一约束

1. RocketMQ如果Broker挂了怎么办？

- 架构上需要采用master-slave架构，如果主节点挂了，slave上线

1. 消息的完整性保障

2. 消息消费模式

   - 集群模式
   - 广播模式

3. RocketMQ延时队列

   - 提供能量
     - 提供18个等级的延时队列  1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     - 通过`RocketMQProducer.sendDelay("messageKey","message消息体",延时级别);` 发送延时消息
   - 实现原理
     - 设置不同级别的消息延迟，如1s 2s ... 2h 等，让不同规格的消息存储到不同的队列（数据库表、redis中间件等）
     - 为每个级别延时消息配置一个定时器，每次扫描时间比这个早的消息，进行投递

4. RocketMQ的消息重试

   - producer重试

     可以配置超时时间、重试次数、是否发送到下一个broker

     ```csharp
     rocketmq:
       name-server: http://101.200.36.168:9876
       producer:
         #指定消息发送者的组，在控制台查询时会用到
         group: test
         #发送失败超时时间
         send-message-timeout: 3000
         #重试次数
         retry-times-when-send-failed: 3
         #在其他broker服务端进行重试默认false，开启设置为on
         retry-next-server: false
     ```

   - consumer重试

     - 可以通过抛出异常的方式或者回执失败的来完成重试
     - 重试机制和延时等级一致
     - 可以通过` consumer.setMaxReconsumeTimes(2)`设置最大重试次数

1. RocketMQ回溯

   - 支持按照毫米的精确度来回溯



1. kafka和rocketmq选型
   - 吞吐量上：kafka单机几十、几百万tps; RocketMQ单机7万左右
2. 



事务消息

广播模式、集群模式的

hashMap get 时间复杂度



参考文献：

- https://blog.csdn.net/a646705816/article/details/111461528
- https://blog.csdn.net/a1036645146/article/details/109581499