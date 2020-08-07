package com.firewolf.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Description: Test项目主启动类
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/4/18 8:26 上午
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class,args);
    }
}