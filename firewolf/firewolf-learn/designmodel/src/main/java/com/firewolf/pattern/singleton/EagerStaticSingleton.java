package com.firewolf.pattern.singleton;

/**
 * 使用静态代码块完成的饿汉式单例
 */
public class EagerStaticSingleton {
    private static EagerStaticSingleton instance = null;
    static{
        instance = new EagerStaticSingleton();
    }
    private EagerStaticSingleton(){};
    public static EagerStaticSingleton getInstance(){
        return instance;
    }
}