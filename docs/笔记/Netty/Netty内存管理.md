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

#### 非Unsafe

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



#### Unsafe

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

  因此，**`InstrumentedUnpooledUnsafeHeapByteBuf`是在`PlatformDependent`中通过new 的方式创建的堆内存**,也就是说，**不管是Unsafe的还是非Unsafe的，bytebuf数组的构造，都是一样，通过new的方式创建的**

- >经过以上步骤，就把HeapBuffer构建好了，都是通过new的方式，构建的Byte数组



#### Unsafe和非Unsafe的区别

1. `InstrumentedUnpooledUnsafeHeapByteBuf`和`InstrumentedUnpooledHeapByteBuf`内存分配的区别

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

     最终，**是通过Unsafe的本地方法获取**

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



#### 非Unsafe

1. `io.netty.buffer.UnpooledDirectByteBuf#UnpooledDirectByteBuf(io.netty.buffer.ByteBufAllocator, int, int)`：

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


2. `java.nio.ByteBuffer#allocateDirect`：

   ```java
   public static ByteBuffer allocateDirect(int capacity) {
       return new DirectByteBuffer(capacity);
   }
   ```

   可以看到，**这里是直接通过JDK的API构建了`java.nio.DirectByteBuffer`**

3. 然后，通过setByteBuffer保存到全局变量`buffer`中

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

4. `io.netty.buffer.UnpooledDirectByteBuf#getByte`:

   ```java
   public byte getByte(int index) {
       this.ensureAccessible();
       return this._getByte(index);
   }
   
   protected byte _getByte(int index) {
       return this.buffer.get(index);
   }
   ```

   **在获取的时候，是通过索引获取的**

#### Unsafe

1. `io.netty.buffer.UnpooledUnsafeDirectByteBuf#UnpooledUnsafeDirectByteBuf(io.netty.buffer.ByteBufAllocator, int, int)`

   ```java
   public UnpooledUnsafeDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
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
       setByteBuffer(allocateDirect(initialCapacity), false);
   }
   ```

   可以看到，该构造器和`UnpooledDirectByteBuf`的逻辑完全一致。

2. `io.netty.buffer.UnpooledUnsafeDirectByteBuf#allocateDirect`：

   ```java
   protected ByteBuffer allocateDirect(int initialCapacity) {
       return ByteBuffer.allocateDirect(initialCapacity);
   }
   ```

3. `java.nio.ByteBuffer#allocateDirect`：

   ```java
   public static ByteBuffer allocateDirect(int capacity) {
       return new DirectByteBuffer(capacity);
   }
   ```

   可以看到，**这里也是直接通过JDK的API构建了`java.nio.DirectByteBuffer`**

4. `io.netty.buffer.UnpooledUnsafeDirectByteBuf#setByteBuffer`：

   ```java
   final void setByteBuffer(ByteBuffer buffer, boolean tryFree) {
       if (tryFree) {
           ByteBuffer oldBuffer = this.buffer;
           if (oldBuffer != null) {
               if (doNotFree) {
                   doNotFree = false;
               } else {
                   freeDirect(oldBuffer);
               }
           }
       }
       this.buffer = buffer;
       memoryAddress = PlatformDependent.directBufferAddress(buffer);
       tmpNioBuf = null;
       capacity = buffer.remaining();
   }
   ```

   可以看到，这里不仅把`DirectByteBuffer`保存到了buffer变量，还**通过调用PlatformDependent.directBufferAddress()方法获取Buffer真实的内存地址，并保存到memoryAddress变量中**

5. `io.netty.buffer.UnpooledUnsafeDirectByteBuf#getByte`：

   ```java
   public long memoryAddress() {
       this.ensureAccessible();
       return this.memoryAddress;
   }
   
   public byte getByte(int index) {
       this.checkIndex(index);
       return this._getByte(index);
   }
   
   protected byte _getByte(int index) {
       return UnsafeByteBufUtil.getByte(this.addr(index));
   }
   
   final long addr(int index) {
       return this.memoryAddress + (long)index;
   }
   ```

   这里看到，**是通过地址值进行访问的**



