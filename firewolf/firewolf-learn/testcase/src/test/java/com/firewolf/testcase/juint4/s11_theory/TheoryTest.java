package com.firewolf.testcase.juint4.s11_theory;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.ClassRule;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.Verifier;
import org.junit.runner.RunWith;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/6 10:15 下午
 */
@RunWith(Theories.class)
public class TheoryTest {

    /**
     * 验证结果，
     */
    @ClassRule
    public static Verifier verifier = new Verifier() {
        @Override
        protected void verify() throws Throwable {
            Assert.assertEquals(totalCount, 9);
            Assert.assertEquals(passedCount, 6);
        }
    };

    private static int totalCount = 0;
    private static int passedCount = 0;

    /**
     * @DataPoint用于组合参数的数据，要求必须使用public static修饰
     */
    @DataPoint
    public static int a = 0;

    @DataPoint
    public static int b = 1;

    @DataPoint
    public static int c = 2;

    /**
     * @param first
     * @param second
     * @Theory用于说明这是一个理论测试
     */
    @Theory
    public void testDevide(int first, int second) {
        totalCount += 1;
        // 要求跳过second=0的数据
        Assume.assumeThat(second, CoreMatchers.not(0));
        Assert.assertTrue((first / second) >= 0);
        passedCount += 1;
    }
}
