package com.firewolf.pattern.template;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/19 10:58 上午
 */
public class Client {

    public static void main(String[] args) {
        StringHandler stringHandler = new RightPadStringHandler();

        String res = stringHandler.padString2Upcase("aa",10,'b');
        System.out.println(res);
    }
}
