package com.firewolf.lx.rule.engine.core;

/**
 * 规则匹配接口
 * R--> 规则类型
 * D--> 业务数据类型
 */
public interface IRuleMatcher<R, D> {

    /**
     * 匹配规则
     *
     * @param rule 规则
     * @param data 业务数据
     * @return
     */
    boolean match(R rule, D data);
}
