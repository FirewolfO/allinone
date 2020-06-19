package com.firewolf.testcase.junit5.s02_lifecycle;

import org.junit.jupiter.api.*;

/**
 * 描述：生命周期方法，需要依赖@BeforeAll, @AfterAll, @BeforeEach, or @AfterEach等注解
 * Author：liuxing
 * Date：2020/6/19
 */
public class LifeCycleTest {

    /**
     * 在所有测试方法执行之前
     */
    @BeforeAll
    public static void beforeAll() {
        System.out.println("before All");
    }

    /**
     * 在所有测试方法执行之后
     */
    @AfterAll
    public static void afterAll() {
        System.out.println("after all");
    }

    /**
     * 在每个测试方法执行前执行
     */
    @BeforeEach
    public void beforeEach() {
        System.out.println("before each");
    }

    /**
     * 在每个测试方法执行之后执行
     */
    @AfterEach
    public void afterEach() {
        System.out.println("after each");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }


}
