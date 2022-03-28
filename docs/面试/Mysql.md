# 如何监听Binlog变化

- my.ini中配置`log=mylog.log `，监听日志变化
- `Cannal`：伪装自己是slave，向MySQL master发送 dump协议，MySQL master收到dump请求，开始推送binary log给slave（也就是canal）

# 事务隔离级别、实现原理



# Mysql 锁

## 锁分类

- 行锁
- 间隙锁
- 临界锁
- 页锁
- 表锁

## mysql 写锁定一行的时候，其他别的能读到数据么？

和隔离级别有关系

序列化：不可以的，加写锁会阻塞其他操作

读已提交、可重复读：可以的



## InnoDB锁膨胀过程

只有通过索引条件检索数据，InnoDB才会使用行级锁，否则，InnoDB将使用表锁！

