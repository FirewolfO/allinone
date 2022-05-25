package com.firewolf.pattern.abstract_factory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class SpringTextField implements TextField {
    @Override
    public void displayTextField() {
        System.out.println("这是SpringTextField");
    }
}
