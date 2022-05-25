package com.firewolf.pattern.responsibilitychain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class StringLengthValidator extends StringValidator {

    private int length;

    public StringLengthValidator(StringValidator stringValidator, int length) {
        super(stringValidator);
        this.length = length;
    }

    @Override
    public boolean verify(String str) {
        System.out.println("verify length");
        if (str.length() != length) {
            return false;
        }
        if (nextHandler != null) {
            return nextHandler.verify(str);
        }
        return false;
    }
}
