package com.firewolf.testcase.juint4.s03_exception;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 描述：处理异常
 * Author：liuxing
 * Date：2020/6/1
 */
public class ExceptionTest {

    /**
     * 通过assertThrows断言异常的出现
     */
    @Test
    public void testException1() {
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            int a = 1 / 0;
        });
        System.out.println(exception.getMessage());

        assertThrows("/ by zero", ArithmeticException.class, () -> {
            int a = 1 / 0;
        });
    }

    /**
     * 捕获异常之后再做断言
     */
    @Test
    public void testException2() {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            assertEquals(e.getClass(), ArithmeticException.class);
            assertEquals(e.getMessage(), "/ by zero");
        }
    }

    /**
     * 在方法上面指定可能出现的异常
     */
    @Test(expected = ArithmeticException.class)
    public void testException3() {
        int a = 1 / 0;
    }
}
