package com.firewolf.pattern.proxy.staticproxy;

/**
 * 代理主题
 */
public class SearcherProxy implements Searcher {

    private FileSercher fileSercher = new FileSercher();


    @Override
    public String searchByName(String username) {
        // 1. 验证
        if (username == null) {
            throw new RuntimeException("username can't be null !");
        }
        // 2.记录日志
        System.out.println("start search");

        // 调用真实方法
        String result = fileSercher.searchByName(username);

        System.out.println("finish search, result = " + result);
        return result;
    }
}