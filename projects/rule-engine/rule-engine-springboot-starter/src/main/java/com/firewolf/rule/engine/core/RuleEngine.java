package com.firewolf.rule.engine.core;

import com.firewolf.rule.engine.config.RuleProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则引擎
 * R --> 规则类型
 * I --> 规则项类型
 * D --> 进行规则匹配的数据类型
 * 如果没有主子表结构，需要传入一样的类型，请务必传入泛型，如果规则信息放在了单表中，请传入一样的类型
 */
public class RuleEngine<R, I, D> {

    @Autowired
    private RuleProperties ruleProperties;

    @Autowired
    private IRuleService<R, I> iRuleService;

    @Autowired
    private List<IRuleMatcher<R, D>> ruleMatchers;

    /**
     * 添加规则，如果自己实现了RuleEngine，调用这个方法
     *
     * @param rule 规则实体，规则项保存在里面的
     */
    public void addRule(R rule) throws Exception {
        addRule(rule, getSubClazz());
    }

    /**
     * 添加规则，如果是直接注入了系统提供的RuleEngine，需要调用这个参数
     *
     * @param rule     规则实体
     * @param subClazz 规则项类型
     * @throws Exception
     */
    public void addRule(R rule, Class<?> subClazz) throws Exception {
        iRuleService.addRule(rule, rule.getClass(), subClazz);
    }

    /**
     * 更新规则,如果自己实现了RuleEngine，调用这个方法
     *
     * @param rule 规则实体
     */
    public void updateRule(R rule) throws Exception {
        updateRule(rule, getSubClazz());
    }

    /**
     * 更新规则，如果是直接注入了系统提供的RuleEngine，需要调用这个参数
     *
     * @param rule
     * @param subClazz
     */
    public void updateRule(R rule, Class<?> subClazz) throws Exception {
        iRuleService.updateRule(rule, rule.getClass(), subClazz);
    }


    /**
     * 匹配规则，如果自己实现了RuleEngine，调用这个方法
     *
     * @param searcher 子表搜索条件，要求是子表类型
     * @param data     要用来匹配的数据
     * @return
     */
    public List<R> matchRules(QueryVO searcher, D data) {
        return matchRules(searcher, getMainClazz(), getSubClazz(), data);
    }


    /**
     * 查找规则,如果自己实现了RuleEngine，调用这个方法
     *
     * @return
     */
    public List<R> findRules(QueryVO queryVO) {
        return iRuleService.searchRules(queryVO, getMainClazz(), getSubClazz());
    }

    /**
     * 查找规则,如果是直接注入了系统提供的RuleEngine，需要调用这个参数
     *
     * @return
     */
    public List<R> findRules(QueryVO queryVO, Class<?> mainClazz, Class<?> subClazz) {
        return findRules(queryVO, mainClazz, subClazz);
    }

    /**
     * 匹配规则，如果是直接注入了系统提供的RuleEngine，需要调用这个参数
     *
     * @param searcher  搜索条件
     * @param mainClazz 规则主表类
     * @param subClazz  子表
     * @param data      要用来匹配的数据
     * @return 返回满足条件的规则信息
     */
    public List<R> matchRules(QueryVO searcher, Class<?> mainClazz, Class<?> subClazz, D data) {
        List<R> rules = iRuleService.searchRules(searcher, mainClazz, subClazz);
        if (CollectionUtils.isNotEmpty(rules)) {
            return rules.stream().filter(rule -> {
                        if ("or".equals(ruleProperties.getMatchType())) {
                            return ruleMatchers.stream().anyMatch(ruleMatcher -> ruleMatcher.match(rule, data));
                        } else {
                            return ruleMatchers.stream().allMatch(ruleMatcher -> ruleMatcher.match(rule, data));
                        }
                    }
            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * 删除规则，如果自己实现了RuleEngine，调用这个方法
     *
     * @param id
     */
    public void deleteRule(Object id) {
        deleteRule(id, getMainClazz(), getSubClazz());
    }

    /**
     * 删除规则，如果是直接注入了系统提供的RuleEngine，需要调用这个参数
     *
     * @param id
     * @param ruleClazz
     */
    public void deleteRule(Object id, Class<?> ruleClazz, Class<?> ruleItemClazz) {
        iRuleService.deleteRule(id, ruleClazz, ruleItemClazz);
    }


    private Class<?> mainClazz = null;
    private Class<?> subClazz = null;

    /**
     * 获取主表对应的class
     *
     * @return
     */
    private Class<?> getMainClazz() {
        if (mainClazz == null) {
            try {
                ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
                String typeName = genericSuperclass.getActualTypeArguments()[0].getTypeName();
                mainClazz = Class.forName(typeName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return mainClazz;
    }

    /**
     * 获取子表对应的class
     *
     * @return
     */
    private Class<?> getSubClazz() {
        if (subClazz == null) {
            try {
                ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
                String typeName = genericSuperclass.getActualTypeArguments()[1].getTypeName();
                subClazz = Class.forName(typeName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subClazz;
    }


}
