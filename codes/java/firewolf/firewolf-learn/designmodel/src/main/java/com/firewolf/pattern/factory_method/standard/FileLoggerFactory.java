package com.firewolf.pattern.factory_method.standard;

/**
 * 描述：具体工厂
 * Author：liuxing
 * Date：2020/5/14
 */
public class FileLoggerFactory implements LoggerFactory{
    @Override
    public Logger createLogger() {
        return new FileLogger();
    }
}
