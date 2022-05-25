package com.firewolf.pattern.factory_method.simple.logfactory;

import com.firewolf.pattern.factory_method.simple.DBLogger;
import com.firewolf.pattern.factory_method.simple.Logger;
import com.firewolf.pattern.factory_method.simple.LoggerFactory;

/**
 * 描述：具体工厂
 * Author：liuxing
 * Date：2020/5/14
 */
public class DBLoggerFactory extends LoggerFactory {
    @Override
    public Logger createLogger() {
        return new DBLogger();
    }
}
