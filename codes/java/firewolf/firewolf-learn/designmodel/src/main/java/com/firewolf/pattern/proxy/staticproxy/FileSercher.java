package com.firewolf.pattern.proxy.staticproxy;

/**
 *真实主题
 */
public class FileSercher implements Searcher {
    @Override
    public String searchByName(String username) {
        System.out.println("search from file");
        return username.toUpperCase() + " has find !";
    }
}