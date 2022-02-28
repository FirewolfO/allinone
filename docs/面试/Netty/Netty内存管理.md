# ByteBuf

ByteBuf是Netty整个结构里面最为底层的模块，主要负责把数据从底层I/O读到ByteBuf，然后传递给应用程序，应用程序处理完成之后再把数据封装成ByteBuf写回I/O。

### ByteBuf基本结构

```
      +-------------------+------------------+------------------+
      | discardable bytes |  readable bytes  |  writable bytes  |
      |                   |     (CONTENT)    |                  |
      +-------------------+------------------+------------------+
      |                   |                  |                  |
      0      <=      readerIndex   <=   writerIndex    <=    capacity
```

- readerIndex：记录读指针的开始位置
- writerIndex：记录写指针的开始位置
- capacity：缓冲区的总长度



### AbstractByteBuf

ByteBuf的大部分功能（如：读、写、指针移动）是在AbstractByteBuf中实现的。

AbstractByteBuf有众多子类，大致可以从三个维度来进行分类：

- Pooled：池化内存，就是从预先分配好的内存空间中提取一段连续内存封装成一个ByteBuf，分给应用程序使用。
- Unsafe：是JDK底层的一个负责I/O操作的对象，可以直接获得对象的内存地址，基于内存地址进行读写操作。
- Direct：堆外内存，直接调用JDK的底层API进行物理内存分配，不在JVM的堆内存中，需要手动释放。

ByteBuf共会有六种组合：Pooled（池化内存）和Unpooled（非池化内存）；Unsafe和非Unsafe；Heap（堆内内存）和Direct（堆外内存）。

相关类图如下：

![ByteBuf.drawio](https://gitee.com/firewolf/allinone/raw/master/images/ByteBuf.drawio.svg)