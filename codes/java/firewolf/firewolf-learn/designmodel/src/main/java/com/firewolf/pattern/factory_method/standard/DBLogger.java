package com.firewolf.pattern.factory_method.standard;

/**
 * 描述：具体产品类
 * Author：liuxing
 * Date：2020/5/14
 */
public class DBLogger implements Logger {

    @Override
    public void writeLog(String content) {
        System.out.println("数据库日志：" + content);
    }
}
