package com.firewolf.rule.engine.core;

import com.firewolf.rule.engine.config.RuleProperties;
import com.firewolf.rule.engine.core.conflict.resolver.AbstractConflictResolver;
import com.firewolf.rule.engine.utils.BeanUtil;
import com.firewolf.rule.engine.utils.MetaInfoUtil;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.firewolf.rule.engine.utils.MetaInfoUtil.*;

/**
 * 默认规则服务
 */
public class DefaultRuleService<R, I> implements IRuleService<R, I> {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private AbstractConflictResolver conflictStrategy;

    @Autowired
    private RuleProperties ruleProperties;

    @Override
    @Transactional
    public void addRule(R rule, Class<?> mainClazz, Class<?> subClazz) throws Exception {

        // 获取表名
        // 插入主表数据
        EntityMetaInfo metaInfo = getMetaInfo(mainClazz);
        Object id = insertAndReturnKey(metaInfo, rule, null);

        // 如果有子表，插入子表数据
        String itemFieldName = metaInfo.getItemFieldName();
        if (StringUtils.isNotEmpty(itemFieldName)) {
            EntityMetaInfo subMetaInfo = getMetaInfo(subClazz);
            List data = BeanUtil.transObj2List(getObjValue(rule, itemFieldName));
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
            // 只有子表查询参数
            if (MapUtils.isEmpty(mainParams) && MapUtils.isNotEmpty(subParams)) {
                List list = findList(subClazz, subParams, -1, 0);
                String foreignKeyFieldName = subMetaInfo.getColumnFieldNameMap().get(subMetaInfo.getForeignKeyColumn());
                // 根据外键分组，到时候放到规则主表的属性里面
                Map maps = (Map) list.stream().collect(Collectors.groupingBy(x -> getObjValue(x, foreignKeyFieldName)));

                Set ids = maps.keySet();
                mainParams.put(mainMetaInfo.getIdColumn(), ids);

                List resutList = findList(mainClazz, mainParams, data.getStart(), data.getPageSize());

                String itemField = mainMetaInfo.getItemFieldName();
                String idFieldName = mainMetaInfo.getColumnFieldNameMap().get(mainMetaInfo.getIdColumn());
                for (int i = 0; i < resutList.size(); i++) {
                    Object o = resutList.get(i);
                    setObjValue(o, itemField, maps.get(getObjValue(o, idFieldName)));
                }
                return resutList;
            } else {
                // 查询主表数据
                List<R> mainData = findList(mainClazz, mainParams, data.getStart(), data.getPageSize());
                if (CollectionUtils.isEmpty(mainData))
                    return mainData;
                String idFieldName = mainMetaInfo.getColumnFieldNameMap().get(mainMetaInfo.getIdColumn());
                Map<Object, Object> map = new HashMap<>();
                for (int i = 0; i < mainData.size(); i++) {
                    map.put(getObjValue(mainData.get(i), idFieldName), mainData.get(i));
                }

                // 查询子表数据
                String foreignKey = subMetaInfo.getForeignKeyColumn();
                if (StringUtils.isNotEmpty(foreignKey)) {
                    subParams.put(foreignKey, map.keySet()); // 根据外键查询
                    List subData = findList(subClazz, subParams, -1, 0);
                    String foreignKeyFieldName = subMetaInfo.getColumnFieldNameMap().get(foreignKey);
                    Map subDataMap = (Map) subData.stream().collect(Collectors.groupingBy(o -> getObjValue(o, foreignKeyFieldName)));
                    subDataMap.keySet().forEach(key -> {
                        Object o = map.get(key);
                        setObjValue(o, mainMetaInfo.getItemFieldName(), subDataMap.get(key));
                    });
                    return mainData;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("query data error!!!", e);
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
                // 主子表结构，过滤子表
                Object orignalItemDatas = getObjValue(rule, metaInfo.getItemFieldName());
                List conflictItems = getConflictItems(BeanUtil.transObj2List(orignalItemDatas), uniqueColumns);
                // 有冲突的
                if (conflictItems != null) {
                    setObjValue(rule, metaInfo.getItemFieldName(), BeanUtil.transList2Obj(conflictItems, orignalItemDatas.getClass()));
                    return rule;
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }


    private final String EXISTS_ITEMS = "exist";
    private final String NEW_ITEMS = "new";

    /**
     * 获取冲突了的数据集合
     *
     * @return
     */
    private List getConflictItems(List orignalData, List<String> uniqueColumns) {
        return groupItems(orignalData, uniqueColumns).get(EXISTS_ITEMS);
    }

    /**
     * 获取没有冲突的数据
     *
     * @param orignalData
     * @param uniqueColumns
     * @return
     */
    private List getNotConflictItems(List orignalData, List<String> uniqueColumns) {
        return groupItems(orignalData, uniqueColumns).get(NEW_ITEMS);
    }

    /**
     * 对子项进行分组，分成已存在的和未存在的
     *
     * @param orignalData   原始数据
     * @param uniqueColumns 唯一字段
     * @return
     */
    private Map<String, List> groupItems(List orignalData, List<String> uniqueColumns) {
        Map<String, List> group = new HashMap<>();
        if (CollectionUtils.isEmpty(orignalData)) {
            group.put(NEW_ITEMS, orignalData);
            return group;
        }
        //查询冲突项
        String sql = SqlBuilder.buildUniqueQuerySql(MetaInfoUtil.getMetaInfo(orignalData.get(0).getClass()).getTable(), uniqueColumns, orignalData.size());
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < orignalData.size(); i++) {
            Object item = orignalData.get(i);
            Map<String, Object> objValues = objectToMap(item);
            for (String column : uniqueColumns) {
                params.put(column + i, objValues.get(column));
            }
        }

        // 默认认为所有的子表数据项都冲突了
        List<String> conflictUniqueKeys = namedParameterJdbcTemplate.query(sql, params, (resultSet, i) -> resultSet.getString(1));
        List existItems = new ArrayList(); // 存在的规则项
        List newItems = new ArrayList(); // 新的规则项目

        // 数据库中没有数据，那就没有冲突的
        if (CollectionUtils.isEmpty(conflictUniqueKeys)) {
            group.put(EXISTS_ITEMS, orignalData);
            return group;
        } else {
            for (Object item : orignalData) {
                Map<String, Object> objectValues = objectToMap(item);
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
                    existItems.add(item);
                } else {
                    newItems.add(item);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(existItems)) {
            group.put(EXISTS_ITEMS, existItems);
        }

        if (CollectionUtils.isNotEmpty(newItems)) {
            group.put(NEW_ITEMS, newItems);
        }
        return group;

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
        SqlBuildParam sqlBuildParam = SqlBuildParam.builder()
                .table(metaInfo.getTable())
                .limit(Limit.builder().start(start).size(size).build())
                .params(params).like(metaInfo.getLikeColumnMap())
                .orderBy(metaInfo.getOrderColumnMap())
                .build();
        String sql = SqlBuilder.buildQuerySql(sqlBuildParam);

        LinkedHashMap<String, String> fields = metaInfo.getColumnFieldNameMap();
        return namedParameterJdbcTemplate.query(sql, params, (resultSet, rowNumber) -> {
            Object o = null;
            try {
                o = clazz.newInstance();
                Map<String, Object> values = new HashMap<>();
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    values.put(entry.getValue(), resultSet.getObject(entry.getKey()));
                }
                setObjValues(o, values);
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
        String idFileName = mainMetaInfo.getColumnFieldNameMap().get(mainMetaInfo.getIdColumn());
        Object idValue = getObjValue(rule, idFileName);

        Map<String, Object> mainUpdateParams = objectToMapNoNull(rule);

        mainUpdateParams.remove(mainMetaInfo.getIdColumn());

        // 更新规则数据
        Map<String, Object> mainWhereParams = new HashMap<>();
        mainWhereParams.put(mainMetaInfo.getIdColumn(), idValue);
        String mainUpdateSql = SqlBuilder.buildUpdateSql(mainMetaInfo.getTable(), mainUpdateParams, mainWhereParams);
        mainUpdateParams.putAll(mainWhereParams);
        namedParameterJdbcTemplate.update(mainUpdateSql, mainUpdateParams);

        // 如果有子表处理子表：
        String itemFieldName = mainMetaInfo.getItemFieldName();
        if (StringUtils.isNotEmpty(itemFieldName)) {
            // 删除子表旧数据
            EntityMetaInfo subMetaInfo = getMetaInfo(ruleItemClazz);
            Map<String, Object> deleteParams = new HashMap<>();
            deleteParams.put(subMetaInfo.getForeignKeyColumn(), idValue);
            String deletSql = SqlBuilder.buildDeleteSql(subMetaInfo.getTable(), deleteParams);
            namedParameterJdbcTemplate.update(deletSql, deleteParams);

            List data = BeanUtil.transObj2List(getObjValue(rule, itemFieldName));
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
        Map<String, List> items = groupItems(data, ruleProperties.getUniqueColumns());
        List toDealData = data;
        boolean hasConflict = CollectionUtils.isNotEmpty(items.get(EXISTS_ITEMS));
        if (hasConflict) {
            toDealData = conflictStrategy.beforeSub(mainMetaInfo, subMetaInfo, data, items.get(EXISTS_ITEMS), items.get(NEW_ITEMS));
        }
        if (CollectionUtils.isNotEmpty(toDealData)) {
            insertList(subMetaInfo, data, foreignValue);
        }

        if (hasConflict) {
            conflictStrategy.afterSub(mainMetaInfo, subMetaInfo);
        }

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
        Map<String, Object> m = objectToMapNoNull(data);
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
    private void insertList(EntityMetaInfo metaInfo, List data, Object foreignValue) {
        if (CollectionUtils.isEmpty(data))
            return;
        Map<String, Object>[] insertParams = new Map[data.size()];
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> m = objectToMapNoNull(data.get(i));
            m.remove(metaInfo.getIdColumn());
            if (foreignValue != null) { // 如果有外键数据，设置外键数据
                m.put(metaInfo.getForeignKeyColumn(), foreignValue);
            }
            insertParams[i] = m;
        }
        String insertSql = SqlBuilder.buildInsertSql(metaInfo.getTable(), insertParams[0]);
        namedParameterJdbcTemplate.batchUpdate(insertSql, insertParams);
    }
}


