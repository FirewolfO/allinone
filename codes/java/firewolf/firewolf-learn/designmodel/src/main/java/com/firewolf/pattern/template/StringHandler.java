package com.firewolf.pattern.template;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:49 上午
 */
public abstract class StringHandler {
    /**
     * 补齐字符串
     *
     * @param origin
     * @param length
     * @param c
     * @return
     */
    public String padString2Upcase(String origin, int length, char c) {
        if (origin.length() >= length) {
            return origin.toUpperCase();
        }
        // 模板方法中自己的逻辑
        origin = origin.trim();
        // 调用具体的实现方法
        origin = padStr(origin, length, c);

        // 模板方法中自己的逻辑
        return origin.toUpperCase();
    }

    public abstract String padStr(String origin, int length, char c);
}
