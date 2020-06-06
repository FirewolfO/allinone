package com.firewolf.testcase.juint4.rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/5
 */
public class ErrorCollectorTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void test() {

        try {
            int i = 1 / 0;
            System.out.println("计算结果");
        } catch (Exception e) {
            collector.addError(e);
        }
        try {
            String a = null;
            System.out.println(a.length());
        } catch (Exception e) {
            collector.addError(e);
        }
        System.out.println("xxx");
    }
}
