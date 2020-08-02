## 普通获取一个Connection

```java
public class ConnectionUtils {

    public Connection getConnection() throws Exception {

        Class.forName("com.mysql.jdbc.Drivder");
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String password = "asplover";
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}

```

> 存在的问题：
>
> 每获取一个连接，就多出一个Connection对象，占用内存，消耗数据库的连接数；



## 改进一

为了不每次获取Connection连接，我们只在Connection不存在的时候，再获取连接

```java
public class ConnectionUtils {

    private Connection connection;

    public Connection getConnection() throws Exception {
        if (connection == null) {
            Class.forName("com.mysql.jdbc.Drivder");
            String url = "jdbc:mysql://127.0.0.1:3306/test";
            String user = "root";
            String password = "asplover";
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
```

这样，只会有一个Connection，减少了连接；

> 问题：
>
> 多线程情况下，多个线程获取的Connection是同一个，会互相干扰

## 改进二

让各个线程之间的Connection互不干扰，使用ThreadLocal

```java
public class ConnectionUtils {

    private ThreadLocal<Connection> t1 = new ThreadLocal<>();

    public Connection getConnection() throws Exception {
        if (t1.get() == null) {
            Class.forName("com.mysql.jdbc.Drivder");
            String url = "jdbc:mysql://127.0.0.1:3306/test";
            String user = "root";
            String password = "asplover";
            Connection connection = DriverManager.getConnection(url, user, password);
            t1.set(connection);
        }
        return t1.get();
    }
}

```