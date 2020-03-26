package com.firewolf.common.handler;

import com.firewolf.common.entity.Log;
import com.firewolf.common.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

public class DBLogHandler implements LogHandler<Log> {


    @Autowired
    private LogService logService;

    @Override
    public void handle(Log log) {
        logService.save(log);
    }
}
