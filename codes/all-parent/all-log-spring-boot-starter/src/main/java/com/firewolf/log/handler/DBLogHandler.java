package com.firewolf.log.handler;

import com.firewolf.log.entity.Log;
import com.firewolf.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

public class DBLogHandler implements LogHandler<Log> {


    @Autowired
    private LogService logService;

    @Override
    public void handle(Log log) {
        logService.save(log);
    }
}
