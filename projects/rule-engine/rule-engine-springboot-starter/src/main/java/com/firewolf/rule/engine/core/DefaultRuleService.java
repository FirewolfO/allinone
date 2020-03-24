package com.firewolf.rule.engine.core;

import com.firewolf.rule.engine.core.conflict.resolver.AbstractConflictResolver;
import com.firewolf.rule.engine.utils.BeanUtil;
import com.firewolf.rule.engine.utils.sql.Limit;
import com.firewolf.rule.engine.utils.sql.SqlBuildParam;
import com.firewolf.rule.engine.utils.sql.SqlBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.firewolf.rule.engine.utils.MetaInfoUtil.*;

/**
 * 默认规则服务
 */
public class DefaultRuleService<R, I> implements IRuleService<R, I> {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private AbstractConflictResolver conflictStrategy;

    @Override
    @Transactional
    public void addRule(R rule, Class<?> mainClazz, Class<?> subClazz) throws Exception {

        // 获取表名
        // 插入主表数据
        EntityMetaInfo metaInfo = getMetaInfo(mainClazz);
        Object id = insertAndReturnKey(metaInfo, rule, null);

        // 如果有子表，插入子表数据
        Field itemField = metaInfo.getItemField();
        if (itemField != null) {
            EntityMetaInfo subMetaInfo = getMetaInfo(subClazz);
            List data = BeanUtil.transObj2List(itemField.get(rule));
            insertRuleItems(metaInfo, subMetaInfo, data, id);
        }

    }


