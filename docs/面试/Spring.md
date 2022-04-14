[toc]

# Filter 、拦截器区别，项目使用地方

- filter接口在javax.servlet包下面，是servlet规定的。inteceptor定义在org.springframework.web.servlet中，可以用于web程序，也可以用于普通的应用；
- filter是servlet容器支持的，interceptor是spring框架支持的
- filter通过dochain放行，interceptor通过prehandler放行。
- filter只在方法前后执行，interceptor粒度更细，可以深入到方法前后，异常抛出前后
- 拦截器是基于Java反射的，

> 执行顺序：Filter > Listener > Interceptor



# IOC



## IOC实现机制

工厂模式+反射



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



## Bean注册顺序

1. @Configuration  
2. @Component 
3. @Import—类
4. @Bean
5. @Import—ImportBeanDefinitionRegistrar



## BeanFactory 和FactoryBean有什么区别

BeanFactory是一个工厂，也就是一个容器，是来管理和生产bean的；

FactoryBean是一个bean，但是它是一个特殊的bean，所以也是由BeanFactory来管理的，它是一个接口，他必须被一个bean去实现。不过FactoryBean不是一个普通的Bean，它会表现出工厂模式的样子,是一个能产生或者修饰对象生成的工厂Bean，里面的getObject()就是用来获取FactoryBean产生的对象。



## Spring有哪些扩展点

### IOC阶段回调

- BeanFactoryPostProcessor的postProcessBeanFactory方法
- BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法

### Bean生命周期回调

- BeanNameAware
- BeanFactoryAware
- ApplcationContextAware
- BeanPostProcessor接口的postProcessBeforeInitialization方法

- InitializingBean接口的afterPropertiesSet ；或者 工厂方法中声明的 init-mothod指定的方法； 或者 注解方式中 @PostConstuct标注的方法
- BeanPostProcessor 的 postProcessAfterInitialization 方法
- DisposableBean接口的destory 方法； 或者 工厂方法中destroy-method指定的方法； 或者 @PreDestory 标注的方法



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



## Bean生命周期回调方法

### 初始化回调

- init-method指定的方法
- 实现InitializingBean的afterPropertiesSet方法
- @PostConstruct标注的方法

### 销毁回调

- destory-metbod 指定的方法
- 实现DisposableBean的destory方法
- @PreDestory标识的方法



## Bean依赖问题

### 循环依赖

参考文献：https://blog.csdn.net/cristianoxm/article/details/113246104

互相引用， A->B->A 或者是 A->B->C->A

#### 解决办法

三级缓存，准确的讲，是三个map

- singletonObjects （一级缓存）：它是我们最熟悉的朋友，俗称“单例池”“容器”，缓存创建完成单例Bean的地方。
- earlySingletonObjects（二级缓存）：映射Bean的早期引用，也就是说在这个Map里的Bean不是完整的，甚至还不能称之为“Bean”，只是一个Instance。如果bean被AOP切面代理了，则保存的是代理bean示例的ProxyBean；否则，是原始未完成属性填充的实例；
- singletonFactories（三级缓存）： 映射创建Bean的原始工厂，存放的是ObjectFactory，传入的是一个匿名内部类，如果用到三级缓存，会调用objectFactory.getObject()，最终会调用getEarlyBeanReference方法，返回一个示例

> #### 过程描述
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

#### 解决循环依赖必须要三级缓存吗

二级缓存也是可以解决循环依赖的。

如果 Spring 选择二级缓存来解决循环依赖的话，那么就意味着所有 Bean 都需要在实例化完成之后就立马为其创建代理，而Spring 的设计原则是在<font color=red> Bean 初始化完成之后才为其创建代理</font>。所以，Spring 选择了三级缓存。但是因为循环依赖的出现，导致了 Spring 不得不提前去创建代理，因为如果不提前创建代理对象，那么注入的就是原始对象，这样就会产生错误

#### 注意事项

- 非单例模式下，无法处理
- 构造函数的循环依赖会报错。可以通过人工进行解决：@Lazy



### 如何让找不到依赖Bean不报错

 @Autowired(required = false) 

### 如何找到多个依赖的Bean不报错

- 被依赖Bean上添加@Primary
- 通过@Qualifier标注要注入的属性，指定要装配的



## Bean作用域

