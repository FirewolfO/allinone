package com.firewolf.testcase.juint4.s10_rule;

import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * 描述：TestWatcher 定义了五个触发点，分别是测试成功，测试失败，测试开始，测试完成，测试跳过，能让我们在每个触发点执行自定义的逻辑。有点类似于AOP
 * Author：liuxing
 * Date：2020/6/5
 */
public class TestWatcherTest {

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            System.out.println(description.getDisplayName() + "方法执行成功了");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println(description.getDisplayName() + "方法执行失败了");
        }

        @Override
        protected void skipped(AssumptionViolatedException e, Description description) {
            System.out.println(description.getDisplayName() + "方法被跳过了");
        }

        @Override
        protected void starting(Description description) {
            System.out.println(description.getDisplayName() + "方法开始执行了....");
        }

        @Override
        protected void finished(Description description) {
            System.out.println(description.getDisplayName() + "方法执行结束了");
        }
    };

    @Test
    public void test(){
        System.out.println("xxxx");
    }
}
