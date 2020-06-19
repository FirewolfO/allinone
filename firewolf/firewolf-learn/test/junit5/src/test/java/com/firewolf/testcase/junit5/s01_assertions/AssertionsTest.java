package com.firewolf.testcase.junit5.s01_assertions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/19
 */
public class AssertionsTest {


    /**
     * assertTrue  / assertFalse：断言某个表达式的结果是正确/错误的
     */
    @Test
    public void testAssertTrue_False() {
        boolean flag = 1 > 3;
        assertTrue(!flag);
        assertFalse(flag);
    }


    /**
     * assertNull / assertNotNull ： 断言某个对象是null / 非null
     */
    @Test
    public void testAssertNull_NotNull() {
        Object o = null;
        Object o2 = new Object();
        assertNull(o);
        assertNotNull(o2);
    }

    /**
     * assertEquals / assertNotEquals：断言某两个数据是相同/不同的，对象调用equals方法
     */
    @Test
    public void testAssertEquals_NotEquals() {
        assertEquals("aaa", "aaa");
        assertNotEquals("aaa", "bbb");
    }


    /**
     * assertArrayEquals：断言两个数组是的原始是相同的，对象元素对比使用的是equlas方法
     */
    @Test
    public void testAssertArrayEquals() {
        assertArrayEquals(new int[]{1, 2, 3}, new int[]{1, 2, 3});
    }


    /**
     * assertIterableEquals：断言两个集合相同，调用每个元素的equals
     */
    @Test
    public void testAssertIterableEquals() {
        assertIterableEquals(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
    }

    /**
     * 使用正则匹配，第一个参数是正则表达式列表，第二个是要用来被匹配的字符串列表，只有在正则表达式数量和字符串数量一致且对应位置的正则能匹配上字符串的时候，才会返回true
     */
    @Test
    public void testAssertLinesMatch() {
        List<String> regexs = Arrays.asList("^\\d.*_$", "^\\d+$");
        List<String> values = Arrays.asList("23ss_", "1234");
        assertLinesMatch(regexs, values);
    }


    /**
     * assertSame/ assertNotSame：判断两个数据是否相同，==判断
     */
    @Test
    public void testSame_NotSame() {
        assertSame("123", "123");
        assertNotSame("123", new String("123"));

    }


    /**
     * assertAll： 用来归类一组断言，不管前面的断言是否成功，都会继续后面的
     */
    @Test
    public void testAssertAll() {
        assertAll(
                () -> assertEquals(1, 2),
                () -> assertNull(null)
        );
    }

    /**
     * assertThrows / assertDoesNotThrow：断言会抛出指定异常 / 断言不会抛出异常
     */
    public void testAssertThrows_NotThrows() {
        assertThrows(RuntimeException.class, () -> {
            int a = 1 / 0;
        });

        assertDoesNotThrow(() -> {
            int a = 1 + 2;
        });
    }
}
