package com.firewolf.lx.log;

import com.firewolf.lx.tools.log.handler.LogHandler;
import com.firewolf.lx.tools.log.LogPO;

/**
 * Author: liuxing
 * Date: 2020/2/28 10:24
 * 把业务日志保存到数据库中
 */
//@Component
public class DBLogHandler implements LogHandler<LogPO> {


    @Override
    public void handle(LogPO log) {
        System.out.println("handle log");

    }
}
