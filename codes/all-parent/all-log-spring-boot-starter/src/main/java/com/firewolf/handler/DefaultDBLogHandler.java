package com.firewolf.handler;

import com.firewolf.entity.Log;
import com.firewolf.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultDBLogHandler implements LogHandler<Log> {


    @Autowired
    private LogService logService;

    @Override
    public void handle(Log log) {
        logService.save(log);
    }
}
