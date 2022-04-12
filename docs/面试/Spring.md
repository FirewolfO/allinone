[toc]

# Filter 、拦截器区别，项目使用地方

- filter接口在javax.servlet包下面，是servlet规定的。inteceptor定义在org.springframework.web.servlet中，可以用于web程序，也可以用于普通的应用；
- filter是servlet容器支持的，interceptor是spring框架支持的
- filter通过dochain放行，interceptor通过prehandler放行。
- filter只在方法前后执行，interceptor粒度更细，可以深入到方法前后，异常抛出前后
- 拦截器是基于Java反射的，

> 执行顺序：Filter > Listener > Interceptor



# IOC

## IOC容器的加载过程

### 从概念态--->定义态的过程（解析得到BeanDefinition）

1. 实例化一个ApplicationContext的对象； 
2. 调用bean工厂后置处理器完成扫描； 
3. 循环解析扫描出来的类信息； 
4. 实例化一个BeanDefinition对象来存储解析出来的信息； 
5. 把实例化好的beanDefinition对象put到beanDefinitionMap当中缓存起来， 以便后面实例化bean； 
6. 再次调用其他bean工厂后置处理器；

### 从定义态到纯净态（实例化）

1. 实现化对象。（检查是否是lazy，作用域、是否是abstract等、构造方法推断）

### 从纯净态到成熟态（属性注入）

1. spring处理合并后的beanDefinition 
2. 判断是否需要完成**属性注入**(循环依赖的解决)

### 初始化

1. 判断bean的类型回调Aware接口 

2. 调用生命周期回调方法 

3. 如果需要代理则完成代理

### **创建完成** 

1. put到单例池——bean完成，存到Spring容器中



# Spring Bean

## Bean生命周期

1. 实例化
   - 通过反射去推断构造函数进行实例化
   - 实例工厂、 静态工厂等方式实例化
2. 属性赋值
   - 解析自动装配（byname bytype constractor none @Autowired）  DI的体现
   - 解决循环依赖
3. 初始化
   - 调用XXXAware回调方法
     - BeanNameAware
     - BeanFactoryAware
     - ApplcationContextAware
   - 调用初始化生命周期回调
     - BeanPostProcessor接口的postProcessBeforeInitialization方法
     - InitializingBean接口的afterPropertiesSet ；或者 工厂方法中声明的 init-mothod指定的方法； 或者 注解方式中 @PostConstuct标注的方法
     - BeanPostProcessor 的 postProcessAfterInitialization 方法
   - 如果bean实现aop 创建动态代理
4. 销毁
   - 在spring容器关闭的时候进行调用
   - 调用销毁生命周期回调
     - DisposableBean接口的destory 方法； 或者 工厂方法中destroy-method指定的方法； 或者 @PreDestory 标注的方法

# Bean生命周期回调方法

## 初始化回调

- init-method指定的方法
- 实现InitializingBean的afterPropertiesSet方法
- @PostConstruct标注的方法

## 销毁回调

- destory-metbod 指定的方法
- 实现DisposableBean的destory方法
- @PreDestory标识的方法



# 循环依赖

参考文献：https://blog.csdn.net/cristianoxm/article/details/113246104

互相引用， A->B->A 或者是 A->B->C->A

## 解决办法

三级缓存，准确的讲，是三个map

- singletonObjects （一级缓存）：它是我们最熟悉的朋友，俗称“单例池”“容器”，缓存创建完成单例Bean的地方。
- earlySingletonObjects（二级缓存）：映射Bean的早期引用，也就是说在这个Map里的Bean不是完整的，甚至还不能称之为“Bean”，只是一个Instance。如果bean被AOP切面代理了，则保存的是代理bean示例的ProxyBean；否则，是原始未完成属性填充的实例；
- singletonFactories（三级缓存）： 映射创建Bean的原始工厂，存放的是ObjectFactory，传入的是一个匿名内部类，如果用到三级缓存，会调用objectFactory.getObject()，最终会调用getEarlyBeanReference方法，返回一个示例

> ## 过程描述
>
> 1. 实例化 A，此时 A 还未完成属性填充和初始化方法（@PostConstruct）的执行，A 只是一个半成品。
> 2. 为 A 创建一个 Bean工厂，并放入到 singletonFactories 中。
> 3. 发现 A 需要注入 B 对象，但是一级、二级、三级缓存均为发现对象 B。
> 4. 实例化 B，此时 B 还未完成属性填充和初始化方法（@PostConstruct）的执行，B 只是一个半成品。
> 5. 为 B 创建一个 Bean工厂，并放入到 singletonFactories 中。
> 6. 发现 B 需要注入 A 对象，此时在一级、二级未发现对象A，但是在三级缓存中发现了对象 A，从三级缓存中得到对象 A，并将对象 A 放入二级缓存中，同时删除三级缓存中的对象 A（注意，此时的 A还是一个半成品，并没有完成属性填充和执行初始化方法）
> 7. 将对象 A 注入到对象 B 中。
> 8. 对象 B 完成属性填充，执行初始化方法，并放入到一级缓存中，同时删除二级缓存中的对象 B。（此时对象 B 已经是一个成品）
> 9. 对象 A 得到对象B，将对象 B 注入到对象 A 中。（对象 A 得到的是一个完整的对象 B）
> 10. 对象 A完成属性填充，执行初始化方法，并放入到一级缓存中，同时删除二级缓存中的对象 A。



## 解决循环依赖必须要三级缓存吗

二级缓存也是可以解决循环依赖的。

如果 Spring 选择二级缓存来解决循环依赖的话，那么就意味着所有 Bean 都需要在实例化完成之后就立马为其创建代理，而Spring 的设计原则是在<font color=red> Bean 初始化完成之后才为其创建代理</font>。所以，Spring 选择了三级缓存。但是因为循环依赖的出现，导致了 Spring 不得不提前去创建代理，因为如果不提前创建代理对象，那么注入的就是原始对象，这样就会产生错误




## 注意事项

- 非单例模式下，无法处理
- 构造函数的循环依赖会报错。可以通过人工进行解决：@Lazy



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
