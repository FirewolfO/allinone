package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.LogPO;
import com.firewolf.lx.tools.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:22
 */
public class DefaultDBLogHandler implements LogHandler<LogPO> {


    @Autowired
    private LogService logService;

    @Override
    public LogPO transLog2SelfObj(LogPO logPO) {
        return logPO;
    }

    @Override
    public void handle(LogPO log) {
        logService.save(log);
    }
}
