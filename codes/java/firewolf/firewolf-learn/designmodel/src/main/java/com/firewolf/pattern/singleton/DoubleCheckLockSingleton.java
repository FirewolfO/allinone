package com.firewolf.pattern.singleton;

public class DoubleCheckLockSingleton {
    private static DoubleCheckLockSingleton instance = null;

    private DoubleCheckLockSingleton() {
    }

    public static DoubleCheckLockSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLockSingleton.class) {
                // 需要再次判断是存在，原因见下面：
                if (instance == null) {
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }
}