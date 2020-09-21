package com.megvii.utils;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-21
 */
public class Slf4jLog {

    public static void error(String content, Throwable e) {
        System.out.println(content);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void error(String content) {
        error(content, null);
    }
}
