package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.LogHandler;
import com.firewolf.lx.tools.log.LogPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Author: liuxing
 * Date: 2020/2/28 16:22
 */
public class DefaultDBLogHandler implements LogHandler {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void handle(LogPO log) {

    }
}
