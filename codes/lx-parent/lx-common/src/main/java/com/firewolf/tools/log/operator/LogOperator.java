package com.firewolf.tools.log.operator;

/**
 * Author: liuxing
 * Date: 2020/2/28 12:14
 * 获取操作者接口
 */
public interface LogOperator {

    /**
     * 返回当前操作的用户
     * @return
     */
    String getOperator();
}
