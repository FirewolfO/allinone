package com.firewolf.pattern.abstract_factory;

/**
 * 描述：客户端
 * Author：liuxing
 * Date：2020/5/15
 */
public class Client {

    public static void main(String[] args) {
        SkinFactory skinFactory = new SpringSkinFactory();

        Button button = skinFactory.createButton();

        button.displayButton();
    }
}
