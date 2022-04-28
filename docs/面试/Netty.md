[toc]

# I/O模型

## I/O交互流程

通常用户进程中的一次完整I/O交互流程分为两阶段，首先是经过内核空间，也就是由操作系统处理；紧接着就是到用户空间，也就是交由应用程序

 ![image-20220405125107569](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405125107569.png)

## 五种I/O模型

在网络环境下，通俗地讲，将I/O分为两步：第一步是等待；第二步是数据搬迁。

如果想要提高I/O效率，需要将等待时间降低。因此发展出来五种I/O模型，分别是：阻塞I/O模型、非阻塞I/O模型、多路复用I/O模型、信号驱动I/O模型、异步I/O模型

### 阻塞I/O模型

  <img src="https://gitee.com/firewolf/allinone/raw/master/images/image-20220405125456673.png" alt="image-20220405125456673"/>

当用户进程调用了recvfrom这个系统调用，内核就开始了I/O的第一个阶段：准备数据。对于网络I/O来说，很多时候数据在一开始还没有到达（比如，还没有收到一个完整的UDP包），这个时候内核就要等待足够的数据到来。而在用户进程这边，整个进程会被阻塞，当数据准备好时，它就会将数据从内核拷贝到用户内存，然后返回结果，用户进程才解除阻塞的状态，重新运行起来。

### 非阻塞I/O模型

 ![image-20220405210710760](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405210710760.png)

当用户进程发出read操作时，如果内核中的数据还没有准备好，那么它并不会阻塞用户进程，而是立刻返回一个error。从用户进程角度讲，它发起一个read操作后，并不需要等待，而是马上就得到了一个结果，用户进程判断结果是一个error时，它就知道数据还没有准备好。于是它可以再次发送read操作，一旦内核中的数据准备好了，并且再次收到了用户进程的系统调用，那么它会马上将数据拷贝到用户内存，然后返回。

>  非阻塞型接口相比于阻塞型接口的显著差异在于，在被调用之后立即返回

### 多路复用I/O模型

 ![image-20220405211033813](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405211033813.png)

多个进程的I/O可以注册到一个复用器（Selector）上，当用户进程调用该Selector，Selector会监听注册进来的所有I/O，如果Selector监听的所有I/O在内核缓冲区都没有可读数据，select调用进程会被阻塞，而当任一I/O在内核缓冲区中有可读数据时，select调用就会返回，而后select调用进程可以自己或通知另外的进程（注册进程）再次发起读取I/O，读取内核中准备好的数据，多个进程注册I/O后，只有一个select调用进程被阻塞。

其实多路复用I/O模型和阻塞I/O模型并没有太大的不同，事实上，还更差一些，因为这里需要使用两个系统调用（select和recvfrom），而阻塞I/O模型只有一次系统调用（recvfrom）。但是，用Selector的优势在于它可以同时处理多个连接，所以如果处理的连接数不是很多，使用select/epoll的Web Server不一定比使用多线程加阻塞I/O的Web Server性能更好，**<font color=red>可能延迟还更大，select/epoll的优势并不是对于单个连接能处理得更快，而是能处理更多的连接</font>**

#### 支持I/O多路复用的系统调用

目前支持I/O多路复用的系统调用有select、pselect、poll、epoll。

![image-20220406111909807](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406111909807.png)

#### epoll 相对于 select/poll的改进

1. 支持一个进程打开的socket描述符（FD）不受限制（仅受限于操作系统的最大文件句柄数）
   - select仅支持1024个
   - epoll 在1GB内存下可支持约10万个句柄
2. I/O效率不会随着FD数目的增加而线性下降
   - select/poll每次调用都会线性扫描全部的集合，由于网络延时或者链路空闲，任一时刻只有少部分的socket是“活跃”的，这时候就会有很多的空消耗；
   - epoll 只会对“活跃”的socket进行操作——这是因为在内核实现中，epoll是根据每个fd上面的callback函数实现的，实现了伪异步I/O
3. 使用mmap加速内核与用户空间的消息传递

### epoll原理

