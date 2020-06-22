package com.firewolf.testcase.junit5.s15_extentions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/22
 */
public class ExtensionTest2 {

    @ExtendWith(IgnoreExceptionExtentsion.class)

    @Test
    void test3() {
        int a = 1 / 0;
    }


}
