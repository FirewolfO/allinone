package com.firewolf.testcase.junit5.s9_nested;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 描述：内嵌测试类，让一些类似的测试放在一起
 * Author：liuxing
 * Date：2020/6/21
 */
public class NestedTest {

    @Test
    void test1() {
        System.out.println("test1");
    }

    @Nested
    class NestedTests{
        @Test
        void test1(){
            System.out.println("nested test1");
        }
    }

}
