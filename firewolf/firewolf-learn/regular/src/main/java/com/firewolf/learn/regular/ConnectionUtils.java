package com.firewolf.learn.regular;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

