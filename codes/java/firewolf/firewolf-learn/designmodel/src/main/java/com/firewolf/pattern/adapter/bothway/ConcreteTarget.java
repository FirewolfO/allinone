package com.firewolf.pattern.adapter.bothway;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 11:38 下午
 */
public class ConcreteTarget implements Target {
    @Override
    public String sayHelloUpCase(String name) {
        return "HAHA," + name;
    }
}
