package com.firewolf.test.s01_simple;


import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：一些简单的使用：
 * 1. mock对象
 * 2. mock一些简单的行为
 * Author：liuxing
 * Date：2020/6/24
 */
public class SimpleTest {

    /**
     * 验证基本操作
     */
    @Test
    void verifyBehaviors() {
        List mock = mock(List.class);
        mock.add("zhangsan");

        // 正常运行
        verify(mock).add("zhangsan");

        // 这里会报错，因为没有mock 添加 lisi这个元素
        verify(mock).add("lisi");

        // 这里会报错，因为没有mock clean方法
        verify(mock).clear();
    }

    /**
     * 测试存根/打桩
     */
    @Test
    void stub() {
        // mock对象
        List<String> mock = mock(ArrayList.class);

        // 打桩
        when(mock.get(0)).thenReturn("java");
        doReturn("hello").when(mock).get(1);
        // 使用打桩指定的数据
        System.out.println(mock.get(1)); // 会返回java
        System.out.println(mock.get(0)); // 会返回hello
    }


    @Test
    void testMatchers() {
        List<Integer> l = mock(ArrayList.class);
        when(l.get(anyInt())).thenReturn(100);

        when(l.add(isNotNull())).thenReturn(true);

        assertEquals(l.get(999), 100);
        assertTrue(l.add(10));
        assertFalse(l.add(null));
    }

    @Test
    void testTimes() {
        List<Integer> l = mock(ArrayList.class);
        when(l.get(anyInt())).thenReturn(100);
        System.out.println(l.get(0));
        System.out.println(l.get(1));
        System.out.println(l.get(1));
        System.out.println(l.get(2));
        System.out.println(l.get(2));
        System.out.println(l.get(2));
        System.out.println(l.get(2));

        verify(l, times(7)).get(anyInt());
        verify(l, atLeastOnce()).get(0);
        verify(l, atLeast(3)).get(2);
        verify(l, atMost(6)).get(2);
        verify(l, atMostOnce()).get(0);
    }

    @Test
    void testOrder(){

        // mock操作
        List mock = mock(List.class);
        when(mock.add(anyString())).thenReturn(true);

        // 实际调用
        mock.add("world");
        mock.add("hello");

        // 验证方法执行顺序
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).add("world");
        inOrder.verify(mock).add("hello");


        // 验证多个mock对象的方法执行顺序
        List mock1 = mock(List.class);
        List mock2 = mock(List.class);

        when(mock1.add(anyString())).thenReturn(true);
        when(mock2.add(anyString())).thenReturn(true);

        mock1.add("Hello1");
        mock2.add("Hello2");

        InOrder inOrder1 = inOrder(mock1,mock2);
        inOrder1.verify(mock1).add(anyString());
        inOrder1.verify(mock2).add(anyString());

    }


}
