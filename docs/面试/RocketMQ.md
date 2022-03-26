

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

### 消息刷盘机制

RocketMQ 提供消息同步刷盘和异步刷盘两个选择

- Commitlog 是混合存储的，也就是所有 Topic 都存在一起，顺序追加写入，文件名用起始偏移量命名。对文件的顺序写入和内存的写入速度基本上没什么差别
- RocketMQ 的文件都利用了内存映射即 Mmap，将程序虚拟页面直接映射到页缓存上，无需有内核态再往用户态的拷贝，效率高

### 文件预分配

CommitLog 的大小默认是1G，当超过大小限制的时候需要准备新的文件，而 RocketMQ 就起了一个后台线程 AllocateMappedFileService，不断的处理 AllocateRequest，AllocateRequest 其实就是预分配的请求，会提前准备好下一个文件的分配，防止在消息写入的过程中分配文件，产生抖动



# 高可用 & 性能

## Broker HA

从 Broker 会和主 Broker 建立长连接，然后获取主 Broker commitlog 最大偏移量，开始向主 Broker 拉取消息，主 Broker 会返回一定数量的消息，循环进行，达到主从数据同步。

消费者消费消息会先请求主 Broker ，如果主 Broker 觉得现在压力有点大，则会返回从 Broker 拉取消息的建议，然后消费者就去从服务器拉取消息。

## Consumer

### 消费模式

可以选择使用集群模式和广播模式，集群模式下，多个客户端可以一起消费消息，提供消费速度

### 负载均衡机制

Consumer 会定期的获取 Topic 下的队列数，然后再去查找订阅了该 Topic 的同一消费组的所有消费者信息，默认的分配策略是类似分页排序分配。将队列排好序，然后消费者排好序，比如队列有 9 个，消费者有 3 个，那消费者-1 消费队列 0、1、2 的消息，消费者-2 消费队列 3、4、5，以此类推。



## 消息重试

### producer重试

可以配置超时时间、重试次数、是否发送到下一个broker（默认会）

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

### consumer重试

- Rocker会为每个消费者组设置一个重试队列，Topic 是 %RETRY%+consumerGroup，且设定了很多重试级别来延迟重试的时间。
- 会利 RocketMQ 的延时队列功能，把重试消息放在延时队列里，在消息的扩展字段里面会存储原来所属的 Topic 信息。delay 一段时间后再恢复到重试队列中，然后 Consumer 就会消费这个重试队列主题，得到之前的消息。
- 如果超过一定的重试次数都消费失败，则会移入到死信队列，即 Topic %DLQ%" + ConsumerGroup 中，存储死信队列即认为消费成功，之后需要人工干预；
- 可以通过` consumer.setMaxReconsumeTimes(2)`设置最大重试次数



# 消息的顺序性

## 全局顺序

全局顺序就是消除一切并发，一个 Topic 一个队列，Producer 和 Consuemr 的并发都为一；

## 局部顺序

局部顺序其实就是指某个队列顺序，多队列之间还是能并行的。



>可以通过 MessageQueueSelector 指定 Producer 某个业务只发这一个队列，然后 Comsuer 通过MessageListenerOrderly 接受消息，其实就是加锁消费。
>
>在 Broker 会有一个 mqLockTable ，顺序消息在创建拉取消息任务的时候需要在 Broker 锁定该消息队列，之后加锁成功的才能消费；

# 延时队列

## 功能特点

- 提供18个等级的延时队列  1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
- 通过`RocketMQProducer.sendDelay("messageKey","message消息体",延时级别);` 发送延时消息

## 实现原理

- 设置不同级别的消息延迟，如1s 2s ... 2h 等，让不同规格的消息存储到不同的队列（数据库表、redis中间件等）

- 为每个级别延时消息配置一个定时器，每次扫描时间比这个早的消息，进行投递

  

# 集群模式 & 广播模式

## 集群模式

同一个 ConsumerGroup(GroupName相同) 里的每 个 Consumer 只消费所订阅消息的一部分内容， 同一个 ConsumerGroup 里所有的 Consumer消费的内容合起来才是所订阅 Topic 内容的整体， 从而达到负载均衡的目的 。

