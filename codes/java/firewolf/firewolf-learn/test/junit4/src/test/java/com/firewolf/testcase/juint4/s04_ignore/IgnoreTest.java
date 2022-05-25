package com.firewolf.testcase.juint4.s04_ignore;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 描述：忽略某个方法或者某个类，使用@Ignore
 * Author：liuxing
 * Date：2020/6/1
 */
public class IgnoreTest {

    @Ignore("ignore test1")
    @Test
    public void test1(){
        System.out.println("aaa");
    }

    @Test
    public void test2(){
        System.out.println("bbb");
    }
}
