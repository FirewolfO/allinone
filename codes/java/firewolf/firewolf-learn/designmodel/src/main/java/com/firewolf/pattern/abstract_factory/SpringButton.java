package com.firewolf.pattern.abstract_factory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class SpringButton implements Button {
    @Override
    public void displayButton() {
        System.out.println("这是SpringButton");
    }
}
