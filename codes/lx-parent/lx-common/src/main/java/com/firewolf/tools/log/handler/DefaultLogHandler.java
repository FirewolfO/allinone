package com.firewolf.tools.log.handler;

import com.firewolf.tools.log.Log;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:06
 */
public class DefaultLogHandler implements LogHandler<Log> {

    @Override
    public void handle(Log log) {
        System.out.println(log);
    }
}
