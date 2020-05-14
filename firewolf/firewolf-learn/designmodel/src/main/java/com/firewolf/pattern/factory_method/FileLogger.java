package com.firewolf.pattern.factory_method;

/**
 * 描述：具体产品类
 * Author：liuxing
 * Date：2020/5/14
 */
public class FileLogger implements Logger {
    @Override
    public void writeLog(String content) {
        System.out.println("文件日志：" + content);
    }
}
