package com.firewolf.testcase.juint4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：给测试传递参数，单个参数
 * Author：liuxing
 * Date：2020/6/1
 */
@RunWith(Parameterized.class)
public class ParameterizedTest3 {

    /**
     * 注意，这个方法要求是public static修饰的
     *
     * @return
     */
    @Parameters
    public static List<String> datas() {
        return Arrays.asList(
                "hello",
                "world"
        );
    }

    // 使用注解接受参数，要求参数是public修饰的
    // 接受第一个参数，默认为0，不用指定
    @Parameter
    public String original;


    @Test
    public void test1() {
        Assert.assertEquals(5, original.length());
    }

}


