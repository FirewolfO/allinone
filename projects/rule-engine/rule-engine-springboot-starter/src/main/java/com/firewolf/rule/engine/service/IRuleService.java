package com.firewolf.rule.engine.service;

import com.firewolf.rule.engine.entity.RuleQuery;

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
    List<R> queryRules(RuleQuery data, Class<?> mainClazz, Class<?> subClazz);

    /**
     * 检测冲突了的规则,没有冲突返回null
     *
     * @param rule
     * @param mainClazz     主表class
     * @param subClazz      子表Class
     * @param uniqueColumns 规则唯一标志
     * @return 没有冲突返回null
     */
    R checkConflictRule(R rule, Class<?> mainClazz, Class<?> subClazz, List<String> uniqueColumns);
}
