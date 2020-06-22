package com.firewolf.testcase.junit5.s04_assumptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.*;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/20
 */
public class AssumptionsTest {


    @Test
    public void test() {
        int a = 1;

        // 表达式为true的时候执行后续
        assumeTrue(a == 1);
        System.out.println("计算结果正确");

        // 表达式为false的时候执行后续
        assumeFalse(a % 2 == 0);
        System.out.println("结果是奇数");


        // 当前面的条件成立的时候执行后面的
        assumingThat(a < 10, () -> {
            System.out.println("数字小于10");
        });
    }

}
