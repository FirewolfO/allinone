package com.firewolf.testcase.junit5.s08_order;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 描述：让测试方法按顺序执行
 * Author：liuxing
 * Date：2020/6/20
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    @Test
    @Order(1)
    public void test1(){
        System.out.println("test1");
    }

    @Test
    @Order(4)
    public void test2(){
        System.out.println("test2");
    }

    @Test
    @Order(3)
    public void test3(){
        System.out.println("test3");
    }
}
