package com.firewolf.demo.test.simple;

/**
 * Hello工具类
 */
public class HelloUtils {

    /**
     * 打招呼
     *
     * @param name 人名
     * @return
     */
    public String sayHello(String name) {
        return "hello," + name;
    }

    /**
     * 打招呼，自己传入前缀
     *
     * @param name   姓名
     * @param prefix 前缀
     * @return
     */
    public String sayHelloWithPrefix(String name, String prefix) {
        return prefix + "," + name.toString();
    }
}
