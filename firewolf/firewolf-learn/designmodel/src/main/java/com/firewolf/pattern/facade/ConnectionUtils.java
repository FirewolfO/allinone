package com.firewolf.pattern.facade;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/8/2 10:17 上午
 */
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