- **singleton :** bean在每个Spring ioc 容器中只有一个实例。（默认）
- **prototype**：一个bean的定义可以有多个实例
- **request**：每次http请求都会创建一个bean，该作用域仅在基于web的Spring ApplicationContext情形下有效。
- **session**：在一个HTTP Session中，一个bean定义对应一个实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。
- **global-session**：该属性仅用于HTTP Session， 同session 作用域不同的是，所有的Session共享一个Bean实例。该作用域仅在基于web的Spring ApplicationContext情形下有效。



## 单例Bean

### 单例优势

- 减少对象实例化的资源和时间消耗
- 充分利用内存空间，减少JVM的可能

### 线程安全

- Spring框架中的单例bean不是线程安全的。

可以通过如下两种方式解决单例Bean的线程安全问题：

- 不要定义全局变量
- 使用ThreadLocal来存放全局变量



## Spring实例化Bean方式

1. 构造器方式（反射）； 
2. 静态工厂方式； factory-method 
3. 实例工厂方式(@Bean)； factory-bean+factory-method
4. FactoryBean方式



## Bean装配

### 装配方式

1. xml: <bean class="com.tuling.UserService" id="">
2. 注解：@Component(@Controller 、@Service、@Repostory)  前提：需要配置扫描包<component-scan>  反射调用构造方法
3. javaConfig: @Bean  可以自己控制实例化过程
4. @Import  3种方式
   - Import（类）：指定装配某个类
   - Import（ImportSelector）：可以一次装配多个
   - Impor（ImportBeanDefinitionRegistrar ）：可以一次性注册多个，通过BeanDefinitionRegistry来动态注册BeanDefintion

### 自动装配

- no：默认的方式是不进行自动装配的，通过手工设置ref属性来进行装配bean。@Autowired 来进行手动指定需要自动注入的属性
- byName：通过bean的名称进行自动装配，如果一个bean的 property 与另一bean 的name 相同，就进行自动装配。
- byType：通过参数的数据类型进行自动装配。
- constructor：利用构造函数进行装配，并且构造函数的参数通过byType进行装配。

### JavaConfig方式怎么代替XML的

| 对比项               | XML                                               | JavaConfig                                           |
| -------------------- | ------------------------------------------------- | ---------------------------------------------------- |
| Spring容器           | ClassPathXmlApplicationContext("xml")             | AnnotationConfigApplicationContext(javaconfig.class) |
| 配置Bean             | Spring.xml文件                                    | @Configuration                                       |
| Bean属性配置         | <bean scope=.... lazy=...>                        | @Scope、@Lazy                                        |
| 包扫描               | <component-scan>                                  | @ComponentScan                                       |
| 引入外部属性配置文件 | <property-placeHodeler resource="xxx.properties"> | @PropertySource("classpath:db.properties")           |
| 指定其他配置文件     | <import resource=""                               | @Import  @Import({配置类}) 使用比较灵活              |

### @Autowired和@Resource之间的区别

- @Autowired是Spring提供的，默认是按照类型装配注入的，默认情况下它要求依赖对象必须存在（可以设置它required属性为false）。
- @Resource是JAVA规范：默认是按照名称来装配注入的，只有当找不到与名称匹配的bean才会按照类型来装配注入。



## Spring如何处理线程并发问题？

ThreadLocal



# AOP

## 专用术语

1. 切面（Aspect）： 在Spring Aop指定就是“切面类” ，切面类会管理着切点、通知。
2. 连接点（Join point）： 指定就是被增强的业务方法
3. 通知（Advice）：   就是需要增加到业务方法中的公共代码， 通知有很多种类型分别可以在需要增加的业务方法不同位置进行执行（前置通知、后置通知、异常通知、返回通知、环绕通知）
4. 切点（Pointcut）： 由他决定哪些方法需要增强、哪些不需要增强， 结合切点表达式进行实现
5. 目标对象（Target Object）： 指定是增强的对象
6. 织入（Weaving） ： spring aop用的织入方式：动态代理。 就是为目标对象创建动态代理的过程就叫织入。



## 通知类型

1. 前置通知（Before）：在目标方法被调用之前调用通知功能；
2. 后置通知（After）：在目标方法完成之后调用通知，此时不会关心方法的输出是什么；
3. 返回通知（After-returning ）：在目标方法成功执行之后调用通知；图灵课堂
4. 异常通知（After-throwing）：在目标方法抛出异常后调用通知；
5. 环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为



## Spring AOP 和 AspectJ AOP 区别

- Spring AOP使用的动态代理，它基于动态代理来实现（默认地，如果使用接口的，用 JDK 提供的动态代理实现，

  如果没有接口，使用 CGLIB 实现）

