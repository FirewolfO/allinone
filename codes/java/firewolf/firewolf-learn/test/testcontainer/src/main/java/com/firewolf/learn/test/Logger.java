package com.firewolf.learn.test;

/**
 * 描述：日志工具
 * Author：liuxing
 * Date：2020/5/12
 */
public class Logger {


    /**
     * 打印错误信息到控制台
     *
     * @param content
     * @param params
     */
    public static void info(String content, Object... params) {
        String format = content.replaceAll("\\{\\s*}", "%s");
        System.err.println(String.format(format, params));
    }
}
