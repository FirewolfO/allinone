package com.firewolf.lx.tools.log;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:34
 */
public interface LogHandler {
    /**
     * 对日志的处理
     * @param log
     */
    void handle(LogEntity log);
}
