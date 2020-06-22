package com.firewolf.testcase.junit5.s10_timeout;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/21
 */
public class TimeOutTest {
    @Test
    @Tag("dev")
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void test() throws InterruptedException {
        Thread.sleep(2000);
    }
}
