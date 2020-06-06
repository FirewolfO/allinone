package com.firewolf.testcase.juint4.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 描述：异常断言
 * Author：liuxing
 * Date：2020/6/5
 */
public class ExpectedExceptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test() {
        expectedException.expect(NullPointerException.class);
        throw new NullPointerException();
    }
}
