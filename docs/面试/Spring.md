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

- Feign、Ribbon：服务调用、负载均衡
- Hystrix：限流、雪崩、熔断
- Nacos：服务注册
- GateWay：网关



# Zuul 和 gateway 区别





# 
