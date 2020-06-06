package com.firewolf.testcase.juint4.s02_order;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * 让方法按照固定的顺序执行
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MethodOrderTest {

    @Test
    public void testA() {
        System.out.println("first");
    }

    @Test
    public void testB() {
        System.out.println("second");
    }

    @Test
    public void testC() {
        System.out.println("third");
    }
}