[toc]

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



## Spring整合MyBatis将Mapper接口注册为Bean原理

### 注册到Spring容器中

- 首先MyBatis的Mapper接口核心是**JDK动态代理**，由于没有实现，Spring会排除接口，无法注册到IOC容器中。
- 通过**自定义扫描器**（继承Spring内部扫描器ClassPathBeanDefinitionScanner ) 重写排除接口的方法（isCandidateComponent）
- Mybatis通过实现**BeanDefinitionRegistryPostProcessor** 动态注册BeanDefinition；
- 此时无法无法实例化

### 实例化

- 需要将BeanDefinition的BeanClass 替换成JDK动态代理的实例
- Mybatis 通过**FactoryBean**的工厂方法设计模式可以自由控制Bean的实例化过程，可以在getObject方法中创建JDK动态代理



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





## AOP 失效原因及解决

### 配置问题

切面配置错误、切点表达式错误等

### 方法被声明为private

修改为public

### 同类内部调用

解决方式：必须走代理

- 本来注入当前bean，使用bean调用另外的方法
- 设置暴露当前代理到本地线程，使用代理调用方法
  - 暴露：@EnableAspectJAutoProxy(exposeProxy=true)
  - 使用：（强制类型转换）AopContext.currentProxy(). 方法



## Spring AOP在哪里创建的动态代理

1. 正常的Bean会在Bean的生命周期的‘初始化’后， 通过BeanPostProcessor.postProcessAfterInitialization创建aop的动态代理
2. 循环依赖的Bean会在Bean的生命周期‘属性注入’时存在的循环依赖的情况下， 也会为循环依赖的Bean通过MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition创建aop



## Spring的 Aop实现原理

源码参看：https://www.cnblogs.com/zedosu/p/6709921.html

大概可以分为三个步骤：

1. 解析切面

    在Bean创建之前的第一个Bean后置处理器会去解析切面（解析切面中通知、切点，一个通知就会解析成一个advisor(通知、切点)），然后把这些advisor缓存起来（用addAdvisorOnChainCreation方法注册到advisor链上。）。

2. 创建动态代理

   正常的Bean初始化后调用BeanPostProcessor 拿到之前缓存的advisor ，再通过advisor中pointcut 判断当前Bean是否被切点表达式匹配，如果匹配，就会为Bean创建动态代理（创建方式1.jdk动态代理2.cglib)。

3. 调用

   拿到动态代理对象， 调用方法 就会判断当前方法是否增强的方法， 就会通过**调用链**的方式依次去执行通知.



# 事务

## Spring事务支持方式

### 编程式事务

通过编程的方式管理事务，给你带来极大的灵活性，但是难维护

### 声明式事务

可以将业务代码和事务管理分离，你只需用注解和XML配置来管理事务

- 基于接口

  - 基于 **TransactionInterceptor** 的声明式事务：Spring 声明式事务的基础，通常也不建议使用这种方式，但是与aop一样，了解这种方式对理解 Spring 声明式事务有很大作用。
  -  基于 **TransactionProxyFactoryBean**的声明式事务：第一种方式的改进版本，简化的配置文件的书写，这是Spring 早期推荐的声明式事务管理方式，但是在 Spring 2.0 中已经不推荐了。

- 基于**配置文件**方式

  - 使用<tx><aop>等命名空间的一些标签，来管理事务

    ```xml
    <tx:advice id="txAdvice" transaction-manager="txManager">
        <tx:attributes>
            <!-- 配置需要开启事务的方法，事务的传播行为，隔离级别等 -->
            <tx:method name="*" propagation="REQUIRED" />
        </tx:attributes>
    </tx:advice>
    <!-- 配置切面 -->
    <aop:config>
        <aop:pointcut expression="execution(* com.firewolf.spring.tx.xml.*Service.*(..))" id="pc"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pc"/>
    </aop:config>
    ```

