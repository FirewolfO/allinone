package com.firewolf.testcase.junit5.s15_extentions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

/**
 * 描述：忽略异常
 * Author：liuxing
 * Date：2020/6/22
 */
public class IgnoreExceptionExtentsion implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("do nothing");
    }
}
