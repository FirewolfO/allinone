package com.firewolf.pattern.simple_factory.standard;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class ProductFactory {

    public static Product createProduct(String arg) {
        if ("A".equals(arg)) {
            return new ProductA();
        } else if ("B".equals(arg)) {
            return new ProductB();
        } else {
            throw new RuntimeException("参数错误");
        }
    }
}
