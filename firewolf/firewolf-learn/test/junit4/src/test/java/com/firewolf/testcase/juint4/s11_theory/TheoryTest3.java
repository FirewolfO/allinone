package com.firewolf.testcase.juint4.s11_theory;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

/**
 * Description: @TestedOn限定参数
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/6 10:15 下午
 */
@RunWith(Theories.class)
public class TheoryTest3 {

    /**
     * 使用@DataPoints声明一组参数
     */
    @DataPoints
    public static int[] getTestData() {
        return new int[]{0, 1, 2};
    }

    /**
     * @param first
     * @param second
     * @Theory用于说明这是一个理论测试
     */
    @Theory
    public void testDevide(int first, @TestedOn(ints = {1, 2}) int second) {
        // 要求跳过second=0的数据
        Assert.assertTrue((first / second) >= 0);
    }

}
