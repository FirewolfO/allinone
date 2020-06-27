package com.firewolf.learn.test.testcontainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class Junit5 {

    private Jedis jedis;

    // container {
    @Container
    public GenericContainer redis = new GenericContainer<>("redis:5.0.5")
            .withExposedPorts(6379);
    // }


    @BeforeEach
    public void setUp() {
        // 2. 获取连接的URL，PORT等
        String address = redis.getContainerIpAddress();
        Integer port = redis.getFirstMappedPort();
        System.out.println("test redis host = " + address);
        System.out.println("test redis port = " + port);
        // 3. 初始化jedis连接
        jedis = new Jedis(address, port);
    }

    @Test
    public void testSimplePutAndGet() {
        // 4. 使用
        jedis.set("test", "example");
        String retrieved = jedis.get("test");
        assertEquals("example", retrieved);
    }
}