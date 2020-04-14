package com.firewolf.demo.test.testcontainer.mysql;

import com.firewolf.demo.test.entity.User;
import com.firewolf.demo.test.mybatis.UserMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;

@SpringBootTest
public class MySQLTest {
    @ClassRule
    public static MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7") // 指定mysql版本
            .withInitScript("sql/lx-base.sql") //指定初始sql脚本
            .withCommand("--character-set-server=utf8 --collation-server=utf8_unicode_ci")//防止乱码
            .withExposedPorts(3306); // 暴露3306端口号到随机端口


    @BeforeAll
    public static void setUp() {
        mysql.start();
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.driver-class-name", mysql.getDriverClassName());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
    }

    @AfterAll
    public static void tearDown() {
        mysql.stop();
    }


    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        List<User> users = userMapper.selectAll();
        Assertions.assertEquals(users.size(), 4);
    }


}
