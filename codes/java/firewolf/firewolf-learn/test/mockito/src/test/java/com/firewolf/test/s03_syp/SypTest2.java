package com.firewolf.test.s03_syp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 描述：简化syp
 * Author：liuxing
 * Date：2020/6/25
 */
public class SypTest2 {

    @Spy
    UserService uService;

    @Test
    void testSpyWithAnnotation() {
        when(uService.hello("liuxing")).thenCallRealMethod();
        doReturn("HHH").when(uService).hello("LX");
        when(uService.hello("lx")).thenReturn("haha,lx");
        assertEquals(uService.hello("liuxing"), "Hello,liuxing");
        assertEquals(uService.hello("lx"), "haha,lx");
        assertEquals("HHH",uService.hello("LX"));
    }

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
    }

}
