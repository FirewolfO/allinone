package com.firewolf.lx.tools.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:06
 */
@Component
@ConditionalOnProperty(name = "log.handler.default.enable", havingValue = "true")
public class DefaultLogHandler implements LogHandler {

    @Override
    public void handle(LogEntity log) {
        System.out.println(log);
    }
}
