package com.firewolf.pattern.singleton;

/**
 * 枚举方式实现的单例模式
 */
public enum EnumSingleton {
    INSTANCE;
    public void doSomething() {
        System.out.println("doSomething");
    }
}