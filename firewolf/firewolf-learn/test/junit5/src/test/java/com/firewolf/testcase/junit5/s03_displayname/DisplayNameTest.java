package com.firewolf.testcase.junit5.s03_displayname;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 描述：测试名字
 * Author：liuxing
 * Date：2020/6/19
 */
@DisplayName("这是一个特殊测试")
public class DisplayNameTest {

    @DisplayName("测试1")
    @Test
    public void test1() {
        System.out.println("test1");
    }

}
