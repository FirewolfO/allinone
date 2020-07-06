package com.firewolf.pattern.responsibilitychain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class PrefixStringValidator extends StringValidator {

    private String prefix;

    public PrefixStringValidator(StringValidator stringValidator, String prefix) {
        super(stringValidator);
        this.prefix = prefix;
    }

    @Override
    public boolean verify(String str) {
        System.out.println("verify prefix");
        if (str.startsWith(prefix)) {
            return true;
        }
        if (nextHandler != null) {
            return nextHandler.verify(str);
        }
        return false;
    }
}
