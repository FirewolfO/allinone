package com.firewolf.common.simple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MockitoTest {

    /**
     * 验证某些行为
     */
    @Test
    void testBehavior() {

        //构建moock数据
        List<String> list = mock(List.class);
        list.add("1");
        list.add("2");

        System.out.println(list.get(0)); // 会得到null ，前面只是在记录行为而已，没有往list中添加数据

        verify(list).add("1"); // 正确，因为该行为被记住了
//        verify(list).add("3");//报错，因为前面没有记录这个行为

    }


    /**
     * 测试存根
     */
    @Test
    void testStub() {
        List<Integer> l = mock(ArrayList.class);

        when(l.get(0)).thenReturn(10);
        when(l.get(1)).thenReturn(20);
        when(l.get(2)).thenThrow(new RuntimeException("no such element"));

        assertEquals(l.get(0), 10);
        assertEquals(l.get(1), 20);
        assertNull(l.get(4));
        assertThrows(RuntimeException.class, () -> {
            int x = l.get(2);
        });
    }


    @Test
    void testVoidStub() {
        List<Integer> l = mock(ArrayList.class);
        doReturn(10).when(l).get(1);
        doThrow(new RuntimeException("you cant clear this List")).when(l).clear();

        assertEquals(l.get(1), 10);
        assertThrows(RuntimeException.class, () -> l.clear());
    }

    /**
     * 参数匹配器
     */
    @Test
    void testMatchers() {
        List<Integer> l = mock(ArrayList.class);
        when(l.get(anyInt())).thenReturn(100);

        assertEquals(l.get(999), 100);
    }

    /**
     * 调用次数
     */
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


    /**
     * 测试Mock创建
     */
    @Mock
    private List<Long> longs;

    @Test
    void testMockitoCreate() {
        when(longs.get(0)).thenReturn(100L);
        assertEquals(longs.get(0), 100L);
    }

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
    }



    @Spy UserService uService;
    @Test
    void testSpyWithAnnotation() {
        when(uService.hello("liuxing")).thenCallRealMethod();
        when(uService.hello("lx")).thenReturn("haha,lx");
        assertEquals(uService.hello("liuxing"), "Hello,liuxing");
        assertEquals(uService.hello("lx"), "haha,lx");
    }

    @Test
    void testSpy() {

        UserService userService = new UserService();
        UserService spy = spy(userService);
        when(spy.hello("liuxing")).thenCallRealMethod();

        when(spy.hello("lx")).thenReturn("haha,lx");

        assertEquals(spy.hello("liuxing"), "Hello,liuxing");
        assertEquals(spy.hello("lx"), "haha,lx");
    }

    class UserService {
        public String hello(String name) {
            return "Hello," + name;
        }
    }
}
