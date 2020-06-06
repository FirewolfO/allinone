package com.firewolf.testcase.juint4.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/5
 */
public class TestNameTest {

    @Rule
    public TestName testName = new TestName();

    @Test
    public void test() {
        System.out.println(testName.getMethodName());
    }
}
