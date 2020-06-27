package com.firewolf.learn.test.testcontainer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

/**
 * 描述：Junit4继承TestContainers
 * Author：liuxing
 * Date：2020/6/27
 */
public class Junit4 {


    private Jedis jedis;

    /**1.
     * 创建启动容器
     */
    @Rule
    public GenericContainer redis = new GenericContainer<>("redis:5.0.5")
            .withExposedPorts(6379);

    @Before
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
