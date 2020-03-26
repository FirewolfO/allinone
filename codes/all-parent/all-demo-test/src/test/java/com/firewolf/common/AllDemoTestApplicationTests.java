package com.firewolf.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootTest
@MapperScan(basePackages = "com.firewolf")
class AllDemoTestApplicationTests {

    @Test
    void contextLoads() {
    }

}