- 基于**@Transactional 的注解方式**

  ```java
  public @interface Transactional {
      @AliasFor("transactionManager")
      String value() default "";
  
      @AliasFor("value")
      String transactionManager() default "";
  
      Propagation propagation() default Propagation.REQUIRED;
  
      Isolation isolation() default Isolation.DEFAULT;
  
      int timeout() default -1;
  
      boolean readOnly() default false;
  
      Class<? extends Throwable>[] rollbackFor() default {};
  
      String[] rollbackForClassName() default {};
  
      Class<? extends Throwable>[] noRollbackFor() default {};
  
      String[] noRollbackForClassName() default {};
  }
  ```



## Spring支持的事务属性

**1. propagation** ：指定事务的传播行为，即当前的事务方法被另外一个事务调用时如何使用事务

**2. isolation** ：  指定事务的隔离级别，最常用的值为READ_COMMITED;

**3. 回滚规则**：事务的回滚规则，默认情况下声明式事务对所有的运行时异常进行回滚，也可以通过对应的属性进行设置，通常使用默认值；

**4. readOnly**：指定事务是否为只读，表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务；若真的是只读取的，应设置为true，否则默认为false；

**5. timeout** ： 指定强制回滚之前事务可以占用的时间



## 传播行为

### 7种传播行为

- REQUIRED：如果当前没有事务，就创建一个事务，如果已经存在一个事务，就加入这个事务
- REQUIRES_NEW：如果当前存在事务，则把当前事务挂起，并重新创建新的事务并执行，直到新的事务提交或者回滚，才会恢复执行原来的事务
- SUPPORTS：支持当前事务，如果当前没有事务，就以非事务的方式执行。外部不存在事务时，不会开启新的事务，外部存在事务时，将其加入外部事
- MANDATORY：支持当前事务，这种事务传播类型具备强制性，当前操作必须存在事务，如果不存在，则抛出异常。
- NOT_SUPPORTED：以非事务方式执行，如果当前操作在一个事务中，则把当前事务挂起，直到当前操作完成再恢复事务的执行。如果当前操作存在事务，则把事务挂起，以非事务的方式运行
- NEVER：以非事务的方式执行，如果当前操作存在事务，则抛出异常
- NESTED：如果当前方法有一个事务正在运行，则这个方法应该运行在一个嵌套事务中，被嵌套的事务可以独立于被封装的事务进行提交或者回滚。如果没有活动事务，则按照REQUIRED事务传播类型执行。

### 实现原理

.Spring的事务信息是存在ThreadLocal中的， 所以一个线程永远只能有一个事务。

如：

- REQUIRED：当传播行为是融入外部事务则拿到ThreadLocal中的Connection、共享一个数据库连接共同提交、回滚； 

- REQUIRES_NEW：当传播行为是创建新事务，会再将外部事务暂存起来，然后将嵌套新事务存入ThreadLocal； 当嵌套事务提交、回滚后，会将暂存的事务信息恢复到ThreadLocal中



## 隔离级别

### 常见问题

- 脏读（Dirty reads）——脏读发生在一个事务读取了另一个事务改写但尚未提交的数据时。如果改写在稍后被回滚了，那么第一个事务获取的数据就是无效的。
- 不可重复读（Nonrepeatable read）——不可重复读发生在一个事务执行相同的查询两次或两次以上，但是每次都得到不同的数据时。这通常是因为另一个并发事务在两次查询期间进行了更新。
- 幻读（Phantom read）——幻读与不可重复读类似。它发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录。

### Spring隔离级别

- ISOLATION_DEFAULT：使用后端数据库默认的隔离级别
- ISOLATION_READ_UNCOMMITTED：最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
- ISOLATION_READ_COMMITTED：允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
- ISOLATION_REPEATABLE_READ：对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生
- ISOLATION_SERIALIZABLE：最高的隔离级别，完全服从ACID的隔离级别，确保阻止脏读、不可重复读以及幻读，也是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关的数据库表来实现的。



