package com.firewolf.testcase.juint4.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 描述：自定义规则，记录测试耗时
 * Author：liuxing
 * Date：2020/6/5
 */
public class SelfTimeRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        try {
            long start = System.currentTimeMillis();
            base.evaluate();
            long end = System.currentTimeMillis();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