``consumer.setMessageModel(MessageModel.CLURERING)`



## 广播模式

同一个 ConsumerGroup里的每个 Consumer都 能消费到所订阅 Topic 的全部消息，也就是一个消息会被多次分发，被多个 Consumer消费。

`consumer.setMessageModel(MessageModel.BROADCASTING)`



## 如何ACK？如何分配

猜测：集群模式下，应该是分配到不同的MessageQueue，或者轮询不同的consumer发送；

广播模式下，所有的消费者订阅所有的MessageQueue，同时自己保持消费位置；



# 推、拉模式的区别

## RocketMQ推拉模式

- MQPushConsumer方式，consumer把轮询过程封装了，并注册MessageListener监听器，取到消息后，唤醒MessageListener的consumeMessage()来消费，对用户而言，感觉消息是被推送（push）过来的。主要用的也是这种方式。

- MQPullConsumer方式，取消息的过程需要用户自己写，首先通过打算消费的Topic拿到MessageQueue的集合，遍历MessageQueue集合，然后针对每个MessageQueue批量取消息，一次取完后，记录该队列下一次要取的开始offset，直到取完了，再换另一个MessageQueue。

## 真正意义的推拉模式

推模式指的是客户端与服务端建立好网络长连接，服务方有相关数据，直接通过长连接通道推送到客户端。其优点是及时，一旦有数据变更，**客户端立马能感知到**；另外对客户端来说逻辑简单，不需要关心有无数据这些逻辑处理。缺点是**不知道客户端的数据消费能力，可能导致数据积压在客户端，来不及处理。**

拉模式指的是客户端主动向服务端发出请求，拉取相关数据。其优点是此过程由客户端发起请求，故不存在推模式中数据积压的问题。缺点是可能不够及时，对客户端来说需要考虑数据拉取相关逻辑，何时去拉，拉的频率怎么控制等等。

拉模式中，为了保证消息消费的实时性，采取了长轮询消息服务器拉取消息的方式。每隔一定时间，客户端想服务端发起一次请求，服务端有数据就返回数据，服务端如果此时没有数据，保持连接。等到有数据返回（相当于一种push），或者超时返回。

<font color=red>长轮询Pull的好处就是可以减少无效请求，保证消息的实时性，又不会造成客户端积压</font>



# 事务消息

## 相关概念

- Half(Prepare) Message——半消息([预处理](https://so.csdn.net/so/search?q=预处理&spm=1001.2101.3001.7020)消息)：半消息是一种特殊的消息类型，该状态的消息暂时不能被Consumer消费。当一条事务消息被成功投递到Broker上，但是Broker并没有接收到Producer发出的二次确认时，该事务消息就处于"**暂时不可被消费**"状态，该状态的事务消息被称为半消息
- Message Status Check——消息状态回查：由于网络抖动、Producer重启等原因，可能导致Producer向Broker发送的二次确认消息没有成功送达。如果Broker检测到某条事务消息长时间处于半消息状态，则会主动向Producer端发起回查操作，查询该事务消息在Producer端的事务状态(Commit 或 Rollback)。可以看出，Message Status Check主要用来解决分布式事务中的超时问题。

## 原理

![image-20220326113459281](https://gitee.com/firewolf/allinone/raw/master/images/image-20220326113459281.png)

1. Producer向Broker端发送Half Message；
2. Broker ACK，Half Message发送成功；
3. Producer执行本地事务；
4. 本地事务完毕，根据事务的状态，Producer向Broker发送二次确认消息，确认该Half Message的Commit或者Rollback状态Broker收到二次确认消息后，对于Commit状态，则直接发送到Consumer端执行消费逻辑，而对于Rollback则直接标记为失败，一段时间后清除，并不会发给Consumer。正常情况下，到此分布式事务已经完成，剩下要处理的就是超时问题，即一段时间后Broker仍没有收到Producer的二次确认消息；
5. 针对超时状态，Broker主动向Producer发起消息回查；
6. Producer处理回查消息，返回对应的本地事务的执行结果；
7. Broker针对回查消息的结果，执行Commit或Rollback操作，同Step4



# 其他

## consumeFromWhere

- CONSUME_FROM_LAST_OFFSET：从最后的偏移量开始消费，
- CONSUME_FROM_FIRST_OFFSET：从最小偏移量开始消费，
- CONSUME_FROM_TIMESTAMP：从某个时间开始消费



# kafka和rocketmq选型

- 数据可靠性：kafka使用异步刷盘、异步复制；Rocket还支持同步刷盘和复制（更注重消息的可靠性）；
- 性能：kafka单机几十、几百万tps; RocketMQ单机7万左右
- 单机支持队列数：kafka 支持64； Rocket可高达5W
- 消息投递实时性：kafka采用短轮询，取决于轮询间隔；rocket采用长轮询，通常在几毫秒
- 失败重试：kafka不支持；rocket支持，重试间隔时间随时间顺延
- 消息顺序：kafka支持顺序，但是如果一个broker宕机，会出现混乱；RocketMq支持严格顺序，在顺序场景下，如果一台broker宕机，发送消息会失败，但是不会乱序；
- 定时消息：kafka不支持；rocket支持（开源只支持level，专业版支持定时level，可到毫秒级别）；
- 事务消息：kafka不支持；rocket支持
- 消息查询：kafka不支持；rocket支持根据messageId及内容查询，方便问题定位
- 消息回溯：kafka按照offset支持；rocket按照时间支持，精确到毫秒
- 消费并行度：kafka的消费并行度依赖Topic配置的分区数；RocketMQ消费并行度分两种情况：顺序消费和kafka一致，乱序方式并行度取决于Consumer的线程数;
- broker端消息过滤：kafka不支持；rocket可以根据tag过滤；
- 消息堆积能力：kafka更强
- 社区活跃度：rocketmq更活跃



参考文献：

- https://blog.csdn.net/a646705816/article/details/111461528
- https://blog.csdn.net/a1036645146/article/details/109581499
- https://www.cnblogs.com/ynyhl/p/11320797.html