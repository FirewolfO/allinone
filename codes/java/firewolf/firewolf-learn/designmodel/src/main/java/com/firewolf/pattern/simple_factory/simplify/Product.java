package com.firewolf.pattern.simple_factory.simplify;

/**
 * 描述：抽象产品类
 * Author：liuxing
 * Date：2020/5/14
 */
public class Product {

    /**
     * 需要子类重写的方法
     * @return
     */
    protected String getName() {
        return null;
    }


    /**
     *  工厂方法
     * @param arg
     * @return
     */
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
