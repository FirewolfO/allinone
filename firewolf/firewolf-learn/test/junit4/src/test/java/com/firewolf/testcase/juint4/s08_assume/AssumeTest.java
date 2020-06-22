package com.firewolf.testcase.juint4.s08_assume;

import static org.junit.Assume.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

/**
 * Description: 假设
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 10:15 下午
 */
public class AssumeTest {
    @Test
    public void test() {
        int a = 1 + 2; // 调用某个方法计算出来结果
        assumeTrue(a != 0); // 因为条件成立，所有走了后面的步骤，如果不成立，后面的就不再执行
        int b = 100 / a;
        try {
            System.out.println("xxxx");
            String s = new String("aaa"); //计算出来的二级果
            assumeNotNull(s);
        } catch (Exception e) {
            assumeNoException(e);
        }
        assumeThat(a, CoreMatchers.is(3));
    }
}
