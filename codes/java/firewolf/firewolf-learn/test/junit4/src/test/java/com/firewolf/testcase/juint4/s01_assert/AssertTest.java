package com.firewolf.testcase.juint4.s01_assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 描述：断言，用于断定各种结果
 * Author：liuxing
 * Date：2020/6/1
 */
public class AssertTest {

    /**
     * assertTrue / assertFalse
     */
    @Test
    public void testAssertTrue() {
        // 验证某个条件表达式
        assertTrue(1 == 1);

        assertFalse(1 == 2);

        // 当结果不正确的时候，抛出指定信息的异常
        assertTrue("1不等于2", 1 == 1);
    }


    /**
     * assertEquals / assertNotEquals
     */
    @Test
    public void testAssertEquals() {
        assertEquals(100, 100);
        assertNotEquals("aaa", "bbb");

        assertEquals(new String("aaa"), new String("aaa"));
    }

    /**
     * assertArrayEquals
     */
    @Test
    public void testAssertArrayEquals() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        assertArrayEquals(a, b);
    }

    /**
     * assertNull / assertNotNull
     */
    @Test
    public void testAssertNull() {
        String a = null;
        assertNull(a);

        assertNotNull("111");
    }

    /**
     * assertSame / assertNotSame
     */
    @Test
    public void testAssertSame() {
        assertSame("aaa", "aaa");
        assertNotSame(new String("aaa"), new String("aaa"));
    }

    /**
     * 断言某个操作会产生某种异常
     */
    @Test
    public void testAssertThrows() {

        assertThrows(ArithmeticException.class, () -> {
            int a = 1 / 0;
        });

        List s = new ArrayList();
        assertThrows(IndexOutOfBoundsException.class, () -> s.get(1));
    }
}