## PooledByteBufAllocator池化内存分配

## 概述

1. 针对堆内内存和堆外内存，分别使用newDirectBuffer()方法和newHeapBuffer()方法，进行内存分配

   `io.netty.buffer.PooledByteBufAllocator#newDirectBuffer`

   ```java
   protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
       PoolThreadCache cache = threadCache.get();
       PoolArena<byte[]> heapArena = cache.heapArena;
   
       final ByteBuf buf;
       if (heapArena != null) {
           buf = heapArena.allocate(cache, initialCapacity, maxCapacity);
       } else {
           buf = PlatformDependent.hasUnsafe() ?
               new UnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) :
           new UnpooledHeapByteBuf(this, initialCapacity, maxCapacity);
       }
   
       return toLeakAwareBuffer(buf);
   }
   
   protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
       PoolThreadCache cache = (PoolThreadCache)this.threadCache.get();
       PoolArena<ByteBuffer> directArena = cache.directArena;
       Object buf;
       if (directArena != null) {
           buf = directArena.allocate(cache, initialCapacity, maxCapacity);
       } else {
           buf = PlatformDependent.hasUnsafe() ? UnsafeByteBufUtil.newUnsafeDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
       }
   
       return toLeakAwareBuffer((ByteBuf)buf);
   }
   ```

   可以看到，这两个方法的结构基本一致，逻辑如下：

   - 通过`threadCache.get()`获取`PoolThreadCache`类型的cache对象
   - 通过cache获得`PoolArena`类型的`arena`对象（`heapArena`对象或者`directArena`对象）
   - 如果`arena`对象不为空，则通过`arena`对象分配内存，否则，分配逻辑就和 非池化内存分配一致了

## `PoolArena`

