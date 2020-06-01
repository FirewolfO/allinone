package com.firewolf.testcase.juint4;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
/**
 * Description: Matcher
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/6/1 11:54 下午
 */
public class MatcherTest {

    @Test
    public void test1(){
        int id = 1;
        assertThat("不相等",id==1);
        assertThat(id,is(Integer.class));
    }
}
