package com.firewolf.pattern.factory_method;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/5/14
 */
public class DBLoggerFactory implements LoggerFactory {
    @Override
    public Logger createLogger() {
        return new DBLogger();
    }
}
