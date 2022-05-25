package com.firewolf.pattern.adapter.clazz;

/**
 * Description: 适配器
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 10:51 下午
 */
public class Adapter extends Adaptee implements Target {

    @Override
    public String sayHelloUpCase(String name) {
        return sayHello(name).toUpperCase();
    }
}
