[toc]

# 日志类型

**慢查询日志：**记录所有执行时间超过long_query_time的所有查询，方便我们对查询进行优化。

**通用查询日志：**记录所有连接的起始时间和终止时间，以及连接发送给数据库服务器的所有指令，对我们复原操作的实际场景、发现问题，甚至是对数据库操作的审计都有很大的帮助。

**错误日志：**记录MySQL服务的启动、运行或停止MySQL服务时出现的问题，方便我们了解服务器的状态，从而对服务器进行维护。

**二进制日志：**记录所有更改数据的语句，可以用于主从服务器之间的数据同步，以及服务器遇到故障时数据的无损失恢复。

**中继日志：**用于主从服务器架构中，从服务器用来存放主服务器二进制日志内容的一个中间文件。从服务器通过读取中继日志的内容，来同步主服务器上的操作。

**数据定义语句日志：**记录数据定义语句执行的元数据操作。

> 除二进制日志外，其他日志都是 文本文件 。默认情况下，所有日志创建于 MySQL数据目录 中



# 通用查询日志（general log）

通用查询日志用来 记录用户的所有操作 ，包括启动和关闭MySQL服务、所有用户的连接开始时间和截止时间、发给 MySQL 数据库服务器的所有 SQL 指令等。当我们的数据发生异常时，**查看通用查询日志，还原操作时的具体场景**，可以帮助我们准确定位问题

## 基本操作

### 查看状态

```sql
show variables like '%general%'
```

### 启动通用日志

#### 永久

```sql
[mysqld] 
general_log=ON 
general_log_file=[path[filename]] #日志文件所在目录路径，filename为日志文件名
```

#### 临时

```sql
SET GLOBAL general_log=on; # 开启通用查询日志
SET GLOBAL general_log_file=’path/filename’; # 设置日志文件保存位置
```



# 错误日志（error log）

在MySQL数据库中，错误日志功能是 默认开启 的。而且，错误日志 无法被禁止

存储在MySQL数据库的数据文件夹下，可以配置错误日志名（默认为 mysqld.log （Linux系统）或hostname.err （mac系统））

```sql
[mysqld] log-error=[path/[filename]] #path为日志文件所在的目录路径，filename为日志文件名
```



# 二进制日志（bin log）

binlog即binary log，二进制日志文件，也叫作变更日志（update log）。它记录了数据库所有执行的DDL 和 DML 等数据库更新事件的语句，但是不包含没有修改任何数据的语句（如数据查询语句select、 show等）。

> 当MySQL创建二进制日志文件时，先创建一个以“filename”为名称、以“.index”为后缀的文件，再创建一个以“filename”为名称、以“.000001”为后缀的文件

用于两种场景

- 崩溃后数据恢复
- 主从数据复制

## 基本操作

### 查看状态

```sql
show variables like '%log_bin%';
```

### 参数配置

#### 永久

```sql
[mysqld] 
#启用二进制日志 
log-bin=xxx-bin 
binlog_expire_logs_seconds=600 
max_binlog_size=100M
```

#### 临时

```sql
 set global sql_log_bin=0;
```



## Bin Log 格式

- Statement

  每一条会修改数据的sql都会记录在binlog中。

  优点：不需要记录每一行的变化，减少了binlog日志量，节约了IO，提高性能。

  缺点：存储过程函数重放的时候，可能和原始数据不一致，比如now()

- Row 

  5.1.5版本的MySQL才开始支持row level 的复制，它不记录sql语句上下文相关信息，仅保存哪条记录被修改。

  优点：row level 的日志内容会非常清楚的记录下每一行数据修改的细节。而且不会出现某些特定情况下的存储过程，或function，以及trigger的调用和触发无法被正确复制的问题。

- Mixed 

从5.1.8版本开始，MySQL提供了Mixed格式，实际上就是Statement与Row的结合。



## 写入机制

事务执行过程中，先把日志写到 binlog cache ，事务提交的时候，再把binlog cache写到binlog文件中。因为一个事务的binlog不能被拆开，无论这个事务多大，也要确保一次性写入，所以系统会给每个线程分配一个块内存作为binlog cache。

>  刷盘策略和redo log 刷盘策略一致



# 中继日志（relay log ）

参看文献：https://blog.csdn.net/mshxuyi/article/details/100652769

**中继日志只在主从服务器架构的从服务器上存在**。从服务器为了与主服务器保持一致，要从主服务器读取二进制日志的内容，并且把读取到的信息写入本地的日志文件 中，这个从服务器本地的日志文件就叫中继日志 。然后，从服务器读取中继日志，并根据中继日志的内容对从服务器的数据进行更新，完成主从服务器的 数据同步 。

搭建好主从服务器之后，中继日志默认会保存在从服务器的数据目录下。

文件名的格式是： 从服务器名 -relay-bin.序号 。中继日志还有一个索引文件： 从服务器名 -relay- bin.index ，用来定位当前正在使用的中继日志。