## Spring事务实现原理

本质是上也是使用AOP来完成的，所以跟SpringAOP基本一致；

1. 解析切面

   bean的创建前第一个bean的后置处理器进行解析advisor（通过对@Transacational解析的切点）

2. 创建动态代理

   bean的初始化后调用bean的后置处理器进行创建动态代理(有接口使用jdk，没接口使用cglib)， 创建动态代理之前会先根据advisor中pointCut 匹配@Transacational( 方法里面是不是有、类上面是不是有、接口或父类上面是不是有 )  ， 匹配到就创建动态代理。

   ```java
   try{
       a.创建一个数据库连接Connection, 并且修改数据库连接的autoCommit属性为false，禁止此连接的自动提交，这是
       实现Spring事务非常重要的一步
   
       b.然后执行目标方法方法，方法中会执行数据库操作sql 
   }catch{
   	6.如果出现了异常，并且这个异常是需要回滚的就会回滚事务，否则仍然提交事务
   }
   ```

3.调用： 动态代理



## Spring事务的失效原因

同AOP失效原因



# SpringMVC

## SpringMVC工作原理

1. 用户向服务器发送请求，请求被Spring 前端控制Servelt DispatcherServlet的doDispatcher捕获；

2. HandlerMapping根据请求获得该Handler配置的所有相关的对象（包括Handler对象以及Handler对象对应的拦截器），最后以HandlerExecutionChain对象的形式返回;

3. DispatcherServlet 根据获得的Handler，选择一个合适的HandlerAdapter。（**附注**：如果成功获HandlerAdapter后，此时将开始执行拦截器的preHandler(...)方法）

4. 通过HandlerAdapter提取Request中的模型数据，填充Handler入参，开始执行Handler（Controller)。 在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作：

   - HttpMessageConveter： 将请求消息（如Json、xml等数据）转换成一个对象，将对象转换为指定的响应信息
   - 数据转换：对请求消息进行数据转换。如String转换成Integer、Double等
   - 数据格式化：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等
   - 数据验证： 验证数据的有效性（长度、格式等），验证结果存储到BindingResult或Error；

5. Handler执行完成后，向DispatcherServlet 返回一个ModelAndView对象

6. 通过HandlerAdapter调用所有拦截器的postHandle方法

7. 根据返回的ModelAndView，选择一个适合的ViewResolver（必须是已经注册到Spring容器中的ViewResolver)返回给DispatcherServlet ；

8. ViewResolver 结合Model和View，来渲染视图

9. 通过HandlerAdapter调用所有拦截器的afterCompletion方法

10. 将渲染结果返回给客户端。

    

## Spring和SpringMVC父子容器

### 什么是父子容器？

springweb项目中创建了2个WebApplicationContext，分别是spring创建的容器applicationContext.xml和springmvc创建的dispatcher-servlet.xml。

