package com.firewolf.pattern.adapter.object;

/**
 * Description: 适配器
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/5/13 10:51 下午
 */
public class Adapter implements Target {

    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String sayHelloUpCase(String name) {
        return adaptee.sayHello(name).toUpperCase();
    }
}
