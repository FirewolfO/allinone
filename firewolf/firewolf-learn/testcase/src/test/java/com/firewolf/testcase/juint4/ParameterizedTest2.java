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
 * 描述：给测试传递参数，多个参数
 * Author：liuxing
 * Date：2020/6/1
 */
@RunWith(Parameterized.class)
public class ParameterizedTest2 {

    /**
     * 注意，这个方法要求是public static修饰的
     * 可以通过name属性给测试方法取名，里面可以使用如下占位符：
     * {idnex}：第几组测试
     * {0}: 第一个参数
     * {1}: 第二个参数
     * ....
     *
     * @return
     */
    @Parameters(name = "{index} : StringUtils.firstChar2UpCase({0})={1}")
    public static List<String[]> datas() {
        return Arrays.asList(
                new String[][]{
                        {"hello", "Hello"},
                        {"hello WORLD", "Hello World"}
                });
    }

    // 使用注解接受参数，要求参数是public修饰的
    // 接受第一个参数，默认为0，不用指定
    @Parameter
    public String original;
    // 接受第二个参数，指定为1
    @Parameter(1)
    public String expected;


    @Test
    public void test1() {
        Assert.assertEquals(expected, StringUtils.firstChar2UpCase(original));
    }

}


