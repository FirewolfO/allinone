package com.firewolf.testcase.junit5.s11_injectparams;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 描述：为测试类注入信息
 * Author：liuxing
 * Date：2020/6/21
 */
public class InjectParams {

    InjectParams(TestInfo testInfo) {
        System.out.println("构造器：" + testInfo.getDisplayName());
    }

    /**
     * 测试信息TestInfo：
     *
     * @param testInfo
     */
    @Test
    @DisplayName("TEST 1")
    @Tag("my-tag")
    void test1(TestInfo testInfo) {
        assertEquals("TEST 1", testInfo.getDisplayName());
        assertTrue(testInfo.getTags().contains("my-tag"));
    }

    /**
     * 注入测试报告对象，允许我们对测试报告进行更改
     *
     * @param testReporter
     */
    @Test
    void test2(TestReporter testReporter) {
        testReporter.publishEntry("a key", "a value");
    }

    /**
     * 重复测试参数
     *
     * @param repetitionInfo
     */
    @RepeatedTest(5)
    void test3(RepetitionInfo repetitionInfo) {
        assertEquals(5, repetitionInfo.getTotalRepetitions());
    }
}