参考文献：https://www.cnblogs.com/wangcp-2014/p/15828998.html

过程：

- 创建epoll对象
- 维护监视列表
- 接受数据
- 阻塞和唤醒进程



### 信号驱动I/O模型

 ![image-20220405212534129](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405212534129.png)

号驱动I/O是指进程预先告知内核，向内核注册一个信号处理函数，然后用户进程返回不阻塞，当内核数据就绪时会发送一个信号给进程，用户进程便在信号处理函数中调用I/O读取数据。从上图可以看出，实际上I/O内核拷贝到用户进程的过程还是阻塞的，信号驱动I/O并没有实现真正的异步，因为通知到进程之后，依然由进程来完成I/O操作。

### 异步I/O模型

 ![image-20220405212717426](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405212717426.png)

用户进程发起aio_read操作后，给内核传递与read相同的描述符、缓冲区指针、缓冲区大小三个参数及文件偏移，告诉内核当整个操作完成时，如何通知我们立刻就可以开始去做其他的事；而另一方面，从内核的角度，当它收到一个aio_read之后，首先它会立刻返回，所以不会对用户进程产生任何阻塞，内核会等待数据准备完成，然后将数据拷贝到用户内存，当这一切都完成之后，内核会给用户进程发送一个信号，告诉它aio_read操作完成

> 异步I/O和信号驱动I/O模型的区别在于，信号驱动I/O模型是由内核通知我们何时可以启动一个I/O操作，这个I/O操作由用户自定义的信号函数来实现，而异步I/O模型由内核告知我们I/O操作何时完成。

**<font color=red>五种I/O模型中，只有异步I/O实现了真正的异步</font>**



## 同步和异步

同步和异步其实是指CPU时间片的利用，主要看请求发起方对消息结果的获取是主动发起的，还是被动通知的

- 如果是请求方主动发起的，一直在等待应答结果（同步阻塞），或者可以先去处理其他事情，但要不断轮询查看发起的请求是否有应答结果（同步非阻塞），因为不管如何都要发起方主动获取消息结果，所以形式上还是同步操作。
- 如果是由服务方通知的，也就是请求方发出请求后，要么一直等待通知（异步阻塞），要么先去干自己的事（异步非阻塞）。当事情处理完成后，服务方会主动通知请求方，它的请求已经完成，这就是异步。
- 异步通知的方式一般通过状态改变、消息通知或者回调函数来完成，大多数时候采用的都是回调函数

 ![image-20220405213834656](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405213834656.png)



## 阻塞和非阻塞

我们调用了一个函数后，在等待这个函数返回结果之前，当前的线程是处于挂起状态还是运行状态。

