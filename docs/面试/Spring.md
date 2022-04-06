[toc]

# Filter 、拦截器区别，项目使用地方

- filter接口在javax.servlet包下面，是servlet规定的。inteceptor定义在org.springframework.web.servlet中，可以用于web程序，也可以用于普通的应用；
- filter是servlet容器支持的，interceptor是spring框架支持的
- filter通过dochain放行，interceptor通过prehandler放行。
- filter只在方法前后执行，interceptor粒度更细，可以深入到方法前后，异常抛出前后
- 拦截器是基于Java反射的，

> 执行顺序：Filter > Listener > Interceptor



# Bean生命周期回调方法

有两个回调方法：init 和 destory

- init-method、实现InitializingBean的afterPropertiesSet方法、@PostConstruct
- destory-metbod 、实现DisposableBean的destory方法、@PreDestory



# AOP底层怎么实现的

https://www.cnblogs.com/zedosu/p/6709921.html





# 对Spring，SpringBoot的理解





# SpringBoot自动装配原理



# 自定义过starter么？用来做什么？怎么自定义的

- META-INF下面创建spring.factories文件，里面配置 autoConfiguration= 自动配置类
- 在自己的自动配置类里面，装配自己的bean，通过一些@ConditionalOnXXX的形式来进行条件装配



# Springcloud组件

- Feign、Ribbon、loadbalance：服务调用、负载均衡
- Hystrix、sentinel：限流、雪崩、熔断
- Nacos、spring cloud config：服务注册
- GateWay、zuul：网关



# Zuul 和 gateway 

区别

- gateway吞吐率比zuul高，耗时比zuul少，性能比zuul高
- gateway对比zuul多依赖了spring-webflux，
- zuul仅支持同步，gateway支持异步。
- gateway具有更好的扩展性

相同点

- 底层都是servlet
- 两者均是web网关，处理的是http请求



# 服务限流

https://blog.csdn.net/hxyascx/article/details/89512278

## 为什么要限？

- 用户增长过快 
- 热点事件（如微博热搜）
- 竞争对象爬虫
- 恶意刷单

这些情况都是无法预知的，不知道什么时候会有10倍甚至20倍的流量打进来，如果真碰上这种情况，扩容是根本来不及的。

## 限流算法实现

常用的限流算法有：计数器、令牌桶、漏桶

### 计数器算法

这种方式比较暴力：一般我们会限制一秒钟能够通过的请求数，比如qps为100，那么从第一个请求进来开始计时，在接下来的1秒钟内，如果累加的数字达到了100，那么后续的请求会被全部拒绝，等到1秒结束后，计数恢复成0，重新开始计数；

实现思路：通过AtomicLong#incrementAndGet()来完成计数器的增加操作；

缺陷：不能均衡请求，比如钱10ms已经有了100个请求，那么后面的990毫秒只能拒绝请求，形成"突刺"；

> Jdk 提供的 Semaphore
>
> ```java
> Semaphore sp = new Semaphore(3); // 限制许可大小为3；
> sp.acquire(); //获取许可，成功的话，许可数减1，否则会阻塞
> sp.release(); //释放许可，许可数加1
> ```

### 漏桶算法

该算法原理类似于漏桶，不管进来多少个请求，每10ms处理一次请求。

实现思路：通过准备一个队列（有容量），用来保存请求，另外通过一个线程（池）定期从队列中获取请求并执行，可以一次性获取多个并发执行。如果漏桶满了，就拒绝新进来的请求；

缺点：无法应对短时间的突发流量

### 令牌桶算法

在令牌桶算法中，存在一个桶，用来存放固定数量的令牌。算法中存在一种机制，以一定的速率往桶中放令牌。每次请求调用需要先获取令牌，只有拿到令牌，才有机会继续执行，否则选择选择等待可用的令牌、或者直接拒绝。

放令牌这个动作是持续不断的进行，如果桶中令牌数达到上限，就丢弃令牌，这样，如果桶中还剩余大量的令牌，则可以抵挡瞬间的“突刺”请求。在没有令牌的时候，才需要进行等待；以一定的速度执行；

实现思路：可以准备一个队列，用来保存令牌，另外通过一个线程池定期生成令牌放到队列中，每来一个请求，就从队列中获取一个令牌，并继续执行

> Guava提供了一个令牌桶算法的限流器RateLimiter。
>
> ```java
> RateLimiter rateLimiter = RateLimiter.create(4); //每秒产生4个令牌
> rateLimiter.acquire(2); // 获取2令牌；
> ```
>
> RateLimiter和Semaphore的区别在于，RateLimiter不需要对令牌进行回收
>
> RateLimiter是匀速添加令牌

## 分布式限流

可以通过redis + lua 来实现，主要还是利用了incrby 和 expire命令

> Redission 提供的 RRateLimiter
>
> ```java
> RRateLimiter rateLimiter=RedissonClient.getRateLimiter("rate_limiter");
> rateLimiter.trySetRate(RateType.PER_CLIENT,5,2, RateIntervalUnit.MINUTES);
> rateLimiter.acquire()； // 获取令牌
> ```

可以通过给网关整合上述限流能力达到分布式限流的效果
