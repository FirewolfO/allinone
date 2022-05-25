package com.firewolf.test.s03_syp;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/25
 */
public class SypTest {
    @Test
    void testSpy() {

        UserService userService = new UserService();
        UserService spy = spy(userService);
        when(spy.hello("liuxing")).thenCallRealMethod(); // 调用真实方法

        when(spy.hello("lx")).thenReturn("haha,lx"); // mock结果

        assertEquals(spy.hello("liuxing"), "Hello,liuxing");
        assertEquals(spy.hello("lx"), "haha,lx");
    }


}

class UserService {
    public String hello(String name) {
        return "Hello," + name;
    }
}

