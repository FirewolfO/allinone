package com.firewolf.pattern.factory_method.simple.logfactory;

import com.firewolf.pattern.factory_method.simple.FileLogger;
import com.firewolf.pattern.factory_method.simple.Logger;
import com.firewolf.pattern.factory_method.simple.LoggerFactory;

/**
 * 描述：具体工厂
 * Author：liuxing
 * Date：2020/5/14
 */
public class FileLoggerFactory extends LoggerFactory {
    @Override
    protected Logger createLogger() {
        return new FileLogger();
    }
}