- **springmvc   dispatcher-servlet.xml**：在MVC三层模型中的controller控制层中，与视图解析器ViewReslover，HandlerMapping处理器映射器等打交道，用于前后端交互
- **spring   applicationContext.xml**：在MVC三层模型中的service业务层和Dao持久层中，管理bean的注入以及bean所相关依赖对象的注入

 ![image-20220415171131074](https://gitee.com/firewolf/allinone/raw/master/images/image-20220415171131074.png)

### 父子容器特点

1. 父容器和子容器是相互隔离的，他们内部可以存在名称相同的bean
2. 子容器可以访问父容器中的bean，而父容器不能访问子容器中的bean
3. 调用子容器的getBean方法获取bean的时候，会沿着当前容器开始向上面的容器进行查找，直到找到对应的bean为止
4. 子容器中可以通过任何注入方式注入父容器中的bean，而父容器中是无法注入子容器中的bean，原因是第2点



### 为什么需要父子容器

就功能性来说不用子父容器也可以完成（参考：SpringBoot就没用子父容器）

1. <font color=red>**划分框架边界**</font>。有点单一职责的味道。service、dao层我们一般使用spring框架来管理；controller层交给springmvc管理

2. 规范整体架构：使父容器service无法访问子容器controller、子容器controller可以访问父容器 service

3. 方便子容器的切换：如果现在我们想把web层从spring mvc替换成struts，那么只需要将spring­mvc.xml替换成Struts的配置文件struts.xml即可，而spring­core.xml不需要改变。

4. 为了节省重复bean创建（避免子容器创建多余的Bean）

   

### 是否可以把所有Bean都通过Spring容器来管管理

也就是，所有的Bean，都是用Spring容器进行管理（全部配置到Spring文件中）

不可以。这样会导致我们请求接口的时候产生404。 如果所有的Bean都交给父容器，SpringMVC在初始化HandlerMethods的时候（initHandlerMethods）无法根据Controller的handler方法注册HandlerMethod，并没有去查找父容器的bean；也就无法根据请求URI 获取到HandlerMethod来进行匹配。



### 是否可以用子容器来管理所有bean

也就是，所有的Bean，都让spring-mvc子容器进行管理（全部配置到springmvc的文件中）

可以。因为父容器的体现无非是为了获取子容器不包含的bean, 如果全部包含在子容器完全用不到父容器了， 所以是可以全部放在springmvc子容器来管理的。

虽然可以这么做不过一般应该是不推荐这么去做的，一般人也不会这么干的。**如果你的项目里有用到事务、或者aop记得也需要把这部分配置需要放到Spring-mvc子容器的配置文件来，不然一部分内容在子容器和一部分内容在父容器,可能就会导致你的事务或者AOP不生效**。   所以如果aop或事物如果不生效也有可能是通过父容器(spring)去增强子容器(Springmvc)，也就无法增强。



# SpringBoot



## SpringBoot相对于Spring有什么优点？

SpringBoot的用来快速开发Spring应用的一个脚手架、其设计目的是用来简新Spring应用的初始搭建以及开发过程。

- 提供了很多内置的Starter结合自动配置，对主流框架无配置集成、开箱即用。
- 简化了开发，采用JavaConfig的方式可以使用零xml的方式进行开发（Spring3具备了）
- 内置Web容器无需依赖外部Web服务器，省略了Web.xml，直接运行jar文件就可以启动web应用；
- 管理了常用的第三方依赖的版本，减少出现版本冲突的问题；
- 自带了监控功能，可以监控应用程序的运行状况，或者内存、线程池、Http 请求统计等，同时还提供了优雅关闭应用程序等功能。



## Spring和SpringBoot的关系和区别

广义上：Spring是一个生态，SpringBoot是这个生态中的一个产品

狭义上：Spring通常指SpringframeWork，提供了基础的容器能力；SpringBoot是用来快速构建Spring应用的脚手架；



## SpringBoot核心注解

- @SpringBootApplication注解：这个注解标识了一个SpringBoot工程，它实际上是另外三个注解的组合
  - @SpringBootConfiguration：这个注解实际就是一个@Configuration，表示启动类也是一个配置类
  - @EnableAutoConfiguration：向Spring容器中导入了一个Selector，用来加载ClassPath下SpringFactories中所定义的自动配置类，将这些自动加载为配置Bean 
  - @ComponentScan（Spring的）：指定要扫描的类
- @Conditional 也很关键， 如果没有它我们无法在自定义应用中进行定制开发
  - @ConditionalOnBean
  - @ConditionalOnClass
  - @ConditionalOnExpression
  - @ConditionalOnMissingBean
  - .....



## SpringBoot自动装配原理

1. 通过在启动类标注@SpringBootApplication，该注解中的@SpringBootConfiguration 引入了**@EnableAutoConfiguration** (负责启动自动配置功能）
2. @EnableAutoConfiguration 引入了**@Import**
3. Spring容器启动时：加载Ioc容器时会解析@Import 注解
4. @Import导入了一个**DeferredImportSelector**(它会使SpringBoot的自动配置类的顺序在最后，这样方便我们扩展和覆盖)
5. 然后读取所有的**/META-INF/spring.factories**文件（伪SPI)
6. 通过查找`org.springframework.boot.autoconfigure.EnableAutoConfiguration`key后面配置的类，过滤出所有**AutoConfigurtionClass**类型的类。
7. 最后通过**@ConditioOnXXX**排除无效的自动配置类
8. 加载有效的Bean进行加载



## 为什么SpringBoot的Jar可以直接运行

1. SpringBoot提供了一个插件spring-boot-maven-plugin用于把程序打包成一个可执行的jar包。
2. Spring Boot应用打包之后，生成一个Fat jar(jar包中包含jar)，包含了应用依赖的jar包和Spring Boot loader相关的类。并在manifest配置了Main-Class
3. java -jar会去找jar中的manifest.mf文件，在那里面找到真正的启动类（mainfest.mf文件中的Main-Class属性值））；他是一个JarLauncher（`org.springframework.boot.loader.JarLauncher`）
4. JarLauncher负责创建一个LaunchedURLClassLoader来加载boot-lib下面的jar，并以一个新线程启动应用的启动类的Main函数（找到mainfest.mf中的Start-Class属性值）



## SpringBoot启动过程

参看文献：《SpringBoot揭秘》

### 简化步骤

1. 使用SpringFactoriesLoader在应用的classpath中（spring.factories或者是实现了接口的）收集各种条件和回调接口
   - ApplicationContextInitializer：容器刷新前的回调接口
   - ApplicationListener：容器各种状态变化事件的回调接口，如：ContextRefreshedEvent、ContextStartedEvent、ContextStoppedEvent、ContextClosedEvent等；
2. 遍历调用SpringApplicationRunListener的started()方法。告诉要开始执行了
3. 创建并准备环境变量，通过environmentPrepared方法通知SpringApplicationRunListener
4. 推断需要创建的ApplicationContext类型并创建，然后给其设置环境变量，并调用这些ApplicationContextInitializer的initialize方法来对已经创建好的ApplicationContext进行进一步的处理。
5. 遍历SpringApplicationRunListener的contextPrepared（）方法。通知ApplicationContext准备就绪。
6. 将通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext
7. 调用ApplicationContext的refresh（）方法。
8. 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
9. 正常情况下，遍历执行SpringApplicationRunListener的finished（）方法，通知启动完成



### 完整过程

1. 如果我们使用的是SpringApplication的静态run方法，那么，这个方法里面首先需要创建一SpringApplication对象实例，然后调用这个创建好的SpringApplication的实例run方法
   - ·根据classpath里面是否存在某个特征类（org.springframework.web.context.ConfigurableWebApplicationContext）来决定是否应该创建一个为Web应用使用的ApplicationContext类型，还是应该创建一个标准Standalone应用使用的ApplicationContext类型。
   - 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationContextInitializer
   - 使用SpringFactoriesLoader在应用的classpath中查找并加载所有可用的ApplicationListener。
   - 推断并设置main方法的定义类
2. 遍历执行所有通过SpringFactoriesLoader可以查找到并加载的SpringApplicationRunListener，调用它们的started（）方法，通知他们应该要开始执行了
3. 创建并配置当前SpringBoot应用将要使用的Environment（包括配置要使用的PropertySource以及Profile）
4. 遍历调用所有SpringApplicationRunListener的environmentPrepared（）的方法，通知Environment已经准备好了
5. 如果SpringApplication的showBanner属性被设置为true，则打印banner
6. 根据用户是否明确设置了applicationContextClass类型以及初始化阶段的推断结果，决定该为当SpringBoot应用创建什么类型的ApplicationContext并创建完成，然后根据条件决定是否添加ShutdownHook，决定是否使用自定义的BeanNameGenerator，决定是否使用自定义的ResourceLoader，当然，最重要的，将之前准备好的Environment设置给创建好的ApplicationContext使用。
7. ApplicationContext创建好之后，SpringApplication会再次借助Spring-FactoriesLoader，查找并加载classpath中所有可用的ApplicationContext-Initializer，然后遍历调用这些ApplicationContextInitializer的initialize（applicationContext）方法来对已经创建好的ApplicationContext进行进一步的处理。
8. 遍历调用所有SpringApplicationRunListener的contextPrepared（）方法。通知ApplicationContext准备就绪
9. 将通过@EnableAutoConfiguration获取的所有配置以及其他形式的IoC容器配置加载到已经准备完毕的ApplicationContext
10. 遍历调用所有SpringApplicationRunListener的contextLoaded（）方法。通知其ApplicationContext装配就绪
11. 调用ApplicationContext的refresh（）方法，完成IoC容器可用的最后一道工序
12. 查找当前ApplicationContext中是否注册有CommandLineRunner，如果有，则遍历执行它们。
13. 正常情况下，遍历执行SpringApplicationRunListener的finished（）方法，通知启动完成。（如果整个过程出现异常，则依然调用所有SpringApplicationRunListener的finished（）方法，只不过这种情况下会将异常信息一并传入处理）

## 内置Tomcat启动原理

1. 当依赖Spring-boot-starter-web依赖时会在SpringBoot中添加servlet容器自动配置类：ServletWebServerFactoryAutoConfiguration

2. 该自动配置类通过@Import导入了可用(通过@ConditionalOnClass判断决定使用哪一个)的一个Web容器工厂（默认Tomcat)

