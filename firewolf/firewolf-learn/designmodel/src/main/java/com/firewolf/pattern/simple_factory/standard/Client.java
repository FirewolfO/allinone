package com.firewolf.pattern.simple_factory.standard;

/**
 * 描述：调用客户端
 * Author：liuxing
 * Date：2020/5/14
 */
public class Client {

    public static void main(String[] args) {
        Product product = ProductFactory.createProduct("A");
        System.out.println(product.getName());
    }
}
