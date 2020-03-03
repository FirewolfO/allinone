package com.firewolf.lx.tools.log.handler;

import com.firewolf.lx.tools.log.Log;

/**
 * Author: liuxing
 * Date: 2020/2/28 9:34\
 * 日志处理器，用来保存产生的日志
 */
public interface LogHandler<T> {

    /**
     * 把日志对象转换成自己需要的类型
     *
     * @param logPO
     * @return
     */
    default T transLog2SelfObj(Log logPO) {
        return null;
    }

    /**
     * 对日志的处理
     *
     * @param log 日志对象，默认为LogPO，但是用户可以根据自己的需求进行属性转换
     */
    void handle(T log);
}