3. 在内嵌Tomcat类中配置了一个TomcatServletWebServerFactory的Bean（Web容器工厂）

4. 它会在SpringBoot启动时 加载ioc容器(refresh)  OnRefersh 创建内嵌的Tomcat并启动 

   

## 自定义starter

- 编写代码，主要是自动配置类，通过一些@ConditionalOnXXX的形式来进行条件装配
- META-INF下面创建spring.factories文件，里面配置 autoConfiguration= 自动配置类

一般分为两部分：autoconfigurer和starter两部分



## SpringBoot读取配置文件

### 原理

通过事件监听的方式读取的配置文件：ConfigFileApplicationListener

### 加载顺序

- file:./config/
- file:./config/xxx/application.properties
- file:./application.properties
- classpath:config
- claspath:



## SpringBoot默认日志框架

slf4j + logback



# Springcloud

## 微服务架构优缺点

### 优点

- 方便分工协作
- 并发能力强（通过集群）
- 容错能力强（减少单点故障如OOM导致整个系统崩溃）
- 方便扩展

### 缺点

- 需要解决分布式的问题：编程难度、一致性、运维复杂性等
- 需要更多的资源



## SOA、分布式、微服务

- 分布式：是指将单体架构中的各个部分拆分，然后部署不同的机器或进程中去，SOA和微服务基本上都是分布式架构的
- SOA：是一种面向服务的架构，系统的所有服务都注册在总线上，当调用服务时，从总线上查找服务信息，然后调用
- 微服务：是一种更彻底的面向服务的架构，将系统中各个功能个体抽成一个个小的应用程序，基本保持一个应用对应的一个服务的架构



