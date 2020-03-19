package com.firewolf.lx.rule.engine.core.conflict.resolver;

import com.firewolf.lx.rule.engine.core.EntityMetaInfo;
import com.firewolf.lx.rule.engine.utils.sql.SqlBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@ConditionalOnProperty(prefix = "lx.rule", name = "conflict-strategy", havingValue = "discard")
@Component
/**
 * 丢弃策略，保留旧的
 */
public class DiscardConflictResolver extends AbstractConflictResolver {
    @Override
    public List beforeSub(EntityMetaInfo mainEntityMetaInfo, EntityMetaInfo subMetaInfo, List data)  throws Exception{
        Class<?> aClass = data.get(0).getClass();
        String sql = SqlBuilder.buildUniqueCountSql(subMetaInfo, ruleProperties.getUniqueColumns());
        Map<String, Set> params = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        try {
            for (String column : ruleProperties.getUniqueColumns()) {
                Field field = aClass.getDeclaredField(subMetaInfo.getColumnFieldNameMap().get(column));
                field.setAccessible(true);
                if (!params.containsKey(column)) {
                    params.put(column, new HashSet());
                }
                for (int i = 0; i < data.size(); i++) {
                    params.get(column).add(field.get(data.get(i)));
                }
                fields.add(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> countResult = namedParameterJdbcTemplate.query(sql, params, (resultSet, i) -> resultSet.getInt(1) > 0 ? resultSet.getString(2) : null);

        List resultData = (List) data.stream().filter(x -> {
            try {
                String values = fields.stream().map(field -> {
                    try {
                        return field.get(x).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.joining(","));
                values += ",";
                return !countResult.contains(values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        return resultData;
    }

    @Override
    public void afterSub(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo) throws Exception {


    }
}
