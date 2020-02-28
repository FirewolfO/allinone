package com.firewolf.lx.log;

import com.firewolf.lx.tools.log.LogHandler;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.stereotype.Component;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:24
 * 把业务日志保存到数据库中
 */
@Component
public class DBLogHandler implements LogHandler {


    @Override
    public void handle(LogPO log) {
        System.out.println("handle log");

    }
}