## 微服务拆分原则

1. 高内聚低耦合，职责单一，服务粒度适中，服务不要太细
2. 以业务模型切入：比如产品，用户，订单为一个模型来切入）
3. 演进式拆分：刚开始不要划分太细，可以随着迭代过程来逐步优化



## SpringCloud常用组件

- 服务注册中心：<font color=blue>Nacos</font>、Eureka、Zookeeper、Consul
- 负载均衡：<font color=blue>LoadBalancer</font>、 Ribbon
- 服务调用：<font color=blue>OpenFeign</font>、Feign、Dubbo RPC
- 服务配置中心：<font color=blue>Nacos Config </font>、Spring Cloud Config
- 服务熔断：<font color=blue>sentinel </font>、Hystrix
- 服务网关：<font color=blue>Spring Cloud Gateway</font>、Zuul、Kong
- 分布式事务：<font color=blue>Seata</font>



## Zuul 和 gateway对比

### 区别

- gateway吞吐率比zuul高，耗时比zuul少，性能比zuul高
- gateway对比zuul多依赖了spring-webflux，
- zuul仅支持同步，gateway支持异步。
- gateway具有更好的扩展性

### 相同点

- 底层都是servlet
- 两者均是web网关，处理的是http请求



## 注册中心原理

- 服务注册： 当服务启动 通过Rest请求的方式向Nacos Server注册自己的服务

