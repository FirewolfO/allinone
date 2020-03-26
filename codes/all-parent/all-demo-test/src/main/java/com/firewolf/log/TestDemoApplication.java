package com.firewolf.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.firewolf")
public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestDemoApplication.class, args);
    }

}
