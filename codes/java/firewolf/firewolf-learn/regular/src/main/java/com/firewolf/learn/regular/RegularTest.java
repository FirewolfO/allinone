package com.firewolf.learn.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/15
 */
public class RegularTest {

    public static void main(String[] args) {

        // 编译表达式
        Pattern pattern = Pattern.compile("^[a-z]*\\d+$");

        // 匹配字符串
        Matcher matcher = pattern.matcher("abc123we");
        System.out.println(matcher.matches());
        
    }
}
