package com.firewolf.testcase.juint4.s10_rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Description: 自定义Rule
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/6 5:36 下午
 */
public class SelfTimeRuleTest {
    @Rule
    public SelfTimeRule timeRule = new SelfTimeRule();

    @Test
    public void test() throws Exception {
        Thread.sleep(1000);
    }
}

class SelfTimeRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    long start = System.currentTimeMillis();
                    base.evaluate();
                    long end = System.currentTimeMillis();
                    System.out.println("总共耗时：" + (end - start));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
    }
}