    @Override
    public List<R> queryRules(QueryVO data, Class<?> mainClazz, Class<?> subClazz) {
        Map<String, Object> mainParams = data.getMainParams();
        Map<String, Object> subParams = data.getSubParams();

        EntityMetaInfo mainMetaInfo = getMetaInfo(mainClazz);
        EntityMetaInfo subMetaInfo = getMetaInfo(subClazz);

        // 将字段转成属性名
        removeNullParams(mainParams, mainMetaInfo);
        removeNullParams(subParams, subMetaInfo);

        // 没有主子表结构，直接从一张表查出即可
        if (mainClazz == subClazz) {
            mainParams.putAll(subParams);
            return findList(mainClazz, mainParams, data.getStart(), data.getPageSize());
        }

        try {
            // 有主表的查询参数，或者没有主表的查询参数
            if (MapUtils.isNotEmpty(mainParams) || MapUtils.isEmpty(subParams)) {
                // 查询主表数据
                List<R> mainData = findList(mainClazz, mainParams, data.getStart(), data.getPageSize());
                if (CollectionUtils.isEmpty(mainData))
                    return mainData;
                Field mainIdField = mainMetaInfo.getColumnFieldMap().get(mainMetaInfo.getIdColumn());
                Map<Object, Object> map = new HashMap<>();
                for (int i = 0; i < mainData.size(); i++) {
                    map.put(mainIdField.get(mainData.get(i)), mainData.get(i));
                }

                // 查询子表数据
                String foreignKey = subMetaInfo.getForeignKeyColumn();
                if (StringUtils.isNotEmpty(foreignKey)) {
                    subParams.put(subMetaInfo.getForeignKeyColumn(), map.keySet());
                    Field foreignField = subMetaInfo.getColumnFieldMap().get(foreignKey);
                    List subData = findList(subClazz, subParams, -1, 0);
                    Map subDataMap = (Map) subData.stream().collect(Collectors.groupingBy(o -> {
                        try {
                            return foreignField.get(o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }));
                    Field itemField = mainMetaInfo.getItemField();
                    subDataMap.keySet().forEach(key -> {
                        Object o = map.get(key);
                        try {
                            itemField.set(o, subDataMap.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                    return mainData;
                }
            } else {
                List list = findList(subClazz, subParams, -1, 0);
                Map<String, Field> subColumnFieldMap = subMetaInfo.getColumnFieldMap();
                Field foreignField = subColumnFieldMap.get(subMetaInfo.getForeignKeyColumn());
                // 根据外键分组，到时候放到规则主表的属性里面
                Map maps = (Map) list.stream().collect(Collectors.groupingBy(x -> {
                    try {
                        return foreignField.get(x);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }));

                Set ids = maps.keySet();
                mainParams.put(mainMetaInfo.getIdColumn(), ids);

                List resutList = findList(mainClazz, mainParams, data.getStart(), data.getPageSize());
                Field itemField = mainMetaInfo.getItemField();
                Field idField = mainMetaInfo.getColumnFieldMap().get(mainMetaInfo.getIdColumn());
                for (int i = 0; i < resutList.size(); i++) {
                    Object o = resutList.get(i);
                    itemField.set(o, maps.get(idField.get(o)));
                }
                return resutList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public R checkConflictRule(R rule, Class<?> mainClazz, Class<?> subClazz, List<String> uniqueColumns) {
        if (rule == null)
            return null;
        // 非主子表结构
        try {
            EntityMetaInfo metaInfo = getMetaInfo(mainClazz);
            if (mainClazz == subClazz) {
                String mainSql = SqlBuilder.buildUniqueQuerySql(metaInfo.getTable(), uniqueColumns, 1);
                Map<String, Object> objValues = objectToMap(rule);
                Map<String, Object> params = new HashMap<>();
                for (String column : uniqueColumns) {
                    params.put(column + 0, objValues.get(column));
                }
                List<String> conflictUniqueKeys = namedParameterJdbcTemplate.query(mainSql, params, (resultSet, i) -> resultSet.getString(1));
                if (CollectionUtils.isNotEmpty(conflictUniqueKeys)) {
                    return rule;
                }
                return null;
            } else {
                return filterRule(rule, metaInfo.getItemFieldName(), subClazz, uniqueColumns, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }


    /**
     * 过滤规则
     *
     * @param rule          规则
     * @param itemFieldName 规则项数据
     * @param exists        true 返回已经存在的，false返回不存在的
     * @return
     */
    private R filterRule(R rule, String itemFieldName, Class<?> subClazz, List<String> uniqueColumns, boolean exists) {

        // 主子表结构，只看子表的唯一性
        EntityMetaInfo subMetaInfo = getMetaInfo(subClazz);
        // 获取子表数据集合
        Object data = getObjValue(rule, itemFieldName);
        List items = BeanUtil.transObj2List(data);
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        //查询冲突项
        String sql = SqlBuilder.buildUniqueQuerySql(subMetaInfo.getTable(), uniqueColumns, items.size());
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            Map<String, Object> objValues = objectToMap(item);
            for (String column : uniqueColumns) {
                params.put(column + i, objValues.get(column));
            }
        }

        // 默认认为所有的子表数据项都冲突了
        Object conflictItems = data;
        List<Integer> conflictCounts = new ArrayList<>();
        List<String> conflictUniqueKeys = namedParameterJdbcTemplate.query(sql, params, (resultSet, i) -> resultSet.getString(1));
        if (CollectionUtils.isNotEmpty(conflictUniqueKeys)) {
            Stream stream = items.stream().filter(item -> {
                Map<String, Object> objectValues = objectToMap(item);
                //计算唯一key
                String key = uniqueColumns.stream().map(column -> {
                    try {
                        Object o = objectValues.get(column);
                        return o == null ? "" : o.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }).collect(Collectors.joining(","));

                boolean contains = conflictUniqueKeys.contains(key);
                if (contains) {
                    conflictCounts.add(1);
                    return exists;
                }
                return !exists;
            });
            if (data instanceof Set) {
                conflictItems = stream.collect(Collectors.toSet());
            } else if (data instanceof List) {
                conflictItems = stream.collect(Collectors.toList());
            } else {
                conflictItems = stream.toArray();
            }
            // 有冲突的
            if (conflictCounts.size() > 0) {
                setObjValue(rule, itemFieldName, conflictItems);
                return rule;
            }
            return null;
        }
        return null;
    }


    /**
     * 移除空参数，同时把字段转成属性
     *
     * @param params
     * @param mainMetaInfo
     */
    private void removeNullParams(Map<String, Object> params, EntityMetaInfo mainMetaInfo) {

        List<String> toDel = new ArrayList<>();
        Map<String, Object> toAdd = new HashMap<>();
        Map<String, String> filedNameColumnMap = mainMetaInfo.getFiledNameColumnMap();
        params.entrySet().forEach(entry -> {
            String key = entry.getKey();
            if (entry.getValue() == null) {
                toDel.add(key);
                return;
            }
            if (!StringUtils.equals(filedNameColumnMap.get(key), key)) {
                toDel.add(key);
                toAdd.put(filedNameColumnMap.get(key), entry.getValue());
            }
        });
        if (CollectionUtils.isNotEmpty(toDel)) {
            toDel.forEach(key -> params.remove(key));
            params.putAll(toAdd);
        }
    }

    /**
     * 查询表数据
     *
     * @param clazz
     * @param params
     * @return
     */
    private List findList(Class<?> clazz, Map<String, Object> params, int start, int size) {
        EntityMetaInfo metaInfo = getMetaInfo(clazz);
        Map<String, Field> columnFieldMap = metaInfo.getColumnFieldMap();
        SqlBuildParam sqlBuildParam = SqlBuildParam.builder()
                .table(metaInfo.getTable())
                .limit(Limit.builder().start(start).size(size).build())
                .params(params).like(metaInfo.getLikeColumnMap())
                .orderBy(metaInfo.getOrderColumnMap())
                .build();
        String sql = SqlBuilder.buildQuerySql(sqlBuildParam);
        return namedParameterJdbcTemplate.query(sql, params, (resultSet, rowNumber) -> {
            Object o = null;
            try {
                o = clazz.newInstance();
                for (Map.Entry<String, Field> entry : columnFieldMap.entrySet()) {
                    Field field = entry.getValue();
                    field.set(o, resultSet.getObject(entry.getKey()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return o;
        });
    }

    @Override
    @Transactional
    public void deleteRule(Object id, Class<?> ruleClazz, Class<?> ruleItemClazz) {

        // 有子表，删除子表数据
        if (ruleClazz != ruleItemClazz) {
            EntityMetaInfo subMetaInfo = getMetaInfo(ruleItemClazz);
            Map<String, Object> params = new HashMap<>();
            params.put(subMetaInfo.getForeignKeyColumn(), id);

            String subDeleteSql = SqlBuilder.buildDeleteSql(subMetaInfo.getTable(), params);
            namedParameterJdbcTemplate.update(subDeleteSql, params);
        }
        //删除主表数据
        EntityMetaInfo mainMetaInfo = getMetaInfo(ruleClazz);
        Map<String, Object> params = new HashMap<>();
        params.put(mainMetaInfo.getIdColumn(), id);

        String mainDeleteSql = SqlBuilder.buildDeleteSql(mainMetaInfo.getTable(), params);
        namedParameterJdbcTemplate.update(mainDeleteSql, params);
    }

    @Override
    @Transactional
    public void updateRule(R rule, Class<?> ruleClazz, Class<?> ruleItemClazz) throws Exception {
        //更新主表
        EntityMetaInfo mainMetaInfo = getMetaInfo(ruleClazz);
        Map<String, Object> mainUpdateParams = new HashMap<>();
        Map<String, Field> columnFieldMap = mainMetaInfo.getColumnFieldMap();
        Object idValue = columnFieldMap.get(mainMetaInfo.getIdColumn()).get(rule);
        columnFieldMap.entrySet().forEach(entry -> {
            if (!mainMetaInfo.getIdColumn().equals(entry.getKey())) {
                try {
                    mainUpdateParams.put(entry.getKey(), entry.getValue().get(rule));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        // 更新规则数据
        Map<String, Object> mainWhereParams = new HashMap<>();
        mainWhereParams.put(mainMetaInfo.getIdColumn(), mainMetaInfo.getColumnFieldMap().get(mainMetaInfo.getIdColumn()).get(rule));
        String mainUpdateSql = SqlBuilder.buildUpdateSql(mainMetaInfo.getTable(), mainUpdateParams, mainWhereParams);
        mainUpdateParams.putAll(mainWhereParams);
        namedParameterJdbcTemplate.update(mainUpdateSql, mainUpdateParams);

        // 如果有子表处理子表：
        Field itemField = mainMetaInfo.getItemField();
        if (itemField != null) {
            // 删除子表旧数据
            EntityMetaInfo subMetaInfo = getMetaInfo(ruleItemClazz);
            Map<String, Object> deleteParams = new HashMap<>();
            deleteParams.put(subMetaInfo.getForeignKeyColumn(), idValue);
            String deletSql = SqlBuilder.buildDeleteSql(subMetaInfo.getTable(), deleteParams);
            namedParameterJdbcTemplate.update(deletSql, deleteParams);

            List data = BeanUtil.transObj2List(itemField.get(rule));
            insertRuleItems(mainMetaInfo, subMetaInfo, data, idValue);

        }
    }


    /**
     * 插入规则项,需要处理冲突数据，以及插入规则项数据
     *
     * @param mainMetaInfo 主表元数据信息
     * @param subMetaInfo  子表元数据信息
     * @param foreignValue 子表中外键数据
     */
    private void insertRuleItems(EntityMetaInfo mainMetaInfo, EntityMetaInfo subMetaInfo, List data, Object foreignValue) throws Exception {
        // 插入前冲突处理
        List toDealData = conflictStrategy.beforeSub(mainMetaInfo, subMetaInfo, data);
        if (CollectionUtils.isNotEmpty(toDealData)) {
            insertList(subMetaInfo, data, foreignValue);
        }
        conflictStrategy.afterSub(mainMetaInfo, subMetaInfo);
    }


    /**
     * 往表中插入一条数据，返回生成的主键
     *
     * @param metaInfo     要插入的表对应的类的元数据信息
     * @param data         要插入的数据
     * @param foreignValue 外键数据
     * @throws IllegalAccessException
     */
    private Object insertAndReturnKey(EntityMetaInfo metaInfo, Object data, Object foreignValue) throws Exception {
        Map<String, Object> m = new HashMap<>();
        Map<String, Field> columnFieldMap = metaInfo.getColumnFieldMap();
        for (Map.Entry<String, Field> entry : columnFieldMap.entrySet()) {
            if (!StringUtils.equals(metaInfo.getIdColumn(), entry.getKey())) {
                m.put(entry.getKey(), entry.getValue().get(data));
            }
        }
        if (foreignValue != null) { // 如果有外键数据，设置外键数据
            m.put(metaInfo.getForeignKeyColumn(), foreignValue);
        }
        String insertSql = SqlBuilder.buildInsertSql(metaInfo.getTable(), m);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(insertSql, new SqlParameterSource() {
            @Override
            public boolean hasValue(String s) {
                return m.containsKey(s);
            }

            @Override
            public Object getValue(String s) throws IllegalArgumentException {
                return m.get(s);
            }
        }, keyHolder);
        return keyHolder.getKey();
    }


    /**
     * 往表中插入一批数据
     *
     * @param metaInfo     要插入的表对应的类的元数据信息
     * @param data         要插入的数据
     * @param foreignValue 外键数据
     * @throws IllegalAccessException
     */
    private void insertList(EntityMetaInfo metaInfo, List data, Object foreignValue) throws Exception {
        if (CollectionUtils.isEmpty(data))
            return;
        Map<String, Object>[] insertParams = new Map[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> m = new HashMap<>();
            Object o = data.get(i);
            Map<String, Field> subColumnFieldMap = metaInfo.getColumnFieldMap();
            subColumnFieldMap.remove(metaInfo.getIdColumn());
            for (Map.Entry<String, Field> entry : subColumnFieldMap.entrySet()) {
                m.put(entry.getKey(), entry.getValue().get(o));
            }
            if (foreignValue != null) { // 如果有外键数据，设置外键数据
                m.put(metaInfo.getForeignKeyColumn(), foreignValue);
            }
            insertParams[i] = m;
        }
        String insertSql = SqlBuilder.buildInsertSql(metaInfo.getTable(), insertParams[0]);
        namedParameterJdbcTemplate.batchUpdate(insertSql, insertParams);
    }
}


