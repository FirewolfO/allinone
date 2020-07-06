package com.firewolf.pattern.proxy.staticproxy;

public class Client {

    public static void main(String[] args) {
        SearcherProxy searcherProxy = new SearcherProxy();
        searcherProxy.searchByName("zhangsan!");
    }
}