package com.firewolf.pattern.proxy.cglib;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/7/6
 */
public class Client {

    public static void main(String[] args) {
        FileSercher fileSercher = new FileSercher();

        FileSercher proxy = (FileSercher) new ProxyFactory(fileSercher).getProxyInstance();
        String result = proxy.searchByName("王五");
        System.out.println(result);
    }
}
