package com.firewolf.log.simple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;

class RepeatTestDriver {


    @DisplayName("repeat test")
    @RepeatedTest(value = 4, name = "执行测试： {displayName}, 第 {currentRepetition} / {totalRepetitions} 次！ ")
    void testRepeat(TestInfo testInfo) {
        assert !testInfo.getDisplayName().contains("3");
    }
}