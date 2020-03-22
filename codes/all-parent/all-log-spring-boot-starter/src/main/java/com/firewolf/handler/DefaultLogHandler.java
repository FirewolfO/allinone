package com.firewolf.handler;

import com.firewolf.entity.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:06
 */
@Slf4j
public class DefaultLogHandler implements LogHandler<Log> {

    @Override
    public void handle(Log opLog) {
        log.info("do nothing");
    }
}
