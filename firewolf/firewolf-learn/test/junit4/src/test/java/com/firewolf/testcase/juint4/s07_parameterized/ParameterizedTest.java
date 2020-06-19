package com.firewolf.testcase.juint4.s07_parameterized;

import com.firewolf.testcase.juint4.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

/**
 * 描述：给测试传递参数，多个参数
 * Author：liuxing
 * Date：2020/6/1
 */
@RunWith(Parameterized.class)
public class ParameterizedTest {

    /**
     * 注意，这个方法要求是public static修饰的
     *
     * @return
     */
    @Parameters
    public static List<String[]> datas() {
        return Arrays.asList(
                new String[][]{
                        {"hello", "Hello"},
                        {"hello WORLD", "Hello World"}
                });
    }

    private String original, expected;

    /***
     * 使用构造器来接受参数，注意顺序和传进来的参数是对应的
     * @param original
     * @param expected
     */
    public ParameterizedTest(String original, String expected) {
        this.original = original;
        this.expected = expected;
    }

    @Test
    public void test1() {
        Assert.assertEquals(expected, StringUtils.firstChar2UpCase(original));
    }

}


