package com.firewolf.testcase.juint4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


/**
 * Description: 超时测试
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 10:54 下午
 */
public class TimeOutTest {

    /**
     * 要求任何一个测试方法的执行时间不能超过1秒
     */
    @Rule
    public Timeout timeout  = Timeout.seconds(1);
    /**
     * 这个执行时间不能超过1秒
     * @throws Exception
     */
    @Test(timeout = 1000)
    public void test() throws Exception{
        Thread.sleep(500);
    }

    @Test
    public void test2() throws Exception{
        Thread.sleep(600);
    }
}
