package com.firewolf.pattern.singleton;

/**
 * 描述：饿汉式单例
 * Author：liuxing
 * Date：2020/5/15
 */
public class EagerSingleon {
    private static EagerSingleon instance = new EagerSingleon();

    private EagerSingleon() {
    }

    public static EagerSingleon getInstance() {
        return instance;
    }
}
