package com.firewolf.testcase.junit5.s15_extentions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

/**
 * 描述：监测自行过程
 * Author：liuxing
 * Date：2020/6/22
 */
public class MyTestWatcherExtension implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("测试执行成功了....");
    }
}
