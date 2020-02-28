package com.firewolf.lx.tools.log;

/**
 * Author: liuxing
 * Date: 2020/2/28 12:17
 */

public class DefaultLogOperator implements LogOperator{

    public DefaultLogOperator() {
        System.out.println("DefaultLogOperator");
    }
    @Override
    public String getOperator() {
        return null;
    }
}
