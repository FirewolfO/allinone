[toc]

# ByteBuf内存单元

ByteBuf是Netty整个结构里面最为底层的模块，主要负责把数据从底层I/O读到ByteBuf，然后传递给应用程序，应用程序处理完成之后再把数据封装成ByteBuf写回I/O。我们可以把ByteBuf理解为Netty的内存单元。

## ByteBuf基本结构

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



## AbstractByteBuf

ByteBuf的大部分功能（如：读、写、指针移动）是在AbstractByteBuf中实现的。

AbstractByteBuf有众多子类，大致可以从三个维度来进行分类：

- Pooled：池化内存，就是从预先分配好的内存空间中提取一段连续内存封装成一个ByteBuf，分给应用程序使用。
- Unsafe：是JDK底层的一个负责I/O操作的对象，可以直接获得对象的内存地址，基于内存地址进行读写操作。
- Direct：堆外内存，直接调用JDK的底层API进行物理内存分配，不在JVM的堆内存中，需要手动释放。

ByteBuf共会有六种组合：Pooled（池化内存）和Unpooled（非池化内存）；Unsafe和非Unsafe；Heap（堆内内存）和Direct（堆外内存）。

相关类图如下：

![Netty.drawio](https://gitee.com/firewolf/allinone/raw/master/images/Netty.drawio.png)



# ByteBufAllocator内存分配管理器

## ByteBufAllocator介绍

Netty中内存分配有一个顶层的抽象就是ByteBufAllocator，负责分配所有ByteBuf类型的内存

### 主要方法

- `buffer()`：分配一块内存，自动判断是分配堆外内存还是堆内内存；
- `ioBuffer()`：尽可能地分配一块堆外直接内存，如果系统不支持，分配堆内内存；
- `heapBuffer()`：分配一块对内内存
- `directBuffer()`：分配一块堆外内存
- `compositeBuffer`：组合分配，把多个ByteBuffer组合到一起变成一个整体

### 类图

 ![Netty-ByteBufAllocator.drawio](https://gitee.com/firewolf/allinone/raw/master/images/Netty-ByteBufAllocator.drawio.png)



## AbstractByteBufAllocator

AbstractByteBufAllocator 是ByteBufAllocator的默认实现，大部分内存分配功能在这里实现。以比较复杂的`buffer()`方法为例：

1. 对是否默认支持directBuffer做判断，如果支持则分配directBuffer，否则分配heapBuffer

   `io.netty.buffer.AbstractByteBufAllocator#buffer()`：

   ```java
   public ByteBuf buffer() {
       if (directByDefault) {
           return directBuffer();
       }
       return heapBuffer();
   }
   ```

2. `directBuffer()`方法调用过程中经过一些重载方法调用有，最终调用如下：

   `io.netty.buffer.AbstractByteBufAllocator#directBuffer(int, int)`：

   ```java
   public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
       if (initialCapacity == 0 && maxCapacity == 0) {
           return emptyBuf;
       }
       validate(initialCapacity, maxCapacity);
       return newDirectBuffer(initialCapacity, maxCapacity);
   }
   ```

   **也就是说，最终是通过`newDirectBuffer()`方法进行内存分配的。**

   

   同样的，`heapBuffer()`经过一些重载调用后，最终调用为：

   `io.netty.buffer.AbstractByteBufAllocator#heapBuffer(int, int)`：

   ```java
   @Override
   public ByteBuf heapBuffer(int initialCapacity, int maxCapacity) {
       if (initialCapacity == 0 && maxCapacity == 0) {
           return emptyBuf;
       }
       validate(initialCapacity, maxCapacity);
       return newHeapBuffer(initialCapacity, maxCapacity);
   }
   ```

   **也就是说，最终是通过`newHeapBuffer()`方法进行内存分配的。**

3. `newDirectBuffer ()`和`newHeapBuffer()` 是两个抽象方法，都在AbstractByteBufAllocator的子类中进行了实现，分别是：`PooledByteBufAllocator`和`UnpooledByteBufAllocator`

4. 通过判断操作系统是否支持Unsafe，来实现对Unsafe和非Unsafe内存的分配，例如：

    `UnpooledByteBufAllocator`对这两个方法的实现如下：

   ```java
   protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
       return (ByteBuf)(PlatformDependent.hasUnsafe() ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledHeapByteBuf(this, initialCapacity, maxCapacity));
   }
   
   protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
       Object buf;
       if (PlatformDependent.hasUnsafe()) {
           buf = this.noCleaner ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
       } else {
           buf = new UnpooledByteBufAllocator.InstrumentedUnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
       }
   
       return (ByteBuf)(this.disableLeakDetector ? buf : toLeakAwareBuffer((ByteBuf)buf));
   }
   ```

   

## UnpooledByteBufAllocator非池化内存分配

### `HeapBuffer`堆内内存分配

1. `io.netty.buffer.UnpooledByteBufAllocator#newHeapBuffer`

   ```java
   protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
       return (ByteBuf)(PlatformDependent.hasUnsafe() ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledHeapByteBuf(this, initialCapacity, maxCapacity));
   }
   ```

   通过调用`PlatformDependent.hasUnsafe()`方法来判断系统是否支持Unsafe，如果支持，创建`InstrumentedUnpooledUnsafeHeapByteBuf`类实例，否则创建`InstrumentedUnpooledHeapByteBuf`类实例。

   `InstrumentedUnpooledUnsafeHeapByteBuf` 和 `InstrumentedUnpooledHeapByteBuf` 的类关系图如下：

    ![Netty-UnpooledHeapByteBuf.drawio](https://gitee.com/firewolf/allinone/raw/master/images/Netty-UnpooledHeapByteBuf.drawio.png)

   经过代码跟踪得知，其实都通过`UnpooledHeapByteBuf`的构造器完成的实例化

2. `UnpooledHeapByteBuf` 构造器核心代码如下

   ```java
   public UnpooledHeapByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
       super(maxCapacity);
   
       checkNotNull(alloc, "alloc");
   
       if (initialCapacity > maxCapacity) {
           throw new IllegalArgumentException(String.format(
               "initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
       }
   
       this.alloc = alloc;
       setArray(allocateArray(initialCapacity));
       setIndex(0, 0);
   }
   ```

   - 通过`setArray`方法把构造好的bye数组赋值给全局变量array；

     ```java
     private void setArray(byte[] initialArray) {
         array = initialArray;
         ....
     }
     ```

   - 通过`setIndex`方法来设置readerIndex和writerIndex

     ```java
     public ByteBuf setIndex(int readerIndex, int writerIndex) {
         ....
         setIndex0(readerIndex, writerIndex);
         return this;
     }
     ```

     ```java
     final void setIndex0(int readerIndex, int writerIndex) {
         this.readerIndex = readerIndex;
         this.writerIndex = writerIndex;
     }
     ```

   - `allocateArray`方法

     在`setArray`的时候，通过调用`allocateArray`来构造byte数组。

     - `InstrumentedUnpooledHeapByteBuf`

       代码逻辑如下：

       `io.netty.buffer.UnpooledByteBufAllocator.InstrumentedUnpooledHeapByteBuf#allocateArray`：

       ```java
       byte[] allocateArray(int initialCapacity) {
           byte[] bytes = super.allocateArray(initialCapacity);
           ((UnpooledByteBufAllocator) alloc()).incrementHeap(bytes.length);
           return bytes;
       }
       ```

       `io.netty.buffer.UnpooledHeapByteBuf#allocateArray`：

       ```java
       byte[] allocateArray(int initialCapacity) {
           return new byte[initialCapacity];
       }
       ```

       可以看到，**`InstrumentedUnpooledHeapByteBuf`是在`UnpooledHeapByteBuf`中直接通过new 的方式获取的堆内存；**

       

     - `InstrumentedUnpooledUnsafeHeapByteBuf`

       `io.netty.buffer.UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeHeapByteBuf#allocateArray`：

       ```java
       byte[] allocateArray(int initialCapacity) {
           byte[] bytes = super.allocateArray(initialCapacity);
           ((UnpooledByteBufAllocator)this.alloc()).incrementHeap(bytes.length);
           return bytes;
       }
       ```

       `io.netty.buffer.UnpooledUnsafeHeapByteBuf#allocateArray`：

       ```java
       byte[] allocateArray(int initialCapacity) {
           return PlatformDependent.allocateUninitializedArray(initialCapacity);
       }
       ```

       `io.netty.util.internal.PlatformDependent#allocateUninitializedArray`：

       ```java
       public static byte[] allocateUninitializedArray(int size) {
           return UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD < 0 || UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD > size ?
               new byte[size] : PlatformDependent0.allocateUninitializedArray(size);
       }
       ```

       可以看到，byte数组的初始化，取决于`UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD`的值；

       查看`PlatformDependent`静态代码块对`UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD`的初始化，如下：

       ```java
       static {
           ...
           UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = javaVersion() >= 9 && PlatformDependent0.hasAllocateArrayMethod() ?
               tryAllocateUninitializedArray : -1;
           ...
       }
       ```

       可以看到，对于java8及之前的jdk版本，`UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD`的值为-1。

       因此，**`InstrumentedUnpooledUnsafeHeapByteBuf`是在`PlatformDependent`中通过new 的方式创建的堆内存**

   - >经过以上步骤，就把HeapBuffer构建好了，都是通过new的方式，构建的Byte数组

3. `InstrumentedUnpooledUnsafeHeapByteBuf`和`InstrumentedUnpooledHeapByteBuf`内存分配的区别

   这两个类，分别代表的是Unsafe和非Unsafe的堆内存分配，从上面的过程我们看到，都是通过`UnpooledHeapByteBuf`的构造器进行构造的，实际上，他们的区别主要在于`getByte`方法。

   - `InstrumentedUnpooledHeapByteBuf`

     `io.netty.buffer.UnpooledHeapByteBuf#getByte`：

     ```java
     public byte getByte(int index) {
         ensureAccessible();
         return _getByte(index);
     }
     ```

     `io.netty.buffer.UnpooledHeapByteBuf#_getByte`

     ```java
     protected byte _getByte(int index) {
         return HeapByteBufUtil.getByte(array, index);
     }
     ```

     `io.netty.buffer.HeapByteBufUtil#getByte`

     ```java
     static byte getByte(byte[] memory, int index) {
         return memory[index];
     }
     ```

     可以看到，**是根据index索引直接从数组中取值**

   - `InstrumentedUnpooledUnsafeHeapByteBuf`

     `io.netty.buffer.UnpooledUnsafeHeapByteBuf#getByte`:

     ```java
     public byte getByte(int index) {
         checkIndex(index);
         return _getByte(index);
     }
     ```

     `io.netty.buffer.UnpooledUnsafeHeapByteBuf#_getByte`：

     ```java
     protected byte _getByte(int index) {
         return UnsafeByteBufUtil.getByte(array, index);
     }
     ```

     最终，调用的是

     ```java
     native sun.misc.Unsafe#getByte(java.lang.Object, long)
     ```

### `DirectBuffer`堆外内存分配

1. `io.netty.buffer.UnpooledByteBufAllocator#newDirectBuffer`

   ```java
   protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
       final ByteBuf buf;
       if (PlatformDependent.hasUnsafe()) {
           buf = noCleaner ? new InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(this, initialCapacity, maxCapacity) :
           new InstrumentedUnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
       } else {
           buf = new InstrumentedUnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
       }
       return disableLeakDetector ? buf : toLeakAwareBuffer(buf);
   }
   ```

   可以看到，通过调用`PlatformDependent.hasUnsafe()`方法来判断系统是否支持Unsafe，如果支持，创建`InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf`类实例或者`InstrumentedUnpooledUnsafeDirectByteBuf`类实例，否则创建`InstrumentedUnpooledDirectByteBuf`类实例。

   这几个类的关系如下：

    ![UnpooledDirectByteBuf](https://gitee.com/firewolf/allinone/raw/master/images/UnpooledDirectByteBuf.png)

   可以看到，在Unsafe和非Unsafe情况下，分别构造了`UnpooledUnsafeDirectByteBuf`和`UnpooledDirectByteBuf`实例

2. `io.netty.buffer.UnpooledDirectByteBuf#UnpooledDirectByteBuf(io.netty.buffer.ByteBufAllocator, int, int)`：

   ```java
   public UnpooledDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
       super(maxCapacity);
       if (alloc == null) {
           throw new NullPointerException("alloc");
       }
       if (initialCapacity < 0) {
           throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
       }
       if (maxCapacity < 0) {
           throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
       }
       if (initialCapacity > maxCapacity) {
           throw new IllegalArgumentException(String.format(
               "initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
       }
   
       this.alloc = alloc;
       setByteBuffer(ByteBuffer.allocateDirect(initialCapacity));
   }
   ```

   首先，通过`java.nio.ByteBuffer#allocateDirect`构建了`DirectByteBuffer`

   然后，通过setByteBuffer进行bytebuf赋值：

   ```java
   private void setByteBuffer(ByteBuffer buffer) {
       ByteBuffer oldBuffer = this.buffer;
       if (oldBuffer != null) {
           if (doNotFree) {
               doNotFree = false;
           } else {
               freeDirect(oldBuffer);
           }
       }
   
       this.buffer = buffer;
       tmpNioBuf = null;
       capacity = buffer.remaining();
   }
   ```

   

   