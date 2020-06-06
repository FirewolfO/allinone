package com.firewolf.testcase.juint4.s06_multi;

import com.firewolf.testcase.juint4.s07_parameterized.ParameterizedTest;
import com.firewolf.testcase.juint4.s01_assert.AssertTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Description: 一次性测试多个类的测试方法
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 11:04 下午
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        AssertTest.class,
        ParameterizedTest.class
})
public class SuitTest {
}
