package com.firewolf.testcase.junit5.s11_injectparams;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/22
 */
@ExtendWith(RandomParametersExtension.class)
public class MyParamsExtensionTest {

    @Test
    void test(@RandomParametersExtension.Random int i, @RandomParametersExtension.Random int j) {
        System.out.println(i + ", " + j);
    }
}
