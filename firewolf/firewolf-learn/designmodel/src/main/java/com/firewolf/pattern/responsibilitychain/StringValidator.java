package com.firewolf.pattern.responsibilitychain;

/**
 * 描述：抽象处理者
 * Author：liuxing
 * Date：2020/7/6
 */
public abstract class StringValidator {

    protected StringValidator nextHandler;

    public StringValidator(StringValidator nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean verify(String str);
}
