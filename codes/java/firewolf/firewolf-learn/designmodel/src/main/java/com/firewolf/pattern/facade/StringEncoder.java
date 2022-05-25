package com.firewolf.pattern.facade;

/**
 * 描述：业务子系统
 * Author：liuxing
 * Date：2020/7/3
 */
public class StringEncoder {

    public String encode(String content){
        System.out.println("encode string , str = "+content);
        return content.toUpperCase();
    }
}
