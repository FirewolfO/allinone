package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.Log;
import com.firewolf.lx.tools.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:22
 */
public class DefaultDBLogHandler implements LogHandler<Log> {


    @Autowired
    private LogService logService;

    @Override
    public void handle(Log log) {
        logService.save(log);
    }
}
