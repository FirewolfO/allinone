package com.firewolf.pattern.responsibilitychain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class StringNotNullValidator extends StringValidator {

    public StringNotNullValidator(StringValidator validator) {
        super(validator);
    }

    @Override
    public boolean verify(String str) {
        System.out.println("verify not null");
        if (str == null) {
            return false;
        }
        if (nextHandler != null) {
            return nextHandler.verify(str);
        }
        return false;
    }
}
