package com.firewolf.testcase.junit5.s05_disable;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/20
 */
//@Disabled
public class DisableTest {

    @Test
    @Disabled("有Bug还没有修复，先忽略")
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
}
