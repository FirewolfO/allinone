package com.firewolf.test.s02_create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * 描述：简化mock对象的创建
 * Author：liuxing
 * Date：2020/6/25
 */
public class SimpfyCreateTest {
    @Mock
    private List<Long> longs;

    @Test
    void testMockitoCreate() {
        when(longs.get(0)).thenReturn(100L);
        assertEquals(longs.get(0),100L);
    }

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
    }

}