1.  跟踪一下PoolThreadLocalCache的初始化

   - `io.netty.buffer.PooledByteBufAllocator.PoolThreadLocalCache#initialValue`

     ```java
     protected synchronized PoolThreadCache initialValue() {
         // 从heapArenas中获取一个使用率最少的Arena
         final PoolArena<byte[]> heapArena = leastUsedArena(heapArenas);
         // // 从directArenas中获取一个使用率最少的Arena
         final PoolArena<ByteBuffer> directArena = leastUsedArena(directArenas);
     
         Thread current = Thread.currentThread();
         // 有缓存
         if (useCacheForAllThreads || current instanceof FastThreadLocalThread) {
             return new PoolThreadCache(
                 heapArena, directArena, tinyCacheSize, smallCacheSize, normalCacheSize,
                 DEFAULT_MAX_CACHED_BUFFER_CAPACITY, DEFAULT_CACHE_TRIM_INTERVAL);
         }
         // 无缓存
         return new PoolThreadCache(heapArena, directArena, 0, 0, 0, 0, 0);
     }
     ```

   - 关于arenas的初始化

     ```java
      public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder,
                                       int tinyCacheSize, int smallCacheSize, int normalCacheSize,
                                       boolean useCacheForAllThreads, int directMemoryCacheAlignment) {
             ....
     
             if (nHeapArena > 0) {
                 heapArenas = newArenaArray(nHeapArena);
                 ...
             } else {
                 heapArenas = null;
                 heapArenaMetrics = Collections.emptyList();
             }
              if (nDirectArena > 0) {
                  directArenas = newArenaArray(nDirectArena);
                  ...
              } else {
                  directArenas = null;
                  directArenaMetrics = Collections.emptyList();
              }
          ....
      }
     
     ```

   - `io.netty.buffer.PooledByteBufAllocator#newArenaArray`：

     ```java
     private static <T> PoolArena<T>[] newArenaArray(int size) {
         return new PoolArena[size];
     }
     ```

     其实就是创建了一个固定大小的PoolArena数组，数组大小由传入的参数nHeapArena和nDirectArena决定。

     再回到`PooledByteBufAllocator`的构造器源码，重载构造器的源码为：

     ```java
     public PooledByteBufAllocator(boolean preferDirect) {
         this(preferDirect, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
     }
     ```

     可以看到：nHeapArena和nDirectArena是通过DEFAULT_NUM_HEAP_ARENA和DEFAULT_NUM_DIRECT_ARENA这两个常量默认赋值的

     相关赋值代码为:

     ```java
     final int defaultMinNumArena = NettyRuntime.availableProcessors() * 2;  // CPU核数×2
     final int defaultChunkSize = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
     DEFAULT_NUM_HEAP_ARENA = Math.max(0,
                                       SystemPropertyUtil.getInt(
                                           "io.netty.allocator.numHeapArenas",
                                           (int) Math.min(
                                               defaultMinNumArena,
                                               runtime.maxMemory() / defaultChunkSize / 2 / 3)));
     DEFAULT_NUM_DIRECT_ARENA = Math.max(0,
                                         SystemPropertyUtil.getInt(
                                             "io.netty.allocator.numDirectArenas",
                                             (int) Math.min(
                                                 defaultMinNumArena,
                                                 PlatformDependent.maxDirectMemory() / defaultChunkSize / 2 / 3)));
     ```

     上面的代码看出：**defaultMinNumArena的值赋给nHeapArena和nDirectArena，也就是说，nHeapArena和nDirectArena的默认值就是CPU核数×2**

     **而EventLoopGroup分配线程时，默认线程数也是CPU核数×2。这样，就可以保证Netty中的每一个任务线程都可以有一个独享的Arena，保证在每个线程分配内存的时候不用加锁。**

   

   综上：**Arena有heapArena和directArena。假设有四个线程，那么对应会分配四个Arena。在创建ByteBuf的时候，首先通过PoolThreadCache获取Arena对象并赋值给其成员变量，然后每个线程通过PoolThreadCache调用get()方法的时候会获得它底层的Arena，也就是说通过EventLoop1获得Arena1，通过EventLoop2获得Arena2，依次类推：**

   ![image-20220302185621822](https://gitee.com/firewolf/allinone/raw/master/images/image-20220302185621822.png)



## ByteBuf缓存列表

PoolThreadCache除了可以在Arena上进行内存分配，还可以在它底层维护的ByteBuf缓存列表进行分配。举个例子：我们通过PooledByteBufAllocator创建了一个1024字节的ByteBuf，当用完释放后，可能在其他地方会继续分配1024字节的ByteBuf。这时，其实不需要在Arena上进行内存分配，而是直接通过PoolThreadCache中维护的ByteBuf的缓存列表直接拿过来返回。在PooledByteBufAllocator中维护着三种规格大小的缓存列表，分别是三个值tinyCacheSize、smallCacheSize、normalCacheSize

```java
public PooledByteBufAllocator(boolean preferDirect, int nHeapArena, int nDirectArena, int pageSize, int maxOrder,
                              int tinyCacheSize, int smallCacheSize, int normalCacheSize,
                              boolean useCacheForAllThreads, int directMemoryCacheAlignment) {
    super(preferDirect);
    threadCache = new PoolThreadLocalCache(useCacheForAllThreads);
    this.tinyCacheSize = tinyCacheSize;
    this.smallCacheSize = smallCacheSize;
    this.normalCacheSize = normalCacheSize;
    chunkSize = validateAndCalculateChunkSize(pageSize, maxOrder);
    ....

```

PooledByteBufAllocator的构造器中，分别赋值tinyCacheSize=512，smallCacheSize=256，normalCacheSize=64。通过这种方式，Netty预创建了固定规格的内存池，大大提高了内存分配的性能。