- 服务心跳：Nacose Client 会维护一个定时心跳持续通知Nacos Server , 默认5s一次， 如果nacos Client超过了15秒没有接收心跳，会将服务健康状态设置false（拉取的时候会忽略）,  如果nacos Client超过了30 秒没有接收心跳 剔除服务。
- 服务发现：Nacose Client 会有一个定时任务，实时去Nacos Server 拉取健康服务
- 服务停止： Nacose Client 会主动通过Rest请求Nacos Server 发送一个注销的请求



## 配置中心

### 实现原理

- Nacos 服务端创建了相关的配置项后，客户端就可以进行监听了。
- 客户端是通过一个定时任务来检查自己监听的配置项的数据的，一旦服务端的数据发生变化时，客户端将会拉取到最新的数据，并将最新的数据保存在一个 CacheData 对象中，然后会重新计算 CacheData 的 md5 属性的值，此时就会对该 CacheData 所绑定的 Listener 触发 receiveConfigInfo（接收配置信息） 回调。

### 推还是拉

Nacos采用拉的方式拉是实现的。

- 如果用推的方式，服务端需要维持与客户端的长连接，这样的话需要耗费大量的资源，并且还需要考虑连接的有效性，例如需要通过心跳来维持两者之间的连接。而用拉的方式，客户端只需要通过一个无状态的 http 请求即可获取到服务端的数据。
- 需要监控客户端是否接受到配置



## 网关作用

- 关注稳定与安全：全局性流控、屏蔽工具扫描、日志统计、黑白IP名单、证书/加解密等
- 提供公共服务：服务降级、服务熔断、服务限流、路由与负载均衡、业务校验等；



## 服务熔断、降级

服务熔断：当服务A调用的某个服务B不可用时，上游服务A为了保证自己不受影响，及时切断与服务B的通讯。  防止服务雪崩一种措施

服务降级：提前预想好另外一种兜底措施， 可以进行后期补救。 直到服务B恢复，再恢复和B服务的正常通讯。  当被调用服务不可用时的一种兜底措施



## 服务限流

https://blog.csdn.net/hxyascx/article/details/89512278

### 为什么要限？

- 用户增长过快 
- 热点事件（如微博热搜）
- 竞争对象爬虫
- 恶意刷单

这些情况都是无法预知的，不知道什么时候会有10倍甚至20倍的流量打进来，如果真碰上这种情况，扩容是根本来不及的。

### 限流算法实现

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

### 分布式限流

可以通过redis + lua 来实现，主要还是利用了incrby 和 expire命令

> Redission 提供的 RRateLimiter
>
> ```java
> RRateLimiter rateLimiter=RedissonClient.getRateLimiter("rate_limiter");
> rateLimiter.trySetRate(RateType.PER_CLIENT,5,2, RateIntervalUnit.MINUTES);
> rateLimiter.acquire()； // 获取令牌
> ```

可以通过给网关整合上述限流能力达到分布式限流的效果

