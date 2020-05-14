package com.firewolf.pattern.factory_method;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class Client {

    public static void main(String[] args) {

        // 可以通过XML文件来更改具体的工厂类
        LoggerFactory loggerFactory = new FileLoggerFactory();

        Logger logger = loggerFactory.createLogger();

        logger.writeLog("这是内容");
    }
}
