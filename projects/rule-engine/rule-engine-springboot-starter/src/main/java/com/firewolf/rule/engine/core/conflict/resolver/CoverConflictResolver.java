package com.firewolf.rule.engine.core.conflict.resolver;


import com.firewolf.rule.engine.core.EntityMetaInfo;
import com.firewolf.rule.engine.utils.sql.SqlBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 覆盖策略，删除旧的，重新插入新的，所以新插入的数据不做任何变化
 */
@ConditionalOnProperty(prefix = "lx.rule", name = "conflict-strategy", havingValue = "cover")
@Component
public class CoverConflictResolver extends AbstractConflictResolver {
    @Override
    public List beforeSub(EntityMetaInfo mainEntityMetaInfo, EntityMetaInfo subMetaInfo, List data) throws Exception {

        if (CollectionUtils.isNotEmpty(ruleProperties.getUniqueColumns())) {
            // 只有存在规则唯一约束的时候，才有可能产生冲突
            // 删除旧的规则项数据
            Map<String, Object> params = new HashMap<>();
            for (String column : ruleProperties.getUniqueColumns()) {
                Field field = subMetaInfo.getColumnFieldMap().get(column);
                if (!params.containsKey(column)) {
                    params.put(column, new HashSet());
                }
                for (int i = 0; i < data.size(); i++) {
                    ((Set) params.get(column)).add(field.get(data.get(i)));
                }
            }
            String delSql = SqlBuilder.buildDeleteSql(subMetaInfo.getTable(), params);
            namedParameterJdbcTemplate.update(delSql, params);
        }
        return data;
    }

    @Override
    public void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception {
        // 如果存在主子表结构，删除主表无用的规则数据
        if (!mainMetaInfo.getTable().equals(subMetaInfo.getTable())) {
            String mainDelSql = SqlBuilder.buildUnusedDeleteSql(mainMetaInfo, subMetaInfo);
            namedParameterJdbcTemplate.update(mainDelSql, new HashMap<>());
        }
    }
}