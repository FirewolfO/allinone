package com.firewolf.pattern.adapter.bothway;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 11:39 下午
 */
public class ConcreteAdaptee implements Adaptee {
    @Override
    public String sayHello(String name) {
        return "hello," + name;
    }
}
