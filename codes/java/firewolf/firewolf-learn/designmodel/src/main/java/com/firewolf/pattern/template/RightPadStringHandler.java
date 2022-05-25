package com.firewolf.pattern.template;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:57 上午
 */
public class RightPadStringHandler extends StringHandler {
    @Override
    public String padStr(String origin, int length, char c) {
        for (int i = 0; i < length - origin.length(); i++) {
            origin += c;
        }
        return origin;
    }
}
