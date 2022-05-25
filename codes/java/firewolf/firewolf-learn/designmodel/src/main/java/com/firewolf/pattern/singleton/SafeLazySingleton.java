package com.firewolf.pattern.singleton;

/**
 * 安全的懒汉式单例
 */
public class SafeLazySingleton {
    private static SafeLazySingleton instance = null;

    private SafeLazySingleton() {
    }

    public static synchronized SafeLazySingleton getInstance() {
        if (instance == null) {
            instance = new SafeLazySingleton();
        }
        return instance;
    }
}