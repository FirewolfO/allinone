package com.firewolf.util;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/12
 */
public class Logger {
    public static void console(String content, Object... params) {
        String format = content.replaceAll("\\{\\s*}", "%s");
        System.out.println(String.format(format, params));
    }
}
