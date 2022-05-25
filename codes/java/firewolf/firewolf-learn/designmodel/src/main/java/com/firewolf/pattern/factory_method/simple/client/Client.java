package com.firewolf.pattern.factory_method.simple.client;

import com.firewolf.pattern.factory_method.simple.LoggerFactory;
import com.firewolf.pattern.factory_method.simple.logfactory.FileLoggerFactory;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class Client {

    public static void main(String[] args) {
        LoggerFactory loggerFactory = new FileLoggerFactory();
        loggerFactory.writeLog("这是内容");
    }
}
