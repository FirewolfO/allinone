package com.firewolf.pattern.adapter.clazz;

/**
 * Description: 适配者，已经存在的旧实现
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 10:49 下午
 */
public class Adaptee {

    public String sayHello(String name) {
        return "hello," + name;
    }
}