### Sentinel限流原理



## Seata实现原理

参考文献：https://blog.csdn.net/weixin_36995355/article/details/109133713

### 核心角色

在应用中Seata整体事务逻辑基于两阶段提交的模型，

- TC：事务协调者，即独立运行的seata-server，用于接收事务注册，提交和回滚。
- TM：事务发起者。用来告诉TC全局事务的开始，提交，回滚。
- RM：事务资源，每一个RM都会作为一个分支事务注册在TC

### 实现机制

默认使用AT模式，两阶段提交协议的演变：

- 一阶段：业务数据和回滚日志记录在同一个本地事务中提交，释放本地锁和连接资源。
- 二阶段：
  - 提交异步化，非常快速地完成。
    回滚通过一阶段的回滚日志进行反向补偿

#### 一阶段加载

在一阶段，Seata会拦截『业务SQL』

1. 解析SQL语义，找到『业务SQL』要更新的业务数据，在业务数据被更新前，将其保存成"before image"
2. 执行『业务SQL』更新业务数据
3. 在业务数据更新之后其保存成"after image"，最后生成行锁，产生undo log。

>  以上操作全部在一个数据库事务内完成，这样保证了一阶段操作的原子性

![image-20220415231619128](https://gitee.com/firewolf/allinone/raw/master/images/image-20220415231619128.png)

###  二阶段提交

二阶段如是顺利提交的话，因为『业务SQL』在一阶段已经提交到数据库，所以Seata框架只需将一阶段快照数据和行锁删掉，完成数据清理即可。

![image-20220415231811722](https://gitee.com/firewolf/allinone/raw/master/images/image-20220415231811722.png)

### 二阶段回滚

二阶段如果是回滚的话，Seata就需要回滚一阶段已经执行的『业务SQL』，还原数据。

回滚的方式便是用"before image"还原业务数据；但在还原前首先要校验脏写，对比"数据库当前业务据"和"after image"，如果两份数据完全一致就说明没有脏写，可以还原业务数据，如果不一致就说明有脏写，出现脏写就需要转人工处理。

![image-20220415231933135](https://gitee.com/firewolf/allinone/raw/master/images/image-20220415231933135.png)



# 其他

## Filter 、拦截器区别，项目使用地方

- filter接口在javax.servlet包下面，是servlet规定的。inteceptor定义在org.springframework.web.servlet中，可以用于web程序，也可以用于普通的应用；
- filter是servlet容器支持的，interceptor是spring框架支持的
- filter通过dochain放行，interceptor通过prehandler放行。
- filter只在方法前后执行，interceptor粒度更细，可以深入到方法前后，异常抛出前后
- 拦截器是基于Java反射的，

> 执行顺序：Filter > Listener > Interceptor



## Spring 框架中都用到的设计模式

- 简单工厂：BeanFactory
- 工厂方法：FactoryBean
- 单例模式：Bean实例
- 代理模式：AOP、事务支持
- 观察者模式：Spring的事件监听
- 模板方法模式：Spring提供的各种扩展，如Bean的生命周期，IOC容器过程。
- 责任链模式：AOP的方法调用
- 适配器模式：SpringMVC中的HandlerAdapter
- 装饰器模式：BeanWrapper



## Spring监听器机制

观察者模式、通过多线程实现一步发布事件能力

spring的事件监听有三个部分组成：

**事件**（ApplicationEvent) ：负责对应相应监听器 事件源发生某事件是特定事件监听器被触发的原因。

**监听器(**ApplicationListener)： 对应于观察者模式中的**观察者**。监听器监听特定事件,并在内部定义了事件发生后的响应逻辑。

**事件发布器**（ApplicationEventMulticaster ）：对应于观察者模式中的**被观察者/主题， 负责通知观察者** 对外提供发布事件和增删事件监听器的接口,维护事件和事件监听器之间的映射关系,并在事件发生时负责通知相关监听器



