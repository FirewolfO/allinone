package com.firewolf.testcase.juint4.s12_lifecycle;

import org.junit.*;

/**
 * 描述： 生命周期方法
 * Author：liuxing
 * Date：2020/6/19
 */
public class LifeCycleTest {

    @BeforeClass
    public static void allBefore() {
        System.out.println("所有测试方法开始之前");
    }

    @AfterClass
    public static void allAfter() {
        System.out.println("所有测试方法结束之后");
    }

    @Before
    public void eachBefore() {
        System.out.println("某个测试方法要开始了");
    }

    @After
    public void eachAfter() {
        System.out.println("某个测试方法要结束了");
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
