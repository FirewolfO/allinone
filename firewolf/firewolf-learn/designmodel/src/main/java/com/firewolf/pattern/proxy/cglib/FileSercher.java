package com.firewolf.pattern.proxy.cglib;

/**
 * 真实主题
 */
public class FileSercher {
    public String searchByName(String username) {
        System.out.println("search from file");
        return username.toUpperCase() + " has find !";
    }
}