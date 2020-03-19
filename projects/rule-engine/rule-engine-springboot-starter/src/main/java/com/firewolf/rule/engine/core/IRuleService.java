package com.firewolf.rule.engine.core;

import java.util.List;

/**
 * 规则操作接口接口
 */
public interface IRuleService<R, I> {
    /**
     * 保存规则
     *
     * @param rule
     * @param mainClazz
     * @param subClazz
     */
    void addRule(R rule, Class<?> mainClazz, Class<?> subClazz) throws Exception;

    /**
     * 删除规则
     *
     * @param id        主表id
     * @param mainClazz 主表Class
     * @param subClazz  规则项表Class
     */
    void deleteRule(Object id, Class<?> mainClazz, Class<?> subClazz);

    /**
     * 更新规则
     *
     * @param rule
     */
    void updateRule(R rule, Class<?> mainClazz, Class<?> subClazz) throws Exception;


    /**
     * @param data      查询参数
     * @param mainClazz 主表class
     * @param subClazz  子表class
     * @return
     */
    List<R> searchRules(QueryVO data, Class<?> mainClazz, Class<?> subClazz);
}
