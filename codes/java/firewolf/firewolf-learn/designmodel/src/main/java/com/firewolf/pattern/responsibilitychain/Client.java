package com.firewolf.pattern.responsibilitychain;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class Client {
    public static void main(String[] args) {
        StringValidator stringValidator = new StringNotNullValidator(new StringLengthValidator(new PrefixStringValidator(null, "he"), 5));
        boolean result = stringValidator.verify("hello");
        System.out.println(result);
    }
}
