package com.firewolf.pattern.proxy.dynamic;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class Client {

    public static void main(String[] args) {
        FileSercher fileSercher = new FileSercher();
        ProxyFactory proxyFactory = new ProxyFactory(fileSercher);

        Searcher searcher = (Searcher)proxyFactory.getProxyInstance();
        searcher.searchByName("李四");
    }
}