- 如果是挂起状态，就意味着当前线程什么都不能干，就等着获取结果，这就是同步阻塞；
- 如果仍然是运行状态，就意味着当前线程是可以继续处理其他任务的，但要时不时地看一下是否有结果了，这就是同步非阻塞

 ![image-20220405214313664](https://gitee.com/firewolf/allinone/raw/master/images/image-20220405214313664.png)





## Java IO

### BIO（阻塞I/O）

<font color=blue>同步阻塞</font>，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销，当然可以通过线程池机制改善。

![image-20220406111715994](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406111715994.png)

#### 核心API

- Socket
- 输入输出流（InputStream、OutputStream等）



### 伪异步I/O

当有新的客户端接入时，将客户端的Socket封装成一个Task（该任务实现java.lang.Runnable接口）投递到后端的线程池中进行处理，JDK的线程池维护一个消息队列和N个活跃线程，对消息队列中的任务进行处理。由于线程池可以设置消息队列的大小和最大线程数，因此，它的资源占用是可控的，无论多少个客户端并发访问，都不会导致资源的耗尽和宕机。

![image-20220406111629697](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406111629697.png)



### NIO（非阻塞I/O）

<font color=blue>同步非阻塞</font>，服务器实现模式为一个请求一个线程，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求时才启动一个线程进行处理。

#### 核心API

- Buffer（缓冲区）：负责数据的存取
- Channel（通道）：负责连接
- Selector（选择器）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况



### AIO（异步I/O，NIO2）

<font color=blue>异步非阻塞</font>，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理。AIO是基于NIO升级的。

#### 核心API（异步通道）

- AsynchronousSocketChannel

- AsynchronousServerSocketChannel

- AsynchronousFileChannel

- AsynchronousDatagramChannel



# 反应堆（Reactor模式）

参考文献：https://www.cnblogs.com/conefirst/articles/15204532.html

<font color=blue>I/O 复用机制</font>需要事件分发器。 事件分发器的作用，将那些读写事件源分发给各读写事件的处理者。涉及到事件分发器的两种模式称为：Reactor和Proactor。 Reactor模式是基于同步I/O的，而Proactor模式是和异步I/O相关的。

Reactor模式(反应器模式)是一种处理一个或多个客户端并发交付服务请求的事件设计模式。当请求抵达后，服务处理程序使用I/O多路复用策略，然后同步地派发这些请求至相关的请求处理程序。

## 基本结构

 ![image-20220406115912640](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406115912640.png)

- Handle(句柄或描述符，在Windows下称为句柄，在Linux下称为描述符)：本质上表示一种资源(比如说文件描述符，或是针对网络编程中的socket描述符)，是由操作系统提供的；该资源用于表示一个个的事件，事件既可以来自于外部，也可以来自于内部；外部事件比如说客户端的连接请求，客户端发送过来的数据等；内部事件比如说操作系统产生的定时事件等。它本质上就是一个文件描述符，Handle是事件产生的发源地。

- Synchronous Event Demultiplexer(同步事件分离器)：它本身是一个系统调用，用于等待事件的发生(事件可能是一个，也可能是多个)。调用方在调用它的时候会被阻塞，一直阻塞到同步事件分离器上有事件产生为止。对于Linux来说，同步事件分离器指的就是常用的I/O多路复用机制，比如说select、poll、epoll等。在Java NIO领域中，同步事件分离器对应的组件就是Selector；对应的阻塞方法就是select方法。

- Event Handler(事件处理器)：本身由多个回调方法构成，这些回调方法构成了与应用相关的对于某个事件的反馈机制。在Java NIO领域中并没有提供事件处理器机制让我们调用或去进行回调，是由我们自己编写代码完成的。Netty相比于Java NIO来说，在事件处理器这个角色上进行了一个升级，它为我们开发者提供了大量的回调方法，供我们在特定事件产生时实现相应的回调方法进行业务逻辑的处理，即，ChannelHandler。ChannelHandler中的方法对应的都是一个个事件的回调。

- Concrete Event Handler(具体事件处理器)：是事件处理器的实现。它本身实现了事件处理器所提供的各种回调方法，从而实现了特定于业务的逻辑。它本质上就是我们所编写的一个个的处理器实现。

- Initiation Dispatcher(初始分发器)：实际上就是Reactor角色。它本身定义了一些规范，这些规范用于控制事件的调度方式，同时又提供了应用进行事件处理器的注册、删除等设施。它本身是整个事件处理器的核心所在，Initiation Dispatcher会通过Synchronous Event Demultiplexer来等待事件的发生。一旦事件发生，Initiation Dispatcher首先会分离出每一个事件，然后调用事件处理器，最后调用相关的回调方法来处理这些事件。Netty中ChannelHandler里的一个个回调方法都是由bossGroup或workGroup中的某个EventLoop来调用的。

  

## 单线程Reactor

服务器端的Reactor是一个线程对象，该线程会启动事件循环，并使用Selector来实现IO的多路复用。注册一个Acceptor事件处理器到Reactor中，Acceptor事件处理器所关注的事件是ACCEPT事件，这样Reactor会监听客户端向服务器端发起的连接请求事件(ACCEPT事件)



![image-20220406115957209](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406115957209.png) 



## 工作者线程池

与单线程Reactor模式不同的是，添加了一个工作者线程池，并将非I/O操作从Reactor线程中移出转交给工作者线程池来执行。这样能够提高Reactor线程的I/O响应，不至于因为一些耗时的业务逻辑而延迟对后面I/O请求的处理

![image-20220406120133524](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406120133524.png)



## 多线程Reactor

Reactor线程池中的每一Reactor线程都会有自己的Selector、线程和分发的事件循环逻辑。
mainReactor可以只有一个，但subReactor一般会有多个。mainReactor线程主要负责接收客户端的连接请求，然后将接收到的SocketChannel传递给subReactor，由subReactor来完成和客户端的通信。

![image-20220406120104595](https://gitee.com/firewolf/allinone/raw/master/images/image-20220406120104595.png)



# Netty 与 Reactor模式

Netty的线程模式就是一个实现了Reactor模式的经典模式。

- 结构对应：
  NioEventLoop ———— Initiation Dispatcher
  Synchronous EventDemultiplexer ———— Selector
  Event Handler ———— ChannelHandler
  ConcreteEventHandler ———— 具体的ChannelHandler的实现
- 模式对应：
  Netty服务端使用了“多Reactor线程模式”
  mainReactor ———— bossGroup(NioEventLoopGroup) 中的某个NioEventLoop
  subReactor ———— workerGroup(NioEventLoopGroup) 中的某个NioEventLoop
  acceptor ———— ServerBootstrapAcceptor
  ThreadPool ———— 用户自定义线程池
- 流程：
  ① 当服务器程序启动时，会配置ChannelPipeline，ChannelPipeline中是一个ChannelHandler链，所有的事件发生时都会触发Channelhandler中的某个方法，这个事件会在ChannelPipeline中的ChannelHandler链里传播。然后，从bossGroup事件循环池中获取一个NioEventLoop来现实服务端程序绑定本地端口的操作，将对应的ServerSocketChannel注册到该NioEventLoop中的Selector上，并注册ACCEPT事件为ServerSocketChannel所感兴趣的事件。
  ② NioEventLoop事件循环启动，此时开始监听客户端的连接请求。
  ③ 当有客户端向服务器端发起连接请求时，NioEventLoop的事件循环监听到该ACCEPT事件，Netty底层会接收这个连接，通过accept()方法得到与这个客户端的连接(SocketChannel)，然后触发ChannelRead事件(即，ChannelHandler中的channelRead方法会得到回调)，该事件会在ChannelPipeline中的ChannelHandler链中执行、传播。
  ④ ServerBootstrapAcceptor的readChannel方法会该SocketChannel(客户端的连接)注册到workerGroup(NioEventLoopGroup) 中的某个NioEventLoop的Selector上，并注册READ事件为SocketChannel所感兴趣的事件。启动SocketChannel所在NioEventLoop的事件循环，接下来就可以开始客户端和服务器端的通信了。
- 

# Netty为什么高性能

- 采用异步非阻塞的I/O类库，基于Reactor 模式实现，解决了传统同步阻塞I/O模式下一个服务端无法平滑地处理线性增长的客户端的问题。
- TCP接收和发送缓冲区使用直接内存代替堆内存，避免了内存复制，提升了I/O读取和写入的性能。
- 支持通过内存池的方式循环利用ByteBuf,避免了频繁创建和销毁ByteBuf带来
  的性能损耗。
- 可配置的I/O线程数、TCP参数等，为不同的用户场景提供定制化的调优参数，满足不同的性能场景。
- 采用环形数组缓冲区实现无锁化并发编程，代替传统的线程安全容器或者锁。
- 合理地使用线程安全容器、原子类等，提升系统的并发处理能力。
- 关键资源的处理使用单线程串行化的方式，避免多线程并发访问带来的锁竞争和额外的CPU资源消耗问题。
- 通过引用计数器及时地申请释放不再被引用的对象，细粒度的内存管理降低了GC的频率，减少了频繁GC带来的时延增大和CPU损耗。



# Netty核心组件

- Channel：传入或者传出的数据载体，可以被打开或者关闭，连接或者断开连接
- EventLoop：用于处理IO事件，多线程模型、并发
- ChannelHandler、ChannelPipeline：负责处理接受和发送数据的业务逻辑
- ByteBuf：内存单元
- 核心三件套：Buffer、Selector、Channel
