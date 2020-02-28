package com.firewolf.lx.log;

import com.firewolf.lx.tools.log.LogEntity;
import com.firewolf.lx.tools.log.LogHandler;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:24
 * 自己的业务日志处理器
 */
@Component
public class MyLogHandler implements LogHandler {
    @Override
    public void handle(LogEntity log) {
        System.out.println("user log :" + log);
    }
}
