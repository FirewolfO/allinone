package com.firewolf.testcase.junit5.s12_repeat;

/**
 * 描述：重复测试，可以让测试方法运行多次
 * Author：liuxing
 * Date：2020/6/21
 */

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;

public class RepeatTestDriver {

    @DisplayName("repeat test")
    @RepeatedTest(value = 4, name = "执行测试： {displayName}, 第 {currentRepetition} / {totalRepetitions} 次！ ")
    void testRepeat(TestInfo testInfo) {
        assert !testInfo.getDisplayName().contains("3");
    }
}

