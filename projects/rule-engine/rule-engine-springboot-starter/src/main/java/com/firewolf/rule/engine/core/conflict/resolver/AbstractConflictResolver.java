package com.firewolf.rule.engine.core.conflict.resolver;

import com.firewolf.rule.engine.config.properties.RuleProperties;
import com.firewolf.rule.engine.entity.EntityMetaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public abstract class AbstractConflictResolver {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    protected RuleProperties ruleProperties;

    /**
     * 子表中插入数据之前
     *
     * @param mainEntityMetaInfo 主表元数据信息
     * @param subMetaInfo        子表元数据信息
     * @param data               子表要插入的数据
     * @param conflictItem       冲突项
     * @param notConflictItem    非冲突项
     * @return
     */
    public abstract List beforeSub(EntityMetaInfo mainEntityMetaInfo, EntityMetaInfo subMetaInfo, List data, List conflictItem, List notConflictItem) throws Exception;

    /**
     * 子表中插入数据之后
     *
     * @param mainMetaInfo 主表元数据信息
     * @param subMetaInfo  从表元数据信息
     * @return
     */
    public abstract void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception;
}
