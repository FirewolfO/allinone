package com.firewolf.testcase.juint4;

import static org.junit.Assume.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * Description: 假设
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 10:15 下午
 */
public class AssumptionsTest {
    @Test
    public void test() {
        int a = 1 + 2; // 调用某个方法计算出来结果
        assumeTrue(a != 0);
        int b = 100 / a;
        try {
            String s = new String("aaa"); //计算出来的二级果
            assumeNotNull(s);
        } catch (Exception e) {
            assumeNoException(e);
        }
        assumeThat(a, CoreMatchers.is(3));
    }
}
