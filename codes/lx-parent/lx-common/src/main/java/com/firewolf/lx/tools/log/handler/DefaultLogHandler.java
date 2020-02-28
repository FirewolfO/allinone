package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.LogHandler;
import com.firewolf.lx.tools.log.LogPO;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:06
 */
public class DefaultLogHandler implements LogHandler {

    @Override
    public void handle(LogPO log) {
        System.out.println(log);
    }
}