- AspectJ是静态代理的增强，所谓静态代理，就是AOP框架会在编译阶段生成AOP代理类，因此也称为编译时增强，他会在编译阶段将AspectJ(切面)织入到Java字节码中，运行的时候就是增强之后的AOP对象。



## JDK动态代理和CGLIB动态代理的区别

### JDK代理

#### 要求

**<font color=red>jdk代理要求被代理的对象必须实现了某个接口</font>**

#### 核心API

- `InvocationHandler`：代理类需要实现
- `Proxy.newProxyIntance`：构建代理对象

#### 实现

1. 创建代理：代理类需要实现`InvocationHandler`接口，然后通过method.invoke来调用原始方法

   ```java
   public class ProxyClass implements InvocationHandler{
       private Object target;// 被代理对象
       public ProxyClass(Object target) {
           this.target = target;
       }
       //proxy：被代理的对象
       //method：要调用的方法
       //args：参数
       @Override
       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           // 方法调用前增强
           Object invoke = method.invoke(target, args); // 调用代理方法
           // 方法调用后增强
           return invoke; //也可以对原始方法调用的结果进行修改
       }
   }
   ```

2. 构建代理：这里可以通过构建代理工厂来达到方便调用的目的

   ```java
   public class ProxyFactory {
       public static TargetClass getInstance(Object target) {
           InvocationHandler handler = new ProxyClass(target);
           TargetClass proxy = null;
           proxy = (TargetClass) Proxy.newProxyInstance(
                   target.getClass().getClassLoader(),
                   target.getClass().getInterfaces(),
                   handler);
           return proxy;
       }
   }
   ```

3. 使用

   ```java
   ProxyFactory.getIntance(targetObject).xxxMethod();
   ```

### cglib代理

#### 要求

- cglib通过动态修改字节码达到代理的目的，通过集成需要代理的类，然后调用方法的时候，通过super.xxx调用原始方法。所以，<font color=red><b>被代理的类，不能使用final修饰</b></font>

#### 核心API

- `MethodInterceptor`：代理接口，在这里实现代理逻辑
- `Enhancer`：主要的增强类，用来生成代理。

#### 实现

1. 创建代理类

   ```java
   public class ProxyClass implements MethodInterceptor{
   	//methodProxy：方法的代理类
   	@Override
   	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
   		 // 增强
   		Object result =  methodProxy.invokeSuper(obj, args);
           //增强
           return result;
   	}
   
   }
   ```

2. 创建代理工厂

   ```java
   public class ProxyFactory {
   	public static Object getInstance(Class<?> clazz){
   		Enhancer enhancer = new Enhancer();
   		enhancer.setSuperclass(clazz);
           enhancer.setCallback(new ProxyClass());
           return enhancer.create();
   	}
   }
   ```

3. 使用

   ```java
   ProxyFactory.getIntance(TargetClass.class).xxxMethod();
   ```

   

## AOP底层

https://www.cnblogs.com/zedosu/p/6709921.html



## AOP 失效原因及解决

### 配置问题

切面配置错误、切点表达式错误等

### 方法被生命为private的

修改为public

### 同类内部调用

解决方式：必须走代理

- 本来注入当前bean，使用bean调用另外的方法
- 设置暴露当前代理到本地线程，使用代理调用方法
  - 暴露：@EnableAspectJAutoProxy(exposeProxy=true)
  - 使用：（强制类型转换）AopContext.currentProxy(). 方法



# 事务

## 传播行为

- REQUIRED：如果当前没有事务，就创建一个事务，如果已经存在一个事务，就加入这个事务
- REQUIRES_NEW：如果当前存在事务，则把当前事务挂起，并重新创建新的事务并执行，直到新的事务提交或者回滚，才会恢复执行原来的事务
- SUPPORTS：支持当前事务，如果当前没有事务，就以非事务的方式执行。外部不存在事务时，不会开启新的事务，外部存在事务时，将其加入外部事
- MANDATORY：支持当前事务，这种事务传播类型具备强制性，当前操作必须存在事务，如果不存在，则抛出异常。
- NOT_SUPPORTED：以非事务方式执行，如果当前操作在一个事务中，则把当前事务挂起，直到当前操作完成再恢复事务的执行。如果当前操作存在事务，则把事务挂起，以非事务的方式运行
- NEVER：以非事务的方式执行，如果当前操作存在事务，则抛出异常
- NESTED：如果当前方法有一个事务正在运行，则这个方法应该运行在一个嵌套事务中，被嵌套的事务可以独立于被封装的事务进行提交或者回滚。如果没有活动事务，则按照REQUIRED事务传播类型执行。



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

