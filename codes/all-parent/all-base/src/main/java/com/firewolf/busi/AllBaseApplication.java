package com.firewolf.busi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.firewolf.busi.example.springtest")
public class AllBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllBaseApplication.class, args);
    }

}
