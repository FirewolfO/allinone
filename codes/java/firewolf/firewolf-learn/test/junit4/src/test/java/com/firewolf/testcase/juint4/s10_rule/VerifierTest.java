package com.firewolf.testcase.juint4.s10_rule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Verifier;

/**
 * 描述：可以在测试执行完成之后做一些校验，以验证测试结果是不是正确
 * Author：liuxing
 * Date：2020/6/5
 */
public class VerifierTest {

    private String result;

    @Rule
    public Verifier verifier = new Verifier() {
        @Override
        protected void verify() throws Throwable {
            if (!"success".equals(result)) {
                System.out.println("结果不正确");
                // 这里可以抛出异常
            }
        }
    };

    @Test
    public void test() {
        result = "failed";
    }
}
