package com.firewolf.testcase.junit5.s15_extentions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/22
 */
public class ExtensionTest3 {

    @RegisterExtension
    public static IgnoreExceptionExtentsion exceptionExtentsion = new IgnoreExceptionExtentsion();

    @Test
    void test3() {
        int a = 1 / 0;
    }


}
