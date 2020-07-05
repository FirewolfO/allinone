package com.firewolf.pattern.facade;

/**
 * Description:
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/4 8:31 下午
 */
public class Client {
    public static void main(String[] args) {
        FileEncoderFacade fileEncoder = new FileEncoderFacade();
        fileEncoder.encodeFile("aaa.txt","bbb.txt");
    }
}
