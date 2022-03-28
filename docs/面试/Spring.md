[toc]

# 生命周期回调方法

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

- gateway吞吐率比zuul高，耗时比zuul少，性能比zuul高倍左右，
- gateway对比zuul多依赖了spring-webflux，
- zuul仅支持同步，gateway支持异步。
- gateway具有更好的扩展性

相同点

- 底层都是servlet
- 两者均是web网关，处理的是http请求



# 
